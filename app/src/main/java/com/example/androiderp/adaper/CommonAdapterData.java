package com.example.androiderp.adaper;

import android.os.Parcel;
import android.os.Parcelable;

public class CommonAdapterData implements Parcelable{



    private String name;
    private String number;
    private double fqty;
    private double saleamount;
    private double salesprice;
    private int image;
    private int selectImage;
    private String category;
    private String badge;
    private int id;

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

    public double getFqty() {
        return fqty;
    }

    public void setFqty(double fqty) {
        this.fqty = fqty;
    }

    public double getSaleamount() {
        return saleamount;
    }

    public void setSaleamount(double saleamount) {
        this.saleamount = saleamount;
    }

    public double getSalesprice() {
        return salesprice;
    }

    public void setSalesprice(double salesprice) {
        this.salesprice = salesprice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getSelectImage() {
        return selectImage;
    }

    public void setSelectImage(int selectImage) {
        this.selectImage = selectImage;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(name);


    }
    public static final Parcelable.Creator<CommonAdapterData> CREATOR=new Parcelable.Creator<CommonAdapterData>(){
        @Override
        public CommonAdapterData createFromParcel(Parcel source) {
            CommonAdapterData testUser=new CommonAdapterData();
            testUser.id=source.readInt();
            testUser.name=source.readString();


            return testUser;
        }

        @Override
        public CommonAdapterData[] newArray(int size) {
            return new CommonAdapterData[size];
        }
    };
}
