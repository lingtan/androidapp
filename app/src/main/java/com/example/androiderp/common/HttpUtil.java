package com.example.androiderp.common;

import android.util.Log;

import com.example.androiderp.CustomDataClass.TestUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lingtan on 2017/9/18.
 */



public class HttpUtil {
    public  static void sendOkHttpRequst(String ip,String serlet,TestUser postTestUser,okhttp3.Callback callback) {
        String address = ip + serlet;
        Gson gson = new Gson();
        String json = gson.toJson(postTestUser);
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(address).post(body).build();
        client.newCall(request).enqueue(callback);




    }





}
