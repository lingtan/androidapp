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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiderp.CustomDataClass.Product;
import com.example.androiderp.CustomDataClass.ProductCategory;
import com.example.androiderp.CustomDataClass.ProductShopping;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonAdapter;
import com.example.androiderp.adaper.CommonDataStructure;
import com.example.androiderp.adaper.DataStructure;
import com.example.androiderp.adaper.ProductAdapter;
import com.example.androiderp.adaper.ProductBadgeAdapter;
import com.example.androiderp.custom.CustomBadgeView;
import com.example.androiderp.custom.CustomSearch;
import com.example.androiderp.custom.CustomSearchBase;
import com.example.androiderp.form.ProductForm;
import com.example.androiderp.form.ProductShoppingForm;

import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ProductBadgeListView extends CustomSearchBase implements View.OnClickListener {
    private ProductBadgeAdapter rightAdapter;
    private CommonAdapter leftAdapter;
    private ListView rightListView;
    private ListView leftListView;
    private DisplayMetrics dm;
    private List<Product> searchDatas= new ArrayList<Product>();
    private List<Product> customAllDatas;
    private TextView toobar_l,toobar_r,toobar_m,bottoncount;
    private CustomSearch search;
    private Intent intent;
    private List<ProductCategory> categoryAllDatas;
    private List<CommonDataStructure> categorylistdatas = new ArrayList<CommonDataStructure>();
    private int countall;
    private double countamount;
   private ImageView imageView;
    private HashSet<Long> pselectedItems = new HashSet<Long>();
    private CustomBadgeView badgeView;
    private List<ProductShopping> shoppings = new ArrayList<ProductShopping>();

    @Override
    public void iniView(){
        setContentView(R.layout.custom_badge_listview_layout);
        toobar_l=(TextView)findViewById(R.id.custom_toobar_left) ;
        toobar_m=(TextView)findViewById(R.id.custom_toobar_midd);
        toobar_r=(TextView)findViewById(R.id.custom_toobar_right);
        toobar_m.setText("商品信息");
        toobar_m.setCompoundDrawables(null,null,null,null);
        toobar_l.setOnClickListener(this);
        toobar_r.setOnClickListener(this);
        toobar_m.setOnClickListener(this);
        search = (CustomSearch) findViewById(R.id.search);
        bottoncount=(TextView)findViewById(R.id.product_item_layout_count) ;
        customAllDatas= DataSupport.findAll(Product.class);
        intent= new Intent(ProductBadgeListView.this, ProductShoppingForm.class);
        categoryAllDatas= DataSupport.findAll(ProductCategory.class);
        CommonDataStructure commonDataAll=new CommonDataStructure();
        commonDataAll.setName("全部产品");
        categorylistdatas.add(commonDataAll);
        CommonDataStructure commonDataN=new CommonDataStructure();
        commonDataN.setName("未分类");
        categorylistdatas.add(commonDataN);
        badgeView = new CustomBadgeView(this);
        badgeView.setTargetView((ImageView)findViewById(R.id.product_shopping_badge));
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
                leftAdapter.setSeclection(position);
                leftAdapter.notifyDataSetInvalidated();
                Object[] obj = categorySearch(categorylistdatas.get(position).getName().toString());
                updateLayout(obj);
            }
        });
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        rightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                imageView=(ImageView) view.findViewById(R.id.custom_item_layout_one_image);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                    }
                });
                if(rightAdapter.selectedItems.contains((long)position)){
                    badgeView.decrementBadgeCount(1);
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

            leftAdapter = new CommonAdapter(ProductBadgeListView.this, R.layout.custom_item, categorylistdatas);
            leftAdapter.setSeclection(0);
            leftListView.setAdapter(leftAdapter);
            rightAdapter = new ProductBadgeAdapter(ProductBadgeListView.this, R.layout.product_badge_item, customAllDatas);
            rightListView.setAdapter(rightAdapter);



        search.addTextChangedListener(textWatcher);


    }

    //筛选条件
    public Object[] search(String name) {
        if(searchDatas!=null) {
            searchDatas.clear();
        }
        for (int i = 0; i < customAllDatas.size(); i++) {
            int index = customAllDatas.get(i).getName().indexOf(name);

            // 存在匹配的数据
            if (index != -1) {
                searchDatas.add(customAllDatas.get(i));
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

            rightAdapter = new ProductBadgeAdapter(ProductBadgeListView.this, R.layout.product_badge_item, searchDatas);
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

                    badgeView.setBadgeCount(countall);
                    if(customAllDatas.size()!=0) {
                        customAllDatas.clear();
                    }
                    customAllDatas= DataSupport.findAll(Product.class);

                    rightAdapter = new ProductBadgeAdapter(ProductBadgeListView.this, R.layout.product_badge_item, customAllDatas);
                    rightAdapter.selectedItems=pselectedItems;
                    rightListView.setAdapter(rightAdapter);

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
                        CommonDataStructure commonData=new CommonDataStructure();
                        commonData.setName(productCategory.getName());
                        commonData.setId(productCategory.getId());
                        categorylistdatas.add(commonData);

                    }

                    leftAdapter = new CommonAdapter(ProductBadgeListView.this, R.layout.custom_item, categorylistdatas);
                    leftAdapter.setSeclection(0);
                    leftListView.setAdapter(leftAdapter);
                    DecimalFormat df = new DecimalFormat("#####0.00");
                    bottoncount.setText(df.format(countamount));
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
                ProductBadgeListView.this.finish();
                break;

            case R.id.custom_toobar_right:

                intent.removeExtra("product_item");
                intent.putExtra("action","add");
                startActivityForResult(intent,1);
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
}