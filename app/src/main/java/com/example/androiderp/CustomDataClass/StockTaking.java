package com.example.androiderp.CustomDataClass;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingtan on 2017/5/15.
 */

public class StockTaking extends DataSupport {
    private int id;
    private String date;
    private String nuber;
    private String stock;
    private String note;

    private List<StockTakingEnty> stockTakingEntyList=new ArrayList<StockTakingEnty>();

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

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<StockTakingEnty> getStockTakingEntyList() {
        return stockTakingEntyList;
    }

    public void setStockTakingEntyList(List<StockTakingEnty> stockTakingEntyList) {
        this.stockTakingEntyList = stockTakingEntyList;
    }
}
