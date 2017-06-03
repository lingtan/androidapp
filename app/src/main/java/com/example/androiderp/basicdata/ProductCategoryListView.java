package com.example.androiderp.basicdata;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androiderp.CustomDataClass.ProductCategory;
import com.example.androiderp.CustomDataClass.SupplierCategory;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonAdapter;
import com.example.androiderp.adaper.CommonDataStructure;
import com.example.androiderp.custom.CustomSearch;
import com.example.androiderp.custom.CustomSearchBase;
import com.example.androiderp.form.ProductCategoryForm;
import com.example.androiderp.form.SupplierCategoryForm;
import com.example.androiderp.form.SupplierForm;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryListView extends CustomSearchBase implements View.OnClickListener {
    private List<CommonDataStructure> listdatas = new ArrayList<CommonDataStructure>();
    private CommonAdapter adapter;
    private ListView plistView;
    private DisplayMetrics dm;
    private List<CommonDataStructure> searchdatas= new ArrayList<CommonDataStructure>();
    private List<ProductCategory> customlist;
    private TextView custom_toobar_l,custom_toobar_r,custom_toobar_m;
    private CustomSearch custom_search;
    private String categoryid;
    private ImageView lastCheckedOption;
    private int pposition;
    private int indexpositon;
    private String indexname;
    private int searchindex=-1;
    @Override
    public void iniView(){
        setContentView(R.layout.custom_layout);
        custom_toobar_l=(TextView)findViewById(R.id.custom_toobar_left) ;
        custom_toobar_m=(TextView)findViewById(R.id.custom_toobar_midd);
        custom_toobar_m.setText("商品分类");
        custom_toobar_r=(TextView)findViewById(R.id.custom_toobar_right);
        custom_toobar_l.setOnClickListener(this);
        custom_toobar_r.setOnClickListener(this);
        custom_toobar_m.setOnClickListener(this);
        custom_search = (CustomSearch) findViewById(R.id.search);
        customlist= DataSupport.findAll(ProductCategory.class);

        custom_toobar_l.setCompoundDrawables(null,null,null,null);
        Toolbar.LayoutParams params = new Toolbar.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 0, 0, 0);
        custom_toobar_l.setLayoutParams(params);
        Drawable del= getResources().getDrawable(R.drawable.suppliercategory_delete);
        del.setBounds(0, 0, del.getMinimumWidth(), del.getMinimumHeight());
        custom_toobar_r.setCompoundDrawables(del,null,null,null);
        custom_toobar_m.setCompoundDrawables(null,null,null,null);
        Intent intent=getIntent();
        categoryid=intent.getStringExtra("category");
        indexname=intent.getStringExtra("index");
        custom_toobar_l.setText("新增分类");
        for(ProductCategory productCategory:customlist)

        {
            if(productCategory.getName().equals(indexname))
            {
                indexpositon =customlist.indexOf(productCategory);
            }
            CommonDataStructure commonData=new CommonDataStructure();
            commonData.setName(productCategory.getName());
            commonData.setId(productCategory.getId());
            commonData.setImage(R.drawable.seclec_arrow);
            listdatas.add(commonData);



        }
        if(indexname.isEmpty())
        {
            indexpositon=-1;
        }else {
            pposition = indexpositon;
        }
        //构造函数第一参数是类的对象，第二个是布局文件，第三个是数据源
        plistView = (ListView) findViewById(R.id.list);
        plistView.setTextFilterEnabled(true);
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        plistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                Intent intent=new Intent();
                        if(searchdatas.size()!=0) {

                            intent.putExtra("data_return", searchdatas.get(position).getName());
                            indexname=searchdatas.get(position).getName();

                        }else {

                            intent.putExtra("data_return", listdatas.get(position).getName());
                            indexname=listdatas.get(position).getName();
                        }
               setResult(RESULT_OK,intent);
                if(lastCheckedOption != null){
                    lastCheckedOption.setVisibility(View.INVISIBLE);
                }
                lastCheckedOption = (ImageView)view.findViewById(R.id.custom_item_layout_one_image);
                lastCheckedOption.setVisibility(View.VISIBLE);
                pposition=position;
                ProductCategoryListView.this.finish();


            }
        });


        if(listdatas.size()!=0) {
             if(categoryid!=null) {
                 Object[] obj = searchCategory(categoryid);
                 updateLayout("10");
                 custom_toobar_m.setText(categoryid);
             }else {
                 adapter = new CommonAdapter(ProductCategoryListView.this, R.layout.custom_item, listdatas);
                 adapter.setSeclection(indexpositon);
                 plistView.setAdapter(adapter);
             }

        }

        custom_search.addTextChangedListener(textWatcher);


    }

    //筛选条件
    public Object[] searchItem(String name) {
        if(searchdatas!=null) {
            searchdatas.clear();
        }
        for (int i = 0; i < listdatas.size(); i++) {
            int index = listdatas.get(i).getName().indexOf(name);
            // 存在匹配的数据
            if (index != -1) {
                searchdatas.add(listdatas.get(i));
            }
        }
        return searchdatas.toArray();
    }

    public Object[] searchCategory(String name) {

        if(searchdatas!=null) {
            searchdatas.clear();
        }
        for (int i = 0; i < listdatas.size(); i++) {
            ;
            if(listdatas.get(i).getCategory()!=null) {
                int index = listdatas.get(i).getCategory().indexOf(name);
                // 存在匹配的数据
                if (index != -1) {
                    searchdatas.add(listdatas.get(i));
                }
            }
        }
        return searchdatas.toArray();
    }
//adapter刷新,重写Filter方式会出现BUG.
    public void updateLayout(String name) {
        if(searchdatas!=null) {
            searchindex=-1;
            if(!name.isEmpty())
            {
                for(int i=0;i<searchdatas.size();i++)
                {
                    if(searchdatas.get(i).getName().equals(indexname))
                    {
                        searchindex=i;
                    }
                }
            }else
            {
                searchindex=pposition;
            }
            adapter = new CommonAdapter(ProductCategoryListView.this, R.layout.custom_item, searchdatas);
            adapter.setSeclection(searchindex);
            plistView.setAdapter(adapter);
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.custom_toobar_left:
                Intent cate = new Intent(ProductCategoryListView.this, ProductCategoryForm.class);
                cate.putExtra("action","add");
                startActivityForResult(cate,2);

                break;

            case R.id.custom_toobar_midd:


                break;

            case R.id.custom_toobar_right:
                ProductCategoryListView.this.finish();
                break;


        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 2:
                if(resultCode==RESULT_OK){
                    if(listdatas.size()!=0) {
                        listdatas.clear();
                    }
                    customlist= DataSupport.findAll(ProductCategory.class);
                    for(ProductCategory productCategory:customlist)

                    {
                        CommonDataStructure commonData=new CommonDataStructure();
                        commonData.setName(productCategory.getName());
                        commonData.setId(productCategory.getId());
                        commonData.setImage(R.drawable.seclec_arrow);
                        listdatas.add(commonData);



                    }
                    adapter = new CommonAdapter(ProductCategoryListView.this, R.layout.custom_item, listdatas);
                    adapter.setSeclection(pposition);
                    plistView.setAdapter(adapter);
                }
                break;
            default:
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

            Object[] obj = searchItem(custom_search.getText().toString());
            updateLayout(custom_search.getText().toString());

        }
    };
}
