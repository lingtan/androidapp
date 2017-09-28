package com.example.androiderp.form;

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
import com.example.androiderp.CustomDataClass.PostUserData;
import com.example.androiderp.CustomDataClass.Product;
import com.example.androiderp.CustomDataClass.ReturnUserData;
import com.example.androiderp.CustomDataClass.Unit;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonAdapterData;
import com.example.androiderp.adaper.DataStructure;
import com.example.androiderp.common.Common;
import com.example.androiderp.common.HttpUtil;
import com.example.androiderp.common.MD5;
import com.google.gson.Gson;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lingtan on 2017/5/15.
 */

public class UnitForm extends AppCompatActivity implements View.OnClickListener {
    private InputMethodManager manager;
    private EditText userName;
    private TextView toobarSave, toobarTile, toobarBack;
    private String getPostName;
    private String  getPostType;
    private CommonAdapterData getPostData;
    private boolean isSave=false;
    private List<Unit> unitList;
    private PostUserData postUserData = new PostUserData();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customcategory);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        userName =(EditText)findViewById(R.id.customcategory_name);
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
{ if (getPostData != null) {

    getPostName = getPostData.getName();
    userName.setText(getPostData.getName());

}
    if(getPostType.equals("edit"))
    {
        toobarTile.setText("单位修改");
    }else {
        toobarTile.setText("单位新增");
    }

}
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.custom_toobar_right:
                 unitList= DataStructure.where("name = ?",userName.getText().toString()).find(Unit.class);
                if (TextUtils.isEmpty(userName.getText().toString())) {
                    userName.setError("需要输入单位");
                }else if (unitList.size()>0)
                {
                    Toast.makeText(UnitForm.this,"单位已经存在",Toast.LENGTH_SHORT).show();
                } else if  (getPostType.equals("edit"))
                { postUserData.setOriginal(getPostName.toString());
                    postUserData.setUnitId(getPostData.getId());
                    postUserData.setName(userName.getText().toString().trim());
                    postUserData.setRequestType("update");
                    postUserData.setServerIp(Common.ip);
                    postUserData.setServlet("UnitOperate");
                    getHttpData(postUserData);
                    isSave = true;

                    }
                    else {
                    try {
                        String md5=MD5.getMD5("888");
                        postUserData.setName(userName.getText().toString().trim());
                        postUserData.setNote(md5);
                        postUserData.setRequestType("insert");
                        postUserData.setServerIp(Common.ip);
                        postUserData.setServlet("UnitOperate");
                        getHttpData(postUserData);


                    }catch (Exception e)
                    {

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


                        Toast.makeText(UnitForm.this, "网络连接失败", Toast.LENGTH_SHORT).show();

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
                                UnitForm.this.finish();


                            } else {

                                Toast.makeText(UnitForm.this, "操作失败", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e)
                        {
                            Toast.makeText(UnitForm.this, "网络连接失败", Toast.LENGTH_SHORT).show();
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
