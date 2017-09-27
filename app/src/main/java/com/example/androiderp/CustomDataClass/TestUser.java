package com.example.androiderp.CustomDataClass;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by lingtan on 2017/9/18.
 */

public class TestUser implements Parcelable{
    private int unitId;
    private  String name;
    private  String note;
    private  String original;
    private  String requestType;
    private  String serverIp;
    private  String servlet;

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServlet() {
        return servlet;
    }

    public void setServlet(String servlet) {
        this.servlet = servlet;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(unitId);
        dest.writeString(name);
        dest.writeString(note);
        dest.writeString(original);
        dest.writeString(requestType);
        dest.writeString(serverIp);
        dest.writeString(servlet);
    }
    public static final Parcelable.Creator<TestUser> CREATOR=new Parcelable.Creator<TestUser>(){
        @Override
        public TestUser createFromParcel(Parcel source) {
            TestUser testUser=new TestUser();
            testUser.unitId=source.readInt();
            testUser.name=source.readString();
            testUser.note=source.readString();
            testUser.original=source.readString();
            testUser.requestType=source.readString();
            testUser.serverIp=source.readString();
            testUser.serverIp=source.readString();


            return testUser;
        }

        @Override
        public TestUser[] newArray(int size) {
            return new TestUser[size];
        }
    };
}
