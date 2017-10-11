package com.example.androiderp.activities.purchaseview;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androiderp.bean.Product;
import com.example.androiderp.bean.ProductCategory;
import com.example.androiderp.bean.ProductShopping;
import com.example.androiderp.bean.SalesOutEnty;
import com.example.androiderp.bean.ShoppingData;
import com.example.androiderp.bean.StockIniti;
import com.example.androiderp.bean.StockTakingEnty;
import com.example.androiderp.R;
import com.example.androiderp.adaper.AppropriationBadgeAdapter;
import com.example.androiderp.adaper.CommonAdapter;
import com.example.androiderp.adaper.CommonAdapterData;
import com.example.androiderp.ui.CBadgeView;
import com.example.androiderp.ui.CSearch;
import com.example.androiderp.ui.CSearchBase;
import com.example.androiderp.activities.warehouseform.AppropriationShoppingForm;
import com.example.androiderp.activities.basicfrom.ProductForm;
import com.example.androiderp.activities.salesfrom.SaleForm;
import com.example.androiderp.scanning.CommonScanActivity;
import com.example.androiderp.scanning.utils.Constant;

import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ProductStockTakingView extends CSearchBase implements View.OnClickListener, AppropriationBadgeAdapter.Callback{
    private AppropriationBadgeAdapter rightAdapter;
    private CommonAdapter leftAdapter;
    private ListView rightListView;
    private ListView leftListView;
    private DisplayMetrics dm;
    private List<Product> productSearch = new ArrayList<Product>();
    private List<Product> productList;
    private TextView toobarBack, toobarAdd, toobarTile, toobarScreen;
    private CSearch search;
    private Intent intent;
    private List<ProductCategory> productCategoryList;
    private List<CommonAdapterData> categorylist = new ArrayList<CommonAdapterData>();
    private double quantityCount;
    private double categorycount;
    private double amountCount;
    private HashSet<Long> selectedItems = new HashSet<Long>();
    private CBadgeView CBadgeView;
    private List<ProductShopping> productShoppingList = new ArrayList<ProductShopping>();
    private ImageView badgeImage;
    private  int leftListSelecte;
    private  String leftListSelecteText;
    private LinearLayout account;
    private List<StockIniti> stockInitiList = new ArrayList<StockIniti>();
    private List<StockTakingEnty> stockTakingEntyList = new ArrayList<StockTakingEnty>();
    private List<SalesOutEnty> salesOutEntyList;
    private List<SalesOutEnty> supplierOutEntieList;
    private double fqty;
    private String appropriOutValue;
    private DecimalFormat df = new DecimalFormat("#####0.##");

    @Override
    public void iniView(){
        setContentView(R.layout.appropriation_listview_layout);
        toobarBack =(TextView)findViewById(R.id.custom_toobar_left) ;
        toobarTile =(TextView)findViewById(R.id.custom_toobar_midd);
        toobarAdd =(TextView)findViewById(R.id.custom_toobar_right);
        toobarScreen =(TextView)findViewById(R.id.customtoobar_screen);
        toobarScreen.setOnClickListener(this);
        toobarTile.setText("商品信息");
        toobarTile.setCompoundDrawables(null,null,null,null);
        toobarBack.setOnClickListener(this);
        toobarAdd.setOnClickListener(this);
        toobarTile.setOnClickListener(this);
        Intent intentValue=getIntent();
        appropriOutValue=intentValue.getStringExtra("appropriout");
        search = (CSearch) findViewById(R.id.search);
        account =(LinearLayout)findViewById(R.id.product_item_layout_bottom);
        productList = DataSupport.findAll(Product.class);
        stockInitiList = DataSupport.findAll(StockIniti.class);
        stockTakingEntyList = DataSupport.findAll(StockTakingEnty.class);
        salesOutEntyList =DataSupport.where("billtype =?","2").find(SalesOutEnty.class);
        supplierOutEntieList =DataSupport.where("billtype =?","1").find(SalesOutEnty.class);
        for(Product product: productList)

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
        intent= new Intent(ProductStockTakingView.this, AppropriationShoppingForm.class);
        productCategoryList = DataSupport.findAll(ProductCategory.class);
        CommonAdapterData commonDataAll=new CommonAdapterData();
        commonDataAll.setName("全部产品");
        categorylist.add(commonDataAll);
        CommonAdapterData commonDataN=new CommonAdapterData();
        commonDataN.setName("未分类");
        categorylist.add(commonDataN);
        CBadgeView = new CBadgeView(this);
        badgeImage =(ImageView)findViewById(R.id.product_shopping_badge);
        CBadgeView.setTargetView(badgeImage);
        CBadgeView.setBadgeMargin(25,0,0,0);
        CBadgeView.setBadgeGravity(Gravity.RIGHT & Gravity.TOP);
        for(ProductCategory productCategory: productCategoryList)

        {
            CommonAdapterData commonData=new CommonAdapterData();
            commonData.setName(productCategory.getName());
            commonData.setUnitId(productCategory.getId());
            categorylist.add(commonData);

        }
        //构造函数第一参数是类的对象，第二个是布局文件，第三个是数据源
        leftListView=(ListView) findViewById(R.id.left_list);
        leftListView.setTextFilterEnabled(true);
        rightListView = (ListView) findViewById(R.id.right_list);
        rightListView.setTextFilterEnabled(true);
        leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                leftListSelecte =position;
                leftAdapter.setSeclection(position);
                leftAdapter.notifyDataSetInvalidated();
                Object[] obj = categorySearch(categorylist.get(position).getName().toString());
                updateLayout(obj);
                toobarTile.setText(categorylist.get(position).getName().toString());
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

            leftAdapter = new CommonAdapter(ProductStockTakingView.this, R.layout.custom_item, categorylist);
            leftAdapter.setSeclection(0);
            leftListView.setAdapter(leftAdapter);
            Object[] obj = categorySearch(categorylist.get(0).getName().toString());
            updateLayout(obj);



        search.addTextChangedListener(textWatcher);

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(productShoppingList !=null& productShoppingList.size()!=0){
                    Intent intentdata=new Intent(ProductStockTakingView.this, SaleForm.class);
                    ShoppingData shoppingData=new ShoppingData();
                    shoppingData.setProductShoppingList(productShoppingList);
                    intentdata.putExtra("shoppingdata",shoppingData);
                    setResult(RESULT_OK,intentdata);
                    ProductStockTakingView.this.finish();
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
            for (int i = 0; i < productList.size(); i++) {
                if(productList.get(i).getCategory()==null)
                { int index = productList.get(i).getNumber().indexOf(name);
                    if (index != -1) {
                        productSearch.add(productList.get(i));
                    }

                }
            }

        }else if (leftListSelecteText.equals("全部产品"))
        {
            for (int i = 0; i < productList.size(); i++) {
                int index = productList.get(i).getNumber().indexOf(name);
                if (index != -1) {
                    productSearch.add(productList.get(i));
                }

            }

        }

        else {
            for (int i = 0; i < productList.size(); i++) {
                int index = productList.get(i).getNumber().indexOf(name);
                int indey = productList.get(i).getCategory().indexOf(leftListSelecteText);
                // 存在匹配的数据
                if (index != -1 & indey != -1) {
                    productSearch.add(productList.get(i));
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
            for (int i = 0; i < productList.size(); i++) {
               if(productList.get(i).getCategory()==null)
               {
                    productSearch.add(productList.get(i));
               }
            }

        }else if (name.equals("全部产品"))
        {
            for (int i = 0; i < productList.size(); i++) {

                    productSearch.add(productList.get(i));

            }

        }

        else {
        for (int i = 0; i < productList.size(); i++) {
              if(productList.get(i).getCategory()!=null){
                int index = productList.get(i).getCategory().indexOf(name);
                // 存在匹配的数据
                if (index != -1) {
                    productSearch.add(productList.get(i));
                }
            }
        }}
        return productSearch.toArray();
    }
//adapter刷新,重写Filter方式会出现BUG.
    public void updateLayout(Object[] obj) {
        if(productSearch !=null) {

            rightAdapter = new AppropriationBadgeAdapter(ProductStockTakingView.this, R.layout.appropriation_badge_item, productSearch,this);
            rightAdapter.selectedItems= selectedItems;
            rightListView.setAdapter(rightAdapter);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK)
                {
                    DecimalFormat df = new DecimalFormat("#####0.##");
                    ProductShopping shopping=(ProductShopping) data.getParcelableExtra("shop_data");
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

                    }
                    badgeImage.setVisibility(View.VISIBLE);
                    CBadgeView.setBadgeCount(quantityCount);
                    for(Product product: productSearch)

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
                    productCategoryList = DataSupport.findAll(ProductCategory.class);
                    CommonAdapterData commonDataAll=new CommonAdapterData();
                    commonDataAll.setName("全部产品");
                    commonDataAll.setBadge(String.valueOf(df.format(quantityCount)));
                    categorylist.add(commonDataAll);
                    CommonAdapterData commonDataN=new CommonAdapterData();
                    commonDataN.setName("未分类");
                    categorylist.add(commonDataN);
                    for(ProductCategory productCategory: productCategoryList)
                    {
                        categorycount=0;
                        for(ProductShopping shop: productShoppingList)
                    {
                        if(shop.getCategory().equals(productCategory.getName()))
                        {
                            categorycount+=shop.getQuantity();
                        }



                    }
                        CommonAdapterData commonData=new CommonAdapterData();
                        commonData.setName(productCategory.getName());
                        commonData.setUnitId(productCategory.getId());
                        if(categorycount>0) {
                            commonData.setBadge(String.valueOf(df.format(categorycount)));
                        }
                        categorylist.add(commonData);

                    }

                    leftAdapter = new CommonAdapter(ProductStockTakingView.this, R.layout.custom_item, categorylist);
                    leftAdapter.setSeclection(leftListSelecte);
                    leftListView.setAdapter(leftAdapter);
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

                    for(Product product: productSearch)

                    {
                        if(product.getNumber().equals(data.getStringExtra("scanResult")))
                        {
                            intent.removeExtra("action");
                            intent.putExtra("action", "edit");
                            intent.putExtra("product_item", String.valueOf(product.getId()));
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
                ProductStockTakingView.this.finish();
                break;

            case R.id.custom_toobar_right:
                Intent intentnew= new Intent(ProductStockTakingView.this, ProductForm.class);
                intentnew.removeExtra("product_item");
                intentnew.putExtra("action","add");
                startActivityForResult(intentnew,2);
                break;
            case R.id.customtoobar_screen:
                Intent openCameraIntent=new Intent(ProductStockTakingView.this,CommonScanActivity.class);
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
        productCategoryList = DataSupport.findAll(ProductCategory.class);
        CommonAdapterData commonDataAll=new CommonAdapterData();
        commonDataAll.setName("全部产品");
        categorylist.add(commonDataAll);
        CommonAdapterData commonDataN=new CommonAdapterData();
        commonDataN.setName("未分类");
        categorylist.add(commonDataN);
        for(ProductCategory productCategory: productCategoryList)
        {
            categorycount=0;
            for(ProductShopping shop: productShoppingList)
            {
                if(shop.getCategory().equals(productCategory.getName()))
                {
                    categorycount+=shop.getQuantity();
                }



            }
            CommonAdapterData commonData=new CommonAdapterData();
            commonData.setName(productCategory.getName());
            commonData.setUnitId(productCategory.getId());
            if(categorycount>0) {
                commonData.setCategory(String.valueOf(categorycount));
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
}
