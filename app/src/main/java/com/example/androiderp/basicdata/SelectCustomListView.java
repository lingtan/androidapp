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

public class SelectCustomListView extends CustomSearchBase implements View.OnClickListener {
    private List<CommonDataStructure> customListDatas = new ArrayList<CommonDataStructure>();
    private CommonAdapter rightAdapter;
    private CommonAdapter leftAdapter;
    private ListView rightListView;
    private ListView leftListView;
    private DisplayMetrics dm;
    private User user;
    private List<CommonDataStructure> searchDatas= new ArrayList<CommonDataStructure>();
    private List<Custom> customAllDatas;
    private TextView toobar_l,toobar_r,toobar_m;
    private CustomSearch search;
    private List<CustomCategory> categoryAllDatas;
    private List<CommonDataStructure> categorylistdatas = new ArrayList<CommonDataStructure>();
    private String selectCategory;
    private int pposition;

    @Override
    public void iniView(){
        setContentView(R.layout.custom_listview_layout);
        toobar_l=(TextView)findViewById(R.id.custom_toobar_left) ;
        toobar_m=(TextView)findViewById(R.id.custom_toobar_midd);
        toobar_r=(TextView)findViewById(R.id.custom_toobar_right);
        toobar_l.setOnClickListener(this);
        toobar_r.setOnClickListener(this);
        toobar_m.setOnClickListener(this);
        search = (CustomSearch) findViewById(R.id.search);
        customAllDatas= DataSupport.findAll(Custom.class);
        toobar_m.setText("客户");
        selectCategory="全部";
        for(Custom custom:customAllDatas)

        {
            CommonDataStructure commonData=new CommonDataStructure();
            commonData.setName(custom.getName());
            commonData.setCategory(custom.getCategory());
            commonData.setId(custom.getId());
            customListDatas.add(commonData);



        }
        categoryAllDatas= DataSupport.findAll(CustomCategory.class);
        CommonDataStructure commonDataAll=new CommonDataStructure();
        commonDataAll.setName("全部");
        categorylistdatas.add(commonDataAll);
        CommonDataStructure commonDataN=new CommonDataStructure();
        commonDataN.setName("未分类");
        categorylistdatas.add(commonDataN);
        for(CustomCategory custom:categoryAllDatas)

        {
            CommonDataStructure commonData=new CommonDataStructure();
            commonData.setName(custom.getName());
            commonData.setId(custom.getId());
            categorylistdatas.add(commonData);

        }
        //构造函数第一参数是类的对象，第二个是布局文件，第三个是数据源
        leftListView=(ListView) findViewById(R.id.left_list);
        leftListView.setTextFilterEnabled(true);
        rightListView = (ListView) findViewById(R.id.right_list);
        rightListView.setTextFilterEnabled(true);
        leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectCategory=categorylistdatas.get(position).getName().toString();
                pposition=position;
                leftAdapter.setSeclection(position);
                leftAdapter.notifyDataSetInvalidated();
                Object[] obj = categorySearch(categorylistdatas.get(position).getName().toString());
                updateLayout(obj);
            }
        });
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        user=(User) getIntent().getParcelableExtra("user_data");
        rightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                Intent intent=getIntent();
                        if(searchDatas.size()!=0) {

                            intent.putExtra("data_return", String.valueOf(searchDatas.get(position).getName()));


                        }else {

                            intent.putExtra("data_return", String.valueOf(customListDatas.get(position).getName()));
                        }
                setResult(RESULT_OK,intent);
                finish();


            }
        });

            leftAdapter = new CommonAdapter(SelectCustomListView.this, R.layout.custom_item, categorylistdatas);
            leftAdapter.setSeclection(0);
            leftListView.setAdapter(leftAdapter);
            rightAdapter = new CommonAdapter(SelectCustomListView.this, R.layout.custom_item, customListDatas);
            rightListView.setAdapter(rightAdapter);
            


        search.addTextChangedListener(textWatcher);

    }

    //筛选条件
    public Object[] search(String name) {
        if(searchDatas!=null) {
            searchDatas.clear();
        }
        for (int i = 0; i < customListDatas.size(); i++) {
            int index = customListDatas.get(i).getName().indexOf(name);
            int indey;
            if(selectCategory.equals("全部"))
            {
                indey=0;
            }else {
                indey = customListDatas.get(i).getCategory().indexOf(selectCategory);

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
        if(name.equals("未分类"))
        {
            for (int i = 0; i < customListDatas.size(); i++) {
               if(customListDatas.get(i).getCategory()==null)
               {
                    searchDatas.add(customListDatas.get(i));
               }
            }

        }else if (name.equals("全部"))
        {
            for (int i = 0; i < customListDatas.size(); i++) {

                    searchDatas.add(customListDatas.get(i));

            }

        }

        else {
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
            rightAdapter = new CommonAdapter(SelectCustomListView.this, R.layout.custom_item, searchDatas);
            rightListView.setAdapter(rightAdapter);
        }
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
                    rightAdapter = new CommonAdapter(SelectCustomListView.this, R.layout.custom_item, customListDatas);
                    rightListView.setAdapter(rightAdapter);
                }
                break;

            default:
                }
        }



    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.custom_toobar_left:
                SelectCustomListView.this.finish();
                break;

            case R.id.custom_toobar_right:
                Intent intent=new Intent(SelectCustomListView.this,CustomForm.class);
                intent.removeExtra("custom_item");
                intent.putExtra("action","add");
                startActivityForResult(intent,1);
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
