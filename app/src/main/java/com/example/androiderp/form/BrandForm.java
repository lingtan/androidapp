package com.example.androiderp.form;

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

import com.example.androiderp.CustomDataClass.Brand;
import com.example.androiderp.R;

import org.litepal.crud.DataSupport;

/**
 * Created by lingtan on 2017/5/15.
 */

public class BrandForm extends AppCompatActivity implements View.OnClickListener {
    private InputMethodManager manager;
    private EditText username;
    private TextView usersave,toobar_tile,toobar_back;
    private Brand custom;
    private Brand customlist;
    private String customid,edit;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customcategory);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        username=(EditText)findViewById(R.id.customcategory_name);
        final Intent intent=getIntent();
        customid=intent.getStringExtra("customid");
        edit=intent.getStringExtra("action");
        usersave=(TextView)findViewById(R.id.custom_toobar_right);
        toobar_tile=(TextView)findViewById(R.id.custom_toobar_midd);
        toobar_back=(TextView)findViewById(R.id.custom_toobar_left);
        usersave.setCompoundDrawables(null,null,null,null);
        toobar_tile.setCompoundDrawables(null,null,null,null);
        usersave.setText("保存");
        usersave.setOnClickListener(this);
        toobar_back.setOnClickListener(this);
       formInit();

    }
private  void formInit()
{if(customid!=null) {
    customlist = DataSupport.find(Brand.class, Long.parseLong(customid));
    username.setText(customlist.getName());
}
    if(edit.equals("edit"))
    {
        toobar_tile.setText("品牌修改");
    }else {
        toobar_tile.setText("品牌新增");
    }

}
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.custom_toobar_right:
                if (TextUtils.isEmpty(username.getText().toString())) {
                    username.setError("需要输入品牌");
                } else {
                    if (edit.equals("edit")) {
                        custom = new Brand();
                        custom.setName(username.getText().toString());
                        custom.update(Long.parseLong(customid));
                        Intent intent = new Intent();
                        setResult(RESULT_OK,intent);
                        BrandForm.this.finish();
                    } else {
                        custom = new Brand();
                        custom.setName(username.getText().toString());

                        custom.save();
                        Intent intent = new Intent();
                        setResult(RESULT_OK,intent);
                        BrandForm.this.finish();
                    }
                }
                break;
            case R.id.custom_toobar_left:
                if(edit.equals("edit"))
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