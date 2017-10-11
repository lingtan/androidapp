package com.example.androiderp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lingtan on 2017/5/15.
 */

public class StockInitiData implements Parcelable {
    private List<StockInitiTem> stockInitiTemList;

    public List<StockInitiTem> getShoppingdata() {
        return stockInitiTemList;
    }

    public void setShoppingdata(List<StockInitiTem> shoppingdata) {
        this.stockInitiTemList = shoppingdata;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeList(stockInitiTemList);
    }
    public static final Creator<StockInitiData> CREATOR=new Creator<StockInitiData>(){
        @Override
        public StockInitiData createFromParcel(Parcel source) {
            StockInitiData productShopping=new StockInitiData();
            productShopping.stockInitiTemList =new ArrayList();
            source.readList(productShopping.stockInitiTemList,getClass().getClassLoader());
            return productShopping;
        }

        @Override
        public StockInitiData[] newArray(int size) {
            return new StockInitiData[size];
        }
    };
}
