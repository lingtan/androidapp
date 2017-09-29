package com.example.androiderp.adaper;

import android.os.Parcel;
import android.os.Parcelable;

public class CommonAdapterData implements Parcelable{


    private int unitId;
    private String name;
    private String number;
    private  String note;
    private double fqty;
    private double saleamount;
    private double salesprice;
    private int image;
    private int selectImage;
    private String category;
    private String badge;

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

        dest.writeInt(unitId);
        dest.writeString(name);
        dest.writeString(number);
        dest.writeString(note);
        dest.writeDouble(fqty);
        dest.writeDouble(saleamount);
        dest.writeDouble(salesprice);
        dest.writeInt(image);
        dest.writeInt(selectImage);
        dest.writeString(category);
        dest.writeString(badge);




    }
    public static final Parcelable.Creator<CommonAdapterData> CREATOR=new Parcelable.Creator<CommonAdapterData>(){
        @Override
        public CommonAdapterData createFromParcel(Parcel source) {
            CommonAdapterData commonAdapterData=new CommonAdapterData();
            commonAdapterData.unitId =source.readInt();
            commonAdapterData.name=source.readString();
            commonAdapterData.number=source.readString();
            commonAdapterData.note=source.readString();
            commonAdapterData.fqty=source.readDouble();
            commonAdapterData.saleamount=source.readDouble();
            commonAdapterData.salesprice=source.readDouble();
            commonAdapterData.image =source.readInt();
            commonAdapterData.selectImage =source.readInt();
            commonAdapterData.category=source.readString();
            commonAdapterData.badge=source.readString();


            return commonAdapterData;
        }

        @Override
        public CommonAdapterData[] newArray(int size) {
            return new CommonAdapterData[size];
        }
    };
}
