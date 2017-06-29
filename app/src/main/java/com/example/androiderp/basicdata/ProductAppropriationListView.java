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

import com.example.androiderp.CustomDataClass.Product;
import com.example.androiderp.CustomDataClass.ProductCategory;
import com.example.androiderp.CustomDataClass.ProductShopping;
import com.example.androiderp.CustomDataClass.ShoppingData;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonAdapter;
import com.example.androiderp.adaper.CommonDataStructure;
import com.example.androiderp.adaper.ProductBadgeAdapter;
import com.example.androiderp.custom.CustomBadgeView;
import com.example.androiderp.custom.CustomSearch;
import com.example.androiderp.custom.CustomSearchBase;
import com.example.androiderp.form.ProductForm;
import com.example.androiderp.form.ProductShoppingForm;
import com.example.androiderp.form.SaleProductForm;
import com.example.androiderp.scanning.CommonScanActivity;
import com.example.androiderp.scanning.utils.Constant;

import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ProductAppropriationListView extends CustomSearchBase implements View.OnClickListener, ProductBadgeAdapter.Callback{
    private ProductBadgeAdapter rightAdapter;
    private CommonAdapter leftAdapter;
    private ListView rightListView;
    private ListView leftListView;
    private DisplayMetrics dm;
    private List<Product> searchDatas= new ArrayList<Product>();
    private List<Product> customAllDatas;
    private TextView toobar_l,toobar_r,toobar_m,toobar_screen;
    private CustomSearch search;
    private Intent intent;
    private List<ProductCategory> categoryAllDatas;
    private List<CommonDataStructure> categorylistdatas = new ArrayList<CommonDataStructure>();
    private int countall;
    private int categorycount;
    private double countamount;
   private ImageView imageView;
    private HashSet<Long> pselectedItems = new HashSet<Long>();
    private CustomBadgeView badgeView;
    private List<ProductShopping> shoppings = new ArrayList<ProductShopping>();
    private ImageView badgeimage;
    private  int   leftlistselecte;
    private  String   leftlistselectetext;
    private LinearLayout accounts;

    @Override
    public void iniView(){
        setContentView(R.layout.appropriation_listview_layout);
        toobar_l=(TextView)findViewById(R.id.custom_toobar_left) ;
        toobar_m=(TextView)findViewById(R.id.custom_toobar_midd);
        toobar_r=(TextView)findViewById(R.id.custom_toobar_right);
        toobar_screen=(TextView)findViewById(R.id.customtoobar_screen);
        toobar_screen.setOnClickListener(this);
        toobar_m.setText("商品信息");
        toobar_m.setCompoundDrawables(null,null,null,null);
        toobar_l.setOnClickListener(this);
        toobar_r.setOnClickListener(this);
        toobar_m.setOnClickListener(this);
        search = (CustomSearch) findViewById(R.id.search);
        accounts=(LinearLayout)findViewById(R.id.product_item_layout_bottom);
        customAllDatas= DataSupport.findAll(Product.class);
        intent= new Intent(ProductAppropriationListView.this, ProductShoppingForm.class);
        categoryAllDatas= DataSupport.findAll(ProductCategory.class);
        CommonDataStructure commonDataAll=new CommonDataStructure();
        commonDataAll.setName("全部产品");
        categorylistdatas.add(commonDataAll);
        CommonDataStructure commonDataN=new CommonDataStructure();
        commonDataN.setName("未分类");
        categorylistdatas.add(commonDataN);
        badgeView = new CustomBadgeView(this);
        badgeimage=(ImageView)findViewById(R.id.product_shopping_badge);
        badgeView.setTargetView(badgeimage);
        badgeView.setBadgeMargin(25,0,0,0);
        badgeView.setBadgeGravity(Gravity.RIGHT & Gravity.TOP);
        for(ProductCategory productCategory:categoryAllDatas)

        {
            CommonDataStructure commonData=new CommonDataStructure();
            commonData.setName(productCategory.getName());
            commonData.setId(productCategory.getId());
            categorylistdatas.add(commonData);

        }
        //构造函数第一参数是类的对象，第二个是布局文件，第三个是数据源
        leftListView=(ListView) findViewById(R.id.left_list);
        leftListView.setTextFilterEnabled(true);
        rightListView = (ListView) findViewById(R.id.right_list);
        rightListView.setTextFilterEnabled(true);
        leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                leftlistselecte=position;
                leftAdapter.setSeclection(position);
                leftAdapter.notifyDataSetInvalidated();
                Object[] obj = categorySearch(categorylistdatas.get(position).getName().toString());
                updateLayout(obj);
                toobar_m.setText(categorylistdatas.get(position).getName().toString());
                leftlistselectetext=categorylistdatas.get(position).getName().toString();
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
                pselectedItems=rightAdapter.selectedItems;


                intent.removeExtra("action");
                if(searchDatas.size()!=0) {

                    intent.putExtra("action", "edit");
                    intent.putExtra("product_item", String.valueOf(searchDatas.get(position).getId()));


                }else {

                    intent.putExtra("action", "edit");
                    intent.putExtra("product_item", String.valueOf(customAllDatas.get(position).getId()));

                }
                startActivityForResult(intent,1);

            }
        });

            leftAdapter = new CommonAdapter(ProductAppropriationListView.this, R.layout.custom_item, categorylistdatas);
            leftAdapter.setSeclection(0);
            leftListView.setAdapter(leftAdapter);
            Object[] obj = categorySearch(categorylistdatas.get(0).getName().toString());
            updateLayout(obj);



        search.addTextChangedListener(textWatcher);

        accounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(shoppings!=null&shoppings.size()!=0){
                    Intent intentdata=new Intent(ProductAppropriationListView.this, SaleProductForm.class);
                    ShoppingData shoppingData=new ShoppingData();
                    shoppingData.setShoppingdata(shoppings);
                    intentdata.putExtra("shoppingdata",shoppingData);
                    setResult(RESULT_OK,intentdata);
                    ProductAppropriationListView.this.finish();
                }
            }
        });


    }

    //筛选条件
    public Object[] search(String name) {
        if(searchDatas!=null) {
            searchDatas.clear();
        }
        if(leftlistselectetext==null){
            leftlistselectetext="全部产品";
        }
        if(leftlistselectetext.equals("未分类"))
        {
            for (int i = 0; i < customAllDatas.size(); i++) {
                if(customAllDatas.get(i).getCategory()==null)
                { int index = customAllDatas.get(i).getNumber().indexOf(name);
                    if (index != -1) {
                        searchDatas.add(customAllDatas.get(i));
                    }

                }
            }

        }else if (leftlistselectetext.equals("全部产品"))
        {
            for (int i = 0; i < customAllDatas.size(); i++) {
                int index = customAllDatas.get(i).getNumber().indexOf(name);
                if (index != -1) {
                    searchDatas.add(customAllDatas.get(i));
                }

            }

        }

        else {
            for (int i = 0; i < customAllDatas.size(); i++) {
                int index = customAllDatas.get(i).getNumber().indexOf(name);
                int indey = customAllDatas.get(i).getCategory().indexOf(leftlistselectetext);
                // 存在匹配的数据
                if (index != -1 & indey != -1) {
                    searchDatas.add(customAllDatas.get(i));
                }
            }
        }
        return searchDatas.toArray();
    }

    public Object[] categorySearch(String name) {

        if(searchDatas!=null) {
            searchDatas.clear();
        }
        if(name.equals("未分类"))
        {
            for (int i = 0; i < customAllDatas.size(); i++) {
               if(customAllDatas.get(i).getCategory()==null)
               {
                    searchDatas.add(customAllDatas.get(i));
               }
            }

        }else if (name.equals("全部产品"))
        {
            for (int i = 0; i < customAllDatas.size(); i++) {

                    searchDatas.add(customAllDatas.get(i));

            }

        }

        else {
        for (int i = 0; i < customAllDatas.size(); i++) {
              if(customAllDatas.get(i).getCategory()!=null){
                int index = customAllDatas.get(i).getCategory().indexOf(name);
                // 存在匹配的数据
                if (index != -1) {
                    searchDatas.add(customAllDatas.get(i));
                }
            }
        }}
        return searchDatas.toArray();
    }
//adapter刷新,重写Filter方式会出现BUG.
    public void updateLayout(Object[] obj) {
        if(searchDatas!=null) {

            rightAdapter = new ProductBadgeAdapter(ProductAppropriationListView.this, R.layout.product_badge_item, searchDatas,this);
            rightAdapter.selectedItems=pselectedItems;
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
                    Log.d("tongtan",shopping.getSalename());
                    for(ProductShopping shop:shoppings)
                    {

                       if(shop.getSalenumber().equals(shopping.getSalenumber())){
                           shoppings.remove(shop);
                       }
                    }

                    shoppings.add(shopping);
                    countall=0;
                    countamount=0.00;
                    for (int i = 0; i < shoppings.size(); i++) {
                        countall+= shoppings.get(i).getSalefqty();
                        countamount+=shoppings.get(i).getSaleamount();

                    }
                    badgeimage.setVisibility(View.VISIBLE);
                    badgeView.setBadgeCount(countall);
                    for(Product product:searchDatas)

                    {
                        if(product.getNumber().equals(shopping.getSalenumber()))
                        {
                            product.setBadgeshow(String.valueOf(shopping.getSalefqty()));
                        }

                        product.setImage(R.drawable.listvist_item_delete);

                    }
                    rightAdapter.notifyDataSetChanged();


                    if(categorylistdatas.size()!=0)
                    {
                        categorylistdatas.clear();
                    }
                    categoryAllDatas= DataSupport.findAll(ProductCategory.class);
                    CommonDataStructure commonDataAll=new CommonDataStructure();
                    commonDataAll.setName("全部产品");
                    commonDataAll.setBadge(String.valueOf(countall));
                    categorylistdatas.add(commonDataAll);
                    CommonDataStructure commonDataN=new CommonDataStructure();
                    commonDataN.setName("未分类");
                    categorylistdatas.add(commonDataN);
                    for(ProductCategory productCategory:categoryAllDatas)
                    {
                        categorycount=0;
                        for(ProductShopping shop:shoppings)
                    {
                        if(shop.getCategory().equals(productCategory.getName()))
                        {
                            categorycount+=shop.getSalefqty();
                        }



                    }
                        CommonDataStructure commonData=new CommonDataStructure();
                        commonData.setName(productCategory.getName());
                        commonData.setId(productCategory.getId());
                        if(categorycount>0) {
                            commonData.setBadge(String.valueOf(categorycount));
                        }
                        categorylistdatas.add(commonData);

                    }

                    leftAdapter = new CommonAdapter(ProductAppropriationListView.this, R.layout.custom_item, categorylistdatas);
                    leftAdapter.setSeclection(leftlistselecte);
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
                    searchDatas.add(lastProduct);
                    rightAdapter.notifyDataSetChanged();

                }
                if(resultCode==RESULT_FIRST_USER){

                    rightAdapter.notifyDataSetChanged();
                }

                break;
            case 3:
                if(resultCode==RESULT_OK) {

                    for(Product product:searchDatas)

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

            Object[] obj = search(search.getText().toString());
            updateLayout(obj);

        }
    };

    @Override
    public void click(View v) {
        DecimalFormat df = new DecimalFormat("#####0.00");
        searchDatas.get((Integer) v.getTag()).setBadgeshow("");
        searchDatas.get((Integer) v.getTag()).setImage(0);
        countall=0;
        countamount=0.00;
        for (int i = 0; i < shoppings.size(); i++) {

            if(shoppings.get(i).getSalenumber().equals(searchDatas.get((Integer) v.getTag()).getNumber()))
            {
                shoppings.remove(i);
            }
        }

        for(int i = 0; i < shoppings.size(); i++)
        {

            countall+= shoppings.get(i).getSalefqty();
            countamount+=shoppings.get(i).getSaleamount();
        }
        if(categorylistdatas.size()!=0)
        {
            categorylistdatas.clear();
        }
        categoryAllDatas= DataSupport.findAll(ProductCategory.class);
        CommonDataStructure commonDataAll=new CommonDataStructure();
        commonDataAll.setName("全部产品");
        categorylistdatas.add(commonDataAll);
        CommonDataStructure commonDataN=new CommonDataStructure();
        commonDataN.setName("未分类");
        categorylistdatas.add(commonDataN);
        for(ProductCategory productCategory:categoryAllDatas)
        {
            categorycount=0;
            for(ProductShopping shop:shoppings)
            {
                if(shop.getCategory().equals(productCategory.getName()))
                {
                    categorycount+=shop.getSalefqty();
                }



            }
            CommonDataStructure commonData=new CommonDataStructure();
            commonData.setName(productCategory.getName());
            commonData.setId(productCategory.getId());
            if(categorycount>0) {
                commonData.setCategory(String.valueOf(categorycount));
            }
            categorylistdatas.add(commonData);

        }

       rightAdapter.notifyDataSetChanged();
       leftAdapter.notifyDataSetChanged();
        badgeView.setBadgeCount(countall);
        if(countamount==0.00)
        {
            badgeimage.setVisibility(View.INVISIBLE);
        }
    }
}
