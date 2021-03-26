package com.bpal.myapplication.DBModel;

public class Order {

    private String  phone, pname, pquantity, pprice, pdiscount, pimage;

    public Order() {

    }

    public Order(String phone, String pname, String pquantity, String pprice, String pdiscount, String pimage) {
        this.phone = phone;
        this.pname = pname;
        this.pquantity = pquantity;
        this.pprice = pprice;
        this.pdiscount = pdiscount;
        this.pimage = pimage;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPquantity() {
        return pquantity;
    }

    public void setPquantity(String pquantity) {
        this.pquantity = pquantity;
    }

    public String getPprice() {
        return pprice;
    }

    public void setPprice(String pprice) {
        this.pprice = pprice;
    }

    public String getPdiscount() {
        return pdiscount;
    }

    public void setPdiscount(String pdiscount) {
        this.pdiscount = pdiscount;
    }

    public String getPimage() {
        return pimage;
    }

    public void setPimage(String pimage) {
        this.pimage = pimage;
    }

}
