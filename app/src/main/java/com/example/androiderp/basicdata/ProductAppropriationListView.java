package com.example.androiderp.basicdata;

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

import com.example.androiderp.CustomDataClass.AppropriationEnty;
import com.example.androiderp.CustomDataClass.Product;
import com.example.androiderp.CustomDataClass.ProductCategory;
import com.example.androiderp.CustomDataClass.ProductShopping;
import com.example.androiderp.CustomDataClass.SalesOutEnty;
import com.example.androiderp.CustomDataClass.ShoppingData;
import com.example.androiderp.CustomDataClass.StockIniti;
import com.example.androiderp.R;
import com.example.androiderp.adaper.AppropriationBadgeAdapter;
import com.example.androiderp.adaper.CommonAdapter;
import com.example.androiderp.adaper.CommonDataStructure;
import com.example.androiderp.custom.CustomBadgeView;
import com.example.androiderp.custom.CustomSearch;
import com.example.androiderp.custom.CustomSearchBase;
import com.example.androiderp.form.AppropriationShoppingForm;
import com.example.androiderp.form.ProductForm;
import com.example.androiderp.form.SaleProductForm;
import com.example.androiderp.scanning.CommonScanActivity;
import com.example.androiderp.scanning.utils.Constant;

import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ProductAppropriationListView extends CustomSearchBase implements View.OnClickListener, AppropriationBadgeAdapter.Callback{
    private AppropriationBadgeAdapter rightAdapter;
    private CommonAdapter leftAdapter;
    private ListView rightListView;
    private ListView leftListView;
    private DisplayMetrics dm;
    private List<Product> productSearch = new ArrayList<Product>();
    private List<Product> productList;
    private TextView toobarBack, toobarAdd, toobarTile, toobarScreen;
    private CustomSearch customSearch;
    private Intent intent;
    private List<ProductCategory> productCategoryList;
    private List<CommonDataStructure> categorylist = new ArrayList<CommonDataStructure>();
    private double quantityCount;
    private double categorycount;
    private double amountCount;
    private HashSet<Long> selectedItems = new HashSet<Long>();
    private CustomBadgeView badgeView;
    private List<ProductShopping> productShoppingList = new ArrayList<ProductShopping>();
    private ImageView badgeImage;
    private  int leftListSelecte;
    private  String leftListSelecteText;
    private LinearLayout account;
    private List<StockIniti> stockInitiList = new ArrayList<StockIniti>();
    private List<SalesOutEnty> salesOutEntyList;
    private List<SalesOutEnty> supplierOutEntieList;
    private double fqty;
    private double quantity;
    private int  stockCheck=1;
    private String appropriOutValue;
    private List<Integer> stockCheckList=new ArrayList<Integer>();
    private DecimalFormat df = new DecimalFormat("#####0.00");

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
        customSearch = (CustomSearch) findViewById(R.id.search);
        account =(LinearLayout)findViewById(R.id.product_item_layout_bottom);
        productList = DataSupport.findAll(Product.class);
        stockInitiList = DataSupport.findAll(StockIniti.class);
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
        intent= new Intent(ProductAppropriationListView.this, AppropriationShoppingForm.class);
        productCategoryList = DataSupport.findAll(ProductCategory.class);
        CommonDataStructure commonDataAll=new CommonDataStructure();
        commonDataAll.setName("全部产品");
        categorylist.add(commonDataAll);
        CommonDataStructure commonDataN=new CommonDataStructure();
        commonDataN.setName("未分类");
        categorylist.add(commonDataN);
        badgeView = new CustomBadgeView(this);
        badgeImage =(ImageView)findViewById(R.id.product_shopping_badge);
        badgeView.setTargetView(badgeImage);
        badgeView.setBadgeMargin(25,0,0,0);
        badgeView.setBadgeGravity(Gravity.RIGHT & Gravity.TOP);
        for(ProductCategory productCategory: productCategoryList)

        {
            CommonDataStructure commonData=new CommonDataStructure();
            commonData.setName(productCategory.getName());
            commonData.setId(productCategory.getId());
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

            leftAdapter = new CommonAdapter(ProductAppropriationListView.this, R.layout.custom_item, categorylist);
            leftAdapter.setSeclection(0);
            leftListView.setAdapter(leftAdapter);
            Object[] obj = categorySearch(categorylist.get(0).getName().toString());
            updateLayout(obj);



        customSearch.addTextChangedListener(textWatcher);

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(productShoppingList !=null& productShoppingList.size()!=0){
                    Intent intentdata=new Intent(ProductAppropriationListView.this, SaleProductForm.class);
                    ShoppingData shoppingData=new ShoppingData();
                    shoppingData.setProductShoppingList(productShoppingList);
                    intentdata.putExtra("shoppingdata",shoppingData);
                    setResult(RESULT_OK,intentdata);
                    ProductAppropriationListView.this.finish();
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

            rightAdapter = new AppropriationBadgeAdapter(ProductAppropriationListView.this, R.layout.appropriation_badge_item, productSearch,this);
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

                    ProductShopping shopping=(ProductShopping) data.getParcelableExtra("shop_data");
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
                            Toast.makeText(ProductAppropriationListView.this,"调出仓库数量不足，实际库存为："+df.format(quantity),Toast.LENGTH_SHORT).show();
                        }

                    }
                    badgeImage.setVisibility(View.VISIBLE);
                    badgeView.setBadgeCount(quantityCount);
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
                    productCategoryList = DataSupport.findAll(ProductCategory.class);
                    CommonDataStructure commonDataAll=new CommonDataStructure();
                    commonDataAll.setName("全部产品");
                    commonDataAll.setBadge(String.valueOf(quantityCount));
                    categorylist.add(commonDataAll);
                    CommonDataStructure commonDataN=new CommonDataStructure();
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
                        CommonDataStructure commonData=new CommonDataStructure();
                        commonData.setName(productCategory.getName());
                        commonData.setId(productCategory.getId());
                        if(categorycount>0) {
                            commonData.setBadge(String.valueOf(categorycount));
                        }
                        categorylist.add(commonData);

                    }

                    leftAdapter = new CommonAdapter(ProductAppropriationListView.this, R.layout.custom_item, categorylist);
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
                ProductAppropriationListView.this.finish();
                break;

            case R.id.custom_toobar_right:
                Intent intentnew= new Intent(ProductAppropriationListView.this, ProductForm.class);
                intentnew.removeExtra("product_item");
                intentnew.putExtra("action","add");
                startActivityForResult(intentnew,2);
                break;
            case R.id.customtoobar_screen:
                Intent openCameraIntent=new Intent(ProductAppropriationListView.this,CommonScanActivity.class);
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

            Object[] obj = search(customSearch.getText().toString());
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
        productCategoryList = DataSupport.findAll(ProductCategory.class);
        CommonDataStructure commonDataAll=new CommonDataStructure();
        commonDataAll.setName("全部产品");
        categorylist.add(commonDataAll);
        CommonDataStructure commonDataN=new CommonDataStructure();
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
            CommonDataStructure commonData=new CommonDataStructure();
            commonData.setName(productCategory.getName());
            commonData.setId(productCategory.getId());
            if(categorycount>0) {
                commonData.setCategory(String.valueOf(categorycount));
            }
            categorylist.add(commonData);

        }

       rightAdapter.notifyDataSetChanged();
       leftAdapter.notifyDataSetChanged();
        badgeView.setBadgeCount(quantityCount);
        if(amountCount ==0.00)
        {
            badgeImage.setVisibility(View.INVISIBLE);
        }



    }
    private int stockCheck(String stockname,String number,double sfqty)
    {

        double  in=DataSupport.where("instock=? and itemnumber=?",stockname,number).sum(AppropriationEnty.class,"itemfqty",double.class);
        double  out=DataSupport.where("outstock=? and itemnumber=?",stockname,number).sum(AppropriationEnty.class,"itemfqty",double.class);
        double  initis=DataSupport.where("stock=? and number=?",stockname,number).sum(StockIniti.class,"fqty",double.class);
        double   salesOut=DataSupport.where("billtype =? and stock=? and itemnumber=?","2",stockname,number).sum(SalesOutEnty.class,"itemfqty",double.class);
        double  supplierin=DataSupport.where("billtype =? and stock=? and itemnumber=?","1",stockname,number).sum(SalesOutEnty.class,"itemfqty",double.class);
        quantity=0.00;
        quantity=initis+supplierin+in-salesOut-out;

        if(sfqty>quantity)
        {
            stockCheck=0;
            stockCheckList.add(stockCheck);
        }else {
            stockCheck=1;
        }
        return  stockCheck;
    }
}
