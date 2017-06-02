package com.example.androiderp.basicdata;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androiderp.CustomDataClass.CustomCategory;
import com.example.androiderp.CustomDataClass.SupplierCategory;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonAdapter;
import com.example.androiderp.adaper.CommonDataStructure;
import com.example.androiderp.adaper.DataStructure;
import com.example.androiderp.custom.CustomSearch;
import com.example.androiderp.custom.CustomSearchBase;
import com.example.androiderp.form.CustomCategoryForm;
import com.example.androiderp.form.SupplierCategoryForm;
import com.example.androiderp.home.ErpHome;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class SupplierCategoryList extends CustomSearchBase implements View.OnClickListener {
    private List<CommonDataStructure> listdatas = new ArrayList<CommonDataStructure>();
    private List<DataStructure> fruit = new ArrayList<DataStructure>();
    private CommonAdapter adapter;
    private ListView plistView;
    private DisplayMetrics dm;
    private List<CommonDataStructure> searchdatas= new ArrayList<CommonDataStructure>();
    private List<SupplierCategory> customlist;
    private TextView custom_toobar_l,custom_toobar_r,custom_toobar_m;
    private CustomSearch custom_search;
    private String categoryid;
    private int pposition;
    private int indexpositon;
    private String indexname;
    @Override
    public void iniView(){
        setContentView(R.layout.custom_layout);
        custom_toobar_l=(TextView)findViewById(R.id.custom_toobar_left) ;
        custom_toobar_m=(TextView)findViewById(R.id.custom_toobar_midd);
        custom_toobar_m.setText("供应商分类");
        custom_toobar_r=(TextView)findViewById(R.id.custom_toobar_right);
        custom_toobar_l.setOnClickListener(this);
        custom_toobar_r.setOnClickListener(this);
        custom_toobar_m.setOnClickListener(this);
        custom_search = (CustomSearch) findViewById(R.id.search);
        customlist= DataSupport.findAll(SupplierCategory.class);
        for(SupplierCategory supplierCategory:customlist)

        {
            CommonDataStructure commonData=new CommonDataStructure();
            commonData.setName(supplierCategory.getName());
            commonData.setId(supplierCategory.getId());
            listdatas.add(commonData);



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
                Intent intent=new Intent(SupplierCategoryList.this,SupplierCategoryForm.class);
                        if(searchdatas.size()!=0) {

                            intent.putExtra("action", "edit");
                            intent.putExtra("customid", String.valueOf(searchdatas.get(position).getId()));


                        }else {

                            intent.putExtra("action", "edit");
                            intent.putExtra("customid", String.valueOf(listdatas.get(position).getId()));

                        }
                startActivity(intent);
                SupplierCategoryList.this.finish();


            }
        });
        Intent intent=getIntent();
        categoryid=intent.getStringExtra("category");
        if(listdatas.size()!=0) {
             if(categoryid!=null) {
                 Object[] obj = searchCategory(categoryid);
                 updateLayout(obj);
                 custom_toobar_m.setText(categoryid);
             }else {
                 adapter = new CommonAdapter(SupplierCategoryList.this, R.layout.custom_item, listdatas);
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
    public void updateLayout(Object[] obj) {
        if(searchdatas!=null) {
            adapter = new CommonAdapter(SupplierCategoryList.this, R.layout.custom_item, searchdatas);
            plistView.setAdapter(adapter);
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.custom_toobar_left:
                Intent cate = new Intent(SupplierCategoryList.this, SupplierCategoryForm.class);
                cate.putExtra("action","add");
                startActivity(cate);

                break;

            case R.id.custom_toobar_midd:


                break;

            case R.id.custom_toobar_right:
                SupplierCategoryList.this.finish();
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

            Object[] obj = searchItem(custom_search.getText().toString());
            updateLayout(obj);

        }
    };
}
