package com.example.androiderp.form;

import android.app.Dialog;
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

import com.example.androiderp.CustomDataClass.Brand;
import com.example.androiderp.CustomDataClass.ReturnUserData;
import com.example.androiderp.CustomDataClass.PostUserData;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonAdapterData;
import com.example.androiderp.adaper.DataStructure;
import com.example.androiderp.common.Common;
import com.example.androiderp.common.HttpUtil;
import com.example.androiderp.common.MD5;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lingtan on 2017/5/15.
 */

public class TestUserForm extends AppCompatActivity implements View.OnClickListener {
    private InputMethodManager manager;
    private EditText userName;
    private TextView toobarSave, toobarTile, toobarBack;
    private String  getPostType;
    private CommonAdapterData getPostData;
    private List<Brand> brandList;
    private String getPostName;
    private boolean isSave = false;
    private Dialog dialog;
    private PostUserData postUserData = new PostUserData();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customcategory);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        userName = (EditText) findViewById(R.id.customcategory_name);
        final Intent intent = getIntent();
        getPostData =intent.getParcelableExtra("postdata");
        getPostType = intent.getStringExtra("type");
        toobarSave = (TextView) findViewById(R.id.custom_toobar_right);
        toobarTile = (TextView) findViewById(R.id.custom_toobar_midd);
        toobarBack = (TextView) findViewById(R.id.custom_toobar_left);
        toobarSave.setCompoundDrawables(null, null, null, null);
        toobarTile.setCompoundDrawables(null, null, null, null);
        toobarSave.setText("保存");
        toobarSave.setOnClickListener(this);
        toobarBack.setOnClickListener(this);
        formInit();

    }

    private void formInit() {
        if (getPostData != null) {

            getPostName = getPostData.getName();
            userName.setText(getPostData.getName());

        }
        if (getPostType.equals("edit")) {
            toobarTile.setText("品牌修改");
        } else {
            toobarTile.setText("品牌新增");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.custom_toobar_right:

                brandList = DataStructure.where("name = ?", userName.getText().toString()).find(Brand.class);
                if (TextUtils.isEmpty(userName.getText().toString())) {
                    userName.setError("需要输入品牌");
                } else if (brandList.size() > 0) {
                    Toast.makeText(TestUserForm.this, "品牌已经存在", Toast.LENGTH_SHORT).show();
                } else {
                    if (getPostType.equals("edit")) {
                        postUserData.setOriginal(getPostName.toString());
                        postUserData.setUnitId(getPostData.getId());
                        postUserData.setName(userName.getText().toString().trim());
                        postUserData.setRequestType("update");
                        postUserData.setServerIp(Common.ip);
                        postUserData.setServlet("BrandOperate");
                        getHttpData(postUserData);
                        isSave = true;

                    } else {
                        //getHttpData(Common.ip + "UnitOperate", userName.getText().toString().trim(), "insert");
                        try {
                            String md5=MD5.getMD5("888");
                            postUserData.setName(userName.getText().toString().trim());
                            postUserData.setNote(md5);
                            postUserData.setRequestType("insert");
                            postUserData.setServerIp(Common.ip);
                            postUserData.setServlet("BrandOperate");
                            getHttpData(postUserData);


                        }catch (Exception e)
                        {

                        }


                    }
                }
                break;
            case R.id.custom_toobar_left:
                if (getPostType.equals("edit")) {
                    Intent intent = new Intent();
                    if (isSave) {
                        intent.putExtra("returnName", userName.getText().toString());
                    } else {
                        intent.putExtra("returnName", getPostName);
                    }
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    finish();
                }


        }
    }
    private void getHttpData( final PostUserData postPostUserData) {


        HttpUtil.sendOkHttpRequst(postPostUserData, new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(TestUserForm.this, "网络连接失败", Toast.LENGTH_SHORT).show();

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
                            ReturnUserData returnUserData = (ReturnUserData) gson.fromJson(response.body().string(), ReturnUserData.class);


                                if (returnUserData.getResult() > 0) {
                                    Intent intent = new Intent();
                                    setResult(RESULT_OK, intent);
                                    if(getPostData !=null) {
                                        CommonAdapterData user = new CommonAdapterData();
                                        user.setId(getPostData.getId());
                                        user.setName(userName.getText().toString().trim());
                                        intent.putExtra("customid", user);
                                    }else
                                    {
                                        CommonAdapterData user = new CommonAdapterData();
                                        user.setName(userName.getText().toString().trim());
                                        user.setId(returnUserData.getResult());
                                        intent.putExtra("customid", user);
                                    }
                                    TestUserForm.this.finish();


                                } else {

                                    Toast.makeText(TestUserForm.this, "操作失败", Toast.LENGTH_SHORT).show();
                                }
                       }catch (Exception e)
                       {
                           Toast.makeText(TestUserForm.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                       }



                    }
                });


            }
        });


    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 显示进度对话框
     */
    private void showDialog() {

        dialog = Common.createLoadingDialog(this, "正在加载中...");
        dialog.setCancelable(true);//允许返回
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
