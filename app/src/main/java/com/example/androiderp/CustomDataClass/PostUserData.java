package com.example.androiderp.CustomDataClass;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lingtan on 2017/9/18.
 */

public class PostUserData implements Parcelable{
    private int unitId;
    private  String name;
    private  String note;
    private  int classType;
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


    public int getClassType() {
        return classType;
    }

    public void setClassType(int classType) {
        this.classType = classType;
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
        dest.writeInt(classType);
        dest.writeString(requestType);
        dest.writeString(serverIp);
        dest.writeString(servlet);
    }
    public static final Parcelable.Creator<PostUserData> CREATOR=new Parcelable.Creator<PostUserData>(){
        @Override
        public PostUserData createFromParcel(Parcel source) {
            PostUserData postUserData =new PostUserData();
            postUserData.unitId=source.readInt();
            postUserData.name=source.readString();
            postUserData.note=source.readString();
            postUserData.classType =source.readInt();
            postUserData.requestType=source.readString();
            postUserData.serverIp=source.readString();
            postUserData.serverIp=source.readString();


            return postUserData;
        }

        @Override
        public PostUserData[] newArray(int size) {
            return new PostUserData[size];
        }
    };
}
