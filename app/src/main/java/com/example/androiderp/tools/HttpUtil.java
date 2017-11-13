package com.example.androiderp.tools;

import android.util.Log;

import com.example.androiderp.bean.AcivityPostBean;
import com.example.androiderp.bean.Custom;
import com.example.androiderp.bean.HttpPostBean;
import com.example.androiderp.bean.PostProductData;
import com.example.androiderp.bean.PostUserData;
import com.example.androiderp.bean.ReceiveData;
import com.example.androiderp.bean.ReceiveParamet;
import com.google.gson.Gson;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.FormBody;

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

    public  static void sendOkHttpRequst(String reqName, ReceiveParamet paramet, okhttp3.Callback callback) {
        String address ="http://192.168.1.102:8000/bbs/"+ reqName;
        Gson gson = new Gson();
        String json = gson.toJson(paramet);
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, HttpAes.encode(json));
        Request request = new Request.Builder().url(address).post(body).build();
        client.newCall(request).enqueue(callback);

        Log.d("lingtanaa",json);

    }

    public  static void sendOkHttpRequst(String reqName, ReceiveData receiveData, okhttp3.Callback callback) {
        String address ="http://192.168.1.102:8000/bbs/"+ reqName;
        Gson gson = new Gson();
        String json = gson.toJson(receiveData);
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, HttpAes.encode(json));
        Request request = new Request.Builder().url(address).post(body).build();
        client.newCall(request).enqueue(callback);

        Log.d("lingtanaa",json);

    }

    public  static void sendOkHttpRequst(String reqName,int id, String tableName,okhttp3.Callback callback) {
        String address ="http://192.168.1.100:8000/bbs/"+ reqName;
        Gson gson = new Gson();
        Log.d("lingtanaaa",address);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("id",String.valueOf(id))//顺序要与服务端的类属性顺序一致
                .add("tableName",tableName)
                .build();
        Request request = new Request.Builder().url(address).post(body).build();
        client.newCall(request).enqueue(callback);
        Log.d("lingtanaaa",body.toString());




    }



    public  static void sendOkHttpRequst(HttpPostBean httpPostBean, okhttp3.Callback callback) {
        String address = httpPostBean.getServer() + httpPostBean.getServlet();
        Gson gson = new Gson();
        String json = gson.toJson(httpPostBean);
        Log.d("lingtanaa", json);
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



    public  static void sendOkHttpRequst(Custom custom, HttpPostBean acivityPostBean, okhttp3.Callback callback) {
        String address = acivityPostBean.getServer() +acivityPostBean.getServlet();
        Gson gson = new Gson();
        String customJson = gson.toJson(custom);
        String acivityPostBeanJson = gson.toJson(acivityPostBean);


        JSONObject json = new JSONObject();
        try {
            json.put("custom",customJson);
            json.put("httppostbean", acivityPostBeanJson);
        }catch (Exception e)
        {

        }

        Log.d("lingtanaa", customJson);
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, HttpAes.encode(json.toString()));
        Request request = new Request.Builder().url(address).post(body).build();
        client.newCall(request).enqueue(callback);




    }

}
