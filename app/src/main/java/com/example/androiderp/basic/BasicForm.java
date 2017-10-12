package com.example.androiderp.basic;

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

import com.example.androiderp.bean.AcivityPostBen;
import com.example.androiderp.bean.AdapterBean;
import com.example.androiderp.bean.PostUserData;
import com.example.androiderp.bean.ReturnUserData;
import com.example.androiderp.bean.Unit;
import com.example.androiderp.R;
import com.example.androiderp.bean.DataStructure;
import com.example.androiderp.tools.Common;
import com.example.androiderp.tools.HttpUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lingtan on 2017/5/15.
 */

public class BasicForm extends AppCompatActivity implements View.OnClickListener {
    private InputMethodManager manager;
    private EditText name,note;
    private TextView toobarSave, toobarTile, toobarBack;
    private String getPostName;
    private String  getPostType;
    private AdapterBean getPostData;
    private boolean isSave=false;
    private List<Unit> unitList;
    private PostUserData postUserData = new PostUserData();
    private AcivityPostBen acivityPostBen;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customcategory);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        name =(EditText)findViewById(R.id.customcategory_name);
        note =(EditText)findViewById(R.id.customcategory_note);
        final Intent intent=getIntent();
        getPostData =intent.getParcelableExtra("postdata");
        getPostType = intent.getStringExtra("type");
        acivityPostBen=intent.getParcelableExtra("acivityPostBen");
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
    name.setText(getPostData.getName());
    note.setText(getPostData.getNote());

}
    if(getPostType.equals("edit"))
    {
        toobarTile.setText(acivityPostBen.getAcivityName()+"修改");
    }else {
        toobarTile.setText(acivityPostBen.getAcivityName()+"新增");
    }

}
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.custom_toobar_right:
                 unitList= DataStructure.where("name = ?", name.getText().toString()).find(Unit.class);
                if (TextUtils.isEmpty(name.getText().toString())) {
                    name.setError("需要输入"+acivityPostBen.getAcivityName());
                }else if (unitList.size()>0)
                {
                    Toast.makeText(BasicForm.this,acivityPostBen.getAcivityName()+"已经存在",Toast.LENGTH_SHORT).show();
                } else if  (getPostType.equals("edit"))
                {
                    postUserData.setUnitId(getPostData.getUnitId());
                    postUserData.setName(name.getText().toString().trim());
                    postUserData.setNote(note.getText().toString().trim());
                    postUserData.setRequestType("update");
                    postUserData.setServerIp(Common.ip);
                    postUserData.setServlet(acivityPostBen.getRequestServlet());
                    postUserData.setClassType(acivityPostBen.getSetClassType());
                    getHttpData(postUserData);
                    isSave = true;

                    }
                    else {
                    try {
                        postUserData.setName(name.getText().toString().trim());
                        postUserData.setNote(note.getText().toString().trim());
                        postUserData.setRequestType("insert");
                        postUserData.setServerIp(Common.ip);
                        postUserData.setServlet(acivityPostBen.getRequestServlet());
                        postUserData.setClassType(acivityPostBen.getSetClassType());
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
                        intent.putExtra("returnName", name.getText().toString());
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


                        Toast.makeText(BasicForm.this, "网络连接失败", Toast.LENGTH_SHORT).show();

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
                                    getPostData.setUnitId(getPostData.getUnitId());
                                    getPostData.setName(name.getText().toString().trim());
                                    getPostData.setNote(note.getText().toString().trim());
                                    intent.putExtra("getPostData", getPostData);
                                }else
                                {
                                    AdapterBean user = new AdapterBean();
                                    user.setName(name.getText().toString().trim());
                                    user.setUnitId(returnUserData.getResult());
                                    user.setNote(note.getText().toString().trim());
                                    intent.putExtra("getPostData", user);
                                }
                                BasicForm.this.finish();


                            } else {

                                Toast.makeText(BasicForm.this, "操作失败", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e)
                        {
                            Toast.makeText(BasicForm.this, "网络连接失败", Toast.LENGTH_SHORT).show();
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
