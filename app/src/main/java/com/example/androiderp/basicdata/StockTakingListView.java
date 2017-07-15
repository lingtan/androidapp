package com.example.androiderp.basicdata;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androiderp.CustomDataClass.Appropriation;
import com.example.androiderp.CustomDataClass.StockIniti;
import com.example.androiderp.CustomDataClass.StockTaking;
import com.example.androiderp.R;
import com.example.androiderp.adaper.AppropriationAdapter;
import com.example.androiderp.adaper.StockTakingAdapter;
import com.example.androiderp.custom.CustomSearch;
import com.example.androiderp.custom.CustomSearchBase;
import com.example.androiderp.form.AppropriationForm;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class StockTakingListView extends CustomSearchBase implements View.OnClickListener {
    private StockTakingAdapter adapter;
    private ListView listView;
    private DisplayMetrics dm;
    private List<StockTaking> stockInitiSearch= new ArrayList<StockTaking>();
    private List<StockTaking> stockInitiList;
    private TextView toobarBack,toobarAdd,toobarTile;
    private CustomSearch customSearch;
    private Intent intentEdit;
    private Intent intentAdd;

    @Override
    public void iniView(){
        setContentView(R.layout.custom_layout);
        toobarBack=(TextView)findViewById(R.id.custom_toobar_left) ;
        toobarTile=(TextView)findViewById(R.id.custom_toobar_midd);
        toobarAdd=(TextView)findViewById(R.id.custom_toobar_right);
        toobarBack.setOnClickListener(this);
        toobarAdd.setOnClickListener(this);
        toobarTile.setOnClickListener(this);
        customSearch = (CustomSearch) findViewById(R.id.search);
        stockInitiList=DataSupport.findAll(StockTaking.class);
        intentEdit= new Intent(StockTakingListView.this, StockTakingEntyList.class);
        toobarTile.setText("盘点流水");
        toobarBack.setText("返回");

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
                        if(stockInitiSearch.size()!=0) {

                            intentEdit.putExtra("action", "edit");
                            intentEdit.putExtra("custom_item", String.valueOf(stockInitiSearch.get(position).getId()));


                        }else {

                            intentEdit.putExtra("action", "edit");
                            intentEdit.putExtra("custom_item", String.valueOf(stockInitiList.get(position).getId()));

                        }
                startActivityForResult(intentEdit,1);


            }
        });
        if(stockInitiList.size()!=0) {

                 adapter = new StockTakingAdapter(StockTakingListView.this, R.layout.appropriation_item, stockInitiList);
                 listView.setAdapter(adapter);
            
        }

        customSearch.addTextChangedListener(textWatcher);

    }

    //筛选条件
    public Object[] search(String name) {
        if(stockInitiSearch!=null) {
            stockInitiSearch.clear();
        }
        for (int i = 0; i < stockInitiList.size(); i++) {
            int index = stockInitiList.get(i).getNuber().indexOf(name);

            // 存在匹配的数据
            if (index != -1) {
                stockInitiSearch.add(stockInitiList.get(i));
            }
        }
        return stockInitiSearch.toArray();
    }

    public Object[] categorySearch(String name) {

        if(stockInitiSearch!=null) {
            stockInitiSearch.clear();
        }

        for (int i = 0; i < stockInitiList.size(); i++) {

        }
        return stockInitiSearch.toArray();
    }
//adapter刷新,重写Filter方式会出现BUG.
    public void updateLayout(Object[] obj) {
        if(stockInitiSearch!=null) {
            adapter = new StockTakingAdapter(StockTakingListView.this, R.layout.saleout_item, stockInitiSearch);
            listView.setAdapter(adapter);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK)
                {
                    if(stockInitiList.size()!=0) {
                        stockInitiList.clear();
                    }
                    stockInitiList=DataSupport.findAll(StockTaking.class);

                    adapter = new StockTakingAdapter(StockTakingListView.this, R.layout.saleout_item, stockInitiList);
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
                StockTakingListView.this.finish();
                break;

            case R.id.custom_toobar_right:
                intentAdd = new Intent(StockTakingListView.this, AppropriationForm.class);
                startActivity(intentAdd);
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
