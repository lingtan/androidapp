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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androiderp.CustomDataClass.BalanceAccount;
import com.example.androiderp.CustomDataClass.Tally;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonAccountAdapter;
import com.example.androiderp.adaper.CommonAdapterData;
import com.example.androiderp.custom.CustomSearch;
import com.example.androiderp.custom.CustomSearchBase;
import com.example.androiderp.form.TallyForm;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class TallyListView extends CustomSearchBase implements View.OnClickListener {
    private CommonAccountAdapter adapter;
    private ListView listView;
    private DisplayMetrics dm;
    private List<CommonAdapterData> listdatas = new ArrayList<CommonAdapterData>();
    private List<BalanceAccount> balanceAccountList;
    private TextView toobarBack,toobarAdd,toobarTile;
    private CustomSearch customSearch;
    private double amount;
    private Intent intentEdit;
    private Intent intentAdd;

    @Override
    public void iniView(){
        setContentView(R.layout.custom_layout);
        toobarBack=(TextView)findViewById(R.id.custom_toobar_left) ;
        toobarTile=(TextView)findViewById(R.id.custom_toobar_midd);
        toobarAdd=(TextView)findViewById(R.id.custom_toobar_right);
        toobarBack.setOnClickListener(this);
        toobarAdd.setOnClickListener(this);
        toobarTile.setOnClickListener(this);
        customSearch = (CustomSearch) findViewById(R.id.search);
        balanceAccountList =DataSupport.findAll(BalanceAccount.class);
        intentEdit= new Intent(TallyListView.this, TallyAccountsListView.class);
        toobarTile.setText("账户流水");
        toobarBack.setText("返回");

        //构造函数第一参数是类的对象，第二个是布局文件，第三个是数据源
        listView = (ListView) findViewById(R.id.list);
        listView.setTextFilterEnabled(true);
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        for(BalanceAccount balanceAccount: balanceAccountList)

        {
            amount=DataSupport.where("balanceAccount= ?",balanceAccount.getName()).sum(Tally.class, "amount", double.class);
            CommonAdapterData commonData=new CommonAdapterData();
            commonData.setName(balanceAccount.getName());
            commonData.setId(balanceAccount.getId());
            commonData.setSaleamount(amount);
            commonData.setImage(R.drawable.accountimage);
            commonData.setSelectImage(R.drawable.seclec_arrow);
            listdatas.add(commonData);



        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                            intentEdit.putExtra("balanceAccount_name", listdatas.get(position).getName());


                startActivityForResult(intentEdit,1);


            }
        });
        if(balanceAccountList.size()!=0) {

                 adapter = new CommonAccountAdapter(TallyListView.this, R.layout.account_item, listdatas);
                 listView.setAdapter(adapter);
            
        }

        customSearch.addTextChangedListener(textWatcher);

    }

    //筛选条件
    public Object[] search(String name) {

        if(listdatas.size()!=0)
        {
            listdatas.clear();
        }

        balanceAccountList =DataSupport.where("name like ?","%" + name + "%").find(BalanceAccount.class);
        for(BalanceAccount balanceAccount: balanceAccountList)

        {
            amount=DataSupport.where("balanceAccount= ?",balanceAccount.getName()).sum(Tally.class, "amount", double.class);
            CommonAdapterData commonData=new CommonAdapterData();
            commonData.setName(balanceAccount.getName());
            commonData.setId(balanceAccount.getId());
            commonData.setSaleamount(amount);
            commonData.setImage(R.drawable.accountimage);
            commonData.setSelectImage(R.drawable.seclec_arrow);
            listdatas.add(commonData);



        }
        return listdatas.toArray();
    }


//adapter刷新,重写Filter方式会出现BUG.
    public void updateLayout(Object[] obj) {
        if(listdatas !=null) {
            adapter = new CommonAccountAdapter(TallyListView.this, R.layout.account_item, listdatas);
            listView.setAdapter(adapter);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK)
                {
                    if(listdatas.size()!=0) {
                        listdatas.clear();
                    }
                    balanceAccountList =DataSupport.findAll(BalanceAccount.class);
                    for(BalanceAccount balanceAccount: balanceAccountList)

                    {
                        amount=DataSupport.where("balanceAccount= ?",balanceAccount.getName()).sum(Tally.class, "amount", double.class);
                        CommonAdapterData commonData=new CommonAdapterData();
                        commonData.setName(balanceAccount.getName());
                        commonData.setId(balanceAccount.getId());
                        commonData.setSaleamount(amount);
                        commonData.setImage(R.drawable.accountimage);
                        commonData.setSelectImage(R.drawable.seclec_arrow);
                        listdatas.add(commonData);



                    }
                    adapter = new CommonAccountAdapter(TallyListView.this, R.layout.account_item, listdatas);
                    listView.setAdapter(adapter);
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
                TallyListView.this.finish();
                break;

            case R.id.custom_toobar_right:
                intentAdd = new Intent(TallyListView.this, TallyForm.class);
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

            Object[] obj = search(customSearch.getText().toString());
            updateLayout(obj);

        }
    };
}
