package com.example.androiderp.CustomDataClass;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by lingtan on 2017/5/15.
 */

public class StockInitiTem implements Parcelable {
    private int id;
    private String salename;
    private double salefqty;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSalename() {
        return salename;
    }

    public void setSalename(String salename) {
        this.salename = salename;
    }

    public double getSalefqty() {
        return salefqty;
    }

    public void setSalefqty(double salefqty) {
        this.salefqty = salefqty;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(salename);
        dest.writeDouble(salefqty);
        dest.writeInt(id);
    }
    public static final Creator<StockInitiTem> CREATOR=new Creator<StockInitiTem>(){
        @Override
        public StockInitiTem createFromParcel(Parcel source) {
            StockInitiTem productShopping=new StockInitiTem();
            productShopping.salename=source.readString();
            productShopping.salefqty=source.readDouble();
            productShopping.id=source.readInt();

            return productShopping;
        }

        @Override
        public StockInitiTem[] newArray(int size) {
            return new StockInitiTem[0];
        }
    };
}
