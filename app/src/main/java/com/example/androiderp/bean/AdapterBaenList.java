package com.example.androiderp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lingtan on 2017/5/15.
 */

public class AdapterBaenList implements Parcelable {
    private List<AdapterBean> adapterBeen;

    public List<AdapterBean> getAdapterBeen() {
        return adapterBeen;
    }

    public void setAdapterBeen(List<AdapterBean> adapterBeen) {
        this.adapterBeen = adapterBeen;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeList(adapterBeen);
    }
    public static final Creator<AdapterBaenList> CREATOR=new Creator<AdapterBaenList>(){
        @Override
        public AdapterBaenList createFromParcel(Parcel source) {
            AdapterBaenList productShopping=new AdapterBaenList();
            productShopping.adapterBeen =new ArrayList();
            source.readList(productShopping.adapterBeen,getClass().getClassLoader());
            return productShopping;
        }

        @Override
        public AdapterBaenList[] newArray(int size) {
            return new AdapterBaenList[size];
        }
    };
}
