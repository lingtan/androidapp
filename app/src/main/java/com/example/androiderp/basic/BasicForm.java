package com.example.androiderp.basic;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiderp.R;
import com.example.androiderp.bean.AcivityPostBean;
import com.example.androiderp.bean.AdapterBean;
import com.example.androiderp.bean.DataStructure;
import com.example.androiderp.bean.HttpPostBean;
import com.example.androiderp.bean.PostUserData;
import com.example.androiderp.bean.ReceiveData;
import com.example.androiderp.bean.ReceiveParamet;
import com.example.androiderp.bean.ReturnUserData;
import com.example.androiderp.bean.Unit;
import com.example.androiderp.interfaces.OerationForm;
import com.example.androiderp.tools.Common;
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

public class BasicForm extends AppCompatActivity implements View.OnClickListener ,OerationForm{
    private InputMethodManager manager;
    private EditText name, note;
    private TextView save, tile, back;
    private String getPostName;
    private   AdapterBean getPostData;
    private final  AdapterBean insertBean=new AdapterBean();
    private final ReceiveParamet insertParamet=new ReceiveParamet();
    private final ReceiveData insertData=new ReceiveData();
    private final  AdapterBean updateBean=new AdapterBean();
    private final ReceiveParamet updateParamet=new ReceiveParamet();
    private final ReceiveData updateData=new ReceiveData();
    private  ReturnUserData returnUserData;
    private boolean isSave = false;
    private List<Unit> unitList;
    private Dialog dialog;
    private String acivityName,actionType,tableName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        name = (EditText) findViewById(R.id.basiclayout_name);
        note = (EditText) findViewById(R.id.basiclayout_note);
        final Intent intent = getIntent();
        getPostData = intent.getParcelableExtra("postdata");
        acivityName=intent.getStringExtra("acivityName");
        actionType=intent.getStringExtra("actionType");
        tableName=intent.getStringExtra("tableName");
        save = (TextView) findViewById(R.id.custom_toobar_right);
        tile = (TextView) findViewById(R.id.custom_toobar_midd);
        back = (TextView) findViewById(R.id.custom_toobar_left);
        save.setCompoundDrawables(null, null, null, null);
        tile.setCompoundDrawables(null, null, null, null);
        save.setText("保存");
        save.setOnClickListener(this);
        back.setOnClickListener(this);
        formInit();

    }

    private void formInit() {
        if (getPostData != null) {
            getPostName = getPostData.getName();
            name.setText(getPostData.getName());
            note.setText(getPostData.getNote());

        }
        if (actionType.equals("edit")) {
            tile.setText(acivityName + "修改");
        } else {
            tile.setText(acivityName + "新增");
        }

    }

    @Override
    public void insertData(String address, ReceiveData receiveData) {

        showDialog();

        HttpUtil.sendOkHttpRequst(address,receiveData, new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(BasicForm.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        closeDialog();

                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                 resultHandler(response.body().string());

            }
        });


    }

    @Override
    public void updateById(String address, ReceiveData receiveData) {

        showDialog();

        HttpUtil.sendOkHttpRequst(address,receiveData, new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(BasicForm.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        closeDialog();

                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                resultHandler(response.body().string());

            }
        });


    }

    @Override
    public void resultHandler(final String result) {


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {


                    Gson gson = new Gson();
                    ReturnUserData returnUserData = gson.fromJson(result, ReturnUserData.class);


                    if (Integer.valueOf(returnUserData.getResult()) > 0) {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        if (getPostData != null) {
                            getPostData.setId(getPostData.getId());
                            getPostData.setName(name.getText().toString().trim());
                            getPostData.setNote(note.getText().toString().trim());
                            intent.putExtra("getPostData", getPostData);
                        } else {
                            AdapterBean user = new AdapterBean();
                            user.setName(name.getText().toString().trim());
                            user.setId(Integer.valueOf(returnUserData.getResult()));
                            user.setNote(note.getText().toString().trim());
                            intent.putExtra("getPostData", user);
                        }
                        closeDialog();
                        BasicForm.this.finish();


                    } else if(Integer.valueOf(returnUserData.getResult())==0)
                    {
                        Toast.makeText(getApplicationContext(), name.getText()+"已经存在", Toast.LENGTH_SHORT).show();
                        closeDialog();
                    }else

                    {

                        Toast.makeText(BasicForm.this, "操作失败", Toast.LENGTH_SHORT).show();
                        closeDialog();
                    }
                } catch (Exception e) {
                    Toast.makeText(BasicForm.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    closeDialog();
                }


            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.custom_toobar_right:
                if (actionType.equals("edit")) {
                    updateParamet.setId(String.valueOf(getPostData.getId()));
                    updateParamet.setTableName(tableName);
                    updateBean.setName(name.getText().toString().trim());
                    updateBean.setNote(note.getText().toString().trim());
                    updateData.setParamet(updateParamet);
                    updateData.setAdapterBean(updateBean);
                   updateById("updateData",updateData);
                    isSave = true;

                } else {

                        insertParamet.setTableName(tableName);
                        insertBean.setName(name.getText().toString().trim());
                        insertBean.setNote(note.getText().toString().trim());
                        insertData.setParamet(insertParamet);
                        insertData.setAdapterBean(insertBean);
                        insertData("insertData",insertData);

                }

                break;
            case R.id.custom_toobar_left:
                if (actionType.equals("edit")) {
                    Intent intent = new Intent();
                    if (isSave) {
                        intent.putExtra("returnName", name.getText().toString());
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

        dialog = new DataLoadingDialog(this);
        dialog.setCanceledOnTouchOutside(false);
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
