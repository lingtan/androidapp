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
import com.example.androiderp.CustomDataClass.Product;
import com.example.androiderp.CustomDataClass.ProductCategory;
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
import com.example.androiderp.form.ProductForm;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class ProductTowListView extends CustomSearchBase implements View.OnClickListener {
    private List<CommonDataStructure> customListDatas = new ArrayList<CommonDataStructure>();
    private List<PopuMenuDataStructure> popuMenuDatas;
    private CommonAdapter rightAdapter;
    private CommonAdapter leftAdapter;
    private ListView rightListView;
    private ListView leftListView;
    private DisplayMetrics dm;
    private List<CommonDataStructure> searchDatas= new ArrayList<CommonDataStructure>();
    private List<Product> customAllDatas;
    private List<ProductCategory> categoryDatas;
    private Common common;
    private TextView toobar_l,toobar_r,toobar_m;
    private CustomSearch search;
    private Intent intent;
    private List<ProductCategory> categoryAllDatas;
    private List<CommonDataStructure> categorylistdatas = new ArrayList<CommonDataStructure>();
    private int pposition;

    @Override
    public void iniView(){
        setContentView(R.layout.custom_listview_layout);
        toobar_l=(TextView)findViewById(R.id.custom_toobar_left) ;
        toobar_m=(TextView)findViewById(R.id.custom_toobar_midd);
        toobar_r=(TextView)findViewById(R.id.custom_toobar_right);
        toobar_m.setText("商品信息");
        toobar_m.setCompoundDrawables(null,null,null,null);
        toobar_l.setOnClickListener(this);
        toobar_r.setOnClickListener(this);
        toobar_m.setOnClickListener(this);
        search = (CustomSearch) findViewById(R.id.search);
        customAllDatas= DataSupport.findAll(Product.class);
        intent= new Intent(ProductTowListView.this, ProductForm.class);
        for(Product product:customAllDatas)

        {
            CommonDataStructure commonData=new CommonDataStructure();
            commonData.setName(product.getName());
            commonData.setCategory(product.getCategory());
            commonData.setId(product.getId());
            customListDatas.add(commonData);



        }
        categoryAllDatas= DataSupport.findAll(ProductCategory.class);
        CommonDataStructure commonDataAll=new CommonDataStructure();
        commonDataAll.setName("全部产品");
        categorylistdatas.add(commonDataAll);
        CommonDataStructure commonDataN=new CommonDataStructure();
        commonDataN.setName("未分类");
        categorylistdatas.add(commonDataN);
        for(ProductCategory productCategory:categoryAllDatas)

        {
            CommonDataStructure commonData=new CommonDataStructure();
            commonData.setName(productCategory.getName());
            commonData.setId(productCategory.getId());
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
                pposition=position;
                leftAdapter.setSeclection(position);
                leftAdapter.notifyDataSetInvalidated();
                Object[] obj = categorySearch(categorylistdatas.get(position).getName().toString());
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
                        if(searchDatas.size()!=0) {

                            intent.putExtra("action", "edit");
                            intent.putExtra("product_item", String.valueOf(searchDatas.get(position).getId()));


                        }else {

                            intent.putExtra("action", "edit");
                            intent.putExtra("product_item", String.valueOf(customListDatas.get(position).getId()));

                        }
                startActivityForResult(intent,1);


            }
        });

            leftAdapter = new CommonAdapter(ProductTowListView.this, R.layout.custom_item, categorylistdatas);
            leftAdapter.setSeclection(0);
            leftListView.setAdapter(leftAdapter);
            rightAdapter = new CommonAdapter(ProductTowListView.this, R.layout.custom_item, customListDatas);
            rightListView.setAdapter(rightAdapter);
            


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
        if(name.equals("未分类"))
        {
            for (int i = 0; i < customListDatas.size(); i++) {
               if(customListDatas.get(i).getCategory()==null)
               {
                    searchDatas.add(customListDatas.get(i));
               }
            }

        }else if (name.equals("全部产品"))
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
            rightAdapter = new CommonAdapter(ProductTowListView.this, R.layout.custom_item, searchDatas);
            rightListView.setAdapter(rightAdapter);
        }
    }

    private void showPopupWindow(final List<PopuMenuDataStructure> popuMenuData) {
        common = new Common();

        common.PopupWindow(ProductTowListView.this, dm, popuMenuData);
        common.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                if(popuMenuDatas.get(position).getName().equals("商品新增"))
                {
                    intent.removeExtra("product_item");
                    intent.putExtra("action","add");
                    startActivityForResult(intent,1);
                }
                else if(popuMenuDatas.get(position).getName().equals("商品修改")){

                }else
                {

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
                    customAllDatas= DataSupport.findAll(Product.class);
                    for(Product category:customAllDatas)

                    {
                        CommonDataStructure commonData=new CommonDataStructure();
                        commonData.setName(category.getName());
                        commonData.setCategory(category.getCategory());
                        commonData.setId(category.getId());
                        customListDatas.add(commonData);



                    }
                    rightAdapter = new CommonAdapter(ProductTowListView.this, R.layout.custom_item, customListDatas);
                    rightListView.setAdapter(rightAdapter);
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
                ProductTowListView.this.finish();
                break;

            case R.id.custom_toobar_right:
                if( common.mPopWindow==null ||!common.mPopWindow.isShowing())
                {   popuMenuDatas.clear();

                    PopuMenuDataStructure popuMenub = new PopuMenuDataStructure(R.drawable.poppu_wrie, "商品新增");
                    popuMenuDatas.add(popuMenub);
                    PopuMenuDataStructure popuMenua = new PopuMenuDataStructure(R.drawable.poppu_wrie, "商品修改");
                    popuMenuDatas.add(popuMenua);
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
