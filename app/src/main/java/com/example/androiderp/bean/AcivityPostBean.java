package com.example.androiderp.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class AcivityPostBean implements Parcelable{

    private  String acivityName;
    private  String actionType;
    private  String name;
    private String isSelect="";

    public String getAcivityName() {
        return acivityName;
    }

    public void setAcivityName(String acivityName) {
        this.acivityName = acivityName;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(String isSelect) {
        this.isSelect = isSelect;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.acivityName);
        dest.writeString(this.actionType);
        dest.writeString(this.name);
        dest.writeString(this.isSelect);
    }

    public AcivityPostBean() {
    }

    protected AcivityPostBean(Parcel in) {
        this.acivityName = in.readString();
        this.actionType = in.readString();
        this.name = in.readString();
        this.isSelect = in.readString();
    }

    public static final Creator<AcivityPostBean> CREATOR = new Creator<AcivityPostBean>() {
        @Override
        public AcivityPostBean createFromParcel(Parcel source) {
            return new AcivityPostBean(source);
        }

        @Override
        public AcivityPostBean[] newArray(int size) {
            return new AcivityPostBean[size];
        }
    };
}
