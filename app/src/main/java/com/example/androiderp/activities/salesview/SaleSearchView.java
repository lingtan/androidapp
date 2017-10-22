package com.example.androiderp.activities.salesview;

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
import com.example.androiderp.bean.SalesOut;
import com.example.androiderp.R;
import com.example.androiderp.bean.PopBean;
import com.example.androiderp.adaper.SaleOutAdapter;
import com.example.androiderp.tools.Common;
import com.example.androiderp.ui.CHomeSearch;
import com.example.androiderp.ui.CSearchBase;

import org.litepal.crud.DataSupport;
import java.util.ArrayList;
import java.util.List;

public class SaleSearchView extends CSearchBase implements View.OnClickListener {
    private List<PopBean> popuMenuDatas;
    private SaleOutAdapter adapter;
    private ListView listView;
    private DisplayMetrics dm;
    private List<SalesOut> salesOutSearch = new ArrayList<SalesOut>();
    private List<SalesOut> salesOutList;
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
        cHomeSearch.setHint("输入客户 | 单据编号");
        salesOutList = DataSupport.where("billtype =?","2").find(SalesOut.class);
        intent= new Intent(SaleSearchView.this, SaleEntyView.class);

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
                        if(salesOutSearch.size()!=0) {

                            intent.putExtra("action", "edit");
                            intent.putExtra("custom_item", String.valueOf(salesOutSearch.get(position).getId()));


                        }else {

                            intent.putExtra("action", "edit");
                            intent.putExtra("custom_item", String.valueOf(salesOutList.get(position).getId()));

                        }
                startActivityForResult(intent,1);


            }
        });
        if(salesOutList.size()!=0) {

                 adapter = new SaleOutAdapter(SaleSearchView.this,R.layout.saleout_item, salesOutList);
                 listView.setAdapter(adapter);
            
        }

        cHomeSearch.addTextChangedListener(textWatcher);

        popuMenuDatas = new ArrayList<PopBean>();
        PopBean popuMenua = new PopBean(android.R.drawable.ic_menu_edit, "美的");
        popuMenuDatas.add(popuMenua);
        PopBean popuMenub = new PopBean(android.R.drawable.ic_menu_edit, "松下");
        popuMenuDatas.add(popuMenub);
        showPopupWindow(popuMenuDatas);

    }

    //筛选条件
    public Object[] search(String name) {
        if(salesOutSearch !=null) {
            salesOutSearch.clear();
        }
        for (int i = 0; i < salesOutList.size(); i++) {
            int index = salesOutList.get(i).getCustomer().indexOf(name);
            int indey = salesOutList.get(i).getNuber().indexOf(name);
            // 存在匹配的数据
            if (index != -1||indey!=-1) {
                salesOutSearch.add(salesOutList.get(i));
            }
        }
        return salesOutSearch.toArray();
    }


//adapter刷新,重写Filter方式会出现BUG.
    public void updateLayout(Object[] obj) {
        if(salesOutSearch !=null) {
            adapter = new SaleOutAdapter(SaleSearchView.this, R.layout.saleout_item, salesOutSearch);
            listView.setAdapter(adapter);
        }
    }

    private void showPopupWindow(final List<PopBean> popuMenuData) {
        common = new Common();

        common.PopupWindow(SaleSearchView.this, dm, popuMenuData);
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
                    if(salesOutList.size()!=0) {
                        salesOutList.clear();
                    }
                    salesOutList = DataSupport.where("billtype =?","2").find(SalesOut.class);

                    adapter = new SaleOutAdapter(SaleSearchView.this, R.layout.saleout_item, salesOutList);
                    listView.setAdapter(adapter);
                }
                break;

            default:
                break;
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
                    PopBean popuMenua = new PopBean(R.drawable.poppu_wrie, "客户修改");
                    popuMenuDatas.add(popuMenua);
                    PopBean popuMenub = new PopBean(R.drawable.poppu_wrie, "客户新增");
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

            Object[] obj = search(cHomeSearch.getText().toString());
            updateLayout(obj);

        }
    };
}
