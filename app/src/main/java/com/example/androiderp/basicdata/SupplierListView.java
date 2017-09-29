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
import com.example.androiderp.adaper.CommonAdapterData;
import com.example.androiderp.adaper.DataStructure;
import com.example.androiderp.custom.CustomSearch;
import com.example.androiderp.custom.CustomSearchBase;
import com.example.androiderp.form.SupplierForm;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class SupplierListView extends CustomSearchBase implements View.OnClickListener {
    private List<CommonAdapterData> commonAdapterDataList = new ArrayList<CommonAdapterData>();
    private List<DataStructure> fruit = new ArrayList<DataStructure>();
    private CommonAdapter adapter;
    private ListView listView;
    private DisplayMetrics dm;
    private List<CommonAdapterData> commonAdapterDataSearch = new ArrayList<CommonAdapterData>();
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
            CommonAdapterData commonData=new CommonAdapterData();
            commonData.setName(supplier.getName());
            commonData.setUnitId(supplier.getId());
            commonAdapterDataList.add(commonData);



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

                        if(commonAdapterDataSearch.size()!=0) {

                            intent.putExtra("action", "edit");
                            intent.putExtra("supller_item", String.valueOf(commonAdapterDataSearch.get(position).getUnitId()));


                        }else {

                            intent.putExtra("action", "edit");
                            intent.putExtra("supller_item", String.valueOf(commonAdapterDataList.get(position).getUnitId()));

                        }
                startActivityForResult(intent,1);

            }
        });
        if(commonAdapterDataList.size()!=0) {

                 adapter = new CommonAdapter(SupplierListView.this, R.layout.custom_item, commonAdapterDataList);
                 listView.setAdapter(adapter);

        }

        customSearch.addTextChangedListener(textWatcher);


    }

    //筛选条件
    public Object[] search(String name) {
        if(commonAdapterDataSearch !=null) {
            commonAdapterDataSearch.clear();
        }
        for (int i = 0; i < commonAdapterDataList.size(); i++) {
            int index = commonAdapterDataList.get(i).getName().indexOf(name);
            // 存在匹配的数据
            if (index != -1) {
                commonAdapterDataSearch.add(commonAdapterDataList.get(i));
            }
        }
        return commonAdapterDataSearch.toArray();
    }

//adapter刷新,重写Filter方式会出现BUG.
    public void updateLayout(Object[] obj) {
        if(commonAdapterDataSearch !=null) {
            adapter = new CommonAdapter(SupplierListView.this, R.layout.custom_item, commonAdapterDataSearch);
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
                    if(commonAdapterDataList.size()!=0) {
                        commonAdapterDataList.clear();
                    }
                    supplierList = DataSupport.findAll(Supplier.class);
                    for(Supplier category: supplierList)

                    {
                        CommonAdapterData commonData=new CommonAdapterData();
                        commonData.setName(category.getName());
                        commonData.setUnitId(category.getId());
                        commonAdapterDataList.add(commonData);



                    }
                    adapter = new CommonAdapter(SupplierListView.this, R.layout.custom_item, commonAdapterDataList);
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
