package com.example.androiderp.CustomDataClass;

import org.litepal.crud.DataSupport;

/**
 * Created by lingtan on 2017/5/15.
 */

public class AppropriationEnty extends DataSupport {
    private int id;
    private String name;
    private String number;
    private double quantity;
    private String stockIn;
    private String stockOut;


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

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getStockIn() {
        return stockIn;
    }

    public void setStockIn(String stockIn) {
        this.stockIn = stockIn;
    }

    public String getStockOut() {
        return stockOut;
    }

    public void setStockOut(String stockOut) {
        this.stockOut = stockOut;
    }
}
