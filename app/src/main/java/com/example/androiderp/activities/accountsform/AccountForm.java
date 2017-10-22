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

import com.example.androiderp.bean.Account;
import com.example.androiderp.R;

import org.litepal.crud.DataSupport;

/**
 * Created by lingtan on 2017/5/15.
 */

public class AccountForm extends AppCompatActivity implements View.OnClickListener {
    private InputMethodManager manager;
    private EditText name;
    private TextView save, tile, back;
    private Account account;
    private String id,cType;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        name =(EditText)findViewById(R.id.basiclayout_name);
        final Intent intent=getIntent();
        id=intent.getStringExtra("customid");
        cType=intent.getStringExtra("action");
        save =(TextView)findViewById(R.id.custom_toobar_right);
        tile =(TextView)findViewById(R.id.custom_toobar_midd);
        back =(TextView)findViewById(R.id.custom_toobar_left);
        save.setCompoundDrawables(null,null,null,null);
        tile.setCompoundDrawables(null,null,null,null);
        save.setText("保存");
        save.setOnClickListener(this);
        back.setOnClickListener(this);
       formInit();

    }
private  void formInit()
{if(id!=null) {
    account = DataSupport.find(Account.class, Long.parseLong(id));
    name.setText(account.getName());
}
    if(cType.equals("edit"))
    {
        tile.setText("账户修改");
    }else {
        tile.setText("账户新增");
    }

}
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.custom_toobar_right:
                if (TextUtils.isEmpty(name.getText().toString())) {
                    name.setError("需要输入账户");
                } else {
                    if (cType.equals("edit")) {
                  Account account = new Account();
                        account.setName(name.getText().toString());
                        account.update(Long.parseLong(id));
                        Intent intent = new Intent();
                        setResult(RESULT_OK,intent);
                        AccountForm.this.finish();
                    } else {
                   Account     account = new Account();
                        account.setName(name.getText().toString());
                        account.save();
                        Intent intent = new Intent();
                        setResult(RESULT_OK,intent);
                        AccountForm.this.finish();
                    }
                }
                break;
            case R.id.custom_toobar_left:
                if(cType.equals("edit"))
                {
                    Intent intent = new Intent();
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
