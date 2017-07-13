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
    private EditText userName;
    private TextView toobarSave, toobarTile, toobarBack;
    private Brand brand;
    private String customid,edit;
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
    brand = DataSupport.find(Brand.class, Long.parseLong(customid));
    userName.setText(brand.getName());
}
    if(edit.equals("edit"))
    {
        toobarTile.setText("品牌修改");
    }else {
        toobarTile.setText("品牌新增");
    }

}
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.custom_toobar_right:
                if (TextUtils.isEmpty(userName.getText().toString())) {
                    userName.setError("需要输入品牌");
                } else {
                    if (edit.equals("edit")) {
                      Brand  brand = new Brand();
                        brand.setName(userName.getText().toString());
                        brand.update(Long.parseLong(customid));
                        Intent intent = new Intent();
                        setResult(RESULT_OK,intent);
                        BrandForm.this.finish();
                    } else {
                      Brand  brand = new Brand();
                        brand.setName(userName.getText().toString());

                        brand.save();
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
