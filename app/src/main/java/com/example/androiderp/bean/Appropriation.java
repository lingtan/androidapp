package com.example.androiderp.bean;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingtan on 2017/5/15.
 */

public class Appropriation extends DataSupport {
    private int id;
    private String date;
    private String nuber;
    private String stockIn;
    private String stockOut;
    private String note;

    private List<AppropriationEnty> salesOutEntyList=new ArrayList<AppropriationEnty>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNuber() {
        return nuber;
    }

    public void setNuber(String nuber) {
        this.nuber = nuber;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<AppropriationEnty> getSalesOutEntyList() {
        return salesOutEntyList;
    }

    public void setSalesOutEntyList(List<AppropriationEnty> salesOutEntyList) {
        this.salesOutEntyList = salesOutEntyList;
    }
}
