/*
实现功能：记录调拨流情况
使用控件：ListView、CustomSearch、TextView
使用类型：Adapter、Appropriation

 */


package com.example.androiderp.basicdata;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import com.example.androiderp.CustomDataClass.Accounts;
import com.example.androiderp.CustomDataClass.BalanceAccount;
import com.example.androiderp.CustomDataClass.GroupItem;
import com.example.androiderp.CustomDataClass.GroupMark;
import com.example.androiderp.CustomDataClass.Tally;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonAdapterData;
import com.example.androiderp.adaper.ExpandableListViewAdapter;
import com.example.androiderp.custom.CustomSearch;
import com.example.androiderp.custom.CustomSearchBase;
import com.example.androiderp.form.TallyForm;
import org.litepal.crud.DataSupport;
import java.util.ArrayList;
import java.util.List;

public class TallyExpandableListView extends CustomSearchBase implements View.OnClickListener {
    private ExpandableListViewAdapter adapter;
    private ExpandableListView listView;
    private DisplayMetrics dm;
    private List<GroupItem>groupItemList=new ArrayList<GroupItem>();
    private List<GroupMark>groupMarkList=new ArrayList<GroupMark>();
    private List<BalanceAccount> balanceAccountList;//组分类
    private List<Accounts> accountsList;//子项目
    private List<CommonAdapterData> accountsList1;//子项目
    private List<List<CommonAdapterData>> item_list;
    private List<CommonAdapterData> listdatas = new ArrayList<CommonAdapterData>();
    private TextView toobarBack,toobarAdd,toobarTile;
    private CustomSearch customSearch;
    private double amount;
    private Intent intentEdit;
    private Intent intentAdd;

    @Override
    public void iniView(){
        setContentView(R.layout.tallyexpandablelayout);
        toobarBack=(TextView)findViewById(R.id.custom_toobar_left) ;
        toobarTile=(TextView)findViewById(R.id.custom_toobar_midd);
        toobarAdd=(TextView)findViewById(R.id.custom_toobar_right);
        toobarBack.setOnClickListener(this);
        toobarAdd.setOnClickListener(this);
        toobarTile.setOnClickListener(this);
        Intent intent=getIntent();
        customSearch = (CustomSearch) findViewById(R.id.search);
        intentEdit= new Intent(TallyExpandableListView.this, TallyAccountsEntyList.class);
        toobarTile.setText("账目流水");
        toobarBack.setText("返回");
        //构造函数第一参数是类的对象，第二个是布局文件，第三个是数据源
        listView = (ExpandableListView) findViewById(R.id.expendlist);
        listView.setGroupIndicator(null);
        listView.setTextFilterEnabled(true);
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        accountsList =DataSupport.findAll(Accounts.class);
        balanceAccountList=DataSupport.findAll(BalanceAccount.class);
        for (BalanceAccount balanceAccount:balanceAccountList) {
            GroupMark groupMark=new GroupMark();
            groupMark.setMark("false");
            groupMark.setIndex(-1);
            groupMarkList.add(groupMark);
            GroupItem groupItem=new GroupItem();
            amount = DataSupport.where("balanceAccount = ?", balanceAccount.getName()).sum(Tally.class, "amount", double.class);
            groupItem.setName(balanceAccount.getName());
            groupItem.setAmount(amount);
            groupItemList.add(groupItem);
            for (Accounts accounts : accountsList)

            {
                amount = DataSupport.where("accounts= ? and balanceAccount = ?", accounts.getName(), balanceAccount.getName()).sum(Tally.class, "amount", double.class);

               if(amount!=0) {
                   CommonAdapterData commonData = new CommonAdapterData();
                   commonData.setName(accounts.getName());
                   commonData.setNumber(balanceAccount.getName());
                   commonData.setId(accounts.getId());
                   commonData.setSaleamount(amount);
                   commonData.setImage(R.drawable.accountsimage);
                   commonData.setSelectImage(R.drawable.seclec_arrow);
                   listdatas.add(commonData);
               }


            }
        }

        item_list = new ArrayList<List<CommonAdapterData>>();
        for (int i = 0; i < balanceAccountList.size(); i++) {
            accountsList1 = new ArrayList<CommonAdapterData>();
            for (int j = 0; j < listdatas.size(); j++) {
                if (listdatas.get(j).getNumber().equals(balanceAccountList.get(i).getName())) {
                    accountsList1.add(listdatas.get(j));
                }

            }

            item_list.add(accountsList1);
        }

        if(accountsList.size()!=0 && balanceAccountList.size()!=0) {

                 adapter = new ExpandableListViewAdapter(TallyExpandableListView.this,groupItemList, item_list);
                 listView.setAdapter(adapter);
            
        }

        // 监听组点击
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

        if(groupMarkList.get(groupPosition).getMark().equals("false"))
        {
            groupMarkList.get(groupPosition).setMark("ture");
            groupMarkList.get(groupPosition).setIndex(groupPosition);
        }else {
            groupMarkList.get(groupPosition).setMark("false");
            groupMarkList.get(groupPosition).setIndex(-1);
        }

                return false;
            }
        });
        // 监听每个分组里子控件的点击事件
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {



                intentEdit.putExtra("balanceAccount_name", balanceAccountList.get(groupPosition).getName());
                intentEdit.putExtra("accounts_name",item_list.get(groupPosition).get(childPosition).getName());
                startActivityForResult(intentEdit,1);
                return false;
            }
        });

        customSearch.addTextChangedListener(textWatcher);

    }

    //筛选条件
    public void search(String name) {

        listdatas.clear();
        item_list.clear();

        for (BalanceAccount balanceAccount:balanceAccountList) {

            for (Accounts accounts : accountsList)

            {
                amount = DataSupport.where("accounts= ? and balanceAccount = ?", accounts.getName(), balanceAccount.getName()).sum(Tally.class, "amount", double.class);
                int index = accounts.getName().indexOf(name);
                if(amount!=0) {
                    if(index!=-1) {
                        CommonAdapterData commonData = new CommonAdapterData();
                        commonData.setName(accounts.getName());
                        commonData.setNumber(balanceAccount.getName());
                        commonData.setId(accounts.getId());
                        commonData.setSaleamount(amount);
                        commonData.setImage(R.drawable.accountsimage);
                        commonData.setSelectImage(R.drawable.seclec_arrow);
                        listdatas.add(commonData);
                    }
                }


            }
        }

        item_list = new ArrayList<List<CommonAdapterData>>();
        for (int i = 0; i < balanceAccountList.size(); i++) {
            accountsList1 = new ArrayList<CommonAdapterData>();
            for (int j = 0; j < listdatas.size(); j++) {
                if (listdatas.get(j).getNumber().equals(balanceAccountList.get(i).getName())) {
                    accountsList1.add(listdatas.get(j));
                }

            }

            item_list.add(accountsList1);
        }
    }


    //adapter刷新,重写Filter方式会出现BUG.
    public void updateLayout() {
        if(accountsList.size()!=0 && balanceAccountList.size()!=0) {

            adapter = new ExpandableListViewAdapter(TallyExpandableListView.this,groupItemList, item_list);
            listView.setAdapter(adapter);
            for(GroupMark groupMark:groupMarkList)
            {
                if(groupMark.getMark().equals("ture"))
                {
                    listView.expandGroup(groupMark.getIndex());
                }

            }



        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK)
                {
                    listdatas.clear();
                    item_list.clear();
                    groupItemList.clear();
                    accountsList =DataSupport.findAll(Accounts.class);
                    balanceAccountList=DataSupport.findAll(BalanceAccount.class);
                    for (BalanceAccount balanceAccount:balanceAccountList) {
                        GroupItem groupItem=new GroupItem();
                        amount = DataSupport.where("balanceAccount = ?", balanceAccount.getName()).sum(Tally.class, "amount", double.class);
                        groupItem.setName(balanceAccount.getName());
                        groupItem.setAmount(amount);
                        groupItemList.add(groupItem);

                        for (Accounts accounts : accountsList)

                        {
                            amount = DataSupport.where("accounts= ? and balanceAccount = ?", accounts.getName(), balanceAccount.getName()).sum(Tally.class, "amount", double.class);
                            if(amount!=0) {

                                CommonAdapterData commonData = new CommonAdapterData();
                                commonData.setName(accounts.getName());
                                commonData.setNumber(balanceAccount.getName());
                                commonData.setId(accounts.getId());
                                commonData.setSaleamount(amount);
                                commonData.setImage(R.drawable.accountsimage);
                                commonData.setSelectImage(R.drawable.seclec_arrow);
                                listdatas.add(commonData);
                            }


                        }
                    }

                    for (int i = 0; i < balanceAccountList.size(); i++) {
                        accountsList1 = new ArrayList<CommonAdapterData>();
                        for (int j = 0; j < listdatas.size(); j++) {
                            if (listdatas.get(j).getNumber().equals(balanceAccountList.get(i).getName())) {
                                accountsList1.add(listdatas.get(j));
                            }

                        }

                        item_list.add(accountsList1);
                    }
                    if(accountsList.size()!=0 && balanceAccountList.size()!=0) {

                        adapter = new ExpandableListViewAdapter(TallyExpandableListView.this,groupItemList, item_list);
                        listView.setAdapter(adapter);
                        //默认全部展开

                        for(GroupMark groupMark:groupMarkList)
                        {
                            if(groupMark.getMark().equals("ture"))
                            {
                                listView.expandGroup(groupMark.getIndex());
                            }

                        }




                    }
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
                TallyExpandableListView.this.finish();
                break;

            case R.id.custom_toobar_right:
                intentAdd = new Intent(TallyExpandableListView.this, TallyForm.class);
                startActivityForResult(intentAdd,1);
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

           search(customSearch.getText().toString());
            updateLayout();


        }
    };
}
