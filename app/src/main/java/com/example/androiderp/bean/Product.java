package com.example.androiderp.bean;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by lingtan on 2017/5/15.
 */

public class Product implements Parcelable {
    private Integer product_id;
    private String name;
    private String number;
    private double purchasePrice;
    private double salesPrice;
    private String barcode;
    private String  model;
    private String  note;
    private Integer category_id;
    private String category_name;
    private Integer brand_id;
    private String brand_name;
    private Integer unit_id;
    private String unit_name;
    private Integer    image;
    private double quantity;
    private String badgeShow;
    private String photoFirstPath;
    private String photoSecondPath;
    private String photoThressPath;
    private String photoMainPath;
    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
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

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public Integer getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(Integer brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public Integer getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(Integer unit_id) {
        this.unit_id = unit_id;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getBadgeShow() {
        return badgeShow;
    }

    public void setBadgeShow(String badgeShow) {
        this.badgeShow = badgeShow;
    }

    public String getPhotoFirstPath() {
        return photoFirstPath;
    }

    public void setPhotoFirstPath(String photoFirstPath) {
        this.photoFirstPath = photoFirstPath;
    }

    public String getPhotoSecondPath() {
        return photoSecondPath;
    }

    public void setPhotoSecondPath(String photoSecondPath) {
        this.photoSecondPath = photoSecondPath;
    }

    public String getPhotoThressPath() {
        return photoThressPath;
    }

    public void setPhotoThressPath(String photoThressPath) {
        this.photoThressPath = photoThressPath;
    }

    public String getPhotoMainPath() {
        return photoMainPath;
    }

    public void setPhotoMainPath(String photoMainPath) {
        this.photoMainPath = photoMainPath;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(product_id);
        dest.writeString(name);
        dest.writeString(number);
        dest.writeDouble(purchasePrice);
        dest.writeDouble(salesPrice);
        dest.writeString(barcode);
        dest.writeString(model);
        dest.writeString(note);
        dest.writeInt(category_id);
        dest.writeString(category_name);
        dest.writeInt(brand_id);
        dest.writeString(brand_name);
        dest.writeInt(unit_id);
        dest.writeString(unit_name);
        dest.writeInt(image);
        dest.writeDouble(quantity);
        dest.writeString(badgeShow);
        dest.writeString(photoFirstPath);
        dest.writeString(photoSecondPath);
        dest.writeString(photoThressPath);
        dest.writeString(photoMainPath);

    }
    public static final Parcelable.Creator<Product> CREATOR=new Parcelable.Creator<Product>(){
        @Override
        public Product createFromParcel(Parcel source) {
            Product postUserData =new Product();
            postUserData.product_id=source.readInt();
            postUserData.name=source.readString();
            postUserData.number=source.readString();
            postUserData.purchasePrice =source.readDouble();
            postUserData.salesPrice=source.readDouble();
            postUserData.barcode=source.readString();
            postUserData.model=source.readString();
            postUserData.note=source.readString();
            postUserData.category_id=source.readInt();
            postUserData.category_name=source.readString();
            postUserData.brand_id=source.readInt();
            postUserData.brand_name=source.readString();
            postUserData.unit_id=source.readInt();
            postUserData.unit_name=source.readString();
            postUserData.image=source.readInt();
            postUserData.quantity =source.readDouble();
            postUserData.badgeShow=source.readString();
            postUserData.photoFirstPath=source.readString();
            postUserData.photoSecondPath=source.readString();
            postUserData.photoThressPath=source.readString();
            postUserData.photoMainPath=source.readString();
            return postUserData;
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
