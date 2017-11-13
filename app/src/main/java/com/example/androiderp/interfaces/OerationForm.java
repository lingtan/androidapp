package com.example.androiderp.interfaces;

import com.example.androiderp.bean.ReceiveData;
import com.example.androiderp.bean.ReceiveParamet;

/**
 * Created by lingtan on 2017/11/9.
 */

public interface OerationForm {
    public void insertData(String address,ReceiveData receiveData);
    public void updateById(String address,ReceiveData receiveData);
    public void resultHandler(String result);
}
