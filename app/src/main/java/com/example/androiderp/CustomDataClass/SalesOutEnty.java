package com.example.androiderp.CustomDataClass;

import org.litepal.crud.DataSupport;

/**
 * Created by lingtan on 2017/5/15.
 */

public class SalesOutEnty extends DataSupport {
    private int id;
    private String itemname;
    private String itemnumber;
    private String itemcategory;
    private double itemprice;
    private int itemfqty;
    private double itemamount;
    private SalesOut salesOut;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemnumber() {
        return itemnumber;
    }

    public void setItemnumber(String itemnumber) {
        this.itemnumber = itemnumber;
    }

    public String getItemcategory() {
        return itemcategory;
    }

    public void setItemcategory(String itemcategory) {
        this.itemcategory = itemcategory;
    }

    public double getItemprice() {
        return itemprice;
    }

    public void setItemprice(double itemprice) {
        this.itemprice = itemprice;
    }

    public int getItemfqty() {
        return itemfqty;
    }

    public void setItemfqty(int itemfqty) {
        this.itemfqty = itemfqty;
    }

    public double getItemamount() {
        return itemamount;
    }

    public void setItemamount(double itemamount) {
        this.itemamount = itemamount;
    }

    public SalesOut getSalesOut() {
        return salesOut;
    }

    public void setSalesOut(SalesOut salesOut) {
        this.salesOut = salesOut;
    }
}
