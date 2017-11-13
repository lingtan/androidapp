package com.example.androiderp.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lingtan on 2017/10/29.
 */

public class ContactBean implements Parcelable {

    private Integer contact_id;
    private String name;
    private String address;
    private String phone;
    private String fax;

    public Integer getContact_id() {
        return contact_id;
    }

    public void setContact_id(Integer contact_id) {
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



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.contact_id);
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeString(this.phone);
        dest.writeString(this.fax);

    }

    public ContactBean() {
    }

    protected ContactBean(Parcel in) {
        this.contact_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.address = in.readString();
        this.phone = in.readString();
        this.fax = in.readString();

    }

    public static final Parcelable.Creator<ContactBean> CREATOR = new Parcelable.Creator<ContactBean>() {
        @Override
        public ContactBean createFromParcel(Parcel source) {
            return new ContactBean(source);
        }

        @Override
        public ContactBean[] newArray(int size) {
            return new ContactBean[size];
        }
    };
}
