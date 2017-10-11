package com.example.androiderp.activities.basicfrom;

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

import com.example.androiderp.bean.CustomCategory;
import com.example.androiderp.R;
import com.example.androiderp.bean.DataStructure;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by lingtan on 2017/5/15.
 */

public class CustomCategoryForm extends AppCompatActivity implements View.OnClickListener {
    private InputMethodManager manager;
    private EditText userName;
    private TextView toobarSave, toobarTile, toobarBack;
    private CustomCategory customCategory;
    private String customid,edit;
    private boolean isSave=false;
    private List<CustomCategory> customCategoryList;
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
    customCategory = DataSupport.find(CustomCategory.class, Long.parseLong(customid));
    userName.setText(customCategory.getName());
}
    if(edit.equals("edit"))
    {
        toobarTile.setText("客户分类修改");
    }else {
        toobarTile.setText("客户分类新增");
    }

}
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.custom_toobar_right:
                customCategoryList = DataStructure.where("name = ?",userName.getText().toString()).find(CustomCategory.class);
                if (TextUtils.isEmpty(userName.getText().toString())) {
                    userName.setError("需要输入客户分类");
                } else if (customCategoryList.size()>0)
                {
                    Toast.makeText(CustomCategoryForm.this,"分类已经存在",Toast.LENGTH_SHORT).show();
                }else {
                    if (edit.equals("edit")) {
                 CustomCategory      supplierCategory = new CustomCategory();
                       supplierCategory.setName(userName.getText().toString());
                       supplierCategory.update(Long.parseLong(customid));
                        Intent intent = new Intent();
                        intent.putExtra("returnName",userName.getText().toString());
                        setResult(RESULT_OK,intent);
                        isSave=true;
                        CustomCategoryForm.this.finish();
                    } else {
                CustomCategory       supplierCategory = new CustomCategory();
                        supplierCategory.setName(userName.getText().toString());
                       supplierCategory.save();
                        Intent intent = new Intent();
                        setResult(RESULT_OK,intent);
                        CustomCategoryForm.this.finish();
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
                        intent.putExtra("returnName", customCategory.getName());
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
