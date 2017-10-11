package com.example.androiderp.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lingtan on 2017/5/8.
 */

public class User  implements Parcelable{

    private String name;
    private String password;
    private String  address;
    private String phone;

    public String getName()
    {
        return name;
    }
    public void  setName(String name)
    {
        this.name=name;

    }
    public String getPassword()
    {
        return password;
    }
    public void  setPassword(String password)
    {
        this.password=password;

    }

    public String getAddress()
    {
        return address;
    }
    public void  setAddress(String address)
    {
        this.address=address;

    }
    public String getPhone()
    {
        return phone;
    }
    public void  setPhone(String phone)
    {
        this.phone=phone;

    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(password);
        dest.writeString(address);
        dest.writeString(phone);

    }

    public static final Parcelable.Creator<User> CREATOR=new Parcelable.Creator<User>()
    {
        @Override
        public User createFromParcel(Parcel source) {
            User user=new User();
            user.name=source.readString();
            user.password=source.readString();
            user.address=source.readString();
            user.phone=source.readString();
            return  user;
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
