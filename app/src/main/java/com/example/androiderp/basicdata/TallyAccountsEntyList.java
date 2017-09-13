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

import com.example.androiderp.CustomDataClass.Tally;
import com.example.androiderp.R;
import com.example.androiderp.adaper.TallyAdapter;
import com.example.androiderp.custom.CustomSearch;
import com.example.androiderp.custom.CustomSearchBase;
import com.example.androiderp.form.TallyForm;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class TallyAccountsEntyList extends CustomSearchBase implements View.OnClickListener {
    private TallyAdapter adapter;
    private ListView listView;
    private DisplayMetrics dm;
    private List<Tally> appropriationSearch= new ArrayList<Tally>();
    private List<Tally> tallyList;
    private TextView toobarBack,toobarAdd,toobarTile;
    private CustomSearch customSearch;
    private Intent intentEdit;
    private Intent intentAdd;
    private String tallybalanceAccount,accounts;

    @Override
    public void iniView(){
        setContentView(R.layout.tallyaccounts_layout);
        toobarBack=(TextView)findViewById(R.id.custom_toobar_left) ;
        toobarTile=(TextView)findViewById(R.id.custom_toobar_midd);
        toobarAdd=(TextView)findViewById(R.id.custom_toobar_right);
        toobarAdd.setCompoundDrawables(null,null,null,null);
        toobarBack.setOnClickListener(this);
        Intent intent=getIntent();
        accounts=intent.getStringExtra("accounts_name");
        tallybalanceAccount =intent.getStringExtra("balanceAccount_name");
        toobarAdd.setOnClickListener(this);
        toobarTile.setOnClickListener(this);
        customSearch = (CustomSearch) findViewById(R.id.search);
        tallyList =DataSupport.where("balanceAccount = ? and accounts=?", tallybalanceAccount,accounts).find(Tally.class);
        intentEdit= new Intent(TallyAccountsEntyList.this, AppropriationEntyList.class);
        toobarTile.setText("交易流水");
        toobarBack.setText("返回");

        //构造函数第一参数是类的对象，第二个是布局文件，第三个是数据源
        listView = (ListView) findViewById(R.id.list);
        listView.setTextFilterEnabled(true);
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {



            }
        });
        if(tallyList.size()!=0) {

                 adapter = new TallyAdapter(TallyAccountsEntyList.this, R.layout.tally_item, tallyList);
                 listView.setAdapter(adapter);
            
        }

        customSearch.addTextChangedListener(textWatcher);

    }

    //筛选条件
    public List<Tally> search(String name) {

        tallyList =DataSupport.where("accounts like ?", "%"+name+"%").find(Tally.class);

        return  tallyList;

    }
//adapter刷新,重写Filter方式会出现BUG.
    public void updateLayout(List<Tally> tallyList) {
        if(tallyList!=null) {
            adapter = new TallyAdapter(TallyAccountsEntyList.this, R.layout.tally_item, tallyList);
            listView.setAdapter(adapter);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK)
                {
                    tallyList =DataSupport.where("balanceAccount = ? and accounts=?", tallybalanceAccount,accounts).find(Tally.class);

                    adapter = new TallyAdapter(TallyAccountsEntyList.this, R.layout.tally_item, tallyList);
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
                TallyAccountsEntyList.this.finish();
                break;

            case R.id.custom_toobar_right:
                intentAdd = new Intent(TallyAccountsEntyList.this, TallyForm.class);
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


            updateLayout(search(customSearch.getText().toString()));

        }
    };
}
