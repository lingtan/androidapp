package com.example.androiderp.bean;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by lingtan on 2017/5/15.
 */

public class StockInitiTem implements Parcelable {
    private int id;
    private String name;
    private double quantity;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(quantity);
        dest.writeInt(id);
    }
    public static final Creator<StockInitiTem> CREATOR=new Creator<StockInitiTem>(){
        @Override
        public StockInitiTem createFromParcel(Parcel source) {
            StockInitiTem productShopping=new StockInitiTem();
            productShopping.name =source.readString();
            productShopping.quantity =source.readDouble();
            productShopping.id=source.readInt();

            return productShopping;
        }

        @Override
        public StockInitiTem[] newArray(int size) {
            return new StockInitiTem[0];
        }
    };
}
