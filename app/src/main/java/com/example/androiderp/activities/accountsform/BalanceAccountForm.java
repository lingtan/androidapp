package com.example.androiderp.activities.accountsform;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiderp.bean.BalanceAccount;
import com.example.androiderp.R;
import com.example.androiderp.bean.DataStructure;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by lingtan on 2017/5/15.
 */

public class BalanceAccountForm extends AppCompatActivity implements View.OnClickListener {
    private InputMethodManager manager;
    private EditText userName;
    private TextView toobarSave, toobarTile, toobarBack;
    private BalanceAccount brandName;
    private String customid,edit;
    private List<BalanceAccount> balanceAccountList;
    private boolean isSave=false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customcategory);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        userName =(EditText)findViewById(R.id.customcategory_name);
        final Intent intent=getIntent();
        customid=intent.getStringExtra("customid");
        edit=intent.getStringExtra("action");
        toobarSave =(TextView)findViewById(R.id.custom_toobar_right);
        toobarTile =(TextView)findViewById(R.id.custom_toobar_midd);
        toobarBack =(TextView)findViewById(R.id.custom_toobar_left);
        toobarSave.setCompoundDrawables(null,null,null,null);
        toobarTile.setCompoundDrawables(null,null,null,null);
        toobarSave.setText("保存");
        toobarSave.setOnClickListener(this);
        toobarBack.setOnClickListener(this);
       formInit();

    }
private  void formInit()
{if(customid!=null) {
    brandName = DataSupport.find(BalanceAccount.class, Long.parseLong(customid));
    userName.setText(brandName.getName());
}
    if(edit.equals("edit"))
    {
        toobarTile.setText("结算账户修改");
    }else {
        toobarTile.setText("结算账户新增");
    }

}
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.custom_toobar_right:

                balanceAccountList = DataStructure.where("name = ?",userName.getText().toString()).find(BalanceAccount.class);
                if (TextUtils.isEmpty(userName.getText().toString())) {
                    userName.setError("需要输入账目类型");
                }else if (balanceAccountList.size()>0)
                {
                    Toast.makeText(BalanceAccountForm.this,"账目类型已经存在",Toast.LENGTH_SHORT).show();
                } else {
                    if (edit.equals("edit")) {
                      BalanceAccount  balanceAccount = new BalanceAccount();
                        balanceAccount.setName(userName.getText().toString());
                        balanceAccount.update(Long.parseLong(customid));
                        Intent intent = new Intent();
                        intent.putExtra("returnName",userName.getText().toString());
                        setResult(RESULT_OK,intent);
                        isSave=true;
                        BalanceAccountForm.this.finish();
                    } else {
                        BalanceAccount  balanceAccount = new BalanceAccount();
                        balanceAccount.setName(userName.getText().toString());
                        balanceAccount.save();
                        Intent intent = new Intent();
                        setResult(RESULT_OK,intent);
                        BalanceAccountForm.this.finish();
                    }
                }
                break;
            case R.id.custom_toobar_left:
                if(edit.equals("edit"))
                {
                    Intent intent = new Intent();
                    if(isSave) {
                        intent.putExtra("returnName", userName.getText().toString());
                    }else {
                        intent.putExtra("returnName",brandName.getName());
                    }
                    setResult(RESULT_OK,intent);
                    finish();
                }else {
                    finish();
                }



        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(getCurrentFocus()!=null && getCurrentFocus().getWindowToken()!=null){
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

}
