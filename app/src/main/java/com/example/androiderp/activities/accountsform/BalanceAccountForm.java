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
    private EditText name;
    private TextView save, tile, back;
    private BalanceAccount balanceAccount;
    private String id,cType;
    private List<BalanceAccount> balanceAccountList;
    private boolean isSave=false;
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
    balanceAccount = DataSupport.find(BalanceAccount.class, Long.parseLong(id));
    name.setText(balanceAccount.getName());
}
    if(cType.equals("edit"))
    {
        tile.setText("结算账户修改");
    }else {
        tile.setText("结算账户新增");
    }

}
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.custom_toobar_right:

                balanceAccountList = DataStructure.where("name = ?", name.getText().toString()).find(BalanceAccount.class);
                if (TextUtils.isEmpty(name.getText().toString())) {
                    name.setError("需要输入账目类型");
                }else if (balanceAccountList.size()>0)
                {
                    Toast.makeText(BalanceAccountForm.this,"账目类型已经存在",Toast.LENGTH_SHORT).show();
                } else {
                    if (cType.equals("edit")) {
                      BalanceAccount  balanceAccount = new BalanceAccount();
                        balanceAccount.setName(name.getText().toString());
                        balanceAccount.update(Long.parseLong(id));
                        Intent intent = new Intent();
                        intent.putExtra("returnName", name.getText().toString());
                        setResult(RESULT_OK,intent);
                        isSave=true;
                        BalanceAccountForm.this.finish();
                    } else {
                        BalanceAccount  balanceAccount = new BalanceAccount();
                        balanceAccount.setName(name.getText().toString());
                        balanceAccount.save();
                        Intent intent = new Intent();
                        setResult(RESULT_OK,intent);
                        BalanceAccountForm.this.finish();
                    }
                }
                break;
            case R.id.custom_toobar_left:
                if(cType.equals("edit"))
                {
                    Intent intent = new Intent();
                    if(isSave) {
                        intent.putExtra("returnName", name.getText().toString());
                    }else {
                        intent.putExtra("returnName", balanceAccount.getName());
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
