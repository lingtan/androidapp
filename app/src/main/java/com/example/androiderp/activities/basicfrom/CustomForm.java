package com.example.androiderp.activities.basicfrom;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiderp.basic.BasicView;
import com.example.androiderp.bean.AcivityPostBean;
import com.example.androiderp.bean.AdapterBean;
import com.example.androiderp.bean.Custom;
import com.example.androiderp.bean.CustomCategory;
import com.example.androiderp.bean.HttpPostBean;
import com.example.androiderp.bean.PostUserData;
import com.example.androiderp.bean.ReturnUserData;
import com.example.androiderp.R;
import com.example.androiderp.bean.DataStructure;
import com.example.androiderp.tools.Common;
import com.example.androiderp.tools.GlobalVariable;
import com.example.androiderp.tools.HttpUtil;
import com.example.androiderp.ui.DataLoadingDialog;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lingtan on 2017/5/15.
 */

public class CustomForm extends AppCompatActivity implements View.OnClickListener {
    private InputMethodManager manager;
    private EditText name,address,phone,fax;
    private TextView save, tile, back, add,category;
    private DisplayMetrics dm;
    private LinearLayout linearLayout;
    private List<Custom> customList;
    private Button deleteButton;
    private AcivityPostBean acivityPostBen=new AcivityPostBean();
    private CustomCategory customCategory=new CustomCategory();
    private HttpPostBean httpPostBean=new HttpPostBean();
    private PostUserData postUserData = new PostUserData();
    private Custom getPostData;
    private Dialog dialog;
    private Custom custom=new Custom();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userform);
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final Intent intent=getIntent();
        getPostData =intent.getParcelableExtra("postdata");
        acivityPostBen=intent.getParcelableExtra("acivityPostBen");
        httpPostBean=intent.getParcelableExtra("httpPostBean");
        httpPostBean.setServlet("ContactOperate");
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        name=(EditText)findViewById(R.id.username);
        address=(EditText)findViewById(R.id.useraddress);
        phone=(EditText)findViewById(R.id.userphone);
        fax=(EditText)findViewById(R.id.userfax);
        category=(TextView)findViewById(R.id.usercategory);
        save =(TextView)findViewById(R.id.customtoobar_right);
        tile =(TextView)findViewById(R.id.customtoobar_midd);
        back =(TextView)findViewById(R.id.customtoobar_left);
        add =(TextView)findViewById(R.id.customtoobar_r) ;
        deleteButton =(Button)findViewById(R.id.loginbutton);
        linearLayout=(LinearLayout)findViewById(R.id.usercategory_layout);
        save.setOnClickListener(this);
        add.setOnClickListener(this);
        back.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        linearLayout.setOnClickListener(this);
        formInit();
    }
    private void formInit()
    {
        if(getPostData!=null) {

            name.setText(getPostData.getName());
            address.setText(getPostData.getAddress());
            category.setText(getPostData.getCustomCategory().getName());
            phone.setText(getPostData.getPhone());
            fax.setText(getPostData.getFax());

                add.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);

        }else {

        }
        if(acivityPostBen.getAcivityName().equals("edit"))
        {
            tile.setText(acivityPostBen.getAcivityName());
        }else {
            tile.setText(acivityPostBen.getAcivityName()+"新增");
        }


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.customtoobar_right:

                    if (acivityPostBen.getActionType().equals("edit")) {

                        custom.setContact_id(getPostData.getContact_id());
                        custom.setName(name.getText().toString());
                        custom.setAddress(address.getText().toString());
                        custom.setPhone(phone.getText().toString());
                        custom.setFax(fax.getText().toString());
                        if(customCategory.getId()!=0)
                        {
                            custom.setCustomCategory(customCategory);
                        }else {
                            custom.setCustomCategory(getPostData.getCustomCategory());
                        }

                        httpPostBean.setServer(Common.ip);
                        httpPostBean.setOperation(GlobalVariable.cfUpdate);
                        showDialog();
                        getHttpData(custom,httpPostBean);
                        hintKbTwo();
                    } else {

                        custom.setName(name.getText().toString());
                        custom.setAddress(address.getText().toString());
                        custom.setPhone(phone.getText().toString());
                        custom.setFax(fax.getText().toString());
                        if(customCategory.getId()!=0)
                        {
                            custom.setCustomCategory(customCategory);
                        }
                        httpPostBean.setServer(Common.ip);
                        httpPostBean.setOperation(GlobalVariable.cfInsert);
                        postUserData.setRequestType(GlobalVariable.cfInsert);
                        showDialog();
                        getHttpData(custom,httpPostBean);
                        acivityPostBen.setActionType("edit");
                        add.setVisibility(View.VISIBLE);
                        deleteButton.setVisibility(View.VISIBLE);
                        hintKbTwo();

                    }




                break;
            case R.id.loginbutton:
                AlertDialog.Builder dialog=new AlertDialog.Builder(CustomForm.this);
                dialog.setTitle("提示");
                dialog.setMessage("您确认要删除该客户？");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            DataStructure.deleteAll(Custom.class, "name = ?", name.getText().toString());

                        httpPostBean.setServer(Common.ip);
                        httpPostBean.setOperation(GlobalVariable.cfDelete);
                        showDialog();
                        getHttpData(getPostData,httpPostBean);
                        Intent intent = new Intent();
                        setResult(RESULT_OK,intent);


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
                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();

                break;

            case R.id.usercategory_layout:
             AcivityPostBean acivityPostBenc=new AcivityPostBean();
             HttpPostBean  httpPostBeanc=new HttpPostBean();
                acivityPostBenc.setAcivityName(acivityPostBen.getAcivityName());
                httpPostBeanc.setServlet("BrandOperate");
                acivityPostBenc.setName(category.getText().toString());
                if(httpPostBean.getClassType()==1)
                {
                    httpPostBeanc.setClassType(8);
                }else {
                    httpPostBeanc.setClassType(7);
                }

                acivityPostBenc.setIsSelect("YES");
                Intent intentcategory=new Intent(CustomForm.this, BasicView.class);
                intentcategory.putExtra("acivityPostBen",acivityPostBenc);
                intentcategory.putExtra("httpPostBean",httpPostBeanc);
                startActivityForResult(intentcategory,1);
                break;
            case R.id.customtoobar_r:
                name.setText("");
                address.setText("");
                phone.setText("");
                fax.setText("");
                category.setText("");
                add.setVisibility(View.INVISIBLE);
                tile.setText("客户新增");
                deleteButton.setVisibility(View.INVISIBLE);
                acivityPostBen.setActionType("");
                break;


        }
    }
    private void getHttpData( final Custom custom,HttpPostBean httpPostBean) {


        HttpUtil.sendOkHttpRequst(custom,httpPostBean, new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {


                            Gson gson = new Gson();
                            ReturnUserData returnUserData = gson.fromJson(response.body().string(), ReturnUserData.class);


                            if (Integer.valueOf(returnUserData.getResult()) > 0) {
                                Intent intent = new Intent();

                                if(getPostData !=null) {
                                    setResult(RESULT_OK, intent);
                                    getPostData.setContact_id(getPostData.getContact_id());
                                    getPostData.setName(name.getText().toString().trim());
                                    getPostData.setAddress(address.getText().toString().trim());
                                    getPostData.getCustomCategory().setCategory_name(category.getText().toString().trim());
                                    getPostData.setPhone(phone.getText().toString().trim());
                                    getPostData.setFax(fax.getText().toString().trim());
                                    intent.putExtra("getPostData", getPostData);
                                }else
                                {
                                    setResult(RESULT_OK, intent);
                                    AdapterBean user = new AdapterBean();
                                    user.setContact_id(Integer.valueOf(returnUserData.getResult()));
                                    user.setName(name.getText().toString().trim());
                                    user.setAddress(address.getText().toString().trim());
                                    user.setCategory_name(category.getText().toString().trim());
                                    user.setPhone(phone.getText().toString().trim());
                                    user.setFax(fax.getText().toString().trim());
                                    intent.putExtra("getPostData", user);
                                }
                                finish();
                                Toast.makeText(getApplicationContext(), "操作成功", Toast.LENGTH_SHORT).show();
                            } else {
                                closeDialog();
                                Toast.makeText(getApplicationContext(), "操作失败", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e)
                        {
                            closeDialog();
                            Toast.makeText(getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
                        }



                    }
                });


            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    AdapterBean adapterBean=new AdapterBean();
                    adapterBean=data.getParcelableExtra("data_return");
                    if(adapterBean!=null) {
                        category.setText(adapterBean.getName());
                        customCategory.setId(adapterBean.getId());
                        customCategory.setName(adapterBean.getName());
                        customCategory.setNote(adapterBean.getNote());
                    }
                }
                break;
            default:
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

    /**
     * 显示进度对话框
     */
    private void showDialog() {

        dialog = new DataLoadingDialog(this);
        dialog.show();//显示

    }

    /**
     * 关闭进度对话框
     */
    private void closeDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

}
