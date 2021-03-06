/*
实现功能：记录调拨流情况
使用控件：ListView、CustomSearch、TextView
使用类型：Adapter、Appropriation

 */


package com.example.androiderp.activities.accountsview;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androiderp.bean.Tally;
import com.example.androiderp.R;
import com.example.androiderp.activities.warehouseview.RequisitionEntyView;
import com.example.androiderp.adaper.TallyAdapter;
import com.example.androiderp.ui.CSearch;
import com.example.androiderp.ui.CSearchBase;
import com.example.androiderp.activities.accountsform.TallyForm;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class TallyAccountsEntyView extends CSearchBase implements View.OnClickListener {
    private TallyAdapter adapter;
    private ListView listView;
    private DisplayMetrics dm;
    private List<Tally> appropriationSearch= new ArrayList<Tally>();
    private List<Tally> tallyList;
    private TextView back, add, tile;
    private CSearch search;
    private Intent intentEdit;
    private Intent iAdd;
    private String tallybalanceAccount,accounts;

    @Override
    public void iniView(){
        setContentView(R.layout.tallyaccounts_layout);
        back =(TextView)findViewById(R.id.custom_toobar_left) ;
        tile =(TextView)findViewById(R.id.custom_toobar_midd);
        add =(TextView)findViewById(R.id.custom_toobar_right);
        add.setCompoundDrawables(null,null,null,null);
        back.setOnClickListener(this);
        Intent intent=getIntent();
        accounts=intent.getStringExtra("accounts_name");
        tallybalanceAccount =intent.getStringExtra("balanceAccount_name");
        add.setOnClickListener(this);
        tile.setOnClickListener(this);
        search = (CSearch) findViewById(R.id.search);
        tallyList =DataSupport.where("balanceAccount = ? and accounts=?", tallybalanceAccount,accounts).find(Tally.class);
        intentEdit= new Intent(TallyAccountsEntyView.this, RequisitionEntyView.class);
        tile.setText("交易流水");
        back.setText("返回");

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

                 adapter = new TallyAdapter(TallyAccountsEntyView.this, R.layout.tally_item, tallyList);
                 listView.setAdapter(adapter);
            
        }

        search.addTextChangedListener(textWatcher);

    }

    //筛选条件
    public List<Tally> search(String name) {

        tallyList =DataSupport.where("accounts like ?", "%"+name+"%").find(Tally.class);

        return  tallyList;

    }
//adapter刷新,重写Filter方式会出现BUG.
    public void updateLayout(List<Tally> tallyList) {
        if(tallyList!=null) {
            adapter = new TallyAdapter(TallyAccountsEntyView.this, R.layout.tally_item, tallyList);
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

                    adapter = new TallyAdapter(TallyAccountsEntyView.this, R.layout.tally_item, tallyList);
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
                TallyAccountsEntyView.this.finish();
                break;

            case R.id.custom_toobar_right:
                iAdd = new Intent(TallyAccountsEntyView.this, TallyForm.class);
                startActivityForResult(iAdd,1);
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


            updateLayout(search(search.getText().toString()));

        }
    };
}
