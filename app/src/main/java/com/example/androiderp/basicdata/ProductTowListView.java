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
import org.litepal.crud.DataSupport;
import java.util.ArrayList;
import java.util.List;

public class ProductTowListView extends CustomSearchBase implements View.OnClickListener {
    private ProductAdapter rightAdapter;
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

    @Override
    public void iniView(){
        setContentView(R.layout.custom_listview_layout);
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
        intent= new Intent(ProductTowListView.this, ProductForm.class);
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
        bottoncount.setText(String.valueOf(customAllDatas.size()));
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

            leftAdapter = new CommonAdapter(ProductTowListView.this, R.layout.custom_item, categorylistdatas);
            leftAdapter.setSeclection(0);
            leftListView.setAdapter(leftAdapter);
            rightAdapter = new ProductAdapter(ProductTowListView.this, R.layout.product_item, customAllDatas);
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
            rightAdapter = new ProductAdapter(ProductTowListView.this, R.layout.product_item, searchDatas);
            rightListView.setAdapter(rightAdapter);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK)
                {
                    if(customAllDatas.size()!=0) {
                        customAllDatas.clear();
                    }
                    customAllDatas= DataSupport.findAll(Product.class);

                    rightAdapter = new ProductAdapter(ProductTowListView.this, R.layout.product_item, customAllDatas);
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

                    leftAdapter = new CommonAdapter(ProductTowListView.this, R.layout.custom_item, categorylistdatas);
                    leftAdapter.setSeclection(0);
                    leftListView.setAdapter(leftAdapter);
                    bottoncount.setText(String.valueOf(customAllDatas.size()));
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
