package com.example.androiderp.activities.basicview;

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

import com.example.androiderp.bean.Custom;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonAdapter;
import com.example.androiderp.adaper.CommonAdapterData;
import com.example.androiderp.bean.PopBean;
import com.example.androiderp.tools.Common;
import com.example.androiderp.ui.CHomeSearch;
import com.example.androiderp.ui.CSearchBase;
import com.example.androiderp.activities.basicfrom.CustomForm;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class CustomSearchView extends CSearchBase implements View.OnClickListener {
    private List<CommonAdapterData> commonAdapterDataList = new ArrayList<CommonAdapterData>();
    private List<PopBean> popList;
    private CommonAdapter adapter;
    private ListView listView;
    private DisplayMetrics dm;
    private List<CommonAdapterData> commonAdapterDataSearch = new ArrayList<CommonAdapterData>();
    private List<Custom> customList;
    private Common common;
    private CHomeSearch cHomeSearch;
    private Intent intent;

    @Override
    public void iniView(){
        setContentView(R.layout.custom_search_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.custom_search_toolbar);
        setSupportActionBar(toolbar);//加载工具栏
        cHomeSearch = (CHomeSearch) findViewById(R.id.home_custom_search);
        cHomeSearch.setVisibility(View.VISIBLE);
        customList = DataSupport.findAll(Custom.class);
        intent= new Intent(CustomSearchView.this, CustomForm.class);
        for(Custom custom: customList)

        {
            CommonAdapterData commonData=new CommonAdapterData();
            commonData.setName(custom.getName());
            commonData.setCategory(custom.getCategory());
            commonData.setUnitId(custom.getId());
            commonAdapterDataList.add(commonData);



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
                        if(commonAdapterDataSearch.size()!=0) {

                            intent.putExtra("action", "edit");
                            intent.putExtra("custom_item", String.valueOf(commonAdapterDataSearch.get(position).getUnitId()));


                        }else {

                            intent.putExtra("action", "edit");
                            intent.putExtra("custom_item", String.valueOf(commonAdapterDataList.get(position).getUnitId()));

                        }
                startActivityForResult(intent,1);


            }
        });
        if(commonAdapterDataList.size()!=0) {

                 adapter = new CommonAdapter(CustomSearchView.this, R.layout.custom_item, commonAdapterDataList);
                 listView.setAdapter(adapter);
            
        }

        cHomeSearch.addTextChangedListener(textWatcher);

        popList = new ArrayList<PopBean>();
        PopBean popuMenua = new PopBean(android.R.drawable.ic_menu_edit, "美的");
        popList.add(popuMenua);
        PopBean popuMenub = new PopBean(android.R.drawable.ic_menu_edit, "松下");
        popList.add(popuMenub);
        showPopupWindow(popList);

    }

    //筛选条件
    public Object[] search(String name) {
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

    public Object[] categorySearch(String name) {

        if(commonAdapterDataSearch !=null) {
            commonAdapterDataSearch.clear();
        }
        if(name.equals("全部类别"))
        {
            for (int i = 0; i < commonAdapterDataList.size(); i++) {
               if(commonAdapterDataList.get(i).getCategory()!=null)
               {
                    commonAdapterDataSearch.add(commonAdapterDataList.get(i));
               }
            }

        }else {
        for (int i = 0; i < commonAdapterDataList.size(); i++) {
              if(commonAdapterDataList.get(i).getCategory()!=null){
                int index = commonAdapterDataList.get(i).getCategory().indexOf(name);
                // 存在匹配的数据
                if (index != -1) {
                    commonAdapterDataSearch.add(commonAdapterDataList.get(i));
                }
            }
        }}
        return commonAdapterDataSearch.toArray();
    }
//adapter刷新,重写Filter方式会出现BUG.
    public void updateLayout(Object[] obj) {
        if(commonAdapterDataSearch !=null) {
            adapter = new CommonAdapter(CustomSearchView.this, R.layout.custom_item, commonAdapterDataSearch);
            listView.setAdapter(adapter);
        }
    }

    private void showPopupWindow(final List<PopBean> popuMenuData) {
        common = new Common();

        common.PopupWindow(CustomSearchView.this, dm, popuMenuData);
        common.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                if(popList.get(position).getName().equals("客户新增"))
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
                    if(commonAdapterDataList.size()!=0) {
                        commonAdapterDataList.clear();
                    }
                    customList = DataSupport.findAll(Custom.class);
                    for(Custom category: customList)

                    {
                        CommonAdapterData commonData=new CommonAdapterData();
                        commonData.setName(category.getName());
                        commonData.setCategory(category.getCategory());
                        commonData.setUnitId(category.getId());
                        commonAdapterDataList.add(commonData);



                    }
                    adapter = new CommonAdapter(CustomSearchView.this, R.layout.custom_item, commonAdapterDataList);
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
                {   popList.clear();
                    PopBean popuMenua = new PopBean(R.drawable.poppu_wrie, "客户修改");
                    popList.add(popuMenua);
                    PopBean popuMenub = new PopBean(R.drawable.poppu_wrie, "客户新增");
                    popList.add(popuMenub);
                    int xPos = dm.widthPixels / 3;
                    showPopupWindow(popList);
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

            Object[] obj = search(cHomeSearch.getText().toString());
            updateLayout(obj);

        }
    };
}