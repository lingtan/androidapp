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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiderp.CustomDataClass.Brand;
import com.example.androiderp.CustomDataClass.SalesOut;
import com.example.androiderp.CustomDataClass.Stock;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonDataStructure;
import com.example.androiderp.adaper.CommonListViewAdapter;
import com.example.androiderp.adaper.DataStructure;
import com.example.androiderp.custom.CustomSearch;
import com.example.androiderp.custom.CustomSearchBase;
import com.example.androiderp.form.BrandForm;
import com.example.androiderp.form.CustomForm;
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
    private List<CommonDataStructure> listdatas = new ArrayList<CommonDataStructure>();
    private CommonListViewAdapter adapter;
    private SlideAndDragListView<CommonDataStructure>  plistView;
    private DisplayMetrics dm;
    private List<CommonDataStructure> searchdatas= new ArrayList<CommonDataStructure>();
    private List<Stock> customlist;
    private TextView custom_toobar_l,custom_toobar_r,custom_toobar_m;
    private CustomSearch custom_search;
    private String categoryid;
    private Menu mMenu;
    private List<SalesOut> findCustomDatas=new ArrayList<SalesOut>();
    @Override
    public void iniView(){
        setContentView(R.layout.customlistview_category_layout);
        initMenu();
        initUiAndListener();
        custom_toobar_l=(TextView)findViewById(R.id.custom_toobar_left) ;
        custom_toobar_m=(TextView)findViewById(R.id.custom_toobar_midd);
        custom_toobar_m.setText("新增仓库");
        custom_toobar_r=(TextView)findViewById(R.id.custom_toobar_right);
        custom_toobar_l.setOnClickListener(this);
        custom_toobar_r.setOnClickListener(this);
        custom_toobar_m.setOnClickListener(this);
        custom_search = (CustomSearch) findViewById(R.id.search);
        Intent intent=getIntent();
        categoryid=intent.getStringExtra("category");
        customlist= DataSupport.findAll(Stock.class);
        custom_toobar_m.setCompoundDrawables(null,null,null,null);
        for(Stock stock:customlist)

        {
            CommonDataStructure commonData=new CommonDataStructure();
            commonData.setName(stock.getName());
            commonData.setId(stock.getId());
            commonData.setImage(R.drawable.seclec_arrow);
            listdatas.add(commonData);



        }
        //构造函数第一参数是类的对象，第二个是布局文件，第三个是数据源
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        if(listdatas.size()!=0) {
             if(categoryid!=null) {
                 Object[] obj = searchCategory(categoryid);
                 updateLayout("10");
                 custom_toobar_m.setText(categoryid);
             }else {
                 adapter = new CommonListViewAdapter(StockListView.this, R.layout.custom_item, listdatas);
                 plistView.setAdapter(adapter);
             }

        }

        custom_search.addTextChangedListener(textWatcher);




    }
    public void initMenu() {
        mMenu = new Menu(true);
        mMenu.addItem(new MenuItem.Builder().setWidth((int) getResources().getDimension(R.dimen.slv_item_bg_btn_width))
                .setBackground(Utils.getDrawable(this, R.drawable.btn_right0))
                .setText("编辑")
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setTextColor(Color.BLACK)
                .setTextSize(14)
                .build());
        mMenu.addItem(new MenuItem.Builder().setWidth((int) getResources().getDimension(R.dimen.slv_item_bg_btn_width_img))
                .setBackground(Utils.getDrawable(this, R.drawable.btn_right1))
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setText("删除")
                .setTextSize(14)
                .build());
    }

    public void initUiAndListener() {
        plistView = (SlideAndDragListView) findViewById(R.id.custom_listview_category);
        plistView.setMenu(mMenu);
        plistView.setOnItemClickListener(this);
        plistView.setOnMenuItemClickListener(this);
        plistView.setOnItemDeleteListener(this);
    }
    @Override
    public int onMenuItemClick(View v, final int itemPosition, int buttonPosition, int direction) {
        switch (direction) {
            case MenuItem.DIRECTION_RIGHT:
                switch (buttonPosition) {
                    case 0:
                        Intent intent=new Intent(StockListView.this,StockForm.class);
                        if(searchdatas.size()!=0) {

                            intent.putExtra("action", "edit");
                            intent.putExtra("customid", String.valueOf(searchdatas.get(itemPosition).getId()));

                        }else {

                            intent.putExtra("action", "edit");
                            intent.putExtra("customid", String.valueOf(listdatas.get(itemPosition).getId()));
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

                                if(isCustom(listdatas.get(itemPosition - plistView.getHeaderViewsCount()).getName().toString()))
                                {
                                    Toast.makeText(StockListView.this,"已经有业务发生，不能删除",Toast.LENGTH_SHORT).show();
                                    adapter.notifyDataSetChanged();
                                }else {
                                    DataStructure.deleteAll(Stock.class, "name = ?", listdatas.get(itemPosition - plistView.getHeaderViewsCount()).getName().toString());

                                    AlertDialog.Builder dialogOK = new AlertDialog.Builder(StockListView.this);
                                    dialogOK.setMessage("该仓库已经删除");
                                    dialogOK.setNegativeButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            listdatas.remove(itemPosition - plistView.getHeaderViewsCount());
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
        if(searchdatas!=null) {
            searchdatas.clear();
        }
        for (int i = 0; i < listdatas.size(); i++) {
            int index = listdatas.get(i).getName().indexOf(name);
            // 存在匹配的数据
            if (index != -1) {
                searchdatas.add(listdatas.get(i));
            }
        }
        return searchdatas.toArray();
    }

    public Object[] searchCategory(String name) {

        if(searchdatas!=null) {
            searchdatas.clear();
        }
        for (int i = 0; i < listdatas.size(); i++) {
            if(listdatas.get(i).getCategory()!=null) {
                int index = listdatas.get(i).getCategory().indexOf(name);
                // 存在匹配的数据
                if (index != -1) {
                    searchdatas.add(listdatas.get(i));
                }
            }
        }
        return searchdatas.toArray();
    }
//adapter刷新,重写Filter方式会出现BUG.
    public void updateLayout(String name) {
        if(searchdatas!=null) {

            adapter = new CommonListViewAdapter(StockListView.this, R.layout.custom_item, searchdatas);
            plistView.setAdapter(adapter);
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
                    if(listdatas.size()!=0) {
                        listdatas.clear();
                    }
                    customlist= DataSupport.findAll(Stock.class);
                    for(Stock stock:customlist)

                    {
                        CommonDataStructure commonData=new CommonDataStructure();
                        commonData.setName(stock.getName());
                        commonData.setId(stock.getId());
                        commonData.setImage(R.drawable.seclec_arrow);
                        listdatas.add(commonData);



                    }
                    adapter = new CommonListViewAdapter(StockListView.this, R.layout.custom_item, listdatas);
                    plistView.setAdapter(adapter);


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

            Object[] obj = searchItem(custom_search.getText().toString());
            updateLayout(custom_search.getText().toString());

        }
    };

    public boolean isCustom(String name)
    {

        findCustomDatas=DataSupport.where("stock =?",name).find(SalesOut.class);

        if (findCustomDatas.size()>0)
        {
            return true;
        }else {
            return false;
        }

    }
}
