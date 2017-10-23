package com.example.androiderp.activities.basicview;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androiderp.bean.Custom;
import com.example.androiderp.bean.CustomCategory;
import com.example.androiderp.bean.User;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonAdapter;
import com.example.androiderp.adaper.CommonAdapterData;
import com.example.androiderp.ui.CSearch;
import com.example.androiderp.ui.CSearchBase;
import com.example.androiderp.activities.basicfrom.CustomForm;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class CustomSelectView extends CSearchBase implements View.OnClickListener {
    private List<CommonAdapterData> commonAdapterDataList = new ArrayList<CommonAdapterData>();
    private CommonAdapter rightAdapter;
    private CommonAdapter leftAdapter;
    private ListView rightListView;
    private ListView leftListView;
    private DisplayMetrics dm;
    private User user;
    private List<CommonAdapterData> commonAdapterDataSearch = new ArrayList<CommonAdapterData>();
    private List<Custom> customList;
    private TextView toobarBack, toobarAdd, toobarTile;
    private CSearch search;
    private List<CustomCategory> customCategoryList;
    private List<CommonAdapterData> categorylist = new ArrayList<CommonAdapterData>();
    private String selectCategory;
    private ImageView lastCheckedOption;
    private int positionTemp;
    private int indexPositon;
    private String indexName;

    @Override
    public void iniView(){
        setContentView(R.layout.custom_listview_layout);
        toobarBack =(TextView)findViewById(R.id.custom_toobar_left) ;
        toobarTile =(TextView)findViewById(R.id.custom_toobar_midd);
        toobarAdd =(TextView)findViewById(R.id.custom_toobar_right);
        toobarBack.setOnClickListener(this);
        toobarAdd.setOnClickListener(this);
        toobarTile.setOnClickListener(this);
        Intent intent=getIntent();
        indexName =intent.getStringExtra("index");
        search = (CSearch) findViewById(R.id.search);
        customList = DataSupport.findAll(Custom.class);
        toobarTile.setText("客户");
        selectCategory="全部";
        for(Custom custom: customList)

        {
            if(custom.getName().equals(indexName))
            {
                indexPositon = customList.indexOf(custom);
            }
            CommonAdapterData commonData=new CommonAdapterData();
            commonData.setName(custom.getName());
            commonData.setCategory(custom.getCategory());
            commonData.setUnitId(custom.getId());
            commonData.setImage(R.drawable.seclec_arrow);
            commonAdapterDataList.add(commonData);



        }
        customCategoryList = DataSupport.findAll(CustomCategory.class);
        CommonAdapterData commonDataAll=new CommonAdapterData();
        commonDataAll.setName("全部");
        categorylist.add(commonDataAll);
        CommonAdapterData commonDataN=new CommonAdapterData();
        commonDataN.setName("未分类");
        categorylist.add(commonDataN);
        for(CustomCategory custom: customCategoryList)

        {
            CommonAdapterData commonData=new CommonAdapterData();
            commonData.setName(custom.getName());
            commonData.setUnitId(custom.getId());
            categorylist.add(commonData);

        }
        //构造函数第一参数是类的对象，第二个是布局文件，第三个是数据源
        leftListView=(ListView) findViewById(R.id.left_list);
        leftListView.setTextFilterEnabled(true);
        rightListView = (ListView) findViewById(R.id.right_list);
        rightListView.setTextFilterEnabled(true);
        leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectCategory= categorylist.get(position).getName().toString();
                positionTemp =position;
                leftAdapter.setSeclection(position);
                leftAdapter.notifyDataSetInvalidated();
                Object[] obj = categorySearch(categorylist.get(position).getName().toString());
                updateLayout(categorylist.get(position).getName().toString());
            }
        });
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        user= getIntent().getParcelableExtra("user_data");
        if(indexName.isEmpty())
        {
            indexPositon =-1;
        }else {
            positionTemp = indexPositon;
        }
        rightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                Intent intent=getIntent();
                        if(commonAdapterDataSearch.size()!=0) {

                            intent.putExtra("data_return", String.valueOf(commonAdapterDataSearch.get(position).getName()));
                            indexName = commonAdapterDataSearch.get(position).getName();

                        }else {

                            intent.putExtra("data_return", String.valueOf(commonAdapterDataList.get(position).getName()));
                            indexName = commonAdapterDataList.get(position).getName();
                        }
                if(lastCheckedOption != null){
                    lastCheckedOption.setVisibility(View.INVISIBLE);
                }
                lastCheckedOption = (ImageView)view.findViewById(R.id.custom_item_layout_one_image);
                lastCheckedOption.setVisibility(View.VISIBLE);
                positionTemp =position;
                setResult(RESULT_OK,intent);
                finish();


            }
        });

            leftAdapter = new CommonAdapter(CustomSelectView.this, R.layout.custom_item, categorylist);
            leftAdapter.setSeclection(0);
            leftListView.setAdapter(leftAdapter);
            rightAdapter = new CommonAdapter(CustomSelectView.this, R.layout.custom_item, commonAdapterDataList);
             rightAdapter.setSeclection(indexPositon);
            rightListView.setAdapter(rightAdapter);
            


        search.addTextChangedListener(textWatcher);

    }

    //筛选条件
    public Object[] search(String name) {
        if(commonAdapterDataSearch !=null) {
            commonAdapterDataSearch.clear();
        }
        for (int i = 0; i < commonAdapterDataList.size(); i++) {
            int index = commonAdapterDataList.get(i).getName().indexOf(name);
            int indey;
            if(selectCategory.equals("全部"))
            {
                indey=0;
            }else {
                indey = commonAdapterDataList.get(i).getCategory().indexOf(selectCategory);

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
        if(name.equals("未分类"))
        {
            for (int i = 0; i < commonAdapterDataList.size(); i++) {
               if(commonAdapterDataList.get(i).getCategory()==null)
               {
                    commonAdapterDataSearch.add(commonAdapterDataList.get(i));
               }
            }

        }else if (name.equals("全部"))
        {
            for (int i = 0; i < commonAdapterDataList.size(); i++) {

                    commonAdapterDataSearch.add(commonAdapterDataList.get(i));

            }

        }

        else {
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
            rightAdapter = new CommonAdapter(CustomSelectView.this, R.layout.custom_item, commonAdapterDataSearch);
            rightAdapter.setSeclection(index);
            rightListView.setAdapter(rightAdapter);
        }
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
                        commonData.setImage(R.drawable.seclec_arrow);
                        commonAdapterDataList.add(commonData);



                    }
                    rightAdapter = new CommonAdapter(CustomSelectView.this, R.layout.custom_item, commonAdapterDataList);
                    rightAdapter.setSeclection(positionTemp);
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
                CustomSelectView.this.finish();
                break;

            case R.id.custom_toobar_right:
                Intent intent=new Intent(CustomSelectView.this,CustomForm.class);
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
            updateLayout(search.getText().toString());

        }
    };
}