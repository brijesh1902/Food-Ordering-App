package com.bpal.myapplication.DBModel;

public class DBFood {

    private String fname, fprice, fdiscount, fdescription, pic, uid;

    public DBFood() {

    }

    public DBFood(String Name, String price, String disc, String desc, String imagelink, String key) {
        this.fname=Name;
        this.fprice=price;
        this.fdiscount=disc;
        this.fdescription=desc;
        this.pic=imagelink;
        this.uid=key;
    }



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFprice() {
        return fprice;
    }

    public void setFprice(String fprice) {
        this.fprice = fprice;
    }

    public String getFdiscount() {
        return fdiscount;
    }

    public void setFdiscount(String fdiscount) {
        this.fdiscount = fdiscount;
    }

    public String getFdescription() {
        return fdescription;
    }

    public void setFdescription(String fdescription) {
        this.fdescription = fdescription;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

}
