package com.example.androiderp.activities.warehouseview;

import android.app.Dialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiderp.adaper.BasicAdapter;
import com.example.androiderp.bean.AcivityPostBean;
import com.example.androiderp.bean.AdapterBean;
import com.example.androiderp.bean.AppropriationEnty;
import com.example.androiderp.bean.HttpPostBean;
import com.example.androiderp.bean.PostProductData;
import com.example.androiderp.bean.Product;
import com.example.androiderp.bean.ProductShopping;
import com.example.androiderp.bean.SalesOutEnty;
import com.example.androiderp.bean.StockIniti;
import com.example.androiderp.bean.StockTakingEnty;
import com.example.androiderp.R;
import com.example.androiderp.adaper.AppropriationBadgeAdapter;
import com.example.androiderp.tools.Common;
import com.example.androiderp.tools.GlobalVariable;
import com.example.androiderp.tools.HttpUtil;
import com.example.androiderp.ui.CBadgeView;
import com.example.androiderp.ui.CSearch;
import com.example.androiderp.ui.CSearchBase;
import com.example.androiderp.activities.basicfrom.ProductForm;
import com.example.androiderp.scanning.CommonScanActivity;
import com.example.androiderp.scanning.utils.Constant;
import com.example.androiderp.ui.DataLoadingDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class InventoryView extends CSearchBase implements View.OnClickListener, AppropriationBadgeAdapter.Callback{
    private AppropriationBadgeAdapter rightAdapter;
    private BasicAdapter leftAdapter;
    private ListView rightListView;
    private ListView leftListView;
    private DisplayMetrics dm;
    private List<Product> productSearch = new ArrayList<>();
    private TextView back, add, tile, screen;
    private CSearch search;
    private Intent intent;
    private List<AdapterBean> categorylist = new ArrayList<>();
    private double quantityCount;
    private double categorycount;
    private double amountCount;
    private HashSet<Long> selectedItems = new HashSet<>();
    private CBadgeView CBadgeView;
    private List<ProductShopping> productShoppingList = new ArrayList<>();
    private ImageView badgeImage;
    private  int leftListSelecte;
    private  String leftListSelecteText;
    private List<StockIniti> stockInitiList = new ArrayList<>();
    private List<StockTakingEnty> stockTakingEntyList = new ArrayList<StockTakingEnty>();
    private List<SalesOutEnty> salesOutEntyList;
    private List<SalesOutEnty> supplierOutEntieList;
    private double fqty;
    private double quantity;
    private int  stockCheck=1;
    private String appropriOutValue;
    private List<Integer> stockCheckList=new ArrayList<>();
    private DecimalFormat df = new DecimalFormat("#####0.00");
    private HttpPostBean getAcivityPostBean = new HttpPostBean();
    private AcivityPostBean postAcivityPostBen = new AcivityPostBean();
    private List<AdapterBean> HttpResponseCategory = new ArrayList<>();
    private List<Product> HttpResponseCustom = new ArrayList<>();
    private List<Product> HttpResponseCustomTemp = new ArrayList<>();
    private Dialog dialog;
    private int selectPositon = 0;
    private String selectCategory = "全部";

    @Override
    public void iniView(){
        setContentView(R.layout.inventory_listview_layout);
        back =(TextView)findViewById(R.id.custom_toobar_left) ;
        tile =(TextView)findViewById(R.id.custom_toobar_midd);
        add =(TextView)findViewById(R.id.custom_toobar_right);
        screen =(TextView)findViewById(R.id.customtoobar_screen);
        screen.setOnClickListener(this);
        tile.setText("库存查询");
        tile.setCompoundDrawables(null,null,null,null);
        back.setOnClickListener(this);
        add.setOnClickListener(this);
        tile.setOnClickListener(this);
        Intent intentValue=getIntent();
        appropriOutValue=intentValue.getStringExtra("appropriout");
        search = (CSearch) findViewById(R.id.search);
        stockInitiList = DataSupport.findAll(StockIniti.class);
        stockTakingEntyList = DataSupport.findAll(StockTakingEnty.class);
        salesOutEntyList =DataSupport.where("billtype =?","2").find(SalesOutEnty.class);
        supplierOutEntieList =DataSupport.where("billtype =?","1").find(SalesOutEnty.class);

        intent= new Intent(InventoryView.this, InventoryEntyView.class);

        CBadgeView = new CBadgeView(this);
        badgeImage =(ImageView)findViewById(R.id.product_shopping_badge);
        CBadgeView.setTargetView(badgeImage);
        CBadgeView.setBadgeMargin(25,0,0,0);
        CBadgeView.setBadgeGravity(Gravity.RIGHT & Gravity.TOP);

        //构造函数第一参数是类的对象，第二个是布局文件，第三个是数据源
        leftListView=(ListView) findViewById(R.id.left_list);
        leftListView.setTextFilterEnabled(true);
        rightListView = (ListView) findViewById(R.id.right_list);
        rightListView.setTextFilterEnabled(true);
        leftAdapter = new BasicAdapter(getApplicationContext(), R.layout.custom_item, categorylist);
        leftAdapter.setSeclection(0);
        leftListView.setAdapter(leftAdapter);
        rightAdapter = new AppropriationBadgeAdapter(getApplicationContext(), R.layout.appropriation_badge_item, HttpResponseCustom,this);
        rightListView.setAdapter(rightAdapter);
        PostProductData postDate = new PostProductData();
        postDate.setName("");
        postDate.setRequestType(GlobalVariable.cmvCusmtAndCategory);
        getAcivityPostBean.setOperation(GlobalVariable.cmvCusmtAndCategory);
        postDate.setServerIp(Common.ip);
        postDate.setClassType(1);
        postDate.setServlet("ProductOperate");
        getHttpData(postDate);
        leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                leftListSelecte =position;
                leftAdapter.setSeclection(position);
                leftAdapter.notifyDataSetInvalidated();
                Object[] obj = categorySearch(categorylist.get(position).getName());
                updateLayout(obj);
                tile.setText(categorylist.get(position).getName());
                leftListSelecteText = categorylist.get(position).getName();
            }
        });
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        rightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                if(rightAdapter.selectedItems.contains((long)position)){

                }else{
                    rightAdapter.selectedItems.add((long)position);
                }
                selectedItems =rightAdapter.selectedItems;




                    intent.putExtra("action", "edit");
                    intent.putExtra("product_item", String.valueOf(HttpResponseCustom.get(position).getProduct_id()));


                startActivityForResult(intent,1);

            }
        });





        search.addTextChangedListener(textWatcher);


    }

    //筛选条件
    public Object[] search(String name) {
        if(productSearch !=null) {
            productSearch.clear();
        }
        if(leftListSelecteText ==null){
            leftListSelecteText ="全部产品";
        }
        if(leftListSelecteText.equals("未分类"))
        {
            for (int i = 0; i < HttpResponseCustom.size(); i++) {
                if(HttpResponseCustom.get(i).getCategory_name()==null)
                { int index = HttpResponseCustom.get(i).getNumber().indexOf(name);
                    int indey = HttpResponseCustom.get(i).getName().indexOf(name);
                    if (index != -1||indey!=-1) {
                        productSearch.add(HttpResponseCustom.get(i));
                    }

                }
            }

        }else if (leftListSelecteText.equals("全部产品"))
        {
            for (int i = 0; i < HttpResponseCustom.size(); i++) {
                int index = HttpResponseCustom.get(i).getNumber().indexOf(name);
                int indey = HttpResponseCustom.get(i).getName().indexOf(name);
                if (index != -1||indey!=-1) {
                    productSearch.add(HttpResponseCustom.get(i));
                }

            }

        }

        else {
            for (int i = 0; i < HttpResponseCustom.size(); i++) {
                int index = HttpResponseCustom.get(i).getNumber().indexOf(name);
                int indey = HttpResponseCustom.get(i).getCategory_name().indexOf(leftListSelecteText);
                // 存在匹配的数据
                if (index != -1 & indey != -1) {
                    productSearch.add(HttpResponseCustom.get(i));
                }
            }
        }
        return productSearch.toArray();
    }

    public Object[] categorySearch(String name) {

        if(productSearch !=null) {
            productSearch.clear();
        }
        if(name.equals("未分类"))
        {
            for (int i = 0; i < HttpResponseCustom.size(); i++) {
               if(HttpResponseCustom.get(i).getCategory_name()==null)
               {
                    productSearch.add(HttpResponseCustom.get(i));
               }
            }

        }else if (name.equals("全部产品"))
        {
            for (int i = 0; i < HttpResponseCustom.size(); i++) {

                    productSearch.add(HttpResponseCustom.get(i));

            }

        }

        else {
        for (int i = 0; i < HttpResponseCustom.size(); i++) {
              if(HttpResponseCustom.get(i).getCategory_name()!=null){
                int index = HttpResponseCustom.get(i).getCategory_name().indexOf(name);
                // 存在匹配的数据
                if (index != -1) {
                    productSearch.add(HttpResponseCustom.get(i));
                }
            }
        }}
        return productSearch.toArray();
    }
//adapter刷新,重写Filter方式会出现BUG.
    public void updateLayout(Object[] obj) {
        if(productSearch !=null) {

            rightAdapter = new AppropriationBadgeAdapter(InventoryView.this, R.layout.appropriation_badge_item, productSearch,this);
            rightAdapter.selectedItems= selectedItems;
            rightListView.setAdapter(rightAdapter);
        }
    }
    private void getHttpData(final PostProductData postPostUserData) {
        showDialog();


        HttpUtil.sendProductRequst(postPostUserData, new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        closeDialog();
                        Toast.makeText(getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            if (getAcivityPostBean.getOperation().equals(GlobalVariable.cmvCusmtAndCategory)) {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                JSONArray jsonArray = jsonObject.getJSONArray("custom");
                                JSONArray jsonArray1 = jsonObject.getJSONArray("customcategory");
                                Gson gson = new Gson();
                                categorylist.clear();
                                HttpResponseCustom.clear();
                                HttpResponseCategory= gson.fromJson(jsonArray1.toString(), new TypeToken<List<AdapterBean>>() {
                                }.getType());

                                AdapterBean commonDataAll=new AdapterBean();
                                commonDataAll.setName("全部产品");
                                categorylist.add(commonDataAll);
                                AdapterBean commonDataN=new AdapterBean();
                                commonDataN.setName("未分类");
                                categorylist.add(commonDataN);
                                categorylist.addAll(HttpResponseCategory);
                                HttpResponseCustomTemp = gson.fromJson(jsonArray.toString(), new TypeToken<List<Product>>() {
                                }.getType());
                                HttpResponseCustom.addAll(HttpResponseCustomTemp);

                                if (HttpResponseCategory != null && HttpResponseCustom != null) {
                                    for(Product product: HttpResponseCustom)

                                    {    fqty=0.00;
                                        for(StockIniti stock: stockInitiList)

                                        {
                                            if(product.getNumber().equals(stock.getNumber()))
                                            {
                                                fqty+=stock.getQuantity();
                                            }


                                        }
                                        for(StockTakingEnty stockTakingEnty: stockTakingEntyList)

                                        {
                                            if(product.getNumber().equals(stockTakingEnty.getNumber()))
                                            {
                                                fqty+=stockTakingEnty.getQuantity();
                                            }


                                        }

                                        for(SalesOutEnty salesOutEnty: salesOutEntyList)
                                        {
                                            if(product.getNumber().equals(salesOutEnty.getNumber()))
                                            {
                                                fqty-=salesOutEnty.getQuantity();
                                            }
                                        }
                                        for(SalesOutEnty salesOutEnty: supplierOutEntieList)
                                        {
                                            if(product.getNumber().equals(salesOutEnty.getNumber()))
                                            {
                                                fqty+=salesOutEnty.getQuantity();
                                            }
                                        }
                                        product.setQuantity(fqty);

                                    }
                                    leftAdapter.setSeclection(selectPositon);
                                    selectCategory = HttpResponseCategory.get(0).getName();
                                    leftAdapter.notifyDataSetChanged();
                                    rightAdapter.notifyDataSetChanged();
                                }


                            } else {

                                Gson gson = new Gson();
                                HttpResponseCustom= gson.fromJson(response.body().string(), new TypeToken<List<Product>>() {
                                }.getType());
                                HttpResponseCustom.clear();
                                HttpResponseCustom.addAll(HttpResponseCustomTemp);
                                if (HttpResponseCustom != null) {
                                    if (HttpResponseCustom.size() != 0) {


                                        rightAdapter.notifyDataSetChanged();


                                    } else {

                                        rightAdapter.notifyDataSetChanged();
                                        Toast.makeText(getApplicationContext(), "没有数据", Toast.LENGTH_SHORT).show();

                                    }
                                } else {
                                    HttpResponseCustom.clear();
                                    rightAdapter.notifyDataSetChanged();
                                }

                            }
                            closeDialog();

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "网络", Toast.LENGTH_SHORT).show();
                            Log.d("lingtana", e.toString());
                            closeDialog();
                        }
                    }
                });


            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK)
                {

                    ProductShopping shopping= data.getParcelableExtra("shop_data");
                    Log.d("tongtan",String.valueOf(shopping.getId()));
                    Log.d("tongtan",shopping.getName());
                    for(ProductShopping shop: productShoppingList)
                    {

                       if(shop.getNumber().equals(shopping.getNumber())){
                           productShoppingList.remove(shop);
                       }
                    }

                    productShoppingList.add(shopping);
                    quantityCount =0;
                    amountCount =0.00;
                    for (int i = 0; i < productShoppingList.size(); i++) {

                        quantityCount += productShoppingList.get(i).getQuantity();
                        amountCount += productShoppingList.get(i).getAmount();
                        if(stockCheck(appropriOutValue, productShoppingList.get(i).getNumber(), productShoppingList.get(i).getQuantity())==0)
                        {
                            Toast.makeText(InventoryView.this,"调出仓库数量不足，实际库存为："+df.format(quantity),Toast.LENGTH_SHORT).show();
                        }

                    }
                    badgeImage.setVisibility(View.VISIBLE);
                    CBadgeView.setBadgeCount(quantityCount);
                    for(Product product: productSearch)

                    {
                        if(product.getNumber().equals(shopping.getNumber()))
                        {
                            product.setBadgeShow(String.valueOf(shopping.getQuantity()));
                        }

                        product.setImage(R.drawable.listvist_item_delete);


                    }
                    rightAdapter.notifyDataSetChanged();


                    if(categorylist.size()!=0)
                    {
                        categorylist.clear();
                    }

                    AdapterBean commonDataAll=new AdapterBean();
                    commonDataAll.setName("全部产品");
                    commonDataAll.setBadge(String.valueOf(df.format(quantityCount)));
                    categorylist.add(commonDataAll);
                    AdapterBean commonDataN=new AdapterBean();
                    commonDataN.setName("未分类");
                    for(AdapterBean productCategory: HttpResponseCategory)
                    {
                        categorycount=0;
                        for(ProductShopping shop: productShoppingList)
                    {
                        if(shop.getCategory().equals(productCategory.getName()))
                        {
                            categorycount+=shop.getQuantity();
                        }



                    }
                        AdapterBean commonData=new AdapterBean();
                        commonData.setName(productCategory.getName());
                        commonData.setId(productCategory.getId());
                        if(categorycount>0) {
                            commonData.setBadge(String.valueOf(categorycount));
                        }
                        categorylist.add(commonData);

                    }

                    leftAdapter = new BasicAdapter(InventoryView.this, R.layout.custom_item, categorylist);
                    leftAdapter.setSeclection(leftListSelecte);
                    leftListView.setAdapter(leftAdapter);
                    DecimalFormat df = new DecimalFormat("#####0.00");
                }

                if(resultCode==RESULT_FIRST_USER){
                    rightAdapter.notifyDataSetChanged();
                }
                break;
            case 2:
                if(resultCode==RESULT_OK) {

                    Product lastProduct = DataSupport.findLast(Product.class);
                    productSearch.add(lastProduct);
                    rightAdapter.notifyDataSetChanged();

                }
                if(resultCode==RESULT_FIRST_USER){

                    rightAdapter.notifyDataSetChanged();
                }

                break;
            case 3:
                if(resultCode==RESULT_OK) {

                    search.requestFocusFromTouch();
                    search.setText(data.getStringExtra("scanResult"));
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
                InventoryView.this.finish();
                break;

            case R.id.custom_toobar_right:
                Intent intentnew= new Intent(InventoryView.this, ProductForm.class);
                intentnew.removeExtra("product_item");
                intentnew.putExtra("action","add");
                startActivityForResult(intentnew,2);
                break;
            case R.id.customtoobar_screen:
                Intent openCameraIntent=new Intent(InventoryView.this,CommonScanActivity.class);
                openCameraIntent.putExtra(Constant.REQUEST_SCAN_MODE,Constant.REQUEST_SCAN_MODE_ALL_MODE);
                startActivityForResult(openCameraIntent,3);

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

            Object[] obj = search(search.getText().toString());
            updateLayout(obj);

        }
    };

    @Override
    public void click(View v) {
        DecimalFormat df = new DecimalFormat("#####0.00");
        productSearch.get((Integer) v.getTag()).setBadgeShow("");
        productSearch.get((Integer) v.getTag()).setImage(0);
        quantityCount =0;
        amountCount =0.00;
        for (int i = 0; i < productShoppingList.size(); i++) {

            if(productShoppingList.get(i).getNumber().equals(productSearch.get((Integer) v.getTag()).getNumber()))
            {
                productShoppingList.remove(i);
            }
        }

        for(int i = 0; i < productShoppingList.size(); i++)
        {

            quantityCount += productShoppingList.get(i).getQuantity();
            amountCount += productShoppingList.get(i).getAmount();
        }
        if(categorylist.size()!=0)
        {
            categorylist.clear();
        }
        AdapterBean commonDataAll=new AdapterBean();
        commonDataAll.setName("全部产品");
        if(quantityCount>0) {
            commonDataAll.setBadge(String.valueOf(df.format(quantityCount)));
        }
        categorylist.add(commonDataAll);
        AdapterBean commonDataN=new AdapterBean();
        commonDataN.setName("未分类");
        categorylist.add(commonDataN);
        for(AdapterBean productCategory: HttpResponseCategory)
        {
            categorycount=0;
            for(ProductShopping shop: productShoppingList)
            {
                if(shop.getCategory().equals(productCategory.getName()))
                {
                    categorycount+=shop.getQuantity();
                }



            }
            AdapterBean commonData=new AdapterBean();
            commonData.setName(productCategory.getName());
            commonData.setId(productCategory.getId());
            if(categorycount>0) {
                commonData.setBadge(String.valueOf(categorycount));
            }
            categorylist.add(commonData);

        }

       rightAdapter.notifyDataSetChanged();
       leftAdapter.notifyDataSetChanged();
        CBadgeView.setBadgeCount(quantityCount);
        if(amountCount ==0.00)
        {
            badgeImage.setVisibility(View.INVISIBLE);
        }



    }
    private int stockCheck(String stockname,String number,double sfqty)
    {

        double  in=DataSupport.where("stockIn=? and number=?",stockname,number).sum(AppropriationEnty.class,"quantity",double.class);
        double  out=DataSupport.where("stockOut=? and number=?",stockname,number).sum(AppropriationEnty.class,"quantity",double.class);
        double  stocktaking=DataSupport.where("stock=? and number=?",stockname,number).sum(StockTakingEnty.class,"quantity",double.class);
        double  initis=DataSupport.where("stock=? and number=?",stockname,number).sum(StockIniti.class,"quantity",double.class);
        double   salesOut=DataSupport.where("billtype =? and stock=? and number=?","2",stockname,number).sum(SalesOutEnty.class,"quantity",double.class);
        double  supplierin=DataSupport.where("billtype =? and stock=? and number=?","1",stockname,number).sum(SalesOutEnty.class,"quantity",double.class);
        quantity=0.00;
        quantity=initis+supplierin+in+stocktaking-salesOut-out;

        if(sfqty>quantity)
        {
            stockCheck=0;
            stockCheckList.add(stockCheck);
        }else {
            stockCheck=1;
        }
        return  stockCheck;
    }

    /**
     * 显示进度对话框
     */
    private void showDialog() {

        dialog = new DataLoadingDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();//显示

    }

    /**
     * 关闭进度对话框
     */
    private void closeDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }


}
