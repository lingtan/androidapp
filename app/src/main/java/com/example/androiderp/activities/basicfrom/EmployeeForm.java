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

import com.example.androiderp.bean.PostUserData;
import com.example.androiderp.bean.ReturnUserData;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonAdapterData;
import com.example.androiderp.tools.Common;
import com.example.androiderp.tools.HttpUtil;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lingtan on 2017/5/15.
 */

public class EmployeeForm extends AppCompatActivity implements View.OnClickListener {
    private InputMethodManager manager;
    private EditText userName,note;
    private TextView toobarSave, toobarTile, toobarBack;
    private CommonAdapterData getPostData;
    private String getPostName;
    private String  getPostType;
    private boolean isSave=false;
    private PostUserData postUserData = new PostUserData();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customcategory);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        userName =(EditText)findViewById(R.id.customcategory_name);
        note =(EditText)findViewById(R.id.customcategory_note);
        final Intent intent=getIntent();
        getPostData =intent.getParcelableExtra("postdata");
        getPostType = intent.getStringExtra("type");
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
{if(getPostData!=null) {
    getPostName = getPostData.getName();
    userName.setText(getPostData.getName());
}
    if(getPostType.equals("edit"))
    {
        toobarTile.setText("职员修改");
    }else {
        toobarTile.setText("职员新增");
    }

}
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.custom_toobar_right:
                if (TextUtils.isEmpty(userName.getText().toString())) {
                    userName.setError("需要输入职员");
                } else {
                    if (getPostType.equals("edit")) {
                        postUserData.setUnitId(getPostData.getUnitId());
                        postUserData.setName(userName.getText().toString().trim());
                        postUserData.setNote(note.getText().toString().trim());
                        postUserData.setRequestType("update");
                        postUserData.setServerIp(Common.ip);
                        postUserData.setServlet("EmployeeOperate");
                        getHttpData(postUserData);
                        isSave = true;
                    } else {
                        try {
                            //String md5= MD5.getMD5("888");
                            postUserData.setName(userName.getText().toString().trim());
                            postUserData.setNote(note.getText().toString().trim());
                            postUserData.setRequestType("insert");
                            postUserData.setServerIp(Common.ip);
                            postUserData.setServlet("EmployeeOperate");
                            getHttpData(postUserData);


                        }catch (Exception e)
                        {

                        }
                    }
                }
                break;
            case R.id.custom_toobar_left:
                if(getPostType.equals("edit"))
                {
                    Intent intent = new Intent();
                    if (isSave) {
                        intent.putExtra("returnName", userName.getText().toString());
                    } else {
                        intent.putExtra("returnName", getPostName);
                    }
                    setResult(RESULT_OK,intent);
                    finish();
                }else {
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


                        Toast.makeText(EmployeeForm.this, "网络连接失败", Toast.LENGTH_SHORT).show();

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
                                    user.setUnitId(getPostData.getUnitId());
                                    user.setName(userName.getText().toString().trim());
                                    intent.putExtra("customid", user);
                                }else
                                {
                                    CommonAdapterData user = new CommonAdapterData();
                                    user.setName(userName.getText().toString().trim());
                                    user.setUnitId(returnUserData.getResult());
                                    intent.putExtra("customid", user);
                                }
                                EmployeeForm.this.finish();


                            } else {

                                Toast.makeText(EmployeeForm.this, "操作失败", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e)
                        {
                            Toast.makeText(EmployeeForm.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        }



                    }
                });


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

}
