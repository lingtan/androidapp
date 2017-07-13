package com.example.androiderp.basicdata;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androiderp.CustomDataClass.SalesOut;
import com.example.androiderp.R;
import com.example.androiderp.adaper.SaleOutAdapter;
import com.example.androiderp.custom.CustomSearch;
import com.example.androiderp.custom.CustomSearchBase;
import com.example.androiderp.form.PurchaseProductForm;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class SupplierOutListView extends CustomSearchBase implements View.OnClickListener {
    private SaleOutAdapter adapter;
    private ListView listView;
    private DisplayMetrics dm;
    private List<SalesOut> salesOutSearch = new ArrayList<SalesOut>();
    private List<SalesOut> salesOutList;
    private TextView toobarBack, toobarAdd, toobarTile;
    private CustomSearch customSearch;
    private Intent intentEdit;
    private Intent intentadd;

    @Override
    public void iniView(){
        setContentView(R.layout.custom_layout);
        toobarBack =(TextView)findViewById(R.id.custom_toobar_left) ;
        toobarTile =(TextView)findViewById(R.id.custom_toobar_midd);
        toobarAdd =(TextView)findViewById(R.id.custom_toobar_right);
        toobarBack.setOnClickListener(this);
        toobarAdd.setOnClickListener(this);
        toobarTile.setOnClickListener(this);
        customSearch = (CustomSearch) findViewById(R.id.search);
        salesOutList =DataSupport.where("billtype =?","1").find(SalesOut.class);
        intentEdit = new Intent(SupplierOutListView.this, SupplierOutEntyList.class);
        toobarTile.setText("采购流水");
        toobarBack.setText("首页");

        //构造函数第一参数是类的对象，第二个是布局文件，第三个是数据源
        listView = (ListView) findViewById(R.id.list);
        listView.setTextFilterEnabled(true);
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                intentEdit.removeExtra("action");
                        if(salesOutSearch.size()!=0) {

                            intentEdit.putExtra("action", "edit");
                            intentEdit.putExtra("custom_item", String.valueOf(salesOutSearch.get(position).getId()));


                        }else {

                            intentEdit.putExtra("action", "edit");
                            intentEdit.putExtra("custom_item", String.valueOf(salesOutList.get(position).getId()));

                        }
                startActivityForResult(intentEdit,1);


            }
        });
        if(salesOutList.size()!=0) {

                 adapter = new SaleOutAdapter(SupplierOutListView.this, R.layout.saleout_item, salesOutList);
                 listView.setAdapter(adapter);
            
        }

        customSearch.addTextChangedListener(textWatcher);

    }

    //筛选条件
    public Object[] search(String name) {
        if(salesOutSearch !=null) {
            salesOutSearch.clear();
        }
        for (int i = 0; i < salesOutList.size(); i++) {
            int index = salesOutList.get(i).getCustomer().indexOf(name);

            // 存在匹配的数据
            if (index != -1) {
                salesOutSearch.add(salesOutList.get(i));
            }
        }
        return salesOutSearch.toArray();
    }

    public Object[] categorySearch(String name) {

        if(salesOutSearch !=null) {
            salesOutSearch.clear();
        }

        for (int i = 0; i < salesOutList.size(); i++) {

        }
        return salesOutSearch.toArray();
    }
//adapter刷新,重写Filter方式会出现BUG.
    public void updateLayout(Object[] obj) {
        if(salesOutSearch !=null) {
            adapter = new SaleOutAdapter(SupplierOutListView.this, R.layout.saleout_item, salesOutSearch);
            listView.setAdapter(adapter);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK)
                {
                    if(salesOutList.size()!=0) {
                        salesOutList.clear();
                    }
                    salesOutList =DataSupport.where("billtype =?","1").find(SalesOut.class);

                    adapter = new SaleOutAdapter(SupplierOutListView.this, R.layout.saleout_item, salesOutList);
                    listView.setAdapter(adapter);
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
                SupplierOutListView.this.finish();
                break;

            case R.id.custom_toobar_right:
                intentadd = new Intent(SupplierOutListView.this, PurchaseProductForm.class);
                startActivity(intentadd);
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
}
