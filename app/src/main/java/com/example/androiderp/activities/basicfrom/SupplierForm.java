package com.example.androiderp.activities.basicfrom;

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

import com.example.androiderp.basic.BasicView;
import com.example.androiderp.bean.AcivityPostBean;
import com.example.androiderp.bean.SalesOut;
import com.example.androiderp.bean.Supplier;
import com.example.androiderp.R;
import com.example.androiderp.bean.DataStructure;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingtan on 2017/5/15.
 */

public class SupplierForm extends AppCompatActivity implements View.OnClickListener {
    private InputMethodManager manager;
    private EditText name,address,phone,fax;
    private TextView toobarSave, toobarTile, toobarBack, toobarAdd,category;
    private DisplayMetrics dm;
    private LinearLayout linearLayout;
    private Supplier supplier;
    private String categoryid,customid,edit;
    private Button deleteButton;
    private Drawable errorIcon;
    private List<SalesOut> salesOutList =new ArrayList<SalesOut>();
    private AcivityPostBean acivityPostBen=new AcivityPostBean();
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
        toobarSave =(TextView)findViewById(R.id.customtoobar_right);
        toobarTile =(TextView)findViewById(R.id.customtoobar_midd);
        toobarBack =(TextView)findViewById(R.id.customtoobar_left);
        linearLayout=(LinearLayout)findViewById(R.id.supplierform_category_layout);
        toobarAdd =(TextView)findViewById(R.id.customtoobar_r) ;
        deleteButton =(Button)findViewById(R.id.loginbutton);
        toobarSave.setOnClickListener(this);
        toobarBack.setOnClickListener(this);
        linearLayout.setOnClickListener(this);
        toobarAdd.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        toobarSave.setCompoundDrawables(null,null,null,null);
        toobarTile.setCompoundDrawables(null,null,null,null);
        errorIcon = getResources().getDrawable(R.drawable.icon_error);
// 设置图片大小
        errorIcon.setBounds(new Rect(0, 0, errorIcon.getIntrinsicWidth(),
                errorIcon.getIntrinsicHeight()));
        toobarSave.setText("保存");
        formInit();

    }
    private void  formInit()
    {

        if(customid!=null) {

            supplier = DataSupport.find(Supplier.class, Long.parseLong(customid));
            name.setText(supplier.getName());
            address.setText(supplier.getAddress());
            phone.setText(supplier.getPhone());
            fax.setText(supplier.getFax());
            category.setText(supplier.getCategory());
            toobarAdd.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        }else {


        }
        if(edit!=null) {
            if (edit.equals("edit")) {
                toobarTile.setText("供应商修改");
            } else {
                toobarTile.setText("供应商新增");
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
                }
                else if (edit.equals("edit")) {
             Supplier       supplier = new Supplier();
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
             Supplier       supplier = new Supplier();
                    supplier.setName(name.getText().toString());
                    supplier.setAddress(address.getText().toString());
                    supplier.setPhone(phone.getText().toString());
                    supplier.setFax(fax.getText().toString());
                    supplier.setCategory(category.getText().toString());
                    supplier.save();
                    Toast.makeText(SupplierForm.this,"新增成功",Toast.LENGTH_SHORT).show();
                    edit="edit";
                    toobarAdd.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);
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
                    Intent intent = new Intent();
                    setResult(RESULT_OK,intent);
                    finish();
                }
             break;
            case R.id.supplierform_category_layout:
                acivityPostBen.setAcivityName("供应商类别");
                acivityPostBen.setRequestServlet("BrandOperate");
                acivityPostBen.setName(category.getText().toString());
                acivityPostBen.setSetClassType(7);
                acivityPostBen.setIsSelect("YES");
                Intent intentcategory=new Intent(SupplierForm.this, BasicView.class);
                intentcategory.putExtra("acivityPostBen",acivityPostBen);
                startActivityForResult(intentcategory,1);
                break;
            case R.id.loginbutton:
                AlertDialog.Builder dialog=new AlertDialog.Builder(SupplierForm.this);
                dialog.setTitle("提示");
                dialog.setMessage("您确认要删除该供应商？");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(isCustom(name.getText().toString()))
                        {
                            Toast.makeText(SupplierForm.this,"已经有业务发生，不能删除",Toast.LENGTH_SHORT).show();

                        }else {
                            DataStructure.deleteAll(Supplier.class, "name = ?", name.getText().toString());

                            AlertDialog.Builder dialogOK = new AlertDialog.Builder(SupplierForm.this);
                            dialogOK.setMessage("该供应商已经删除");
                            dialogOK.setNegativeButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            });
                            dialogOK.show();


                        }

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
                toobarAdd.setVisibility(View.INVISIBLE);
                toobarTile.setText("供应商新增");
                deleteButton.setVisibility(View.INVISIBLE);
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
    public boolean isCustom(String name)
    {

        salesOutList =DataSupport.where("customer=?",name).find(SalesOut.class);

        return salesOutList.size() > 0;

    }

}
