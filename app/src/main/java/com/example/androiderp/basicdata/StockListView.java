package com.example.androiderp.basicdata;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiderp.CustomDataClass.SalesOut;
import com.example.androiderp.CustomDataClass.Stock;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonAdapterData;
import com.example.androiderp.adaper.CommonListViewAdapter;
import com.example.androiderp.adaper.DataStructure;
import com.example.androiderp.custom.CustomSearch;
import com.example.androiderp.custom.CustomSearchBase;
import com.example.androiderp.form.StockForm;
import com.example.androiderp.listview.Menu;
import com.example.androiderp.listview.MenuItem;
import com.example.androiderp.listview.SlideAndDragListView;
import com.example.androiderp.listview.Utils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class StockListView extends CustomSearchBase implements View.OnClickListener,
        AdapterView.OnItemClickListener,
        SlideAndDragListView.OnMenuItemClickListener, SlideAndDragListView.OnItemDeleteListener {
    private List<CommonAdapterData> commonAdapterDataList = new ArrayList<CommonAdapterData>();
    private CommonListViewAdapter adapter;
    private SlideAndDragListView<CommonAdapterData> listView;
    private DisplayMetrics dm;
    private List<CommonAdapterData> commonAdapterDataSearch = new ArrayList<CommonAdapterData>();
    private List<Stock> stockList;
    private TextView toobarBack, toobarAdd, toobarTile;
    private CustomSearch customSearch;
    private String categoryid;
    private Menu menu;
    private List<SalesOut> salesOutList =new ArrayList<SalesOut>();
    @Override
    public void iniView(){
        setContentView(R.layout.customlistview_category_layout);
        initMenu();
        initUiAndListener();
        toobarBack =(TextView)findViewById(R.id.custom_toobar_left) ;
        toobarTile =(TextView)findViewById(R.id.custom_toobar_midd);
        toobarTile.setText("新增仓库");
        toobarAdd =(TextView)findViewById(R.id.custom_toobar_right);
        toobarBack.setOnClickListener(this);
        toobarAdd.setOnClickListener(this);
        toobarTile.setOnClickListener(this);
        customSearch = (CustomSearch) findViewById(R.id.search);
        Intent intent=getIntent();
        categoryid=intent.getStringExtra("category");
        stockList = DataSupport.findAll(Stock.class);
        toobarTile.setCompoundDrawables(null,null,null,null);
        for(Stock stock: stockList)

        {
            CommonAdapterData commonData=new CommonAdapterData();
            commonData.setName(stock.getName());
            commonData.setId(stock.getId());
            commonData.setImage(R.drawable.seclec_arrow);
            commonAdapterDataList.add(commonData);



        }
        //构造函数第一参数是类的对象，第二个是布局文件，第三个是数据源
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        if(commonAdapterDataList.size()!=0) {
             if(categoryid!=null) {
                 Object[] obj = searchCategory(categoryid);
                 updateLayout("10");
                 toobarTile.setText(categoryid);
             }else {
                 adapter = new CommonListViewAdapter(StockListView.this, R.layout.custom_item, commonAdapterDataList);
                 listView.setAdapter(adapter);
             }

        }

        customSearch.addTextChangedListener(textWatcher);




    }
    public void initMenu() {
        menu = new Menu(true);
        menu.addItem(new MenuItem.Builder().setWidth((int) getResources().getDimension(R.dimen.slv_item_bg_btn_width))
                .setBackground(Utils.getDrawable(this, R.drawable.btn_right0))
                .setText("编辑")
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setTextColor(Color.BLACK)
                .setTextSize(14)
                .build());
        menu.addItem(new MenuItem.Builder().setWidth((int) getResources().getDimension(R.dimen.slv_item_bg_btn_width_img))
                .setBackground(Utils.getDrawable(this, R.drawable.btn_right1))
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setText("删除")
                .setTextSize(14)
                .build());
    }

    public void initUiAndListener() {
        listView = (SlideAndDragListView) findViewById(R.id.custom_listview_category);
        listView.setMenu(menu);
        listView.setOnItemClickListener(this);
        listView.setOnMenuItemClickListener(this);
        listView.setOnItemDeleteListener(this);
    }
    @Override
    public int onMenuItemClick(View v, final int itemPosition, int buttonPosition, int direction) {
        switch (direction) {
            case MenuItem.DIRECTION_RIGHT:
                switch (buttonPosition) {
                    case 0:
                        Intent intent=new Intent(StockListView.this,StockForm.class);
                        if(commonAdapterDataSearch.size()!=0) {

                            intent.putExtra("action", "edit");
                            intent.putExtra("customid", String.valueOf(commonAdapterDataSearch.get(itemPosition).getId()));

                        }else {

                            intent.putExtra("action", "edit");
                            intent.putExtra("customid", String.valueOf(commonAdapterDataList.get(itemPosition).getId()));
                        }
                        startActivityForResult(intent,1);

                        return Menu.ITEM_NOTHING;
                    case 1:
                        AlertDialog.Builder dialog=new AlertDialog.Builder(StockListView.this);
                        dialog.setTitle("提示");
                        dialog.setMessage("您确认要删除该仓库？");
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(isCustom(commonAdapterDataList.get(itemPosition - listView.getHeaderViewsCount()).getName().toString()))
                                {
                                    Toast.makeText(StockListView.this,"已经有业务发生，不能删除",Toast.LENGTH_SHORT).show();
                                    adapter.notifyDataSetChanged();
                                }else {
                                    DataStructure.deleteAll(Stock.class, "name = ?", commonAdapterDataList.get(itemPosition - listView.getHeaderViewsCount()).getName().toString());

                                    AlertDialog.Builder dialogOK = new AlertDialog.Builder(StockListView.this);
                                    dialogOK.setMessage("该仓库已经删除");
                                    dialogOK.setNegativeButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            commonAdapterDataList.remove(itemPosition - listView.getHeaderViewsCount());
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                                    dialogOK.show();


                                }

                            }
                        });
                        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        dialog.show();
                    return Menu.ITEM_NOTHING;
                }
        }
        return Menu.ITEM_NOTHING;
    }
    @Override
    public void onItemDelete(View view, int position) {


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }
    //筛选条件
    public Object[] searchItem(String name) {
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

    public Object[] searchCategory(String name) {

        if(commonAdapterDataSearch !=null) {
            commonAdapterDataSearch.clear();
        }
        for (int i = 0; i < commonAdapterDataList.size(); i++) {
            if(commonAdapterDataList.get(i).getCategory()!=null) {
                int index = commonAdapterDataList.get(i).getCategory().indexOf(name);
                // 存在匹配的数据
                if (index != -1) {
                    commonAdapterDataSearch.add(commonAdapterDataList.get(i));
                }
            }
        }
        return commonAdapterDataSearch.toArray();
    }
//adapter刷新,重写Filter方式会出现BUG.
    public void updateLayout(String name) {
        if(commonAdapterDataSearch !=null) {

            adapter = new CommonListViewAdapter(StockListView.this, R.layout.custom_item, commonAdapterDataSearch);
            listView.setAdapter(adapter);
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.custom_toobar_left:
                StockListView.this.finish();
                break;

            case R.id.custom_toobar_midd:


                break;

            case R.id.custom_toobar_right:
                Intent cate = new Intent(StockListView.this, StockForm.class);
                cate.putExtra("action","add");
                startActivityForResult(cate,1);
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
                    stockList = DataSupport.findAll(Stock.class);
                    for(Stock stock: stockList)

                    {
                        CommonAdapterData commonData=new CommonAdapterData();
                        commonData.setName(stock.getName());
                        commonData.setId(stock.getId());
                        commonData.setImage(R.drawable.seclec_arrow);
                        commonAdapterDataList.add(commonData);



                    }
                    adapter = new CommonListViewAdapter(StockListView.this, R.layout.custom_item, commonAdapterDataList);
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

            Object[] obj = searchItem(customSearch.getText().toString());
            updateLayout(customSearch.getText().toString());

        }
    };

    public boolean isCustom(String name)
    {

        salesOutList =DataSupport.where("stock =?",name).find(SalesOut.class);

        if (salesOutList.size()>0)
        {
            return true;
        }else {
            return false;
        }

    }
}
