package com.example.androiderp.activities.warehouseview;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androiderp.bean.StockTaking;
import com.example.androiderp.R;
import com.example.androiderp.adaper.StockTakingAdapter;
import com.example.androiderp.ui.CSearch;
import com.example.androiderp.ui.CSearchBase;
import com.example.androiderp.activities.warehouseform.AppropriationForm;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class StockTakingView extends CSearchBase implements View.OnClickListener {
    private StockTakingAdapter adapter;
    private ListView listView;
    private DisplayMetrics dm;
    private List<StockTaking> stockInitiSearch= new ArrayList<StockTaking>();
    private List<StockTaking> stockInitiList;
    private TextView back, add, tile;
    private CSearch search;
    private Intent iEdit;
    private Intent iAdd;

    @Override
    public void iniView(){
        setContentView(R.layout.custom_layout);
        back =(TextView)findViewById(R.id.custom_toobar_left) ;
        tile =(TextView)findViewById(R.id.custom_toobar_midd);
        add =(TextView)findViewById(R.id.custom_toobar_right);
        back.setOnClickListener(this);
        add.setOnClickListener(this);
        tile.setOnClickListener(this);
        search = (CSearch) findViewById(R.id.search);
        stockInitiList=DataSupport.findAll(StockTaking.class);
        iEdit = new Intent(StockTakingView.this, StockTakingEntyView.class);
        tile.setText("盘点流水");
        back.setText("返回");

        //构造函数第一参数是类的对象，第二个是布局文件，第三个是数据源
        listView = (ListView) findViewById(R.id.list);
        listView.setTextFilterEnabled(true);
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                iEdit.removeExtra("action");
                        if(stockInitiSearch.size()!=0) {

                            iEdit.putExtra("action", "edit");
                            iEdit.putExtra("custom_item", String.valueOf(stockInitiSearch.get(position).getId()));


                        }else {

                            iEdit.putExtra("action", "edit");
                            iEdit.putExtra("custom_item", String.valueOf(stockInitiList.get(position).getId()));

                        }
                startActivityForResult(iEdit,1);


            }
        });
        if(stockInitiList.size()!=0) {

                 adapter = new StockTakingAdapter(StockTakingView.this, R.layout.appropriation_item, stockInitiList);
                 listView.setAdapter(adapter);
            
        }

        search.addTextChangedListener(textWatcher);

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
            adapter = new StockTakingAdapter(StockTakingView.this, R.layout.saleout_item, stockInitiSearch);
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

                    adapter = new StockTakingAdapter(StockTakingView.this, R.layout.saleout_item, stockInitiList);
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
                StockTakingView.this.finish();
                break;

            case R.id.custom_toobar_right:
                iAdd = new Intent(StockTakingView.this, AppropriationForm.class);
                startActivity(iAdd);
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
