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
import com.example.androiderp.form.SaleProductForm;

import org.litepal.crud.DataSupport;
import java.util.ArrayList;
import java.util.List;

public class SaleOutListView extends CustomSearchBase implements View.OnClickListener {
    private SaleOutAdapter adapter;
    private ListView listView;
    private DisplayMetrics dm;
    private List<SalesOut> searchDatas= new ArrayList<SalesOut>();
    private List<SalesOut> customAllDatas;
    private TextView toobar_l,toobar_r,toobar_m;
    private CustomSearch search;
    private Intent intent;
    private Intent intentadd;

    @Override
    public void iniView(){
        setContentView(R.layout.custom_layout);
        toobar_l=(TextView)findViewById(R.id.custom_toobar_left) ;
        toobar_m=(TextView)findViewById(R.id.custom_toobar_midd);
        toobar_r=(TextView)findViewById(R.id.custom_toobar_right);
        toobar_l.setOnClickListener(this);
        toobar_r.setOnClickListener(this);
        toobar_m.setOnClickListener(this);
        search = (CustomSearch) findViewById(R.id.search);
        customAllDatas=DataSupport.where("billtype =?","2").find(SalesOut.class);
        intent= new Intent(SaleOutListView.this, SaleOutEntyList.class);
        toobar_m.setText("销售流水");
        toobar_l.setText("首页");

        //构造函数第一参数是类的对象，第二个是布局文件，第三个是数据源
        listView = (ListView) findViewById(R.id.list);
        listView.setTextFilterEnabled(true);
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                intent.removeExtra("action");
                        if(searchDatas.size()!=0) {

                            intent.putExtra("action", "edit");
                            intent.putExtra("custom_item", String.valueOf(searchDatas.get(position).getId()));


                        }else {

                            intent.putExtra("action", "edit");
                            intent.putExtra("custom_item", String.valueOf(customAllDatas.get(position).getId()));

                        }
                startActivityForResult(intent,1);


            }
        });
        if(customAllDatas.size()!=0) {

                 adapter = new SaleOutAdapter(SaleOutListView.this, R.layout.saleout_item, customAllDatas);
                 listView.setAdapter(adapter);
            
        }

        search.addTextChangedListener(textWatcher);

    }

    //筛选条件
    public Object[] search(String name) {
        if(searchDatas!=null) {
            searchDatas.clear();
        }
        for (int i = 0; i < customAllDatas.size(); i++) {
            int index = customAllDatas.get(i).getCustomer().indexOf(name);

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

        for (int i = 0; i < customAllDatas.size(); i++) {

        }
        return searchDatas.toArray();
    }
//adapter刷新,重写Filter方式会出现BUG.
    public void updateLayout(Object[] obj) {
        if(searchDatas!=null) {
            adapter = new SaleOutAdapter(SaleOutListView.this, R.layout.saleout_item, searchDatas);
            listView.setAdapter(adapter);
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
                    customAllDatas=DataSupport.where("billtype =?","2").find(SalesOut.class);

                    adapter = new SaleOutAdapter(SaleOutListView.this, R.layout.saleout_item, customAllDatas);
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
                SaleOutListView.this.finish();
                break;

            case R.id.custom_toobar_right:
                intentadd = new Intent(SaleOutListView.this, SaleProductForm.class);
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

            Object[] obj = search(search.getText().toString());
            updateLayout(obj);

        }
    };
}
