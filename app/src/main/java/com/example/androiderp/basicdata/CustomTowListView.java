package com.example.androiderp.basicdata;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.androiderp.CustomDataClass.Custom;
import com.example.androiderp.CustomDataClass.CustomCategory;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonAdapter;
import com.example.androiderp.adaper.CommonAdapterData;
import com.example.androiderp.adaper.DataStructure;
import com.example.androiderp.custom.CustomSearch;
import com.example.androiderp.custom.CustomSearchBase;
import com.example.androiderp.form.CustomForm;
import org.litepal.crud.DataSupport;
import java.util.ArrayList;
import java.util.List;

public class CustomTowListView extends CustomSearchBase implements View.OnClickListener {
    private List<CommonAdapterData> commonAdapterDataList = new ArrayList<CommonAdapterData>();
    private CommonAdapter rightAdapter;
    private CommonAdapter leftAdapter;
    private ListView rightListView;
    private ListView leftListView;
    private DisplayMetrics dm;
    private List<CommonAdapterData> commonAdapterDataSearch = new ArrayList<CommonAdapterData>();
    private List<Custom> customList;
    private TextView toobarBack, toobarAdd, toobarTile;
    private CustomSearch customSearch;
    private Intent intent;
    private List<CustomCategory> customCategoryList;
    private List<CommonAdapterData> categorylist = new ArrayList<CommonAdapterData>();
    private String selectCategory;
    private String leftItemName="全部";

    @Override
    public void iniView(){
        setContentView(R.layout.custom_listview_layout);
        toobarBack =(TextView)findViewById(R.id.custom_toobar_left) ;
        toobarTile =(TextView)findViewById(R.id.custom_toobar_midd);
        toobarAdd =(TextView)findViewById(R.id.custom_toobar_right);
        toobarBack.setOnClickListener(this);
        toobarAdd.setOnClickListener(this);
        toobarTile.setOnClickListener(this);
        customSearch = (CustomSearch) findViewById(R.id.search);
        customList = DataSupport.findAll(Custom.class);
        intent= new Intent(CustomTowListView.this, CustomForm.class);
        toobarTile.setText("客户");
        selectCategory="全部";
        for(Custom custom: customList)

        {
            CommonAdapterData commonData=new CommonAdapterData();
            commonData.setName(custom.getName());
            commonData.setCategory(custom.getCategory());
            commonData.setUnitId(custom.getId());
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
                leftAdapter.setSeclection(position);
                leftAdapter.notifyDataSetInvalidated();
                leftItemName=categorylist.get(position).getName().toString();
                Object[] obj = categorySearch(categorylist.get(position).getName().toString());
                updateLayout(obj);
            }
        });
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        rightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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

            leftAdapter = new CommonAdapter(CustomTowListView.this, R.layout.custom_item, categorylist);
            leftAdapter.setSeclection(0);
            leftListView.setAdapter(leftAdapter);
            rightAdapter = new CommonAdapter(CustomTowListView.this, R.layout.custom_item, commonAdapterDataList);
            rightListView.setAdapter(rightAdapter);
            


        customSearch.addTextChangedListener(textWatcher);


    }

    //筛选条件
    public Object[] search(String name) {
        commonAdapterDataSearch.clear();
        commonAdapterDataList.clear();

        customList = DataStructure.where("name like ?","%" + name + "%").find(Custom.class);
        for(Custom custom: customList)

        {
            CommonAdapterData commonData=new CommonAdapterData();
            commonData.setName(custom.getName());
            commonData.setCategory(custom.getCategory());
            commonData.setUnitId(custom.getId());
            commonAdapterDataList.add(commonData);



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

        commonAdapterDataSearch.clear();
        commonAdapterDataList.clear();
        if(name.equals("全部")||name.equals("未分类"))
        {
            customList = DataStructure.findAll(Custom.class);
        }else {
            customList = DataStructure.where("category like ?","%" + name + "%").find(Custom.class);
        }

        for(Custom custom: customList)

        {
            CommonAdapterData commonData=new CommonAdapterData();
            commonData.setName(custom.getName());
            commonData.setCategory(custom.getCategory());
            commonData.setUnitId(custom.getId());
            commonAdapterDataList.add(commonData);



        }

        if(name.equals("未分类"))
        {
            for (int i = 0; i < commonAdapterDataList.size(); i++) {
               if(commonAdapterDataList.get(i).getCategory().isEmpty())
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

                int index = commonAdapterDataList.get(i).getCategory().indexOf(name);
                // 存在匹配的数据
                if (index != -1) {
                    commonAdapterDataSearch.add(commonAdapterDataList.get(i));
                }

        }}
        return commonAdapterDataSearch.toArray();
    }
//adapter刷新,重写Filter方式会出现BUG.
    public void updateLayout(Object[] obj) {
        if(commonAdapterDataSearch !=null) {
            rightAdapter = new CommonAdapter(CustomTowListView.this, R.layout.custom_item, commonAdapterDataSearch);
            rightListView.setAdapter(rightAdapter);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK)
                { Object[] obj = categorySearch(leftItemName);
                    updateLayout(obj);
                    leftAdapter.notifyDataSetChanged();
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
                CustomTowListView.this.finish();
                break;

            case R.id.custom_toobar_right:
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

            Object[] obj = search(customSearch.getText().toString());
            updateLayout(obj);

        }
    };
}
