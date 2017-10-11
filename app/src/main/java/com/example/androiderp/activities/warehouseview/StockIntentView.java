package com.example.androiderp.activities.warehouseview;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiderp.bean.ProductCategory;
import com.example.androiderp.bean.SalesOut;
import com.example.androiderp.bean.Stock;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonAdapterData;
import com.example.androiderp.adaper.CommonListViewAdapter;
import com.example.androiderp.bean.DataStructure;
import com.example.androiderp.ui.CSearch;
import com.example.androiderp.ui.CSearchBase;
import com.example.androiderp.activities.basicfrom.ProductCategoryForm;
import com.example.androiderp.activities.basicfrom.StockForm;
import com.example.androiderp.listview.Menu;
import com.example.androiderp.listview.MenuItem;
import com.example.androiderp.listview.SlideAndDragListView;
import com.example.androiderp.listview.Utils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class StockIntentView extends CSearchBase implements View.OnClickListener,
        AdapterView.OnItemClickListener,
        SlideAndDragListView.OnMenuItemClickListener, SlideAndDragListView.OnItemDeleteListener {
    private List<CommonAdapterData> listdatas = new ArrayList<CommonAdapterData>();
    private CommonListViewAdapter adapter;
    private SlideAndDragListView<CommonAdapterData> listView;
    private DisplayMetrics dm;
    private List<CommonAdapterData> commonAdapterDataSearch = new ArrayList<CommonAdapterData>();
    private List<Stock> stockList;
    private TextView toobarBack, toobarAdd, toobarTile;
    private CSearch search;
    private String categoryid;
    private ImageView lastCheckedOption;
    private int positionTemp;
    private int indexPositon;
    private String indexName;
    private Menu menu;
    private List<SalesOut> salesOutList =new ArrayList<SalesOut>();
    @Override
    public void iniView(){
        setContentView(R.layout.customlistview_category_layout);
        initMenu();
        initUiAndListener();
        toobarBack =(TextView)findViewById(R.id.custom_toobar_left) ;
        toobarTile =(TextView)findViewById(R.id.custom_toobar_midd);
        toobarTile.setText("仓库");
        toobarAdd =(TextView)findViewById(R.id.custom_toobar_right);
        toobarBack.setOnClickListener(this);
        toobarAdd.setOnClickListener(this);
        toobarTile.setOnClickListener(this);
        search = (CSearch) findViewById(R.id.search);
        Intent intent=getIntent();
        categoryid=intent.getStringExtra("category");
        indexName =intent.getStringExtra("index");
        stockList = DataSupport.findAll(Stock.class);
        toobarTile.setCompoundDrawables(null,null,null,null);
        for(Stock stock: stockList)

        {   if(stock.getName().equals(indexName))
        {
            indexPositon = stockList.indexOf(stock);
        }

            CommonAdapterData commonData=new CommonAdapterData();
            commonData.setName(stock.getName());
            commonData.setUnitId(stock.getId());
            commonData.setImage(R.drawable.seclec_arrow);
            listdatas.add(commonData);



        }
        if(indexName.isEmpty())
        {
            indexPositon =-1;
        }else {
            positionTemp = indexPositon;
        }

        //构造函数第一参数是类的对象，第二个是布局文件，第三个是数据源
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        if(listdatas.size()!=0) {
             if(categoryid!=null) {
                 Object[] obj = searchCategory(categoryid);
                 updateLayout("10");
                 toobarTile.setText(categoryid);
             }else {
                 adapter = new CommonListViewAdapter(StockIntentView.this, R.layout.custom_item, listdatas);
                 adapter.setSeclection(indexPositon);
                 listView.setAdapter(adapter);
             }

        }

        search.addTextChangedListener(textWatcher);




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
                        Intent intent=new Intent(StockIntentView.this,ProductCategoryForm.class);
                        if(commonAdapterDataSearch.size()!=0) {

                            intent.putExtra("action", "edit");
                            intent.putExtra("customid", String.valueOf(commonAdapterDataSearch.get(itemPosition).getUnitId()));
                            indexName = commonAdapterDataSearch.get(itemPosition).getName();

                        }else {

                            intent.putExtra("action", "edit");
                            intent.putExtra("customid", String.valueOf(listdatas.get(itemPosition).getUnitId()));
                            indexName =listdatas.get(itemPosition).getName();
                        }
                        startActivityForResult(intent,1);

                        return Menu.ITEM_NOTHING;
                    case 1:
                        AlertDialog.Builder dialog=new AlertDialog.Builder(StockIntentView.this);
                        dialog.setTitle("提示");
                        dialog.setMessage("您确认要删除该发货方式？");
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(isCustom(listdatas.get(itemPosition - listView.getHeaderViewsCount()).getName().toString()))
                                {
                                    Toast.makeText(StockIntentView.this,"已经有业务发生，不能删除",Toast.LENGTH_SHORT).show();
                                    adapter.notifyDataSetChanged();
                                }else {
                                    DataStructure.deleteAll(ProductCategory.class, "name = ?", listdatas.get(itemPosition - listView.getHeaderViewsCount()).getName().toString());

                                    AlertDialog.Builder dialogOK = new AlertDialog.Builder(StockIntentView.this);
                                    dialogOK.setMessage("该发货方式已经删除");
                                    dialogOK.setNegativeButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (indexPositon == itemPosition) {
                                                indexPositon = -1;
                                            }
                                            listdatas.remove(itemPosition - listView.getHeaderViewsCount());
                                            adapter.setSeclection(indexPositon);
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


        Intent intent=new Intent(StockIntentView.this,ProductCategoryForm.class);
        if(commonAdapterDataSearch.size()!=0) {

            intent.putExtra("action", "edit");
            intent.putExtra("data_return", String.valueOf(commonAdapterDataSearch.get(position).getName()));
            indexName = commonAdapterDataSearch.get(position).getName();

        }else {

            intent.putExtra("action", "edit");
            intent.putExtra("data_return", String.valueOf(listdatas.get(position).getName()));
            indexName =listdatas.get(position).getName();
        }
        setResult(RESULT_OK,intent);

        if(lastCheckedOption != null){
            lastCheckedOption.setVisibility(View.INVISIBLE);
        }
        lastCheckedOption = (ImageView)view.findViewById(R.id.custom_item_layout_one_image);
        lastCheckedOption.setVisibility(View.VISIBLE);
        positionTemp =position;
        this.finish();
    }
    //筛选条件
    public Object[] searchItem(String name) {
        if(commonAdapterDataSearch !=null) {
            commonAdapterDataSearch.clear();
        }
        for (int i = 0; i < listdatas.size(); i++) {
            int index = listdatas.get(i).getName().indexOf(name);
            // 存在匹配的数据
            if (index != -1) {
                commonAdapterDataSearch.add(listdatas.get(i));
            }
        }
        return commonAdapterDataSearch.toArray();
    }

    public Object[] searchCategory(String name) {

        if(commonAdapterDataSearch !=null) {
            commonAdapterDataSearch.clear();
        }
        for (int i = 0; i < listdatas.size(); i++) {
            if(listdatas.get(i).getCategory()!=null) {
                int index = listdatas.get(i).getCategory().indexOf(name);
                // 存在匹配的数据
                if (index != -1) {
                    commonAdapterDataSearch.add(listdatas.get(i));
                }
            }
        }
        return commonAdapterDataSearch.toArray();
    }
//adapter刷新,重写Filter方式会出现BUG.
    public void updateLayout(String name) {
        if(commonAdapterDataSearch !=null) {
            int index=-1;
            if(!name.isEmpty())
            {
               for(int i = 0; i< commonAdapterDataSearch.size(); i++)
               {
                   if(commonAdapterDataSearch.get(i).getName().equals(indexName))
                   {
                       index=i;
                   }
               }
            }else
            {
                index= positionTemp;
            }
            adapter = new CommonListViewAdapter(StockIntentView.this, R.layout.custom_item, commonAdapterDataSearch);
            adapter.setSeclection(index);
            listView.setAdapter(adapter);
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.custom_toobar_left:
                StockIntentView.this.finish();
                break;

            case R.id.custom_toobar_midd:


                break;

            case R.id.custom_toobar_right:
                Intent cate = new Intent(StockIntentView.this, StockForm.class);
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
                    if(listdatas.size()!=0) {
                        listdatas.clear();
                    }
                    stockList = DataSupport.findAll(Stock.class);
                    for(Stock stock: stockList)

                    {
                        CommonAdapterData commonData=new CommonAdapterData();
                        commonData.setName(stock.getName());
                        commonData.setUnitId(stock.getId());
                        commonData.setImage(R.drawable.seclec_arrow);
                        listdatas.add(commonData);



                    }
                    adapter = new CommonListViewAdapter(StockIntentView.this, R.layout.custom_item, listdatas);
                    adapter.setSeclection(positionTemp);
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

            Object[] obj = searchItem(search.getText().toString());
            updateLayout(search.getText().toString());

        }
    };
    public boolean isCustom(String name)
    {

        salesOutList =DataSupport.where("consignment=?",name).find(SalesOut.class);

        if (salesOutList.size()>0)
        {
            return true;
        }else {
            return false;
        }

    }

}
