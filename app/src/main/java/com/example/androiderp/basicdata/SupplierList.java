package com.example.androiderp.basicdata;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androiderp.CustomDataClass.Custom;
import com.example.androiderp.CustomDataClass.CustomCategory;
import com.example.androiderp.CustomDataClass.Supplier;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonAdapter;
import com.example.androiderp.adaper.CommonDataStructure;
import com.example.androiderp.adaper.DataStructure;
import com.example.androiderp.custom.CustomSearch;
import com.example.androiderp.custom.CustomSearchBase;
import com.example.androiderp.form.CustomCategoryForm;
import com.example.androiderp.form.SupplierForm;
import com.example.androiderp.home.ErpHome;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class SupplierList extends CustomSearchBase implements View.OnClickListener {
    private List<CommonDataStructure> supplierlistdatas = new ArrayList<CommonDataStructure>();
    private List<DataStructure> fruit = new ArrayList<DataStructure>();
    private CommonAdapter adapter;
    private ListView listView;
    private DisplayMetrics dm;
    private List<CommonDataStructure> suppliersearchdatas= new ArrayList<CommonDataStructure>();
    private List<Supplier> supplieralldatas;
    private TextView toobar_l,toobar_r,toobar_m;
    private CustomSearch search;
    private Intent intent;
    @Override
    public void iniView(){
        setContentView(R.layout.custom_layout);
        toobar_l=(TextView)findViewById(R.id.custom_toobar_left) ;
        toobar_m=(TextView)findViewById(R.id.custom_toobar_midd);
        toobar_m.setText("供应商");
        toobar_r=(TextView)findViewById(R.id.custom_toobar_right);
        toobar_l.setOnClickListener(this);
        toobar_r.setOnClickListener(this);
        toobar_m.setOnClickListener(this);
        search = (CustomSearch) findViewById(R.id.search);
        toobar_m.setCompoundDrawables(null,null,null,null);
        supplieralldatas= DataSupport.findAll(Supplier.class);
        intent=new Intent(SupplierList.this,SupplierForm.class);
        for(Supplier supplier:supplieralldatas)

        {
            CommonDataStructure commonData=new CommonDataStructure();
            commonData.setName(supplier.getName());
            commonData.setId(supplier.getId());
            supplierlistdatas.add(commonData);



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

                        if(suppliersearchdatas.size()!=0) {

                            intent.putExtra("action", "edit");
                            intent.putExtra("supller_item", String.valueOf(suppliersearchdatas.get(position).getId()));


                        }else {

                            intent.putExtra("action", "edit");
                            intent.putExtra("supller_item", String.valueOf(supplierlistdatas.get(position).getId()));

                        }
                startActivityForResult(intent,1);

            }
        });
        if(supplierlistdatas.size()!=0) {

                 adapter = new CommonAdapter(SupplierList.this, R.layout.custom_item, supplierlistdatas);
                 listView.setAdapter(adapter);

        }

        search.addTextChangedListener(textWatcher);


    }

    //筛选条件
    public Object[] search(String name) {
        if(suppliersearchdatas!=null) {
            suppliersearchdatas.clear();
        }
        for (int i = 0; i < supplierlistdatas.size(); i++) {
            int index = supplierlistdatas.get(i).getName().indexOf(name);
            // 存在匹配的数据
            if (index != -1) {
                suppliersearchdatas.add(supplierlistdatas.get(i));
            }
        }
        return suppliersearchdatas.toArray();
    }

//adapter刷新,重写Filter方式会出现BUG.
    public void updateLayout(Object[] obj) {
        if(suppliersearchdatas!=null) {
            adapter = new CommonAdapter(SupplierList.this, R.layout.custom_item, suppliersearchdatas);
            listView.setAdapter(adapter);
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.custom_toobar_left:
                SupplierList.this.finish();
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
                    if(supplierlistdatas.size()!=0) {
                        supplierlistdatas.clear();
                    }
                    supplieralldatas= DataSupport.findAll(Supplier.class);
                    for(Supplier category:supplieralldatas)

                    {
                        CommonDataStructure commonData=new CommonDataStructure();
                        commonData.setName(category.getName());
                        commonData.setId(category.getId());
                        supplierlistdatas.add(commonData);



                    }
                    adapter = new CommonAdapter(SupplierList.this, R.layout.custom_item, supplierlistdatas);
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

            Object[] obj = search(search.getText().toString());
            updateLayout(obj);

        }
    };
}
