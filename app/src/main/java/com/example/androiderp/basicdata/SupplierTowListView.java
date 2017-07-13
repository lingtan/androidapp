package com.example.androiderp.basicdata;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.androiderp.CustomDataClass.Supplier;
import com.example.androiderp.CustomDataClass.SupplierCategory;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonAdapter;
import com.example.androiderp.adaper.CommonDataStructure;
import com.example.androiderp.custom.CustomSearch;
import com.example.androiderp.custom.CustomSearchBase;
import com.example.androiderp.form.SupplierForm;
import org.litepal.crud.DataSupport;
import java.util.ArrayList;
import java.util.List;

public class SupplierTowListView extends CustomSearchBase implements View.OnClickListener {
    private List<CommonDataStructure> commonDataStructureList = new ArrayList<CommonDataStructure>();
    private CommonAdapter rightAdapter;
    private CommonAdapter leftAdapter;
    private ListView rightListView;
    private ListView leftListView;
    private DisplayMetrics dm;
    private List<CommonDataStructure> commonDataStructureSearch = new ArrayList<CommonDataStructure>();
    private List<Supplier> supplierList;
    private TextView toobarBack, toobarAdd, toobarTile;
    private CustomSearch customSearch;
    private String selectCategory;
    private List<SupplierCategory> supplierCategoryList;
    private List<CommonDataStructure> categorylist = new ArrayList<CommonDataStructure>();

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
        supplierList = DataSupport.findAll(Supplier.class);

        toobarTile.setText("供应商");
        selectCategory="全部";
        for(Supplier supplier: supplierList)

        {
            CommonDataStructure commonData=new CommonDataStructure();
            commonData.setName(supplier.getName());
            commonData.setCategory(supplier.getCategory());
            commonData.setId(supplier.getId());
            commonDataStructureList.add(commonData);



        }
        supplierCategoryList = DataSupport.findAll(SupplierCategory.class);
        CommonDataStructure commonDataAll=new CommonDataStructure();
        commonDataAll.setName("全部");
        categorylist.add(commonDataAll);
        CommonDataStructure commonDataN=new CommonDataStructure();
        commonDataN.setName("未分类");
        categorylist.add(commonDataN);
        for(SupplierCategory sCategory: supplierCategoryList)

        {
            CommonDataStructure commonData=new CommonDataStructure();
            commonData.setName(sCategory.getName());
            commonData.setId(sCategory.getId());
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
             Intent   intent= new Intent(SupplierTowListView.this, SupplierForm.class);
                        if(commonDataStructureSearch.size()!=0) {

                            intent.putExtra("action", "edit");
                            intent.putExtra("supller_item", String.valueOf(commonDataStructureSearch.get(position).getId()));


                        }else {

                            intent.putExtra("action", "edit");
                            intent.putExtra("supller_item", String.valueOf(commonDataStructureList.get(position).getId()));

                        }
                startActivityForResult(intent,1);


            }
        });

            leftAdapter = new CommonAdapter(SupplierTowListView.this, R.layout.custom_item, categorylist);
            leftAdapter.setSeclection(0);
            leftListView.setAdapter(leftAdapter);
            rightAdapter = new CommonAdapter(SupplierTowListView.this, R.layout.custom_item, commonDataStructureList);
            rightListView.setAdapter(rightAdapter);
            


        customSearch.addTextChangedListener(textWatcher);

    }

    //筛选条件
    public Object[] search(String name) {
        if(commonDataStructureSearch !=null) {
            commonDataStructureSearch.clear();
        }
        for (int i = 0; i < commonDataStructureList.size(); i++) {
            int index = commonDataStructureList.get(i).getName().indexOf(name);
            int indey;
            if(selectCategory.equals("全部"))
            {
                indey=0;
            }else {
                indey = commonDataStructureList.get(i).getCategory().indexOf(selectCategory);

            }
            // 存在匹配的数据
            if (index != -1&&indey!=-1) {
                commonDataStructureSearch.add(commonDataStructureList.get(i));
            }
        }
        return commonDataStructureSearch.toArray();
    }

    public Object[] categorySearch(String name) {

        if(commonDataStructureSearch !=null) {
            commonDataStructureSearch.clear();
        }
        if(name.equals("未分类"))
        {
            for (int i = 0; i < commonDataStructureList.size(); i++) {
               if(commonDataStructureList.get(i).getCategory()==null)
               {
                    commonDataStructureSearch.add(commonDataStructureList.get(i));
               }
            }

        }else if (name.equals("全部"))
        {
            for (int i = 0; i < commonDataStructureList.size(); i++) {

                    commonDataStructureSearch.add(commonDataStructureList.get(i));

            }

        }

        else {
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
            rightAdapter = new CommonAdapter(SupplierTowListView.this, R.layout.custom_item, commonDataStructureSearch);
            rightListView.setAdapter(rightAdapter);
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
                    supplierList = DataSupport.findAll(Supplier.class);
                    for(Supplier supplier: supplierList)

                    {
                        CommonDataStructure commonData=new CommonDataStructure();
                        commonData.setName(supplier.getName());
                        commonData.setCategory(supplier.getCategory());
                        commonData.setId(supplier.getId());
                        commonDataStructureList.add(commonData);



                    }
                    rightAdapter = new CommonAdapter(SupplierTowListView.this, R.layout.custom_item, commonDataStructureList);
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
                SupplierTowListView.this.finish();
                break;

            case R.id.custom_toobar_right:
                Intent  intent= new Intent(SupplierTowListView.this, SupplierForm.class);
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
