package com.example.androiderp.activities.basicfrom;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
    private TextView toobarSave, toobarTile, toobarBack, toobarAdd,category;
    private DisplayMetrics dm;
    private LinearLayout linearLayout;
    private List<Custom> customList;
    private String getPostType;
    private Button deleteButton;
    private AcivityPostBean acivityPostBen=new AcivityPostBean();
    private PostUserData postUserData = new PostUserData();
    private AdapterBean getPostData;
    private Dialog dialog;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userform);
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final Intent intent=getIntent();
        getPostData =intent.getParcelableExtra("postdata");
        getPostType = intent.getStringExtra("type");
        acivityPostBen=intent.getParcelableExtra("acivityPostBen");
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
        if(getPostData!=null) {

            name.setText(getPostData.getName());
            address.setText(getPostData.getAddress());
            category.setText(getPostData.getCategory_name());
            phone.setText(getPostData.getPhone());
            fax.setText(getPostData.getFax());

                toobarAdd.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);

        }else {

        }
        if(getPostType.equals("edit"))
        {
            toobarTile.setText(acivityPostBen.getAcivityName());
        }else {
            toobarTile.setText(acivityPostBen.getAcivityName()+"新增");
        }


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.customtoobar_right:
                customList= DataStructure.where("name = ?",name.getText().toString()).find(Custom.class);
                if (TextUtils.isEmpty(name.getText().toString())) {
                    name.setError("需要输入"+acivityPostBen.getAcivityName());
                } else if (customList.size()>0)
                {
                    Toast.makeText(CustomForm.this,"客户方式已经存在",Toast.LENGTH_SHORT).show();
                }else {
                    if (getPostType.equals("edit")) {
                        postUserData.setUnitId(getPostData.getContact_id());
                        postUserData.setName(name.getText().toString());
                        postUserData.setAddress(address.getText().toString());
                        postUserData.setPhone(phone.getText().toString());
                        postUserData.setFax(fax.getText().toString());
                        postUserData.setCategory_name(category.getText().toString());
                        postUserData.setRequestType(GlobalVariable.cfUpdate);
                        postUserData.setServerIp(Common.ip);
                        postUserData.setServlet(acivityPostBen.getRequestServlet());
                        postUserData.setClassType(acivityPostBen.getSetClassType());
                        showDialog();
                        getHttpData(postUserData);
                        hintKbTwo();
                    } else {

                        postUserData.setName(name.getText().toString());
                        postUserData.setAddress(address.getText().toString());
                        postUserData.setPhone(phone.getText().toString());
                        postUserData.setFax(fax.getText().toString());
                        postUserData.setCategory_name(category.getText().toString());
                        postUserData.setRequestType(GlobalVariable.cfInsert);
                        postUserData.setServerIp(Common.ip);
                        postUserData.setServlet(acivityPostBen.getRequestServlet());
                        postUserData.setClassType(acivityPostBen.getSetClassType());
                        showDialog();
                        getHttpData(postUserData);
                        getPostType="edit";
                        toobarAdd.setVisibility(View.VISIBLE);
                        deleteButton.setVisibility(View.VISIBLE);
                        hintKbTwo();

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
                            DataStructure.deleteAll(Custom.class, "name = ?", name.getText().toString());
                        postUserData.setUnitId(getPostData.getContact_id());
                        postUserData.setServerIp(Common.ip);
                        postUserData.setRequestType(GlobalVariable.cfDelete);
                        postUserData.setServlet(acivityPostBen.getRequestServlet());
                        postUserData.setClassType(acivityPostBen.getSetClassType());
                        getPostData.setOperationType("delete");
                        showDialog();
                        getHttpData(postUserData);
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
                finish();

                break;

            case R.id.usercategory_layout:
             AcivityPostBean acivityPostBenc=new AcivityPostBean();
                acivityPostBenc.setAcivityName(acivityPostBen.getAcivityName());
                acivityPostBenc.setRequestServlet("BrandOperate");
                acivityPostBenc.setName(category.getText().toString());
                if(acivityPostBen.getSetClassType()==1)
                {
                    acivityPostBenc.setSetClassType(8);
                }else {
                    acivityPostBenc.setSetClassType(7);
                }

                acivityPostBenc.setIsSelect("YES");
                Intent intentcategory=new Intent(CustomForm.this, BasicView.class);
                intentcategory.putExtra("acivityPostBen",acivityPostBenc);
                startActivityForResult(intentcategory,1);
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
                getPostType="";
                break;


        }
    }
    private void getHttpData( final PostUserData postPostUserData) {


        HttpUtil.sendOkHttpRequst(postPostUserData, new okhttp3.Callback() {

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


                            if (returnUserData.getResult() > 0) {
                                Intent intent = new Intent();

                                if(getPostData !=null) {
                                    setResult(RESULT_OK, intent);
                                    getPostData.setContact_id(getPostData.getContact_id());
                                    getPostData.setName(name.getText().toString().trim());
                                    getPostData.setAddress(address.getText().toString().trim());
                                    getPostData.setCategory_name(category.getText().toString().trim());
                                    getPostData.setPhone(phone.getText().toString().trim());
                                    getPostData.setFax(fax.getText().toString().trim());
                                    intent.putExtra("getPostData", getPostData);
                                }else
                                {
                                    setResult(RESULT_OK, intent);
                                    AdapterBean user = new AdapterBean();
                                    user.setContact_id(returnUserData.getResult());
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
                    category.setText(data.getStringExtra("data_return"));
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
