package com.example.androiderp.form;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.androiderp.CustomDataClass.CustomCategory;
import com.example.androiderp.CustomDataClass.SupplierCategory;
import com.example.androiderp.R;
import com.example.androiderp.adaper.PopuMenuDataStructure;
import com.example.androiderp.basicdata.CustomCategoryList;
import com.example.androiderp.basicdata.SupplierCategoryList;
import com.example.androiderp.basicdata.SupplierCategoryResult;
import com.example.androiderp.common.Common;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by lingtan on 2017/5/15.
 */

public class SupplierCategoryForm extends AppCompatActivity implements View.OnClickListener {
    private InputMethodManager manager;
    private EditText username;
    private TextView usersave,toobar_tile,toobar_back;
    private Button save;
    private SupplierCategory custom;
    private SupplierCategory customlist;
    private String customid,edit;
    private Intent intent;
    protected void onCreate(Bundle savedInstanceState) {
        final Typeface textFont = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suppliercategory);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        username=(EditText)findViewById(R.id.suppliercategory_name);
        intent=getIntent();
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
    private  void  formInit()
    {if(customid!=null) {
        customlist = DataSupport.find(SupplierCategory.class, Long.parseLong(customid));
        username.setText(customlist.getName());
    }
        if(edit.equals("edit"))
        {
            toobar_tile.setText("供应商分类修改");
        }else {
            toobar_tile.setText("供应商分类新增");
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.custom_toobar_right:

                if (TextUtils.isEmpty(username.getText().toString())) {
                    username.setError("需要输入供应商分类名称");
                } else {
                    if (edit.equals("edit")) {
                        custom = new SupplierCategory();
                        custom.setName(username.getText().toString());
                        custom.update(Long.parseLong(customid));

                        setResult(RESULT_OK,intent);
                        SupplierCategoryForm.this.finish();
                    } else {
                        custom = new SupplierCategory();
                        custom.setName(username.getText().toString());
                        custom.save();
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
