package com.example.androiderp.CustomDataClass;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingtan on 2017/5/15.
 */

public class Appropriation extends DataSupport {
    private int id;
    private String Fdate;
    private String nuber;
    private String instock;
    private String outstock;
    private String note;

    private List<AppropriationEnty> salesOutEntyList=new ArrayList<AppropriationEnty>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFdate() {
        return Fdate;
    }

    public void setFdate(String fdate) {
        Fdate = fdate;
    }

    public String getNuber() {
        return nuber;
    }

    public void setNuber(String nuber) {
        this.nuber = nuber;
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
