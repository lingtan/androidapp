package com.example.androiderp.CustomDataClass;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lingtan on 2017/5/15.
 */

public class SalesOut extends DataSupport {
    private int id;
    private String customer;
    private String Fdate;
    private String nuber;
    private double saleamount;
    private int    salefqty;
    private String stock;
    private String consignment;
    private String salesman;
    private String note;
    private String billtype;
    private List<SalesOutEnty> salesOutEntyList=new ArrayList<SalesOutEnty>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
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

    public double getSaleamount() {
        return saleamount;
    }

    public void setSaleamount(double saleamount) {
        this.saleamount = saleamount;
    }

    public int getSalefqty() {
        return salefqty;
    }

    public void setSalefqty(int salefqty) {
        this.salefqty = salefqty;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getConsignment() {
        return consignment;
    }

    public void setConsignment(String consignment) {
        this.consignment = consignment;
    }

    public String getSalesman() {
        return salesman;
    }

    public void setSalesman(String salesman) {
        this.salesman = salesman;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getBilltype() {
        return billtype;
    }

    public void setBilltype(String billtype) {
        this.billtype = billtype;
    }

    public List<SalesOutEnty> getSalesOutEntyList() {
        return salesOutEntyList;
    }

    public void setSalesOutEntyList(List<SalesOutEnty> salesOutEntyList) {
        this.salesOutEntyList = salesOutEntyList;
    }
}
