package com.example.androiderp.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class AdapterBean implements Parcelable{


    private int unitId;
    private int contact_id;
    private String name;
    private String address;
    private String phone;
    private String fax;
    private int category_id;
    private String category_name;
    private  String note;
    private int selectImage;
    private  String operationType;



    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }
    public int getContact_id() {
        return contact_id;
    }

    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
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

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(unitId);
        dest.writeInt(contact_id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(phone);
        dest.writeString(fax);
        dest.writeInt(category_id);
        dest.writeString(category_name);
        dest.writeString(note);
        dest.writeInt(selectImage);
        dest.writeString(operationType);





    }
    public static final Creator<AdapterBean> CREATOR=new Creator<AdapterBean>(){
        @Override
        public AdapterBean createFromParcel(Parcel source) {
            AdapterBean commonAdapterData=new AdapterBean();
            commonAdapterData.unitId =source.readInt();
            commonAdapterData.contact_id =source.readInt();
            commonAdapterData.name=source.readString();
            commonAdapterData.address=source.readString();
            commonAdapterData.phone=source.readString();
            commonAdapterData.fax=source.readString();
            commonAdapterData.category_id =source.readInt();
            commonAdapterData.category_name=source.readString();
            commonAdapterData.note=source.readString();
            commonAdapterData.selectImage =source.readInt();
            commonAdapterData.operationType=source.readString();

            return commonAdapterData;
        }

        @Override
        public AdapterBean[] newArray(int size) {
            return new AdapterBean[size];
        }
    };
}
