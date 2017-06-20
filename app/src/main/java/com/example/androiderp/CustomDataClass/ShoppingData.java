package com.example.androiderp.CustomDataClass;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lingtan on 2017/5/15.
 */

public class ShoppingData implements Parcelable {
    private List<ProductShopping> shoppingdata;

    public List<ProductShopping> getShoppingdata() {
        return shoppingdata;
    }

    public void setShoppingdata(List<ProductShopping> shoppingdata) {
        this.shoppingdata = shoppingdata;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeList(shoppingdata);
    }
    public static final Creator<ShoppingData> CREATOR=new Creator<ShoppingData>(){
        @Override
        public ShoppingData createFromParcel(Parcel source) {
            ShoppingData productShopping=new ShoppingData();
            productShopping.shoppingdata=new ArrayList();
            source.readList(productShopping.shoppingdata,getClass().getClassLoader());
            return productShopping;
        }

        @Override
        public ShoppingData[] newArray(int size) {
            return new ShoppingData[size];
        }
    };
}
