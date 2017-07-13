package com.example.androiderp.form;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.androiderp.CustomDataClass.SupplierCategory;
import com.example.androiderp.R;

import org.litepal.crud.DataSupport;

/**
 * Created by lingtan on 2017/5/15.
 */

public class SupplierCategoryForm extends AppCompatActivity implements View.OnClickListener {
    private InputMethodManager manager;
    private EditText userName;
    private TextView toobarSave, toobarTile, toobarBack;
    private SupplierCategory supplierCategory;
    private String customid,edit;
    private Intent intent;
    protected void onCreate(Bundle savedInstanceState) {
        final Typeface textFont = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suppliercategory);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        userName =(EditText)findViewById(R.id.suppliercategory_name);
        intent=getIntent();
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
    private  void  formInit()
    {if(customid!=null) {
        supplierCategory = DataSupport.find(SupplierCategory.class, Long.parseLong(customid));
        userName.setText(supplierCategory.getName());
    }
        if(edit.equals("edit"))
        {
            toobarTile.setText("供应商分类修改");
        }else {
            toobarTile.setText("供应商分类新增");
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.custom_toobar_right:

                if (TextUtils.isEmpty(userName.getText().toString())) {
                    userName.setError("需要输入供应商分类名称");
                } else {
                    if (edit.equals("edit")) {
               SupplierCategory         supplierCategory = new SupplierCategory();
                        supplierCategory.setName(userName.getText().toString());
                        supplierCategory.update(Long.parseLong(customid));

                        setResult(RESULT_OK,intent);
                        SupplierCategoryForm.this.finish();
                    } else {
              SupplierCategory          supplierCategory = new SupplierCategory();
                        supplierCategory.setName(userName.getText().toString());
                        supplierCategory.save();
                        setResult(RESULT_OK,intent);
                        SupplierCategoryForm.this.finish();
                    }
                }

                break;
            case R.id.custom_toobar_left:

                setResult(RESULT_OK,intent);
                SupplierCategoryForm.this.finish();

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
