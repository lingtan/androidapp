package com.example.androiderp.activities.basicview;

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

import com.example.androiderp.bean.CustomCategory;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonAdapterData;
import com.example.androiderp.adaper.CommonListViewAdapter;
import com.example.androiderp.bean.DataStructure;
import com.example.androiderp.ui.CSearch;
import com.example.androiderp.ui.CSearchBase;
import com.example.androiderp.activities.basicfrom.CustomCategoryForm;
import com.example.androiderp.activities.basicfrom.ProductCategoryForm;
import com.example.androiderp.listview.Menu;
import com.example.androiderp.listview.MenuItem;
import com.example.androiderp.listview.SlideAndDragListView;
import com.example.androiderp.listview.Utils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class CustomCategoryView extends CSearchBase implements View.OnClickListener,
        AdapterView.OnItemClickListener,
        SlideAndDragListView.OnMenuItemClickListener, SlideAndDragListView.OnItemDeleteListener {
    private List<CommonAdapterData> listdatas = new ArrayList<CommonAdapterData>();
    private CommonListViewAdapter adapter;
    private SlideAndDragListView<CommonAdapterData> listView;
    private DisplayMetrics dm;
    private List<CustomCategory> customCategoryList;
    private TextView toobarBack, toobarAdd, toobarTile;
    private CSearch search;
    private ImageView lastCheckedOption;
    private int indexPositon=-1;
    private String indexName;
    private String returnName;
    private String searchVale;
    private Menu menu;
    @Override
    public void iniView(){
        setContentView(R.layout.customlistview_category_layout);
        initMenu();
        initUiAndListener();
        toobarBack =(TextView)findViewById(R.id.custom_toobar_left) ;
        toobarTile =(TextView)findViewById(R.id.custom_toobar_midd);
        toobarTile.setText("选择客户分类");
        toobarAdd =(TextView)findViewById(R.id.custom_toobar_right);
        toobarBack.setOnClickListener(this);
        toobarAdd.setOnClickListener(this);
        toobarTile.setOnClickListener(this);
        search = (CSearch) findViewById(R.id.search);
        Intent intent=getIntent();
        indexName =intent.getStringExtra("index");
        customCategoryList = DataSupport.findAll(CustomCategory.class);
        toobarTile.setCompoundDrawables(null,null,null,null);

        for(CustomCategory customCategory: customCategoryList)

        {   if(customCategory.getName().equals(indexName))
        {
            indexPositon = customCategoryList.indexOf(customCategory);
        }

            CommonAdapterData commonData=new CommonAdapterData();
            commonData.setName(customCategory.getName());
            commonData.setUnitId(customCategory.getId());
            commonData.setImage(R.drawable.seclec_arrow);
            listdatas.add(commonData);



        }

        //构造函数第一参数是类的对象，第二个是布局文件，第三个是数据源
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        if(listdatas.size()!=0) {

                 adapter = new CommonListViewAdapter(CustomCategoryView.this, R.layout.custom_item, listdatas);
                 adapter.setSeclection(indexPositon);
                 listView.setAdapter(adapter);


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
                        Intent intent=new Intent(CustomCategoryView.this,CustomCategoryForm.class);


                            intent.putExtra("action", "edit");
                            intent.putExtra("customid", String.valueOf(listdatas.get(itemPosition).getUnitId()));
                        startActivityForResult(intent,1);

                        return Menu.ITEM_NOTHING;
                    case 1:
                        AlertDialog.Builder dialog=new AlertDialog.Builder(CustomCategoryView.this);
                        dialog.setTitle("提示");
                        dialog.setMessage("您确认要删除该分类？");
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DataStructure.deleteAll(CustomCategory.class,"name = ?",listdatas.get(itemPosition - listView.getHeaderViewsCount()).getName().toString());

                                listdatas.remove(itemPosition - listView.getHeaderViewsCount());
                                if(search.getText().toString().isEmpty()) {
                                    if (indexPositon == itemPosition) {
                                        indexPositon = -1;
                                    }

                                    adapter.setSeclection(indexPositon);
                                    adapter.notifyDataSetChanged();
                                }else {
                                    search.setText("");
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


        Intent intent=new Intent(CustomCategoryView.this,ProductCategoryForm.class);


            intent.putExtra("action", "edit");
            intent.putExtra("data_return", String.valueOf(listdatas.get(position).getName()));
            indexName =listdatas.get(position).getName();

        setResult(RESULT_OK,intent);

        if(lastCheckedOption != null){
            lastCheckedOption.setVisibility(View.INVISIBLE);
        }
        lastCheckedOption = (ImageView)view.findViewById(R.id.custom_item_layout_one_image);
        lastCheckedOption.setVisibility(View.VISIBLE);
        indexPositon =position;
        this.finish();
    }
    public void  searchItem(String name) {
        if(listdatas.size()>0) {
            listdatas.clear();
        }
        customCategoryList =DataStructure.where("name like ?","%" + name + "%").find(CustomCategory.class);
        for(CustomCategory customCategory: customCategoryList)

        {
            CommonAdapterData commonData=new CommonAdapterData();
            commonData.setName(customCategory.getName());
            commonData.setUnitId(customCategory.getId());
            commonData.setImage(R.drawable.seclec_arrow);
            listdatas.add(commonData);

        }

        if(listdatas!=null) {
            int index=-1;
            if(!name.isEmpty())
            {
                for(int i=0;i<listdatas.size();i++)
                {
                    if(listdatas.get(i).getName().equals(indexName))
                    {
                        index=i;
                    }
                }
            }else
            {
                index= indexPositon;
            }
            adapter.notifyDataSetChanged();
            adapter.setSeclection(index);
            listView.setAdapter(adapter);
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.custom_toobar_left:
                Intent intent=getIntent();
                if(indexPositon!=-1) {
                    intent.putExtra("data_return", listdatas.get(indexPositon).getName());
                }
                setResult(RESULT_OK,intent);
                CustomCategoryView.this.finish();
                break;

            case R.id.custom_toobar_midd:


                break;

            case R.id.custom_toobar_right:
                Intent cate = new Intent(CustomCategoryView.this, CustomCategoryForm.class);
                cate.putExtra("action","add");
                startActivityForResult(cate,2);
                break;


        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK)
                {   returnName=data.getStringExtra("returnName");
                    if(listdatas.size()>0) {
                        listdatas.clear();
                    }
                    if(search.getText().toString().isEmpty()) {

                        search.requestFocusFromTouch();
                        search.setText("");
                    }else {
                        int i = returnName.indexOf(searchVale);
                        if(i!=-1) {
                            search.requestFocusFromTouch();
                            search.setText(searchVale);
                        }else {
                            search.requestFocusFromTouch();
                            search.setText(returnName);
                        }
                    }

                }
                break;
            case 2:
                if(resultCode==RESULT_OK)
                {
                    if(listdatas.size()>0) {
                        search.setText("");
                        listdatas.clear();
                    }

                    customCategoryList = DataSupport.findAll(CustomCategory.class);
                    for(CustomCategory customCategory: customCategoryList)

                    {
                        CommonAdapterData commonData=new CommonAdapterData();
                        commonData.setName(customCategory.getName());
                        commonData.setUnitId(customCategory.getId());
                        commonData.setImage(R.drawable.seclec_arrow);
                        listdatas.add(commonData);



                    }
                    adapter = new CommonListViewAdapter(CustomCategoryView.this, R.layout.custom_item, listdatas);
                    adapter.setSeclection(indexPositon);
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

            searchItem(search.getText().toString());
            searchVale= search.getText().toString();


        }
    };
}
