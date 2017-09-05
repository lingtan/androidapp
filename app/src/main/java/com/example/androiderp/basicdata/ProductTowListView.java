package com.example.androiderp.basicdata;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.androiderp.CustomDataClass.Product;
import com.example.androiderp.CustomDataClass.ProductCategory;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonAdapter;
import com.example.androiderp.adaper.CommonDataStructure;
import com.example.androiderp.adaper.ProductAdapter;
import com.example.androiderp.custom.CustomSearch;
import com.example.androiderp.custom.CustomSearchBase;
import com.example.androiderp.form.ProductForm;
import com.example.androiderp.scanning.CommonScanActivity;
import com.example.androiderp.scanning.utils.Constant;
import org.litepal.crud.DataSupport;
import java.util.ArrayList;
import java.util.List;

public class ProductTowListView extends CustomSearchBase implements View.OnClickListener {
    private ProductAdapter rightAdapter;
    private CommonAdapter leftAdapter;
    private ListView rightListView;
    private ListView leftListView;
    private DisplayMetrics dm;
    private List<Product> productSearch = new ArrayList<Product>();
    private List<Product> productList;
    private TextView toobarBack, toobarAdd, toobarTile, countShow, toobarScreen;
    private CustomSearch customSearch;
    private Intent intent, intentScreen;
    private List<ProductCategory> productCategoryList;
    private List<CommonDataStructure> categorylist = new ArrayList<CommonDataStructure>();
    private  String leftListSelecteText;
    private  int leftListSelecte;
    private String scanResult;

    @Override
    public void iniView(){
        setContentView(R.layout.product_listview_layout);
        DataInit();
        widgetInit();
        widgetSet();
        widgetListenerSet();
        if(scanResult!=null)
        {
            search(scanResult,leftListSelecteText);
            listViewUpdate(productSearch,rightAdapter,rightListView);
            

        }


    }
    //读取数据库
    private void DataInit()
    {
        productList = DataSupport.findAll(Product.class);
        productCategoryList = DataSupport.findAll(ProductCategory.class);
        CommonDataStructure commonDataAll=new CommonDataStructure();
        commonDataAll.setName("全部产品");
        categorylist.add(commonDataAll);
        CommonDataStructure commonDataN=new CommonDataStructure();
        commonDataN.setName("未分类");
        categorylist.add(commonDataN);
        for(ProductCategory productCategory: productCategoryList)

        {
            CommonDataStructure commonData=new CommonDataStructure();
            commonData.setName(productCategory.getName());
            commonData.setId(productCategory.getId());
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
        customSearch = (CustomSearch) findViewById(R.id.search);
        countShow =(TextView)findViewById(R.id.product_item_layout_count) ;
        leftListView=(ListView) findViewById(R.id.left_list);
        rightListView = (ListView) findViewById(R.id.right_list);
    }
//控件设置
    private void widgetSet()
    {

        toobarTile.setText("商品信息");
        countShow.setText(String.valueOf(productList.size()));
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        intent= new Intent(ProductTowListView.this, ProductForm.class);
        intentScreen =getIntent();
        scanResult= intentScreen.getStringExtra("scanResult");
        toobarTile.setCompoundDrawables(null,null,null,null);
        toobarScreen.setOnClickListener(this);
        toobarBack.setOnClickListener(this);
        toobarAdd.setOnClickListener(this);
        toobarTile.setOnClickListener(this);
        leftListView.setTextFilterEnabled(true);
        rightListView.setTextFilterEnabled(true);
        customSearch.addTextChangedListener(textWatcher);
        leftAdapter = new CommonAdapter(ProductTowListView.this, R.layout.custom_item, categorylist);
        leftAdapter.setSeclection(leftListSelecte);
        leftListView.setAdapter(leftAdapter);
        rightAdapter = new ProductAdapter(ProductTowListView.this, R.layout.product_item, productList);
        rightListView.setAdapter(rightAdapter);

    }

//控件设置事件
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
            }
        });
        rightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                intent.removeExtra("action");
                if(productSearch.size()!=0) {

                    intent.putExtra("action", "edit");
                    intent.putExtra("product_item", String.valueOf(productSearch.get(position).getId()));


                }else {

                    intent.putExtra("action", "edit");
                    intent.putExtra("product_item", String.valueOf(productList.get(position).getId()));

                }
                startActivityForResult(intent,1);


            }
        });

    }

    //内容查找
    public List<Product> search(String searchKey,String selecteCategory) {
         List<Product> searchData = new ArrayList<Product>();
        if(selecteCategory ==null){
            selecteCategory ="全部产品";
        }
        if(selecteCategory.equals("未分类"))

        {
            searchData=DataSupport.where("category=? and name like ?","", "%" + searchKey + "%").find(Product.class);



        }else if (selecteCategory.equals("全部产品"))
        {
            searchData=DataSupport.where("name like ?","%" + searchKey + "%").find(Product.class);

        }

        else {



                searchData=DataSupport.where("category=? and name like ?",selecteCategory,"%" + searchKey + "%").find(Product.class);

        }
        productSearch=searchData;
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
            productSearch=searchData;
        return searchData;
    }
//adapter刷新,重写Filter方式会出现BUG.
    public void listViewUpdate(List<Product> searchData,ProductAdapter rightAdapter,ListView rightListView) {
        if(searchData !=null) {
            rightAdapter = new ProductAdapter(ProductTowListView.this, R.layout.product_item, searchData);
            rightListView.setAdapter(rightAdapter);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK)
                { if(productSearch.size()>0)
                {
                    rightAdapter = new ProductAdapter(ProductTowListView.this, R.layout.product_item, productSearch);
                    rightListView.setAdapter(rightAdapter);
                }else {
                    if (productList.size() != 0) {
                        productList.clear();
                    }
                    productList = DataSupport.findAll(Product.class);

                    rightAdapter = new ProductAdapter(ProductTowListView.this, R.layout.product_item, productList);
                    rightListView.setAdapter(rightAdapter);
                }
                    if(categorylist.size()!=0)
                    {
                        categorylist.clear();
                    }
                    productCategoryList = DataSupport.findAll(ProductCategory.class);
                    CommonDataStructure commonDataAll=new CommonDataStructure();
                    commonDataAll.setName("全部产品");
                    categorylist.add(commonDataAll);
                    CommonDataStructure commonDataN=new CommonDataStructure();
                    commonDataN.setName("未分类");
                    categorylist.add(commonDataN);
                    for(ProductCategory productCategory: productCategoryList)

                    {
                        CommonDataStructure commonData=new CommonDataStructure();
                        commonData.setName(productCategory.getName());
                        commonData.setId(productCategory.getId());
                        categorylist.add(commonData);

                    }

                    leftAdapter = new CommonAdapter(ProductTowListView.this, R.layout.custom_item, categorylist);
                    leftAdapter.setSeclection(leftListSelecte);
                    leftListView.setAdapter(leftAdapter);
                    countShow.setText(String.valueOf(productList.size()));
                }
                break;
            case 2:
                if(resultCode==RESULT_OK) {

                    customSearch.requestFocusFromTouch();
                    customSearch.setText(data.getStringExtra("scanResult"));

                }
                break;


            default:
                }
        }


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.custom_toobar_left:
                ProductTowListView.this.finish();
                break;

            case R.id.custom_toobar_right:

                intent.removeExtra("product_item");
                intent.putExtra("action","add");
                startActivityForResult(intent,1);
                break;

            case R.id.customtoobar_screen:
                Intent openCameraIntent = new Intent(ProductTowListView.this, CommonScanActivity.class);
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


            listViewUpdate(search(customSearch.getText().toString(),leftListSelecteText),rightAdapter,rightListView);

        }
    };
}
