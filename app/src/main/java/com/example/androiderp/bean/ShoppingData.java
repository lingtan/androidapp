package com.example.androiderp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lingtan on 2017/5/15.
 */

public class ShoppingData implements Parcelable {
    private List<ProductShopping> productShoppingList;

    public List<ProductShopping> getProductShoppingList() {
        return productShoppingList;
    }

    public void setProductShoppingList(List<ProductShopping> productShoppingList) {
        this.productShoppingList = productShoppingList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeList(productShoppingList);
    }
    public static final Creator<ShoppingData> CREATOR=new Creator<ShoppingData>(){
        @Override
        public ShoppingData createFromParcel(Parcel source) {
            ShoppingData productShopping=new ShoppingData();
            productShopping.productShoppingList =new ArrayList();
            source.readList(productShopping.productShoppingList,getClass().getClassLoader());
            return productShopping;
        }

        @Override
        public ShoppingData[] newArray(int size) {
            return new ShoppingData[size];
        }
    };
}
