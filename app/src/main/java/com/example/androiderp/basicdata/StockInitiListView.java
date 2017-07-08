package com.example.androiderp.basicdata;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androiderp.CustomDataClass.Custom;
import com.example.androiderp.CustomDataClass.CustomCategory;
import com.example.androiderp.CustomDataClass.ShoppingData;
import com.example.androiderp.CustomDataClass.Stock;
import com.example.androiderp.CustomDataClass.StockInitiData;
import com.example.androiderp.CustomDataClass.StockInitiTem;
import com.example.androiderp.CustomDataClass.User;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonAdapter;
import com.example.androiderp.adaper.CommonDataStructure;
import com.example.androiderp.adaper.DataStructure;
import com.example.androiderp.adaper.PopuMenuDataStructure;
import com.example.androiderp.adaper.StockInitiAdapter;
import com.example.androiderp.common.Common;
import com.example.androiderp.custom.CustomSearch;
import com.example.androiderp.custom.CustomSearchBase;
import com.example.androiderp.form.CustomForm;

import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StockInitiListView extends CustomSearchBase implements View.OnClickListener {
    private List<StockInitiTem> customListDatas = new ArrayList<StockInitiTem>();
    private StockInitiAdapter adapter;
    private ListView listView;
    private DisplayMetrics dm;
    private List<Stock> customAllDatas;
    private TextView toobar_l,toobar_r,toobar_m;

    @Override
    public void iniView(){
        setContentView(R.layout.stockiniti_layout);
        toobar_l=(TextView)findViewById(R.id.custom_toobar_left) ;
        toobar_m=(TextView)findViewById(R.id.custom_toobar_midd);
        toobar_r=(TextView)findViewById(R.id.custom_toobar_right);
        toobar_l.setOnClickListener(this);
        toobar_r.setOnClickListener(this);
        toobar_m.setText("初始库存数量");
        toobar_r.setText("确认");
        Intent intent=getIntent();
        StockInitiData stockInitiData=intent.getParcelableExtra("stockinitidata");
        customListDatas=stockInitiData.getShoppingdata();

        toobar_r.setCompoundDrawables(null,null,null,null);
        customAllDatas= DataSupport.findAll(Stock.class);
        if(customListDatas.size()==0) {
            for (Stock stock : customAllDatas)

            {
                StockInitiTem stockInitiTem = new StockInitiTem();
                stockInitiTem.setSalename(stock.getName());
                customListDatas.add(stockInitiTem);


            }
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

            }
        });
        if(customListDatas.size()!=0) {

                 adapter = new StockInitiAdapter(StockInitiListView.this, R.layout.stockiniti_item, customListDatas);
                 listView.setAdapter(adapter);
            
        }


    }



    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.custom_toobar_left:
                StockInitiListView.this.finish();
                break;

            case R.id.custom_toobar_midd:

                break;

            case R.id.custom_toobar_right:
                Intent intentdata=getIntent();
                StockInitiData stockInitiData=new StockInitiData();
                stockInitiData.setShoppingdata(customListDatas);
                intentdata.putExtra("stockinitidata",stockInitiData);
                setResult(RESULT_OK,intentdata);
                StockInitiListView.this.finish();
                break;


        }

    }

}
