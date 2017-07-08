package com.example.androiderp.CustomDataClass;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lingtan on 2017/5/15.
 */

public class StockInitiData implements Parcelable {
    private List<StockInitiTem> stockinitidata;

    public List<StockInitiTem> getShoppingdata() {
        return stockinitidata;
    }

    public void setShoppingdata(List<StockInitiTem> shoppingdata) {
        this.stockinitidata = shoppingdata;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeList(stockinitidata);
    }
    public static final Creator<StockInitiData> CREATOR=new Creator<StockInitiData>(){
        @Override
        public StockInitiData createFromParcel(Parcel source) {
            StockInitiData productShopping=new StockInitiData();
            productShopping.stockinitidata=new ArrayList();
            source.readList(productShopping.stockinitidata,getClass().getClassLoader());
            return productShopping;
        }

        @Override
        public StockInitiData[] newArray(int size) {
            return new StockInitiData[size];
        }
    };
}
