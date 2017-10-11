package com.example.androiderp.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by lingtan on 2017/5/15.
 */

public class Product extends DataSupport {
    private int id;
    private String name;
    private String number;
    private String purchasePrice;
    private String salesPrice;
    private String barcode;
    private String  model;
    private String  note;
    private String category;
    private String brand;
    private String unit;
    private int    image;
    private double quantity;
    private String badgeShow;
    private String photoFirstPath;
    private String photoSecondPath;
    private String photoThressPath;
    private String photoMainPath;

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

    public String getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(String purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(String salesPrice) {
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
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
}
