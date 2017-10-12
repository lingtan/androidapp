package com.example.androiderp.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class AdapterBean implements Parcelable{


    private int unitId;
    private String name;
    private  String note;
    private int selectImage;

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

    public int getSelectImage() {
        return selectImage;
    }

    public void setSelectImage(int selectImage) {
        this.selectImage = selectImage;
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
        dest.writeInt(selectImage);




    }
    public static final Creator<AdapterBean> CREATOR=new Creator<AdapterBean>(){
        @Override
        public AdapterBean createFromParcel(Parcel source) {
            AdapterBean commonAdapterData=new AdapterBean();
            commonAdapterData.unitId =source.readInt();
            commonAdapterData.name=source.readString();
            commonAdapterData.note=source.readString();
            commonAdapterData.selectImage =source.readInt();
            return commonAdapterData;
        }

        @Override
        public AdapterBean[] newArray(int size) {
            return new AdapterBean[size];
        }
    };
}
