package com.example.androiderp.activities.accountsview;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androiderp.bean.BalanceAccount;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonAdapterData;
import com.example.androiderp.adaper.CommonListViewAdapter;
import com.example.androiderp.bean.DataStructure;
import com.example.androiderp.ui.CSearch;
import com.example.androiderp.ui.CSearchBase;
import com.example.androiderp.activities.accountsform.BalanceAccountForm;
import com.example.androiderp.listview.Menu;
import com.example.androiderp.listview.MenuItem;
import com.example.androiderp.listview.SlideAndDragListView;
import com.example.androiderp.listview.Utils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class BalanceAccountView extends CSearchBase implements View.OnClickListener,
        AdapterView.OnItemClickListener,
        SlideAndDragListView.OnMenuItemClickListener, SlideAndDragListView.OnItemDeleteListener {
    private List<CommonAdapterData> commonAdapterDataList = new ArrayList<CommonAdapterData>();
    private CommonListViewAdapter adapter;
    private SlideAndDragListView<CommonAdapterData>  listView;
    private DisplayMetrics dm;
    private List<CommonAdapterData> commonAdapterDataSearch = new ArrayList<CommonAdapterData>();
    private List<BalanceAccount> balanceAccountList;
    private TextView back, add, tile;
    private CSearch search;
    private ImageView lastCheckedOption;
    private int iPositon =-1;
    private String iName;
    private Menu menu;
    private String returnName;
    private String searchVale;
    @Override
    public void iniView(){
        setContentView(R.layout.customlistview_category_layout);
        initMenu();
        initUiAndListener();

        back =(TextView)findViewById(R.id.custom_toobar_left) ;
        tile =(TextView)findViewById(R.id.custom_toobar_midd);
        add =(TextView)findViewById(R.id.custom_toobar_right);
        back.setOnClickListener(this);
        add.setOnClickListener(this);
        tile.setOnClickListener(this);
        search = (CSearch) findViewById(R.id.search);
        Intent intent=getIntent();
        iName =intent.getStringExtra("index");
        balanceAccountList = DataSupport.findAll(BalanceAccount.class);
        tile.setCompoundDrawables(null,null,null,null);
        tile.setText("选择结算账户");
        for(BalanceAccount balanceAccount: balanceAccountList)

        {   if(balanceAccount.getName().equals(iName))
        {
            iPositon = balanceAccountList.indexOf(balanceAccount);
        }
            CommonAdapterData commonData=new CommonAdapterData();
            commonData.setName(balanceAccount.getName());
            commonData.setUnitId(balanceAccount.getId());
            commonData.setImage(R.drawable.seclec_arrow);
            commonAdapterDataList.add(commonData);



        }

        //构造函数第一参数是类的对象，第二个是布局文件，第三个是数据源
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        if(commonAdapterDataList.size()!=0) {
                 adapter = new CommonListViewAdapter(BalanceAccountView.this, R.layout.custom_item, commonAdapterDataList);
                 adapter.setSeclection(iPositon);
                 listView.setAdapter(adapter);


        }

        search.addTextChangedListener(textWatcher);




    }
    public void initMenu() {
        menu = new Menu(true);
        menu.addItem(new MenuItem.Builder().setWidth((int) getResources().getDimension(R.dimen.slv_item_bg_btn_width))
                .setBackground(Utils.getDrawable(this, R.drawable.btn_right0))
                .setText("编辑")
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setTextColor(Color.BLACK)
                .setTextSize(14)
                .build());
        menu.addItem(new MenuItem.Builder().setWidth((int) getResources().getDimension(R.dimen.slv_item_bg_btn_width_img))
                .setBackground(Utils.getDrawable(this, R.drawable.btn_right1))
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setText("删除")
                .setTextSize(14)
                .build());
    }

    public void initUiAndListener() {
        listView = (SlideAndDragListView) findViewById(R.id.custom_listview_category);
        listView.setMenu(menu);
        listView.setOnItemClickListener(this);
        listView.setOnMenuItemClickListener(this);
        listView.setOnItemDeleteListener(this);
    }
    @Override
    public int onMenuItemClick(View v, final int itemPosition, int buttonPosition, int direction) {
        switch (direction) {
            case MenuItem.DIRECTION_RIGHT:
                switch (buttonPosition) {
                    case 0:
                        Intent intent=new Intent(BalanceAccountView.this,BalanceAccountForm.class);

                            intent.putExtra("action", "edit");
                            intent.putExtra("customid", String.valueOf(commonAdapterDataList.get(itemPosition).getUnitId()));
                        startActivityForResult(intent,1);

                        return Menu.ITEM_NOTHING;
                    case 1:
                        AlertDialog.Builder dialog=new AlertDialog.Builder(BalanceAccountView.this);
                        dialog.setTitle("提示");
                        dialog.setMessage("您确认要删除结算账户？");
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                    DataStructure.deleteAll(BalanceAccount.class, "name = ?", commonAdapterDataList.get(itemPosition - listView.getHeaderViewsCount()).getName().toString());
                                    commonAdapterDataList.remove(itemPosition - listView.getHeaderViewsCount());
                                    if (search.getText().toString().isEmpty()) {
                                        if (iPositon == itemPosition) {
                                            iPositon = -1;
                                        }

                                        adapter.setSeclection(iPositon);
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        search.setText("");

                                }



                            }
                        });
                        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        dialog.show();
                    return Menu.ITEM_NOTHING;
                }
        }
        return Menu.ITEM_NOTHING;
    }
    @Override
    public void onItemDelete(View view, int position) {


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        Intent intent=getIntent();
            intent.putExtra("data_return", String.valueOf(commonAdapterDataList.get(position).getName()));
            iName = commonAdapterDataList.get(position).getName();

        setResult(RESULT_OK,intent);

        if(lastCheckedOption != null){
            lastCheckedOption.setVisibility(View.INVISIBLE);
        }
        lastCheckedOption = (ImageView)view.findViewById(R.id.custom_item_layout_one_image);
        lastCheckedOption.setVisibility(View.VISIBLE);
        iPositon =position;
        this.finish();
    }
    //筛选条件
    public void  searchItem(String name) {
        if(commonAdapterDataList.size()>0) {
            commonAdapterDataList.clear();
        }
        balanceAccountList =DataStructure.where("name like ?","%" + name + "%").find(BalanceAccount.class);
        for(BalanceAccount balanceAccount: balanceAccountList)

        {
            CommonAdapterData commonData=new CommonAdapterData();
            commonData.setName(balanceAccount.getName());
            commonData.setUnitId(balanceAccount.getId());
            commonData.setImage(R.drawable.seclec_arrow);
            commonAdapterDataList.add(commonData);

        }

        if(commonAdapterDataList !=null) {
            int index=-1;
            if(!name.isEmpty())
            {
                for(int i = 0; i< commonAdapterDataList.size(); i++)
                {
                    if(commonAdapterDataList.get(i).getName().equals(iName))
                    {
                        index=i;
                    }
                }
            }else
            {
                index= iPositon;
            }

            adapter.setSeclection(index);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
        }
    }



    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.custom_toobar_left:
                Intent intent=getIntent();
                if(iPositon !=-1) {
                    intent.putExtra("data_return", commonAdapterDataList.get(iPositon).getName());
                }
                setResult(RESULT_OK,intent);
                BalanceAccountView.this.finish();
                break;

            case R.id.custom_toobar_midd:


                break;

            case R.id.custom_toobar_right:
                Intent cate = new Intent(BalanceAccountView.this, BalanceAccountForm.class);
                cate.putExtra("action","add");
                startActivityForResult(cate,2);
                break;


        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK)
                {   returnName=data.getStringExtra("returnName");
                    iName =returnName;
                    if(commonAdapterDataList.size()>0) {
                        commonAdapterDataList.clear();
                    }
                    if(search.getText().toString().isEmpty()) {

                        search.requestFocusFromTouch();
                        search.setText("");
                    }else {
                        int i = returnName.indexOf(searchVale);
                        if(i!=-1) {
                            search.requestFocusFromTouch();
                            search.setText(searchVale);
                        }else {
                            search.requestFocusFromTouch();
                            search.setText(returnName);
                        }
                    }

                }
                break;
            case 2:
                if(resultCode==RESULT_OK)
                {
                    if(commonAdapterDataList.size()!=0) {
                        commonAdapterDataList.clear();
                    }
                    balanceAccountList = DataSupport.findAll(BalanceAccount.class);
                    for(BalanceAccount balanceAccount: balanceAccountList)

                    {
                        CommonAdapterData commonData=new CommonAdapterData();
                        commonData.setName(balanceAccount.getName());
                        commonData.setUnitId(balanceAccount.getId());
                        commonData.setImage(R.drawable.seclec_arrow);
                        commonAdapterDataList.add(commonData);



                    }
                    adapter = new CommonListViewAdapter(BalanceAccountView.this, R.layout.custom_item, commonAdapterDataList);
                    adapter.setSeclection(iPositon);
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

            searchItem(search.getText().toString());
            searchVale= search.getText().toString();

        }
    };
}
