package com.example.androiderp.interfaces;

import com.example.androiderp.bean.ReceiveParamet;

/**
 * Created by lingtan on 2017/11/4.
 */

public interface OerationList {

    public void findAll(String address, ReceiveParamet paramet);
    public void findByName(String address,ReceiveParamet paramet);
    public void deleteById(String address,ReceiveParamet paramet);
    public void resultHandler(String result,int type);
}
