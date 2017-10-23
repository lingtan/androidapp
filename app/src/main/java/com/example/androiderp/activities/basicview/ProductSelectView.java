package com.example.androiderp.activities.basicview;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiderp.adaper.BasicAdapter;
import com.example.androiderp.bean.AcivityPostBean;
import com.example.androiderp.bean.AdapterBean;
import com.example.androiderp.bean.PostProductData;
import com.example.androiderp.bean.Product;
import com.example.androiderp.bean.ProductShopping;
import com.example.androiderp.bean.ShoppingData;
import com.example.androiderp.R;
import com.example.androiderp.adaper.ProductBadgeAdapter;
import com.example.androiderp.tools.Common;
import com.example.androiderp.tools.GlobalVariable;
import com.example.androiderp.tools.HttpUtil;
import com.example.androiderp.ui.CBadgeView;
import com.example.androiderp.ui.CSearch;
import com.example.androiderp.ui.CSearchBase;
import com.example.androiderp.activities.basicfrom.ProductForm;
import com.example.androiderp.activities.purchaseform.ShoppingForm;
import com.example.androiderp.activities.salesfrom.SaleForm;
import com.example.androiderp.scanning.CommonScanActivity;
import com.example.androiderp.scanning.utils.Constant;
import com.example.androiderp.ui.DataLoadingDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class ProductSelectView extends CSearchBase implements View.OnClickListener, ProductBadgeAdapter.Callback{
    private ProductBadgeAdapter rightAdapter;
    private BasicAdapter leftAdapter;
    private ListView rightListView;
    private ListView leftListView;
    private DisplayMetrics dm;
    private List<Product> productSearch = new ArrayList<Product>();
    private TextView back, add, tile, countShow, screen;
    private CSearch search;
    private Intent intent;
    private List<AdapterBean> categorylist = new ArrayList<AdapterBean>();
    private double quantityCount;
    private double categorycount;
    private double amountCount;
    private HashSet<Long> selectedItems = new HashSet<Long>();
    private CBadgeView CBadgeView;
    private List<ProductShopping> productShoppingList = new ArrayList<ProductShopping>();
    private ImageView badgeImage;
    private  int leftListSelecte;
    private  String leftListSelecteText;
    private LinearLayout accountLayout;
    private AcivityPostBean getAcivityPostBean = new AcivityPostBean();
    private AcivityPostBean postAcivityPostBen = new AcivityPostBean();
    private List<AdapterBean> HttpResponseCategory = new ArrayList<>();
    private List<Product> HttpResponseCustom = new ArrayList<>();
    private List<Product> HttpResponseCustomTemp = new ArrayList<>();
    private Dialog dialog;
    private int selectPositon = 0;
    private String selectCategory = "全部";

    @Override
    public void iniView(){
        setContentView(R.layout.custom_badge_listview_layout);
        back =(TextView)findViewById(R.id.custom_toobar_left) ;
        tile =(TextView)findViewById(R.id.custom_toobar_midd);
        add =(TextView)findViewById(R.id.custom_toobar_right);
        screen =(TextView)findViewById(R.id.customtoobar_screen);
        screen.setOnClickListener(this);
        tile.setText("商品信息");
        tile.setCompoundDrawables(null,null,null,null);
        back.setOnClickListener(this);
        add.setOnClickListener(this);
        tile.setOnClickListener(this);
        search = (CSearch) findViewById(R.id.search);
        countShow =(TextView)findViewById(R.id.product_item_layout_count) ;
        accountLayout =(LinearLayout)findViewById(R.id.product_item_layout_bottom);
        intent= new Intent(ProductSelectView.this, ShoppingForm.class);

        countShow.setText("¥0.00");
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

        leftAdapter = new BasicAdapter(ProductSelectView.this, R.layout.custom_item, categorylist);
        leftAdapter.setSeclection(0);
        leftListView.setAdapter(leftAdapter);
        rightAdapter = new ProductBadgeAdapter(ProductSelectView.this, R.layout.product_badge_item, HttpResponseCustom,this);
        rightListView.setAdapter(rightAdapter);

        PostProductData postDate = new PostProductData();
        postDate.setName("");
        postDate.setRequestType(GlobalVariable.cmvCusmtAndCategory);
        getAcivityPostBean.setOperationType(GlobalVariable.cmvCusmtAndCategory);
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
                Object[] obj = categorySearch(categorylist.get(position).getName().toString());
                updateLayout(obj);
                tile.setText(categorylist.get(position).getName().toString());
                leftListSelecteText = categorylist.get(position).getName().toString();
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




                    intent.putExtra("type", "edit");
                    intent.putExtra("acivityPostBen", HttpResponseCustom.get(position));


                startActivityForResult(intent,1);

            }
        });




        search.addTextChangedListener(textWatcher);

        accountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(productShoppingList !=null& productShoppingList.size()!=0){
                    Intent intentdata=new Intent(ProductSelectView.this, SaleForm.class);
                    ShoppingData shoppingData=new ShoppingData();
                    shoppingData.setProductShoppingList(productShoppingList);
                    intentdata.putExtra("shoppingdata",shoppingData);
                    setResult(RESULT_OK,intentdata);
                    ProductSelectView.this.finish();
                }
            }
        });


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
                if(HttpResponseCustom.get(i).getCategory_name().equals("未分类"))
                { int index = HttpResponseCustom.get(i).getNumber().indexOf(name);
                    if (index != -1) {
                        productSearch.add(HttpResponseCustom.get(i));
                    }

                }
            }

        }else if (leftListSelecteText.equals("全部产品"))
        {
            for (int i = 0; i < HttpResponseCustom.size(); i++) {
                int index = HttpResponseCustom.get(i).getNumber().indexOf(name);
                if (index != -1) {
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
               if(HttpResponseCustom.get(i).getCategory_name().equals("未分类"))
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
              if(HttpResponseCustom.get(i).getCategory_id()!=null){
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

            rightAdapter = new ProductBadgeAdapter(ProductSelectView.this, R.layout.product_badge_item, productSearch,this);
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

                            if (getAcivityPostBean.getOperationType().equals(GlobalVariable.cmvCusmtAndCategory)) {
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
                    DecimalFormat df = new DecimalFormat("#####0.##");
                    ProductShopping shopping= data.getParcelableExtra("shop_data");
                    for(ProductShopping shop: productShoppingList)
                    {

                       if(shop.getNumber().equals(shopping.getNumber())){
                           productShoppingList.remove(shop);
                       }
                    }

                    productShoppingList.add(shopping);
                    quantityCount =0.00;
                    amountCount =0.00;
                    for (int i = 0; i < productShoppingList.size(); i++) {
                        quantityCount += productShoppingList.get(i).getQuantity();
                        amountCount += productShoppingList.get(i).getAmount();

                    }

                    badgeImage.setVisibility(View.VISIBLE);
                    CBadgeView.setBadgeCount(Double.valueOf(df.format(quantityCount)));


                    for(Product product: HttpResponseCustom)

                    {
                        if(product.getNumber().equals(shopping.getNumber()))
                        {
                            product.setBadgeShow(String.valueOf(df.format(shopping.getQuantity())));
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
                        commonData.setUnitId(productCategory.getUnitId());
                        if(categorycount>0) {
                            commonData.setBadge(String.valueOf(df.format(categorycount)));
                        }
                        categorylist.add(commonData);

                    }

                    leftAdapter = new BasicAdapter(ProductSelectView.this, R.layout.custom_item, categorylist);
                    leftAdapter.setSeclection(leftListSelecte);
                    leftListView.setAdapter(leftAdapter);
                    countShow.setText("¥"+df.format(amountCount));


                }

                if(resultCode==RESULT_FIRST_USER){
                    rightAdapter.notifyDataSetChanged();
                }
                break;
            case 2:
                if(resultCode==RESULT_OK) {

                    PostProductData postDate = new PostProductData();
                    postDate.setName("");
                    postDate.setRequestType(GlobalVariable.cmvCusmtAndCategory);
                    getAcivityPostBean.setOperationType(GlobalVariable.cmvCusmtAndCategory);
                    postDate.setServerIp(Common.ip);
                    postDate.setClassType(1);
                    postDate.setServlet("ProductOperate");
                    getHttpData(postDate);

                }
                if(resultCode==RESULT_FIRST_USER){

                    rightAdapter.notifyDataSetChanged();
                }

                break;
            case 3:
                if(resultCode==RESULT_OK) {

                    for(Product product: HttpResponseCustom)

                    {
                        if(product.getNumber().equals(data.getStringExtra("scanResult")))
                        {
                            intent.removeExtra("type");
                            intent.putExtra("type", "edit");
                            intent.putExtra("acivityPostBen", product);
                            startActivityForResult(intent,1);
                        }


                    }

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
                ProductSelectView.this.finish();
                break;

            case R.id.custom_toobar_right:
                Intent intentnew= new Intent(ProductSelectView.this, ProductForm.class);
                intentnew.removeExtra("product_item");
                intentnew.putExtra("type","add");
                startActivityForResult(intentnew,2);
                break;
            case R.id.customtoobar_screen:
                Intent openCameraIntent=new Intent(ProductSelectView.this,CommonScanActivity.class);
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
        DecimalFormat df = new DecimalFormat("#####0.##");
        HttpResponseCustom.get((Integer) v.getTag()).setBadgeShow("");
        HttpResponseCustom.get((Integer) v.getTag()).setImage(0);
        quantityCount =0;
        amountCount =0.00;
        for (int i = 0; i < productShoppingList.size(); i++) {

            if(productShoppingList.get(i).getNumber().equals(HttpResponseCustom.get((Integer) v.getTag()).getNumber()))
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
            commonData.setUnitId(productCategory.getUnitId());
            if(categorycount>0) {
                commonData.setBadge(String.valueOf(df.format(quantityCount)));
            }
            categorylist.add(commonData);

        }

       rightAdapter.notifyDataSetChanged();
       leftAdapter.notifyDataSetChanged();
        CBadgeView.setBadgeCount(quantityCount);
        countShow.setText("¥"+df.format(amountCount));
        if(amountCount ==0.00)
        {
            badgeImage.setVisibility(View.INVISIBLE);
        }
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
