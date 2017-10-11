package com.example.androiderp.bean;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by lingtan on 2017/5/15.
 */

public class ProductShopping implements Parcelable {
    private int id;
    private String name;
    private String number;
    private String category;
    private double price;
    private double quantity;
    private double amount;

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(number);
        dest.writeString(category);
        dest.writeDouble(price);
        dest.writeDouble(quantity);
        dest.writeInt(id);
        dest.writeDouble(amount);
    }
    public static final Parcelable.Creator<ProductShopping> CREATOR=new Parcelable.Creator<ProductShopping>(){
        @Override
        public ProductShopping createFromParcel(Parcel source) {
            ProductShopping productShopping=new ProductShopping();
            productShopping.name =source.readString();
            productShopping.number =source.readString();
            productShopping.category=source.readString();
            productShopping.price =source.readDouble();
            productShopping.quantity =source.readDouble();
            productShopping.id=source.readInt();
            productShopping.amount =source.readDouble();
            return productShopping;
        }

        @Override
        public ProductShopping[] newArray(int size) {
            return new ProductShopping[0];
        }
    };
}
