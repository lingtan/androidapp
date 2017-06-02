package com.example.androiderp.form;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiderp.CustomDataClass.Supplier;
import com.example.androiderp.R;
import com.example.androiderp.adaper.DataStructure;
import com.example.androiderp.basicdata.SupplierCategoryListView;
import com.example.androiderp.common.Common;

import org.litepal.crud.DataSupport;

/**
 * Created by lingtan on 2017/5/15.
 */

public class SupplierForm extends AppCompatActivity implements View.OnClickListener {
    private InputMethodManager manager;
    private EditText name,address,phone,fax;
    private TextView save,toobar_tile,toobar_back,toobar_add,category;
    private Supplier supplier;
    private Common common;
    private DisplayMetrics dm;
    private LinearLayout linearLayout;
    private Supplier customlist;
    private String categoryid,customid,edit;
    private Button buttondelete;
    private Drawable errorIcon;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.supplierform);
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final Intent intent=getIntent();
        customid=intent.getStringExtra("supller_item");
        edit=intent.getStringExtra("action");
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        name=(EditText)findViewById(R.id.supplierform_name);
        address=(EditText)findViewById(R.id.supplierform_address);
        phone=(EditText)findViewById(R.id.supplierform_phone);
        fax=(EditText)findViewById(R.id.supplierform_fax);
        category=(TextView)findViewById(R.id.supplierform_category);
        save=(TextView)findViewById(R.id.customtoobar_right);
        toobar_tile=(TextView)findViewById(R.id.customtoobar_midd);
        toobar_back=(TextView)findViewById(R.id.customtoobar_left);
        linearLayout=(LinearLayout)findViewById(R.id.supplierform_category_layout);
        toobar_add=(TextView)findViewById(R.id.customtoobar_r) ;
        buttondelete=(Button)findViewById(R.id.loginbutton);
        save.setOnClickListener(this);
        toobar_back.setOnClickListener(this);
        linearLayout.setOnClickListener(this);
        toobar_add.setOnClickListener(this);
        buttondelete.setOnClickListener(this);
        save.setCompoundDrawables(null,null,null,null);
        toobar_tile.setCompoundDrawables(null,null,null,null);
        errorIcon = getResources().getDrawable(R.drawable.icon_error);
// 设置图片大小
        errorIcon.setBounds(new Rect(0, 0, errorIcon.getIntrinsicWidth(),
                errorIcon.getIntrinsicHeight()));
        save.setText("保存");
        formInit();

    }
    private void  formInit()
    {

        if(customid!=null) {

            customlist = DataSupport.find(Supplier.class, Long.parseLong(customid));
            name.setText(customlist.getName());
            address.setText(customlist.getAddress());
            phone.setText(customlist.getPhone());
            fax.setText(customlist.getFax());
            category.setText(customlist.getCategory());
            toobar_add.setVisibility(View.VISIBLE);
            buttondelete.setVisibility(View.VISIBLE);
        }
        if(edit!=null) {
            if (edit.equals("edit")) {
                toobar_tile.setText("供应商修改");
            } else {
                toobar_tile.setText("供应商新增");
            }
        }
    }
    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())

        {
            case R.id.customtoobar_right:
                if (TextUtils.isEmpty(name.getText().toString())) {
                    name.setError("需要输入供应商",errorIcon);
                }else if (TextUtils.isEmpty(category.getText().toString()))
                {
                    category.setError("请选择供应商分类",errorIcon);
                }
                else if (edit.equals("edit")) {
                    supplier = new Supplier();
                    supplier.setName(name.getText().toString());
                    supplier.setAddress(address.getText().toString());
                    supplier.setPhone(phone.getText().toString());
                    supplier.setFax(fax.getText().toString());
                    supplier.setCategory(category.getText().toString());
                    supplier.update(Long.parseLong(customid));
                    Toast.makeText(SupplierForm.this,"修改成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    setResult(RESULT_OK,intent);
                    hintKbTwo();

                } else {
                    supplier = new Supplier();
                    supplier.setName(name.getText().toString());
                    supplier.setAddress(address.getText().toString());
                    supplier.setPhone(phone.getText().toString());
                    supplier.setFax(fax.getText().toString());
                    supplier.setCategory(category.getText().toString());
                    supplier.save();
                    Toast.makeText(SupplierForm.this,"新增成功",Toast.LENGTH_SHORT).show();
                    edit="edit";
                    toobar_add.setVisibility(View.VISIBLE);
                    buttondelete.setVisibility(View.VISIBLE);
                    Intent intent = new Intent();
                    setResult(RESULT_OK,intent);
                    hintKbTwo();

                }

            break;
            case R.id.customtoobar_left:
                if(edit.equals("edit"))
                {Intent intent = new Intent();
                    intent.putExtra("category",categoryid);
                    setResult(RESULT_OK,intent);
                    finish();
                }else {
                    finish();
                }
             break;
            case R.id.supplierform_category_layout:

                Intent intent=new Intent(SupplierForm.this, SupplierCategoryListView.class);
                intent.putExtra("index",category.getText().toString());
                startActivityForResult(intent,1);
                break;
            case R.id.loginbutton:
                AlertDialog.Builder dialog=new AlertDialog.Builder(SupplierForm.this);
                dialog.setTitle("提示");
                dialog.setMessage("您确认要删除该客户？");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataStructure.deleteAll(Supplier.class,"name = ?",name.getText().toString());

                        AlertDialog.Builder dialogOK=new AlertDialog.Builder(SupplierForm.this);
                        dialogOK.setMessage("该客户已经删除");
                        dialogOK.setNegativeButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                setResult(RESULT_OK,intent);
                                finish();
                            }
                        });
                        dialogOK.show();




                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
                break;

            case R.id.customtoobar_r:
                name.setText("");
                address.setText("");
                phone.setText("");
                fax.setText("");
                category.setText("");
                toobar_add.setVisibility(View.INVISIBLE);
                toobar_tile.setText("供应商新增");
                buttondelete.setVisibility(View.INVISIBLE);
                edit="";
                hintKbTwo();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    category.setText(data.getStringExtra("data_return"));
                }
                break;
            default:
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
