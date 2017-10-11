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

import com.example.androiderp.bean.Accounts;
import com.example.androiderp.R;
import com.example.androiderp.bean.DataStructure;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by lingtan on 2017/5/15.
 */

public class AccountsForm extends AppCompatActivity implements View.OnClickListener {
    private InputMethodManager manager;
    private EditText userName;
    private TextView toobarSave, toobarTile, toobarBack;
    private Accounts brandName;
    private String customid,edit;
    private List<Accounts> accountsList;
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
    brandName = DataSupport.find(Accounts.class, Long.parseLong(customid));
    userName.setText(brandName.getName());
}
    if(edit.equals("edit"))
    {
        toobarTile.setText("账目类型修改");
    }else {
        toobarTile.setText("账目类型新增");
    }

}
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.custom_toobar_right:

                accountsList = DataStructure.where("name = ?",userName.getText().toString()).find(Accounts.class);
                if (TextUtils.isEmpty(userName.getText().toString())) {
                    userName.setError("需要输入账目类型");
                }else if (accountsList.size()>0)
                {
                    Toast.makeText(AccountsForm.this,"账目类型已经存在",Toast.LENGTH_SHORT).show();
                } else {
                    if (edit.equals("edit")) {
                      Accounts  accounts = new Accounts();
                        accounts.setName(userName.getText().toString());
                        accounts.update(Long.parseLong(customid));
                        Intent intent = new Intent();
                        intent.putExtra("returnName",userName.getText().toString());
                        setResult(RESULT_OK,intent);
                        isSave=true;
                        AccountsForm.this.finish();
                    } else {
                      Accounts  accounts = new Accounts();
                        accounts.setName(userName.getText().toString());
                        accounts.save();
                        Intent intent = new Intent();
                        setResult(RESULT_OK,intent);
                        AccountsForm.this.finish();
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
