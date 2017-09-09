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
import com.example.androiderp.CustomDataClass.User;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonAdapter;
import com.example.androiderp.adaper.CommonAdapterData;
import com.example.androiderp.adaper.DataStructure;
import com.example.androiderp.adaper.PopuMenuDataStructure;
import com.example.androiderp.common.Common;
import com.example.androiderp.custom.CustomSearch;
import com.example.androiderp.custom.CustomSearchBase;
import com.example.androiderp.form.CustomForm;
import org.litepal.crud.DataSupport;
import java.util.ArrayList;
import java.util.List;

public class CustomListView extends CustomSearchBase implements View.OnClickListener {
    private List<CommonAdapterData> commonAdapterDataList = new ArrayList<CommonAdapterData>();
    private List<DataStructure> fruit = new ArrayList<DataStructure>();
    private List<PopuMenuDataStructure> popuMenuDatas;
    private CommonAdapter adapter;
    private ListView listView;
    private DisplayMetrics dm;
    private User user;
    private List<CommonAdapterData> commonAdapterDataSearch = new ArrayList<CommonAdapterData>();
    private List<Custom> customList;
    private List<CustomCategory> customCategoryList;
    private Common common;
    private TextView toobarBack, toobarAdd, toobarTile;
    private CustomSearch customSearch;
    private Intent intent;

    @Override
    public void iniView(){
        setContentView(R.layout.custom_layout);
        toobarBack =(TextView)findViewById(R.id.custom_toobar_left) ;
        toobarTile =(TextView)findViewById(R.id.custom_toobar_midd);
        toobarAdd =(TextView)findViewById(R.id.custom_toobar_right);
        toobarBack.setOnClickListener(this);
        toobarAdd.setOnClickListener(this);
        toobarTile.setOnClickListener(this);
        customSearch = (CustomSearch) findViewById(R.id.search);
        customList = DataSupport.findAll(Custom.class);
        intent= new Intent(CustomListView.this, CustomForm.class);
        for(Custom custom: customList)

        {
            CommonAdapterData commonData=new CommonAdapterData();
            commonData.setName(custom.getName());
            commonData.setCategory(custom.getCategory());
            commonData.setId(custom.getId());
            commonAdapterDataList.add(commonData);



        }

        //构造函数第一参数是类的对象，第二个是布局文件，第三个是数据源
        listView = (ListView) findViewById(R.id.list);
        listView.setTextFilterEnabled(true);
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        user=(User) getIntent().getParcelableExtra("user_data");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                intent.removeExtra("action");
                        if(commonAdapterDataSearch.size()!=0) {

                            intent.putExtra("action", "edit");
                            intent.putExtra("custom_item", String.valueOf(commonAdapterDataSearch.get(position).getId()));


                        }else {

                            intent.putExtra("action", "edit");
                            intent.putExtra("custom_item", String.valueOf(commonAdapterDataList.get(position).getId()));

                        }
                startActivityForResult(intent,1);


            }
        });
        if(commonAdapterDataList.size()!=0) {

                 adapter = new CommonAdapter(CustomListView.this, R.layout.custom_item, commonAdapterDataList);
                 listView.setAdapter(adapter);
            
        }

        customSearch.addTextChangedListener(textWatcher);

        popuMenuDatas = new ArrayList<PopuMenuDataStructure>();
        PopuMenuDataStructure popuMenua = new PopuMenuDataStructure(android.R.drawable.ic_menu_edit, "美的");
        popuMenuDatas.add(popuMenua);
        PopuMenuDataStructure popuMenub = new PopuMenuDataStructure(android.R.drawable.ic_menu_edit, "松下");
        popuMenuDatas.add(popuMenub);
        showPopupWindow(popuMenuDatas);

    }

    //筛选条件
    public Object[] search(String name) {
        if(commonAdapterDataSearch !=null) {
            commonAdapterDataSearch.clear();
        }
        for (int i = 0; i < commonAdapterDataList.size(); i++) {
            int index = commonAdapterDataList.get(i).getName().indexOf(name);
            int indey;
            if(toobarTile.getText().toString().equals("全部类别"))
            {
                indey=0;
            }else {
                indey = commonAdapterDataList.get(i).getCategory().indexOf(toobarTile.getText().toString());

            }
            // 存在匹配的数据
            if (index != -1&&indey!=-1) {
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
            adapter = new CommonAdapter(CustomListView.this, R.layout.custom_item, commonAdapterDataSearch);
            listView.setAdapter(adapter);
        }
    }

    private void showPopupWindow(final List<PopuMenuDataStructure> popuMenuData) {
        common = new Common();

        common.PopupWindow(CustomListView.this, dm, popuMenuData);
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
                else if(popuMenuDatas.get(position).getName().equals("客户修改")){

                }else
                {
                    Object[] obj = categorySearch(popuMenuData.get(position).getName().toString());
                    Log.d("lingtan",popuMenuData.get(position).getName().toString());
                    updateLayout(obj);
                    toobarTile.setText(popuMenuData.get(position).getName().toString());
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
                        commonData.setId(category.getId());
                        commonAdapterDataList.add(commonData);



                    }
                    adapter = new CommonAdapter(CustomListView.this, R.layout.custom_item, commonAdapterDataList);
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
            case R.id.custom_toobar_left:
                CustomListView.this.finish();
                break;

            case R.id.custom_toobar_midd:
                if( common.mPopWindow==null ||!common.mPopWindow.isShowing())
                {   popuMenuDatas.clear();
                    PopuMenuDataStructure popuMenub = new PopuMenuDataStructure(R.drawable.poppu_wrie,"全部类别");
                    popuMenuDatas.add(popuMenub);
                    customCategoryList = DataSupport.findAll(CustomCategory.class);
                    for(CustomCategory category: customCategoryList)

                    {
                        PopuMenuDataStructure popuMenua = new PopuMenuDataStructure(R.drawable.poppu_wrie, category.getName());
                        popuMenuDatas.add(popuMenua);

                    }
                    showPopupWindow(popuMenuDatas);
                    int xPos = dm.widthPixels / 3;
                    common.mPopWindow.showAsDropDown(v,-120,5);
                    //mPopWindow.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM, 0, 0);//从底部弹出
                }
                else {
                    common.mPopWindow.dismiss();
                }

                break;

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

            Object[] obj = search(customSearch.getText().toString());
            updateLayout(obj);

        }
    };
}
