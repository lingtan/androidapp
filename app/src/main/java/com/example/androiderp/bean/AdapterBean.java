package com.example.androiderp.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class AdapterBean implements Parcelable{


    private int id;
    private int contact_id;
    private String name;
    private String address;
    private String phone;
    private String fax;
    private String category_name;
    private  String note;
    private int selectImage;
    private  String operationType;
    private String badge;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.contact_id);
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeString(this.phone);
        dest.writeString(this.fax);
        dest.writeString(this.category_name);
        dest.writeString(this.note);
        dest.writeInt(this.selectImage);
        dest.writeString(this.operationType);
        dest.writeString(this.badge);
    }

    public AdapterBean() {
    }

    protected AdapterBean(Parcel in) {
        this.id = in.readInt();
        this.contact_id = in.readInt();
        this.name = in.readString();
        this.address = in.readString();
        this.phone = in.readString();
        this.fax = in.readString();
        this.category_name = in.readString();
        this.note = in.readString();
        this.selectImage = in.readInt();
        this.operationType = in.readString();
        this.badge = in.readString();
    }

    public static final Creator<AdapterBean> CREATOR = new Creator<AdapterBean>() {
        @Override
        public AdapterBean createFromParcel(Parcel source) {
            return new AdapterBean(source);
        }

        @Override
        public AdapterBean[] newArray(int size) {
            return new AdapterBean[size];
        }
    };
}

