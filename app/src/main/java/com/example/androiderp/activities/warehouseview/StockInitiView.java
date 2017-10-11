package com.example.androiderp.activities.warehouseview;

import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androiderp.bean.Stock;
import com.example.androiderp.bean.StockInitiData;
import com.example.androiderp.bean.StockInitiTem;
import com.example.androiderp.R;
import com.example.androiderp.adaper.StockInitiAdapter;
import com.example.androiderp.ui.CSearchBase;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class StockInitiView extends CSearchBase implements View.OnClickListener {
    private List<StockInitiTem> stockInitiTemList = new ArrayList<StockInitiTem>();
    private StockInitiAdapter adapter;
    private ListView listView;
    private DisplayMetrics dm;
    private List<Stock> stockList;
    private TextView toobarBack, toobarAdd, toobarTile;

    @Override
    public void iniView(){
        setContentView(R.layout.stockiniti_layout);
        toobarBack =(TextView)findViewById(R.id.custom_toobar_left) ;
        toobarTile =(TextView)findViewById(R.id.custom_toobar_midd);
        toobarAdd =(TextView)findViewById(R.id.custom_toobar_right);
        toobarBack.setOnClickListener(this);
        toobarAdd.setOnClickListener(this);
        toobarTile.setText("初始库存数量");
        toobarAdd.setText("确认");
        Intent intent=getIntent();
        StockInitiData stockInitiData=intent.getParcelableExtra("stockinitidata");
        stockInitiTemList =stockInitiData.getShoppingdata();

        toobarAdd.setCompoundDrawables(null,null,null,null);
        stockList = DataSupport.findAll(Stock.class);
        if(stockInitiTemList.size()==0) {
            for (Stock stock : stockList)

            {
                StockInitiTem stockInitiTem = new StockInitiTem();
                stockInitiTem.setName(stock.getName());
                stockInitiTemList.add(stockInitiTem);


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
        if(stockInitiTemList.size()!=0) {

                 adapter = new StockInitiAdapter(StockInitiView.this, R.layout.stockiniti_item, stockInitiTemList);
                 listView.setAdapter(adapter);
            
        }


    }



    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.custom_toobar_left:
                StockInitiView.this.finish();
                break;

            case R.id.custom_toobar_midd:

                break;

            case R.id.custom_toobar_right:
                Intent intentdata=getIntent();
                StockInitiData stockInitiData=new StockInitiData();
                stockInitiData.setShoppingdata(stockInitiTemList);
                intentdata.putExtra("stockinitidata",stockInitiData);
                setResult(RESULT_OK,intentdata);
                StockInitiView.this.finish();
                break;


        }

    }

}
