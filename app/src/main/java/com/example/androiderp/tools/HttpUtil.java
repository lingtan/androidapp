package com.example.androiderp.tools;

import com.example.androiderp.bean.PostProductData;
import com.example.androiderp.bean.PostUserData;
import com.example.androiderp.bean.Product;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by lingtan on 2017/9/18.
 */



public class HttpUtil {
    public  static void sendOkHttpRequst(PostUserData postPostUserData, okhttp3.Callback callback) {
        String address = postPostUserData.getServerIp() + postPostUserData.getServlet();
        Gson gson = new Gson();
        String json = gson.toJson(postPostUserData);

        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, HttpAes.encode(json));
        Request request = new Request.Builder().url(address).post(body).build();
        client.newCall(request).enqueue(callback);




    }

    public  static void sendProductRequst(PostProductData postPostUserData, okhttp3.Callback callback) {
        String address = postPostUserData.getServerIp() + postPostUserData.getServlet();
        Gson gson = new Gson();
        String json = gson.toJson(postPostUserData);

        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, HttpAes.encode(json));
        Request request = new Request.Builder().url(address).post(body).build();
        client.newCall(request).enqueue(callback);




    }





}
