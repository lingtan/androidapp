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

/**
 * Created by lingtan on 2017/5/8.
 */

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText name, password, email;
    private CheckBox checked;
    private TextInputLayout layoutName, layoutPassword, layoutEmail;
    private Button loginButton;
    private InputMethodManager manager;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Dialog dialog;


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
            name.setText(nane);
            this.password.setText(password);
            this.email.setText(email);
            checked.setChecked(true);
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
        name = (EditText) findViewById(R.id.login_layout_one_one_name);
        password = (EditText) findViewById(R.id.login_layout_one_tow_password);
        email = (EditText) findViewById(R.id.login_layout_one_three_email);
        checked =(CheckBox)findViewById(R.id.login_layout_one_four_remenberpaw);
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        layoutName = (TextInputLayout) findViewById(R.id.login_layout_one_one);
        layoutPassword = (TextInputLayout) findViewById(R.id.login_layout_one_tow);
        layoutEmail = (TextInputLayout) findViewById(R.id.login_layout_one_three);

        loginButton = (Button) findViewById(R.id.login_layout_one_login);
        loginButton.setOnClickListener(this);

        //添加监听
        name.addTextChangedListener(new MyTextWatcher(name));
        password.addTextChangedListener(new MyTextWatcher(password));
        email.addTextChangedListener(new MyTextWatcher(email));
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

       goIntent(name.getText().toString(), password.getText().toString(), email.getText().toString());

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
    private void goIntent(String name,String password,String email)
    {
        Intent intent=new Intent(Login.this,ErpHome.class);
        refWrite(checked.isChecked(),name,password,email);
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

        if (!name.getText().toString().trim().equals("lingtan") || name.getText().toString().trim().isEmpty()) {
            layoutName.setError(getString(R.string.error_name));
            name.requestFocus();
            return false;
        }
        layoutName.setErrorEnabled(false);
        return true;
    }

    public boolean isPasswordValid() {
        if (!password.getText().toString().trim().equals("123") || password.getText().toString().trim().isEmpty()) {
            layoutPassword.setErrorEnabled(true);
            layoutPassword.setError(getResources().getString(R.string.error_password));
            password.requestFocus();
            return false;
        }
        layoutPassword.setErrorEnabled(false);
        return true;
    }

    public boolean isEmailValid() {
        String email = this.email.getText().toString().trim();
        if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            layoutEmail.setErrorEnabled(true);
            layoutEmail.setError(getString(R.string.error_email));
            layoutEmail.requestFocus();
            return false;
        }
        layoutEmail.setErrorEnabled(false);
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
