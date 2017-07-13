package com.example.androiderp.basicdata;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androiderp.CustomDataClass.SupplierCategory;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonAdapter;
import com.example.androiderp.adaper.CommonDataStructure;
import com.example.androiderp.custom.CustomSearch;
import com.example.androiderp.custom.CustomSearchBase;
import com.example.androiderp.form.SupplierCategoryForm;
import com.example.androiderp.form.SupplierForm;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class SupplierCategoryListView extends CustomSearchBase implements View.OnClickListener {
    private List<CommonDataStructure> commonDataStructureList = new ArrayList<CommonDataStructure>();
    private CommonAdapter adapter;
    private ListView listView;
    private DisplayMetrics dm;
    private List<CommonDataStructure> commonDataStructureSearch = new ArrayList<CommonDataStructure>();
    private List<SupplierCategory> supplierCategoryList;
    private TextView toobarBack, toobarAdd, toobarTile;
    private CustomSearch customSearch;
    private String categoryid;
    private ImageView lastCheckedOption;
    private int positionTemp;
    private int indexPositon;
    private String indexName;
    private int searchIndex =-1;
    @Override
    public void iniView(){
        setContentView(R.layout.custom_layout);
        toobarBack =(TextView)findViewById(R.id.custom_toobar_left) ;
        toobarTile =(TextView)findViewById(R.id.custom_toobar_midd);
        toobarTile.setText("供应商分类");
        toobarAdd =(TextView)findViewById(R.id.custom_toobar_right);
        toobarBack.setOnClickListener(this);
        toobarAdd.setOnClickListener(this);
        toobarTile.setOnClickListener(this);
        customSearch = (CustomSearch) findViewById(R.id.search);
        supplierCategoryList = DataSupport.findAll(SupplierCategory.class);

        toobarBack.setCompoundDrawables(null,null,null,null);
        Toolbar.LayoutParams params = new Toolbar.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 0, 0, 0);
        toobarBack.setLayoutParams(params);
        Drawable del= getResources().getDrawable(R.drawable.suppliercategory_delete);
        del.setBounds(0, 0, del.getMinimumWidth(), del.getMinimumHeight());
        toobarAdd.setCompoundDrawables(del,null,null,null);
        toobarTile.setCompoundDrawables(null,null,null,null);
        Intent intent=getIntent();
        categoryid=intent.getStringExtra("category");
        indexName =intent.getStringExtra("index");
        toobarBack.setText("新增分类");
        for(SupplierCategory supplierCategory: supplierCategoryList)

        {
            if(supplierCategory.getName().equals(indexName))
            {
                indexPositon = supplierCategoryList.indexOf(supplierCategory);
            }
            CommonDataStructure commonData=new CommonDataStructure();
            commonData.setName(supplierCategory.getName());
            commonData.setId(supplierCategory.getId());
            commonData.setImage(R.drawable.seclec_arrow);
            commonDataStructureList.add(commonData);



        }
        if(indexName.isEmpty())
        {
            indexPositon =-1;
        }else {
            positionTemp = indexPositon;
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
                Intent intent=new Intent(SupplierCategoryListView.this,SupplierForm.class);
                        if(commonDataStructureSearch.size()!=0) {

                            intent.putExtra("data_return", commonDataStructureSearch.get(position).getName());
                            indexName = commonDataStructureSearch.get(position).getName();

                        }else {

                            intent.putExtra("data_return", commonDataStructureList.get(position).getName());
                            indexName = commonDataStructureList.get(position).getName();
                        }
               setResult(RESULT_OK,intent);
                if(lastCheckedOption != null){
                    lastCheckedOption.setVisibility(View.INVISIBLE);
                }
                lastCheckedOption = (ImageView)view.findViewById(R.id.custom_item_layout_one_image);
                lastCheckedOption.setVisibility(View.VISIBLE);
                positionTemp =position;
                SupplierCategoryListView.this.finish();


            }
        });


        if(commonDataStructureList.size()!=0) {
             if(categoryid!=null) {
                 Object[] obj = searchCategory(categoryid);
                 updateLayout("10");
                 toobarTile.setText(categoryid);
             }else {
                 adapter = new CommonAdapter(SupplierCategoryListView.this, R.layout.custom_item, commonDataStructureList);
                 adapter.setSeclection(indexPositon);
                 listView.setAdapter(adapter);
             }

        }

        customSearch.addTextChangedListener(textWatcher);


    }

    //筛选条件
    public Object[] searchItem(String name) {
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

    public Object[] searchCategory(String name) {

        if(commonDataStructureSearch !=null) {
            commonDataStructureSearch.clear();
        }
        for (int i = 0; i < commonDataStructureList.size(); i++) {
            ;
            if(commonDataStructureList.get(i).getCategory()!=null) {
                int index = commonDataStructureList.get(i).getCategory().indexOf(name);
                // 存在匹配的数据
                if (index != -1) {
                    commonDataStructureSearch.add(commonDataStructureList.get(i));
                }
            }
        }
        return commonDataStructureSearch.toArray();
    }
//adapter刷新,重写Filter方式会出现BUG.
    public void updateLayout(String name) {
        if(commonDataStructureSearch !=null) {
            searchIndex =-1;
            if(!name.isEmpty())
            {
                for(int i = 0; i< commonDataStructureSearch.size(); i++)
                {
                    if(commonDataStructureSearch.get(i).getName().equals(indexName))
                    {
                        searchIndex =i;
                    }
                }
            }else
            {
                searchIndex = positionTemp;
            }
            adapter = new CommonAdapter(SupplierCategoryListView.this, R.layout.custom_item, commonDataStructureSearch);
            adapter.setSeclection(searchIndex);
            listView.setAdapter(adapter);
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.custom_toobar_left:
                Intent cate = new Intent(SupplierCategoryListView.this, SupplierCategoryForm.class);
                cate.putExtra("action","add");
                startActivityForResult(cate,2);

                break;

            case R.id.custom_toobar_midd:


                break;

            case R.id.custom_toobar_right:
                SupplierCategoryListView.this.finish();
                break;


        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 2:
                if(resultCode==RESULT_OK){
                    if(commonDataStructureList.size()!=0) {
                        commonDataStructureList.clear();
                    }
                    supplierCategoryList = DataSupport.findAll(SupplierCategory.class);
                    for(SupplierCategory supplierCategory: supplierCategoryList)

                    {
                        CommonDataStructure commonData=new CommonDataStructure();
                        commonData.setName(supplierCategory.getName());
                        commonData.setId(supplierCategory.getId());
                        commonData.setImage(R.drawable.seclec_arrow);
                        commonDataStructureList.add(commonData);



                    }
                    adapter = new CommonAdapter(SupplierCategoryListView.this, R.layout.custom_item, commonDataStructureList);
                    adapter.setSeclection(positionTemp);
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

            Object[] obj = searchItem(customSearch.getText().toString());
            updateLayout(customSearch.getText().toString());

        }
    };
}
