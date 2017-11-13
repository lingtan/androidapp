package com.example.androiderp.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lingtan on 2017/10/30.
 */

public class HttpPostBean implements Parcelable {
    private  int  id;
    private  String name;
    private int classType;
    private String servlet;
    private  String operation;
    private  String server;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getClassType() {
        return classType;
    }

    public void setClassType(int classType) {
        this.classType = classType;
    }

    public String getServlet() {
        return servlet;
    }

    public void setServlet(String servlet) {
        this.servlet = servlet;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }


    public HttpPostBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.classType);
        dest.writeString(this.servlet);
        dest.writeString(this.operation);
        dest.writeString(this.server);
    }

    protected HttpPostBean(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.classType = in.readInt();
        this.servlet = in.readString();
        this.operation = in.readString();
        this.server = in.readString();
    }

    public static final Creator<HttpPostBean> CREATOR = new Creator<HttpPostBean>() {
        @Override
        public HttpPostBean createFromParcel(Parcel source) {
            return new HttpPostBean(source);
        }

        @Override
        public HttpPostBean[] newArray(int size) {
            return new HttpPostBean[size];
        }
    };
}
