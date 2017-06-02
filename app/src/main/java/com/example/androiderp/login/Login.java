package com.example.androiderp.login;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androiderp.CustomDataClass.User;
import com.example.androiderp.R;
import com.example.androiderp.common.Common;
import com.example.androiderp.home.ErpHome;
import com.example.androiderp.utili.SenHome;

/**
 * Created by lingtan on 2017/5/8.
 */

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText input_name, input_password, input_email;
    private CheckBox input_checked;
    private TextInputLayout layout_name, layout_password, layout_email;
    private Button btn_login;
    private InputMethodManager manager;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Dialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.login);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initWidget();
        preferences= PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemenber=preferences.getBoolean("remenber_passwored",false);
        refRead(isRemenber);

    }
    private void  refRead(boolean isTrue) {

        if (isTrue) {
            String nane = preferences.getString("name", "");
            String password = preferences.getString("password", "");
            String email = preferences.getString("email", "");
            input_name.setText(nane);
            input_password.setText(password);
            input_email.setText(email);
            input_checked.setChecked(true);
        }
    }

    private void  refWrite(boolean ischechde,String name,String password,String email)
    {
        editor=preferences.edit();
        if(ischechde)
        {
            editor.putBoolean("remenber_passwored",true);
            editor.putString("name",name);
            editor.putString("password",password);
            editor.putString("email",email);


        }else {
            editor.clear();
        }
        editor.apply();
    }
    private void initWidget() {
        input_name = (EditText) findViewById(R.id.login_layout_one_one_name);
        input_password = (EditText) findViewById(R.id.login_layout_one_tow_password);
        input_email = (EditText) findViewById(R.id.login_layout_one_three_email);
        input_checked=(CheckBox)findViewById(R.id.login_layout_one_four_remenberpaw);
        input_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        layout_name = (TextInputLayout) findViewById(R.id.login_layout_one_one);
        layout_password = (TextInputLayout) findViewById(R.id.login_layout_one_tow);
        layout_email = (TextInputLayout) findViewById(R.id.login_layout_one_three);

        btn_login = (Button) findViewById(R.id.login_layout_one_login);
        btn_login.setOnClickListener(this);

        //添加监听
        input_name.addTextChangedListener(new MyTextWatcher(input_name));
        input_password.addTextChangedListener(new MyTextWatcher(input_password));
        input_email.addTextChangedListener(new MyTextWatcher(input_email));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_layout_one_login:
                canLogin();
                break;
            default:
                break;

        }
    }

    /**
     * 判断是否可以登录的方法
     */
    private void canLogin() {
        if (!isNameValid()) {
            Toast.makeText(this, getString(R.string.check), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isPasswordValid()) {
            Toast.makeText(this, getString(R.string.check), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isEmailValid()) {
            Toast.makeText(this, getString(R.string.check), Toast.LENGTH_SHORT).show();
            return;
        }

       goIntent(input_name.getText().toString(),input_password.getText().toString(),input_email.getText().toString());

    }
    /**
     * 显示进度对话框
     */
    private void showDialog() {

           mDialog = Common.createLoadingDialog(this, "正在加载中...");
            mDialog.setCancelable(true);//允许返回
            mDialog.show();//显示

    }

    /**
     * 关闭进度对话框
     */
    private void closeDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }
    private void goIntent(String name,String password,String email)
    {
        Intent intent=new Intent(Login.this,ErpHome.class);
        refWrite(input_checked.isChecked(),name,password,email);
        User user=new User();
        user.setName(name);
        user.setPassword(password);
        user.setAddress("广东省江门鹤山市鹤华中学");
        user.setPhone("13690588448");
        intent.putExtra("fragment","fisrt");
        showDialog();
        startActivity(intent);
    }
    public boolean isNameValid() {

        if (!input_name.getText().toString().trim().equals("lingtan") || input_name.getText().toString().trim().isEmpty()) {
            layout_name.setError(getString(R.string.error_name));
            input_name.requestFocus();
            return false;
        }
        layout_name.setErrorEnabled(false);
        return true;
    }

    public boolean isPasswordValid() {
        if (!input_password.getText().toString().trim().equals("123") || input_password.getText().toString().trim().isEmpty()) {
            layout_password.setErrorEnabled(true);
            layout_password.setError(getResources().getString(R.string.error_password));
            input_password.requestFocus();
            return false;
        }
        layout_password.setErrorEnabled(false);
        return true;
    }

    public boolean isEmailValid() {
        String email = input_email.getText().toString().trim();
        if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            layout_email.setErrorEnabled(true);
            layout_email.setError(getString(R.string.error_email));
            layout_email.requestFocus();
            return false;
        }
        layout_email.setErrorEnabled(false);
        return true;
    }

    //动态监听输入过程
    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()) {
                case R.id.login_layout_one_one_name:
                    isNameValid();
                    break;
                case R.id.login_layout_one_tow_password:
                    isPasswordValid();
                    break;
                case R.id.login_layout_one_three_email:
                    isEmailValid();
                    break;

            }

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

    @Override
    protected void onStop() {
        super.onStop();
        closeDialog();
    }

}
