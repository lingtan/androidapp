package com.example.androiderp.CustomDataClass;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by lingtan on 2017/5/15.
 */

public class ProductShopping implements Parcelable {
    private String salename;
    private String salenumber;
    private String category;
    private double salesprice;
    private int salefqty;
    private double saleamount;

    public String getSalename() {
        return salename;
    }

    public void setSalename(String salename) {
        this.salename = salename;
    }

    public String getSalenumber() {
        return salenumber;
    }

    public void setSalenumber(String salenumber) {
        this.salenumber = salenumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getSalesprice() {
        return salesprice;
    }

    public void setSalesprice(double salesprice) {
        this.salesprice = salesprice;
    }

    public int getSalefqty() {
        return salefqty;
    }

    public void setSalefqty(int salefqty) {
        this.salefqty = salefqty;
    }

    public double getSaleamount() {
        return saleamount;
    }

    public void setSaleamount(double saleamount) {
        this.saleamount = saleamount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(salename);
        dest.writeString(salenumber);
        dest.writeString(category);
        dest.writeDouble(salesprice);
        dest.writeInt(salefqty);
        dest.writeDouble(saleamount);
    }
    public static final Parcelable.Creator<ProductShopping> CREATOR=new Parcelable.Creator<ProductShopping>(){
        @Override
        public ProductShopping createFromParcel(Parcel source) {
            ProductShopping productShopping=new ProductShopping();
            productShopping.salename=source.readString();
            productShopping.salenumber=source.readString();
            productShopping.category=source.readString();
            productShopping.salesprice=source.readDouble();
            productShopping.salefqty=source.readInt();
            productShopping.saleamount=source.readDouble();
            return productShopping;
        }

        @Override
        public ProductShopping[] newArray(int size) {
            return new ProductShopping[0];
        }
    };
}
