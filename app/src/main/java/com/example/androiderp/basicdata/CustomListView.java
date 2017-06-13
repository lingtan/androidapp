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
import com.example.androiderp.adaper.CommonDataStructure;
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
    private List<CommonDataStructure> customListDatas = new ArrayList<CommonDataStructure>();
    private List<DataStructure> fruit = new ArrayList<DataStructure>();
    private List<PopuMenuDataStructure> popuMenuDatas;
    private CommonAdapter adapter;
    private ListView listView;
    private DisplayMetrics dm;
    private User user;
    private List<CommonDataStructure> searchDatas= new ArrayList<CommonDataStructure>();
    private List<Custom> customAllDatas;
    private List<CustomCategory> categoryDatas;
    private Common common;
    private TextView toobar_l,toobar_r,toobar_m;
    private CustomSearch search;
    private Intent intent;

    @Override
    public void iniView(){
        setContentView(R.layout.custom_layout);
        toobar_l=(TextView)findViewById(R.id.custom_toobar_left) ;
        toobar_m=(TextView)findViewById(R.id.custom_toobar_midd);
        toobar_r=(TextView)findViewById(R.id.custom_toobar_right);
        toobar_l.setOnClickListener(this);
        toobar_r.setOnClickListener(this);
        toobar_m.setOnClickListener(this);
        search = (CustomSearch) findViewById(R.id.search);
        customAllDatas= DataSupport.findAll(Custom.class);
        intent= new Intent(CustomListView.this, CustomForm.class);
        for(Custom custom:customAllDatas)

        {
            CommonDataStructure commonData=new CommonDataStructure();
            commonData.setName(custom.getName());
            commonData.setCategory(custom.getCategory());
            commonData.setId(custom.getId());
            customListDatas.add(commonData);



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
                        if(searchDatas.size()!=0) {

                            intent.putExtra("action", "edit");
                            intent.putExtra("custom_item", String.valueOf(searchDatas.get(position).getId()));


                        }else {

                            intent.putExtra("action", "edit");
                            intent.putExtra("custom_item", String.valueOf(customListDatas.get(position).getId()));

                        }
                startActivityForResult(intent,1);


            }
        });
        if(customListDatas.size()!=0) {

                 adapter = new CommonAdapter(CustomListView.this, R.layout.custom_item, customListDatas);
                 listView.setAdapter(adapter);
            
        }

        search.addTextChangedListener(textWatcher);

        popuMenuDatas = new ArrayList<PopuMenuDataStructure>();
        PopuMenuDataStructure popuMenua = new PopuMenuDataStructure(android.R.drawable.ic_menu_edit, "美的");
        popuMenuDatas.add(popuMenua);
        PopuMenuDataStructure popuMenub = new PopuMenuDataStructure(android.R.drawable.ic_menu_edit, "松下");
        popuMenuDatas.add(popuMenub);
        showPopupWindow(popuMenuDatas);

    }

    //筛选条件
    public Object[] search(String name) {
        if(searchDatas!=null) {
            searchDatas.clear();
        }
        for (int i = 0; i < customListDatas.size(); i++) {
            int index = customListDatas.get(i).getName().indexOf(name);
            int indey;
            if(toobar_m.getText().toString().equals("全部类别"))
            {
                indey=0;
            }else {
                indey = customListDatas.get(i).getCategory().indexOf(toobar_m.getText().toString());

            }
            // 存在匹配的数据
            if (index != -1&&indey!=-1) {
                searchDatas.add(customListDatas.get(i));
            }
        }
        return searchDatas.toArray();
    }

    public Object[] categorySearch(String name) {

        if(searchDatas!=null) {
            searchDatas.clear();
        }
        if(name.equals("全部类别"))
        {
            for (int i = 0; i < customListDatas.size(); i++) {
               if(customListDatas.get(i).getCategory()!=null)
               {
                    searchDatas.add(customListDatas.get(i));
               }
            }

        }else {
        for (int i = 0; i < customListDatas.size(); i++) {
              if(customListDatas.get(i).getCategory()!=null){
                int index = customListDatas.get(i).getCategory().indexOf(name);
                // 存在匹配的数据
                if (index != -1) {
                    searchDatas.add(customListDatas.get(i));
                }
            }
        }}
        return searchDatas.toArray();
    }
//adapter刷新,重写Filter方式会出现BUG.
    public void updateLayout(Object[] obj) {
        if(searchDatas!=null) {
            adapter = new CommonAdapter(CustomListView.this, R.layout.custom_item, searchDatas);
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
                    toobar_m.setText(popuMenuData.get(position).getName().toString());
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
                    if(customListDatas.size()!=0) {
                        customListDatas.clear();
                    }
                    customAllDatas= DataSupport.findAll(Custom.class);
                    for(Custom category:customAllDatas)

                    {
                        CommonDataStructure commonData=new CommonDataStructure();
                        commonData.setName(category.getName());
                        commonData.setCategory(category.getCategory());
                        commonData.setId(category.getId());
                        customListDatas.add(commonData);



                    }
                    adapter = new CommonAdapter(CustomListView.this, R.layout.custom_item, customListDatas);
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
                    categoryDatas= DataSupport.findAll(CustomCategory.class);
                    for(CustomCategory category:categoryDatas)

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

            Object[] obj = search(search.getText().toString());
            updateLayout(obj);

        }
    };
}