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

import com.example.androiderp.CustomDataClass.Unit;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonDataStructure;
import com.example.androiderp.adaper.CommonListViewAdapter;
import com.example.androiderp.adaper.DataStructure;
import com.example.androiderp.custom.CustomSearch;
import com.example.androiderp.custom.CustomSearchBase;
import com.example.androiderp.form.UnitForm;
import com.example.androiderp.listview.Menu;
import com.example.androiderp.listview.MenuItem;
import com.example.androiderp.listview.SlideAndDragListView;
import com.example.androiderp.listview.Utils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class UnitListView extends CustomSearchBase implements View.OnClickListener,
        AdapterView.OnItemClickListener,
        SlideAndDragListView.OnMenuItemClickListener, SlideAndDragListView.OnItemDeleteListener {
    private List<CommonDataStructure> commonDataStructureList = new ArrayList<CommonDataStructure>();
    private CommonListViewAdapter adapter;
    private SlideAndDragListView<CommonDataStructure> listView;
    private DisplayMetrics dm;
    private List<CommonDataStructure> commonDataStructureSearch = new ArrayList<CommonDataStructure>();
    private List<Unit> unitList;
    private TextView toobarBack, toobarAdd, toobarTile;
    private CustomSearch customSearch;
    private String categoryid;
    private ImageView lastCheckedOption;
    private int positionTemp;
    private int indexPositon;
    private String indexName;
    private Menu menu;
    @Override
    public void iniView(){
        setContentView(R.layout.customlistview_category_layout);
        initMenu();
        initUiAndListener();
        toobarBack =(TextView)findViewById(R.id.custom_toobar_left) ;
        toobarTile =(TextView)findViewById(R.id.custom_toobar_midd);
        toobarTile.setText("新增品牌");
        toobarAdd =(TextView)findViewById(R.id.custom_toobar_right);
        toobarBack.setOnClickListener(this);
        toobarAdd.setOnClickListener(this);
        toobarTile.setOnClickListener(this);
        customSearch = (CustomSearch) findViewById(R.id.search);
        Intent intent=getIntent();
        categoryid=intent.getStringExtra("category");
        indexName =intent.getStringExtra("index");
        unitList = DataSupport.findAll(Unit.class);
        toobarTile.setCompoundDrawables(null,null,null,null);
        for(Unit unit: unitList)

        {   if(unit.getName().equals(indexName))
        {
            indexPositon = unitList.indexOf(unit);
        }
            CommonDataStructure commonData=new CommonDataStructure();
            commonData.setName(unit.getName());
            commonData.setId(unit.getId());
            commonData.setImage(R.drawable.seclec_arrow);
            commonDataStructureList.add(commonData);



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


        if(commonDataStructureList.size()!=0) {
             if(categoryid!=null) {
                 Object[] obj = searchCategory(categoryid);
                 updateLayout("10");
                 toobarTile.setText(categoryid);
             }else {
                 adapter = new CommonListViewAdapter(UnitListView.this, R.layout.custom_item, commonDataStructureList);
                 adapter.setSeclection(indexPositon);
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
                        Intent intent=new Intent(UnitListView.this,UnitForm.class);
                        if(commonDataStructureSearch.size()!=0) {

                            intent.putExtra("action", "edit");
                            intent.putExtra("customid", String.valueOf(commonDataStructureSearch.get(itemPosition).getId()));
                            indexName = commonDataStructureSearch.get(itemPosition).getName();

                        }else {

                            intent.putExtra("action", "edit");
                            intent.putExtra("customid", String.valueOf(commonDataStructureList.get(itemPosition).getId()));
                            indexName = commonDataStructureList.get(itemPosition).getName();
                        }
                        startActivityForResult(intent,1);

                        return Menu.ITEM_NOTHING;
                    case 1:
                        AlertDialog.Builder dialog=new AlertDialog.Builder(UnitListView.this);
                        dialog.setTitle("提示");
                        dialog.setMessage("您确认要删除该单位？");
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DataStructure.deleteAll(Unit.class,"name = ?", commonDataStructureList.get(itemPosition - listView.getHeaderViewsCount()).getName().toString());

                                AlertDialog.Builder dialogOK=new AlertDialog.Builder(UnitListView.this);
                                dialogOK.setMessage("该单位已经删除");
                                dialogOK.setNegativeButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(indexPositon ==itemPosition)
                                        {
                                            indexPositon =-1;
                                        }
                                        commonDataStructureList.remove(itemPosition - listView.getHeaderViewsCount());
                                        adapter.setSeclection(indexPositon);
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                                dialogOK.show();




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


        Intent intent=new Intent(UnitListView.this,UnitForm.class);
        if(commonDataStructureSearch.size()!=0) {

            intent.putExtra("action", "edit");
            intent.putExtra("data_return", String.valueOf(commonDataStructureSearch.get(position).getName()));
            indexName = commonDataStructureSearch.get(position).getName();

        }else {

            intent.putExtra("action", "edit");
            intent.putExtra("data_return", String.valueOf(commonDataStructureList.get(position).getName()));
            indexName = commonDataStructureList.get(position).getName();
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
        if(commonDataStructureSearch !=null) {
            commonDataStructureSearch.clear();
        }
        for (int i = 0; i < commonDataStructureList.size(); i++) {
            int index = commonDataStructureList.get(i).getName().indexOf(name);
            // 存在匹配的数据
            if (index != -1) {
                commonDataStructureSearch.add(commonDataStructureList.get(i));
            }
        }
        return commonDataStructureSearch.toArray();
    }

    public Object[] searchCategory(String name) {

        if(commonDataStructureSearch !=null) {
            commonDataStructureSearch.clear();
        }
        for (int i = 0; i < commonDataStructureList.size(); i++) {
            if(commonDataStructureList.get(i).getCategory()!=null) {
                int index = commonDataStructureList.get(i).getCategory().indexOf(name);
                // 存在匹配的数据
                if (index != -1) {
                    commonDataStructureSearch.add(commonDataStructureList.get(i));
                }
            }
        }
        return commonDataStructureSearch.toArray();
    }
//adapter刷新,重写Filter方式会出现BUG.
    public void updateLayout(String name) {
        if(commonDataStructureSearch !=null) {
            int index=-1;
            if(!name.isEmpty())
            {
               for(int i = 0; i< commonDataStructureSearch.size(); i++)
               {
                   if(commonDataStructureSearch.get(i).getName().equals(indexName))
                   {
                       index=i;
                   }
               }
            }else
            {
                index= positionTemp;
            }
            adapter = new CommonListViewAdapter(UnitListView.this, R.layout.custom_item, commonDataStructureSearch);
            adapter.setSeclection(index);
            listView.setAdapter(adapter);
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.custom_toobar_left:
                UnitListView.this.finish();
                break;

            case R.id.custom_toobar_midd:


                break;

            case R.id.custom_toobar_right:
                Intent cate = new Intent(UnitListView.this, UnitForm.class);
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
                    if(commonDataStructureList.size()!=0) {
                        commonDataStructureList.clear();
                    }
                    unitList = DataSupport.findAll(Unit.class);
                    for(Unit unit: unitList)

                    {
                        CommonDataStructure commonData=new CommonDataStructure();
                        commonData.setName(unit.getName());
                        commonData.setId(unit.getId());
                        commonData.setImage(R.drawable.seclec_arrow);
                        commonDataStructureList.add(commonData);



                    }
                    adapter = new CommonListViewAdapter(UnitListView.this, R.layout.custom_item, commonDataStructureList);
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

            Object[] obj = searchItem(customSearch.getText().toString());
            updateLayout(customSearch.getText().toString());

        }
    };
}
