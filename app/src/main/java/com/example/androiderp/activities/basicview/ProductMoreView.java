/*
功能：商品信息、商品扫描搜索、商品搜索、商品新增、商品修改、网络状态更变提醒
主要控件：ListView、Adapter、ustomSearch、TextView
数据模型：Product、ProductCategory、CommonAdapterData
 */

package com.example.androiderp.activities.basicview;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.androiderp.bean.Product;
import com.example.androiderp.bean.ProductCategory;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonAdapter;
import com.example.androiderp.adaper.CommonAdapterData;
import com.example.androiderp.adaper.ProductAdapter;
import com.example.androiderp.ui.CSearch;
import com.example.androiderp.ui.CSearchBase;
import com.example.androiderp.activities.basicfrom.ProductForm;
import com.example.androiderp.scanning.CommonScanActivity;
import com.example.androiderp.scanning.utils.Constant;
import org.litepal.crud.DataSupport;
import java.util.ArrayList;
import java.util.List;

public class ProductMoreView extends CSearchBase implements View.OnClickListener {
    private ProductAdapter rightAdapter;
    private CommonAdapter leftAdapter;
    private ListView rightListView;
    private ListView leftListView;
    private DisplayMetrics dm;
    private List<Product> productList;
    private TextView toobarBack, toobarAdd, toobarTile, countShow, toobarScreen;
    private CSearch search;
    private Intent intent, intentScreen;
    private List<ProductCategory> productCategoryList;
    private List<CommonAdapterData> categorylist = new ArrayList<CommonAdapterData>();
    private  String leftListSelecteText="全部产品";
    private  int leftListSelecte=0;
    private String scanResult;
    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;
    private CommonAdapterData proudctcategoryData=new CommonAdapterData();

    @Override
    public void iniView(){
        setContentView(R.layout.product_listview_layout);
        DataInit();
        widgetInit();
        widgetSet();
        widgetListenerSet();
        if(scanResult!=null)
        {

            listViewUpdate(search(scanResult,leftListSelecteText),rightAdapter,rightListView);


        }


    }
//注册广播类型
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentFilter=new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        networkChangeReceiver=new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver,intentFilter);

    }
    //读取数据库
    private void DataInit()
    {
        productList = DataSupport.findAll(Product.class);
        productCategoryList = DataSupport.findAll(ProductCategory.class);
        CommonAdapterData commonDataAll=new CommonAdapterData();
        commonDataAll.setName("全部产品");
        categorylist.add(commonDataAll);
        CommonAdapterData commonDataN=new CommonAdapterData();
        commonDataN.setName("未分类");
        categorylist.add(commonDataN);
        for(ProductCategory productCategory: productCategoryList)

        {
            CommonAdapterData commonData=new CommonAdapterData();
            commonData.setName(productCategory.getName());
            commonData.setUnitId(productCategory.getId());
            categorylist.add(commonData);

        }
    }
//控件初始化
    private void widgetInit()
    {
        toobarBack =(TextView)findViewById(R.id.custom_toobar_left) ;
        toobarTile =(TextView)findViewById(R.id.custom_toobar_midd);
        toobarAdd =(TextView)findViewById(R.id.custom_toobar_right);
        toobarScreen =(TextView)findViewById(R.id.customtoobar_screen);
        search = (CSearch) findViewById(R.id.search);
        countShow =(TextView)findViewById(R.id.product_item_layout_count) ;
        leftListView=(ListView) findViewById(R.id.left_list);
        rightListView = (ListView) findViewById(R.id.right_list);
    }
//控件设置
    private void widgetSet()
    {

        toobarTile.setText("商品信息");
        countShow.setText(String.valueOf(productList.size()));//统计商品数量并显示
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        intent= new Intent(ProductMoreView.this, ProductForm.class);
        intentScreen =getIntent();
        scanResult= intentScreen.getStringExtra("scanResult");//获取扫描返回结果
        toobarTile.setCompoundDrawables(null,null,null,null);//取消标题图片
        toobarScreen.setOnClickListener(this);
        toobarBack.setOnClickListener(this);
        toobarAdd.setOnClickListener(this);
        toobarTile.setOnClickListener(this);
        leftListView.setTextFilterEnabled(true);
        rightListView.setTextFilterEnabled(true);
        search.addTextChangedListener(textWatcher);
        leftAdapter = new CommonAdapter(ProductMoreView.this, R.layout.custom_item, categorylist);
        leftAdapter.setSeclection(leftListSelecte);//突出显示
        leftListView.setAdapter(leftAdapter);
        rightAdapter = new ProductAdapter(ProductMoreView.this, R.layout.product_item, productList);
        rightListView.setAdapter(rightAdapter);

    }

//控件设置事件，点击编辑、获取左类别的对应的位置及名称
    private void widgetListenerSet()
    {
        leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                leftListSelecte=position;
                leftAdapter.setSeclection(position);
                leftAdapter.notifyDataSetInvalidated();
                listViewUpdate(categorySearch(categorylist.get(position).getName().toString()),rightAdapter,rightListView);
                leftListSelecteText = categorylist.get(position).getName().toString();
                countShow.setText(String.valueOf(productList.size()));
            }
        });
        rightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                intent.removeExtra("product_item");
                    intent.putExtra("action", "edit");
                    intent.putExtra("product_item", String.valueOf(productList.get(position).getId()));
                startActivityForResult(intent,1);


            }
        });

    }

    //内容查找
    public List<Product> search(String searchKey, String selecteCategory) {
        List<Product> searchData = new ArrayList<Product>();
        if(selecteCategory==null)
        {
            selecteCategory="全部产";
        }

        if(selecteCategory.equals("未分类"))

        {
            searchData= DataSupport.where("category=? and (name like ? or number like ?)","", "%" + searchKey + "%","%" + searchKey + "%").find(Product.class);



        }else if (selecteCategory.equals("全部产品"))
        {
            searchData=DataSupport.where("name like ? or number like ?","%" + searchKey + "%","%" + searchKey + "%").find(Product.class);

        }

        else {



            searchData=DataSupport.where("category=? and (name like ? or number like ?)",selecteCategory,"%" + searchKey + "%","%" + searchKey + "%").find(Product.class);

        }
        productList=searchData;
        return  searchData;
    }

    //类别
    public List<Product> categorySearch(String searchKey) {

         List<Product> searchData = new ArrayList<Product>();

        if(searchKey.equals("未分类"))
        {
               searchData=DataSupport.where("category=?","").find(Product.class);

        }else if (searchKey.equals("全部产品"))
        {

           searchData=DataSupport.findAll(Product.class);

        }

        else {

                searchData= DataSupport.where("category=?", searchKey).find(Product.class);

            }
            productList=searchData;
        return searchData;
    }
//adapter刷新
    public void listViewUpdate(List<Product> searchData,ProductAdapter rightAdapter,ListView rightListView) {
        if(searchData !=null) {
            rightAdapter = new ProductAdapter(ProductMoreView.this, R.layout.product_item, searchData);
            rightListView.setAdapter(rightAdapter);
        }
    }
//处理返回结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK)
               //如果左菜单是"全部产品"，就不改变leftListSelecteText的值。
                {    if(leftListSelecte!=0)

                {
                    leftListSelecteText=data.getStringExtra("returncategory");
                    //重新统计商品数量并显示
                    countShow.setText(String.valueOf(search(search.getText().toString(),data.getStringExtra("returncategory")).size()));


                }
                //通过查找方式，刷新Adapter
                    listViewUpdate(search(search.getText().toString(),leftListSelecteText),rightAdapter,rightListView);


                //清空左菜单数据集合
                    if(categorylist.size()!=0)
                    {
                        categorylist.clear();
                    }
                 //获取商品类别
                    productCategoryList = DataSupport.findAll(ProductCategory.class);
                 // categorylist 增加元素。
                    CommonAdapterData commonDataAll=new CommonAdapterData();
                    commonDataAll.setName("全部产品");
                    categorylist.add(commonDataAll);
                    CommonAdapterData commonDataN=new CommonAdapterData();
                    commonDataN.setName("未分类");
                    categorylist.add(commonDataN);
                    for(ProductCategory productCategory: productCategoryList)

                    {

                        CommonAdapterData commonData=new CommonAdapterData();
                        if(productCategory.getName().equals(data.getStringExtra("returncategory")))
                        {
                            proudctcategoryData=commonData;
                        }
                        commonData.setName(productCategory.getName());
                        commonData.setUnitId(productCategory.getId());
                        categorylist.add(commonData);

                    }
                    //如果左菜单是"商品全部"，不改变leftListSelecte的值。
                    if(leftListSelecte!=0 &&data.getStringExtra("returncategory")!=null) {
                        leftListSelecte = categorylist.indexOf(proudctcategoryData);

                    }
                    leftAdapter = new CommonAdapter(ProductMoreView.this, R.layout.custom_item, categorylist);
                    leftAdapter.setSeclection(leftListSelecte);
                    leftListView.setAdapter(leftAdapter);

                   }
                break;
            case 2:
                if(resultCode==RESULT_OK) {
                    //通过扫描返回的结果改变customSearch的值
                    search.requestFocusFromTouch();//customSearch获得焦点
                    search.setText(data.getStringExtra("scanResult"));

                }
                break;
            case 3:
                if(resultCode==RESULT_OK)
                {
                     if(leftListSelecte!=0 && data.getStringExtra("returncategory")!=null)

                    {


                        leftListSelecteText=data.getStringExtra("returncategory");
                        //重新统计商品数量并显示
                        countShow.setText(String.valueOf(search(search.getText().toString(),data.getStringExtra("returncategory")).size()));


                    }

                    listViewUpdate(search(search.getText().toString(),leftListSelecteText),rightAdapter,rightListView);
                    //清空左菜单数据集合
                    if(categorylist.size()!=0)
                    {
                        categorylist.clear();
                    }
                    //获取商品类别
                    productCategoryList = DataSupport.findAll(ProductCategory.class);
                    // categorylist 增加元素。
                    CommonAdapterData commonDataAll=new CommonAdapterData();
                    commonDataAll.setName("全部产品");
                    categorylist.add(commonDataAll);
                    CommonAdapterData commonDataN=new CommonAdapterData();
                    commonDataN.setName("未分类");
                    categorylist.add(commonDataN);
                    for(ProductCategory productCategory: productCategoryList)

                    {

                        CommonAdapterData commonData=new CommonAdapterData();
                        if(productCategory.getName().equals(data.getStringExtra("returncategory")))
                        {
                            proudctcategoryData=commonData;
                        }
                        commonData.setName(productCategory.getName());
                        commonData.setUnitId(productCategory.getId());
                        categorylist.add(commonData);

                    }
                    //如果左菜单是"商品全部"，不改变leftListSelecte的值。
                    if(leftListSelecte!=0 && data.getStringExtra("returncategory")!=null) {
                        leftListSelecte = categorylist.indexOf(proudctcategoryData);


                    }
                    leftAdapter = new CommonAdapter(ProductMoreView.this, R.layout.custom_item, categorylist);
                    leftAdapter.setSeclection(leftListSelecte);
                    leftListView.setAdapter(leftAdapter);

                }
                break;


            default:
                }
        }


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {   //返回
            case R.id.custom_toobar_left:
                ProductMoreView.this.finish();
                break;
            //新增按钮
            case R.id.custom_toobar_right:

                intent.removeExtra("product_item");
                intent.putExtra("action","add");
                startActivityForResult(intent,3);
                break;
            //扫描
            case R.id.customtoobar_screen:
                Intent openCameraIntent = new Intent(ProductMoreView.this, CommonScanActivity.class);
                openCameraIntent.putExtra(Constant.REQUEST_SCAN_MODE,Constant.REQUEST_SCAN_MODE_ALL_MODE);
                startActivityForResult(openCameraIntent, 2);

                break;


        }

    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {


        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {



        }

        @Override
        public void afterTextChanged(Editable s) {


            listViewUpdate(search(search.getText().toString(),leftListSelecteText),rightAdapter,rightListView);

        }
    };
//自定义广播类型
    class NetworkChangeReceiver extends BroadcastReceiver {
//重写onReceive方法
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectionManager = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {

            } else {
                Toast.makeText(context, "没有网络，请确认WIFI是否打开",
                        Toast.LENGTH_SHORT).show();
            }

        }

    }
//反注册广播
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }
}
