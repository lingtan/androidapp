package com.example.androiderp.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.example.androiderp.R;

/**
 * Created by lingtan on 2017/10/18.
 */

public class DataLoadingDialog extends Dialog {
    public DataLoadingDialog(Context context) {
        super(context, R.style.loadingdialog);
    }

    public DataLoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.data_loading);
    }
    public void setMsg(String text) {
    }
}