package com.example.androiderp.CustomDataClass;

import org.litepal.crud.DataSupport;

/**
 * Created by lingtan on 2017/5/15.
 */

public class AppropriationEnty extends DataSupport {
    private int id;
    private String itemname;
    private String itemnumber;
    private double itemfqty;
    private String instock;
    private String outstock;


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

    public double getItemfqty() {
        return itemfqty;
    }

    public void setItemfqty(double itemfqty) {
        this.itemfqty = itemfqty;
    }

    public String getInstock() {
        return instock;
    }

    public void setInstock(String instock) {
        this.instock = instock;
    }

    public String getOutstock() {
        return outstock;
    }

    public void setOutstock(String outstock) {
        this.outstock = outstock;
    }
}
