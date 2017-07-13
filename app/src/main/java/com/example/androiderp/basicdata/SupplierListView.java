package com.example.androiderp.basicdata;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androiderp.CustomDataClass.Supplier;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonAdapter;
import com.example.androiderp.adaper.CommonDataStructure;
import com.example.androiderp.adaper.DataStructure;
import com.example.androiderp.custom.CustomSearch;
import com.example.androiderp.custom.CustomSearchBase;
import com.example.androiderp.form.SupplierForm;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class SupplierListView extends CustomSearchBase implements View.OnClickListener {
    private List<CommonDataStructure> commonDataStructureList = new ArrayList<CommonDataStructure>();
    private List<DataStructure> fruit = new ArrayList<DataStructure>();
    private CommonAdapter adapter;
    private ListView listView;
    private DisplayMetrics dm;
    private List<CommonDataStructure> commonDataStructureSearch = new ArrayList<CommonDataStructure>();
    private List<Supplier> supplierList;
    private TextView toobarBack, toobarAdd, toobarTile;
    private CustomSearch customSearch;
    private Intent intent;
    @Override
    public void iniView(){
        setContentView(R.layout.custom_layout);
        toobarBack =(TextView)findViewById(R.id.custom_toobar_left) ;
        toobarTile =(TextView)findViewById(R.id.custom_toobar_midd);
        toobarTile.setText("供应商");
        toobarAdd =(TextView)findViewById(R.id.custom_toobar_right);
        toobarBack.setOnClickListener(this);
        toobarAdd.setOnClickListener(this);
        toobarTile.setOnClickListener(this);
        customSearch = (CustomSearch) findViewById(R.id.search);
        toobarTile.setCompoundDrawables(null,null,null,null);
        supplierList = DataSupport.findAll(Supplier.class);
        intent=new Intent(SupplierListView.this,SupplierForm.class);
        for(Supplier supplier: supplierList)

        {
            CommonDataStructure commonData=new CommonDataStructure();
            commonData.setName(supplier.getName());
            commonData.setId(supplier.getId());
            commonDataStructureList.add(commonData);



        }

        //构造函数第一参数是类的对象，第二个是布局文件，第三个是数据源
        listView = (ListView) findViewById(R.id.list);
        listView.setTextFilterEnabled(true);
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                        if(commonDataStructureSearch.size()!=0) {

                            intent.putExtra("action", "edit");
                            intent.putExtra("supller_item", String.valueOf(commonDataStructureSearch.get(position).getId()));


                        }else {

                            intent.putExtra("action", "edit");
                            intent.putExtra("supller_item", String.valueOf(commonDataStructureList.get(position).getId()));

                        }
                startActivityForResult(intent,1);

            }
        });
        if(commonDataStructureList.size()!=0) {

                 adapter = new CommonAdapter(SupplierListView.this, R.layout.custom_item, commonDataStructureList);
                 listView.setAdapter(adapter);

        }

        customSearch.addTextChangedListener(textWatcher);


    }

    //筛选条件
    public Object[] search(String name) {
        if(commonDataStructureSearch !=null) {
            commonDataStructureSearch.clear();
        }
        for (int i = 0; i < commonDataStructureList.size(); i++) {
            int index = commonDataStructureList.get(i).getName().indexOf(name);
            // 存在匹配的数据
            if (index != -1) {
                commonDataStructureSearch.add(commonDataStructureList.get(i));
            }
        }
        return commonDataStructureSearch.toArray();
    }

//adapter刷新,重写Filter方式会出现BUG.
    public void updateLayout(Object[] obj) {
        if(commonDataStructureSearch !=null) {
            adapter = new CommonAdapter(SupplierListView.this, R.layout.custom_item, commonDataStructureSearch);
            listView.setAdapter(adapter);
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.custom_toobar_left:
                SupplierListView.this.finish();
                break;

            case R.id.custom_toobar_midd:


                break;

            case R.id.custom_toobar_right:
                intent.putExtra("action","add");
                intent.removeExtra("supller_item");
                startActivityForResult(intent,1);
                break;


        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK)
                {
                    if(commonDataStructureList.size()!=0) {
                        commonDataStructureList.clear();
                    }
                    supplierList = DataSupport.findAll(Supplier.class);
                    for(Supplier category: supplierList)

                    {
                        CommonDataStructure commonData=new CommonDataStructure();
                        commonData.setName(category.getName());
                        commonData.setId(category.getId());
                        commonDataStructureList.add(commonData);



                    }
                    adapter = new CommonAdapter(SupplierListView.this, R.layout.custom_item, commonDataStructureList);
                    listView.setAdapter(adapter);
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

            Object[] obj = search(customSearch.getText().toString());
            updateLayout(obj);

        }
    };
}
