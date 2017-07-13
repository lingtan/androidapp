package com.example.androiderp.basicdata;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.androiderp.CustomDataClass.Custom;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonAdapter;
import com.example.androiderp.adaper.CommonDataStructure;
import com.example.androiderp.adaper.PopuMenuDataStructure;
import com.example.androiderp.common.Common;
import com.example.androiderp.custom.CustomHomeSearch;
import com.example.androiderp.custom.CustomSearchBase;
import com.example.androiderp.form.CustomForm;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class CustomSearchListView extends CustomSearchBase implements View.OnClickListener {
    private List<CommonDataStructure> commonDataStructureList = new ArrayList<CommonDataStructure>();
    private List<PopuMenuDataStructure> popuMenuDatas;
    private CommonAdapter adapter;
    private ListView listView;
    private DisplayMetrics dm;
    private List<CommonDataStructure> commonDataStructureSearch = new ArrayList<CommonDataStructure>();
    private List<Custom> customList;
    private Common common;
    private CustomHomeSearch customHomeSearch;
    private Intent intent;

    @Override
    public void iniView(){
        setContentView(R.layout.custom_search_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.custom_search_toolbar);
        setSupportActionBar(toolbar);//加载工具栏
        customHomeSearch = (CustomHomeSearch) findViewById(R.id.home_custom_search);
        customHomeSearch.setVisibility(View.VISIBLE);
        customList = DataSupport.findAll(Custom.class);
        intent= new Intent(CustomSearchListView.this, CustomForm.class);
        for(Custom custom: customList)

        {
            CommonDataStructure commonData=new CommonDataStructure();
            commonData.setName(custom.getName());
            commonData.setCategory(custom.getCategory());
            commonData.setId(custom.getId());
            commonDataStructureList.add(commonData);



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
                intent.removeExtra("action");
                        if(commonDataStructureSearch.size()!=0) {

                            intent.putExtra("action", "edit");
                            intent.putExtra("custom_item", String.valueOf(commonDataStructureSearch.get(position).getId()));


                        }else {

                            intent.putExtra("action", "edit");
                            intent.putExtra("custom_item", String.valueOf(commonDataStructureList.get(position).getId()));

                        }
                startActivityForResult(intent,1);


            }
        });
        if(commonDataStructureList.size()!=0) {

                 adapter = new CommonAdapter(CustomSearchListView.this, R.layout.custom_item, commonDataStructureList);
                 listView.setAdapter(adapter);
            
        }

        customHomeSearch.addTextChangedListener(textWatcher);

        popuMenuDatas = new ArrayList<PopuMenuDataStructure>();
        PopuMenuDataStructure popuMenua = new PopuMenuDataStructure(android.R.drawable.ic_menu_edit, "美的");
        popuMenuDatas.add(popuMenua);
        PopuMenuDataStructure popuMenub = new PopuMenuDataStructure(android.R.drawable.ic_menu_edit, "松下");
        popuMenuDatas.add(popuMenub);
        showPopupWindow(popuMenuDatas);

    }

    //筛选条件
    public Object[] search(String name) {
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

    public Object[] categorySearch(String name) {

        if(commonDataStructureSearch !=null) {
            commonDataStructureSearch.clear();
        }
        if(name.equals("全部类别"))
        {
            for (int i = 0; i < commonDataStructureList.size(); i++) {
               if(commonDataStructureList.get(i).getCategory()!=null)
               {
                    commonDataStructureSearch.add(commonDataStructureList.get(i));
               }
            }

        }else {
        for (int i = 0; i < commonDataStructureList.size(); i++) {
              if(commonDataStructureList.get(i).getCategory()!=null){
                int index = commonDataStructureList.get(i).getCategory().indexOf(name);
                // 存在匹配的数据
                if (index != -1) {
                    commonDataStructureSearch.add(commonDataStructureList.get(i));
                }
            }
        }}
        return commonDataStructureSearch.toArray();
    }
//adapter刷新,重写Filter方式会出现BUG.
    public void updateLayout(Object[] obj) {
        if(commonDataStructureSearch !=null) {
            adapter = new CommonAdapter(CustomSearchListView.this, R.layout.custom_item, commonDataStructureSearch);
            listView.setAdapter(adapter);
        }
    }

    private void showPopupWindow(final List<PopuMenuDataStructure> popuMenuData) {
        common = new Common();

        common.PopupWindow(CustomSearchListView.this, dm, popuMenuData);
        common.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                if(popuMenuDatas.get(position).getName().equals("客户新增"))
                {
                    intent.removeExtra("custom_item");
                    intent.putExtra("action","add");
                    startActivityForResult(intent,1);
                }
                else {

                }
                common.mPopWindow.dismiss();
            }
        });
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
                    customList = DataSupport.findAll(Custom.class);
                    for(Custom category: customList)

                    {
                        CommonDataStructure commonData=new CommonDataStructure();
                        commonData.setName(category.getName());
                        commonData.setCategory(category.getCategory());
                        commonData.setId(category.getId());
                        commonDataStructureList.add(commonData);



                    }
                    adapter = new CommonAdapter(CustomSearchListView.this, R.layout.custom_item, commonDataStructureList);
                    listView.setAdapter(adapter);
                }
                break;

            default:
                }
        }


    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (common.mPopWindow != null && common.mPopWindow.isShowing()) {
                common.mPopWindow.dismiss();
            }
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {

            case R.id.custom_toobar_right:
                if( common.mPopWindow==null ||!common.mPopWindow.isShowing())
                {   popuMenuDatas.clear();
                    PopuMenuDataStructure popuMenua = new PopuMenuDataStructure(R.drawable.poppu_wrie, "客户修改");
                    popuMenuDatas.add(popuMenua);
                    PopuMenuDataStructure popuMenub = new PopuMenuDataStructure(R.drawable.poppu_wrie, "客户新增");
                    popuMenuDatas.add(popuMenub);
                    int xPos = dm.widthPixels / 3;
                    showPopupWindow(popuMenuDatas);
                    common.mPopWindow.showAsDropDown(v,0,5);
                    //mPopWindow.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM, 0, 0);//从底部弹出
                }
                else {
                    common.mPopWindow.dismiss();
                }
                break;


        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toobar_search_nemu, menu);


        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toobar_search_nemu_night:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

            Object[] obj = search(customHomeSearch.getText().toString());
            updateLayout(obj);

        }
    };
}
