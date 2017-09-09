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

import com.example.androiderp.CustomDataClass.Product;
import com.example.androiderp.CustomDataClass.Unit;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonAdapterData;
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
    private List<CommonAdapterData> listdatas = new ArrayList<CommonAdapterData>();
    private CommonListViewAdapter adapter;
    private SlideAndDragListView<CommonAdapterData> listView;
    private DisplayMetrics dm;
    private List<CommonAdapterData> commonAdapterDataSearch = new ArrayList<CommonAdapterData>();
    private List<Unit> unitList;
    private TextView toobarBack, toobarAdd, toobarTile;
    private CustomSearch customSearch;
    private ImageView lastCheckedOption;
    private int indexPositon=-1;
    private String indexName;
    private Menu menu;
    private String returnName;
    private String searchVale;
    @Override
    public void iniView(){
        setContentView(R.layout.customlistview_category_layout);
        initMenu();
        initUiAndListener();
        toobarBack =(TextView)findViewById(R.id.custom_toobar_left) ;
        toobarTile =(TextView)findViewById(R.id.custom_toobar_midd);
        toobarTile.setText("选择品牌");
        toobarAdd =(TextView)findViewById(R.id.custom_toobar_right);
        toobarBack.setOnClickListener(this);
        toobarAdd.setOnClickListener(this);
        toobarTile.setOnClickListener(this);
        customSearch = (CustomSearch) findViewById(R.id.search);
        Intent intent=getIntent();
        indexName =intent.getStringExtra("index");
        unitList = DataSupport.findAll(Unit.class);
        toobarTile.setCompoundDrawables(null,null,null,null);
        for(Unit unit: unitList)

        {   if(unit.getName().equals(indexName))
        {
            indexPositon = unitList.indexOf(unit);
        }
            CommonAdapterData commonData=new CommonAdapterData();
            commonData.setName(unit.getName());
            commonData.setId(unit.getId());
            commonData.setImage(R.drawable.seclec_arrow);
            listdatas.add(commonData);



        }


        //构造函数第一参数是类的对象，第二个是布局文件，第三个是数据源
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        if(listdatas.size()!=0) {

                 adapter = new CommonListViewAdapter(UnitListView.this, R.layout.custom_item, listdatas);
                 adapter.setSeclection(indexPositon);
                 listView.setAdapter(adapter);


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


                            intent.putExtra("action", "edit");
                            intent.putExtra("customid", String.valueOf(listdatas.get(itemPosition).getId()));
                            indexName = listdatas.get(itemPosition).getName();

                        startActivityForResult(intent,1);

                        return Menu.ITEM_NOTHING;
                    case 1:
                        AlertDialog.Builder dialog=new AlertDialog.Builder(UnitListView.this);
                        dialog.setTitle("提示");
                        dialog.setMessage("您确认要删除该单位？");
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                List<Product> productList=DataSupport.where("unit =?",listdatas.get(itemPosition - listView.getHeaderViewsCount()).getName().toString()).find(Product.class);
                                if(productList.size()>0)
                                {
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(UnitListView.this,"业务已经发生不能删除",Toast.LENGTH_SHORT).show();
                                }else {
                                    DataStructure.deleteAll(Unit.class, "name = ?", listdatas.get(itemPosition - listView.getHeaderViewsCount()).getName().toString());


                                    listdatas.remove(itemPosition - listView.getHeaderViewsCount());
                                    if (customSearch.getText().toString().isEmpty()) {
                                        if (indexPositon == itemPosition) {
                                            indexPositon = -1;
                                        }

                                        adapter.setSeclection(indexPositon);
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        customSearch.setText("");
                                    }

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


        Intent intent=new Intent(UnitListView.this,UnitForm.class);


            intent.putExtra("action", "edit");
            intent.putExtra("data_return", String.valueOf(listdatas.get(position).getName()));
            indexName = listdatas.get(position).getName();

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
       unitList=DataStructure.where("name like ?","%" + name + "%").find(Unit.class);
        for(Unit unit:unitList)

        {
            CommonAdapterData commonData=new CommonAdapterData();
            commonData.setName(unit.getName());
            commonData.setId(unit.getId());
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

            adapter.setSeclection(index);
            adapter.notifyDataSetChanged();
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
                UnitListView.this.finish();
                break;

            case R.id.custom_toobar_midd:


                break;

            case R.id.custom_toobar_right:
                Intent cate = new Intent(UnitListView.this, UnitForm.class);
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
                    indexName=returnName;
                    if(listdatas.size()>0) {
                        listdatas.clear();
                    }
                    if(customSearch.getText().toString().isEmpty()) {

                        customSearch.requestFocusFromTouch();
                        customSearch.setText("");
                    }else {
                        int i = returnName.indexOf(searchVale);
                        if(i!=-1) {
                            customSearch.requestFocusFromTouch();
                            customSearch.setText(searchVale);
                        }else {
                            customSearch.requestFocusFromTouch();
                            customSearch.setText(returnName);
                        }
                    }

                }
                break;

            case 2:
                if(resultCode==RESULT_OK)
                {
                    if(listdatas.size()!=0) {
                        listdatas.clear();
                    }
                    unitList = DataSupport.findAll(Unit.class);
                    for(Unit unit: unitList)

                    {
                        CommonAdapterData commonData=new CommonAdapterData();
                        commonData.setName(unit.getName());
                        commonData.setId(unit.getId());
                        commonData.setImage(R.drawable.seclec_arrow);
                        listdatas.add(commonData);



                    }
                    adapter = new CommonListViewAdapter(UnitListView.this, R.layout.custom_item, listdatas);
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

            searchItem(customSearch.getText().toString());
            searchVale=customSearch.getText().toString();



        }
    };
}
