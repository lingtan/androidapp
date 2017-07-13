package com.example.androiderp.form;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiderp.CustomDataClass.Custom;
import com.example.androiderp.CustomDataClass.CustomCategory;
import com.example.androiderp.CustomDataClass.SalesOut;
import com.example.androiderp.R;
import com.example.androiderp.adaper.DataStructure;
import com.example.androiderp.adaper.PopuMenuDataStructure;
import com.example.androiderp.common.Common;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingtan on 2017/5/15.
 */

public class CustomForm extends AppCompatActivity implements View.OnClickListener {
    private InputMethodManager manager;
    private EditText name,address,phone,fax;
    private TextView toobarSave, toobarTile, toobarBack, toobarAdd,category;
    private Common common;
    private DisplayMetrics dm;
    private List<PopuMenuDataStructure> popuMenuDatas;
    private LinearLayout linearLayout;
    private Custom custom;
    private List<CustomCategory> customCategoryList;
    private String edit,customid;
    private Button deleteButton;
    private List<SalesOut> salesOutList =new ArrayList<SalesOut>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userform);
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final Intent intent=getIntent();
        edit=intent.getStringExtra("action");
        customid=intent.getStringExtra("custom_item");
        showPopupWindow();
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        name=(EditText)findViewById(R.id.username);
        address=(EditText)findViewById(R.id.useraddress);
        phone=(EditText)findViewById(R.id.userphone);
        fax=(EditText)findViewById(R.id.userfax);
        category=(TextView)findViewById(R.id.usercategory);
        toobarSave =(TextView)findViewById(R.id.customtoobar_right);
        toobarTile =(TextView)findViewById(R.id.customtoobar_midd);
        toobarBack =(TextView)findViewById(R.id.customtoobar_left);
        toobarAdd =(TextView)findViewById(R.id.customtoobar_r) ;
        deleteButton =(Button)findViewById(R.id.loginbutton);
        linearLayout=(LinearLayout)findViewById(R.id.usercategory_layout);
        toobarSave.setOnClickListener(this);
        toobarAdd.setOnClickListener(this);
        toobarBack.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        linearLayout.setOnClickListener(this);
        formInit();
    }
    private void formInit()
    {
        if(customid!=null) {


            custom = DataSupport.find(Custom.class, Long.parseLong(customid));
            name.setText(custom.getName());
            address.setText(custom.getAddress());
            phone.setText(custom.getPhone());
            fax.setText(custom.getFax());
            category.setText(custom.getCategory());

                toobarAdd.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);

        }else {
         CustomCategory  cCategory  = DataSupport.find(CustomCategory.class, 1);
            if(cCategory==null)
            {

            }else {
                category.setText(cCategory.getName());
            }

        }
        if(edit.equals("edit"))
        {
            toobarTile.setText("客户信息");
        }else {
            toobarTile.setText("客户新增");
        }


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.customtoobar_right:
                if (TextUtils.isEmpty(name.getText().toString())) {
                    name.setError("需要输入客户名称");
                } else {
                    if (edit.equals("edit")) {
                  Custom      custom = new Custom();
                        custom.setName(name.getText().toString());
                        custom.setAddress(address.getText().toString());
                        custom.setPhone(phone.getText().toString());
                        custom.setFax(fax.getText().toString());
                        custom.setCategory(category.getText().toString());
                        custom.update(Long.parseLong(customid));
                        Intent intent = new Intent();
                        setResult(RESULT_OK,intent);
                        Toast.makeText(CustomForm.this,"修改成功",Toast.LENGTH_SHORT).show();
                        hintKbTwo();
                    } else {
                  Custom      custom = new Custom();
                        custom.setName(name.getText().toString());
                        custom.setAddress(address.getText().toString());
                        custom.setPhone(phone.getText().toString());
                        custom.setFax(fax.getText().toString());
                        custom.setCategory(category.getText().toString());
                        custom.save();
                        Toast.makeText(CustomForm.this,"新增成功",Toast.LENGTH_SHORT).show();
                        edit="edit";
                        toobarAdd.setVisibility(View.VISIBLE);
                        deleteButton.setVisibility(View.VISIBLE);
                        hintKbTwo();
                        Intent intent = new Intent();
                        setResult(RESULT_OK,intent);

                    }
                }
                break;
            case R.id.loginbutton:
                AlertDialog.Builder dialog=new AlertDialog.Builder(CustomForm.this);
                dialog.setTitle("提示");
                dialog.setMessage("您确认要删除该客户？");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(isCustom(name.getText().toString()))
                        {
                            Toast.makeText(CustomForm.this,"已经有业务发生，不能删除",Toast.LENGTH_SHORT).show();

                        }else {
                            DataStructure.deleteAll(Custom.class, "name = ?", name.getText().toString());

                            AlertDialog.Builder dialogOK = new AlertDialog.Builder(CustomForm.this);
                            dialogOK.setMessage("该客户已经删除");
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
            case  R.id.customtoobar_left:
                if(edit.equals("edit"))
                {
                    Intent intent = new Intent();
                    setResult(RESULT_OK,intent);
                    CustomForm.this.finish();
                }else {
                    Intent intent = new Intent();
                    setResult(RESULT_OK,intent);
                    finish();
                }

                break;

            case R.id.usercategory_layout:
                if( common.mPopWindow==null ||!common.mPopWindow.isShowing())
                {
                    int xPos = dm.widthPixels / 3;
                    common.mPopWindow.showAsDropDown(v,xPos,5);
                    //mPopWindow.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM, 0, 0);//从底部弹出
                }
                else {
                    common.mPopWindow.dismiss();
                }

                break;
            case R.id.customtoobar_r:
                name.setText("");
                address.setText("");
                phone.setText("");
                fax.setText("");
                category.setText("");
                toobarAdd.setVisibility(View.INVISIBLE);
                toobarTile.setText("客户新增");
                deleteButton.setVisibility(View.INVISIBLE);
                edit="";
                break;


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
    private void showPopupWindow() {
        common = new Common();
        popuMenuDatas = new ArrayList<PopuMenuDataStructure>();
        customCategoryList = DataSupport.findAll(CustomCategory.class);
        for(CustomCategory category: customCategoryList)

        {
            PopuMenuDataStructure popuMenua = new PopuMenuDataStructure(R.drawable.poppu_wrie, category.getName());
            popuMenuDatas.add(popuMenua);

        }
        common.PopupWindow(CustomForm.this, dm, popuMenuDatas);
        common.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

               category.setText(popuMenuDatas.get(position).getName());
                common.mPopWindow.dismiss();
            }
        });
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

        if (salesOutList.size()>0)
        {
            return true;
        }else {
            return false;
        }

    }

}
