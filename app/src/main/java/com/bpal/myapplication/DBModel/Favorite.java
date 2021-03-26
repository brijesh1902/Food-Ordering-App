package com.bpal.myapplication.DBModel;

public class Favorite {

    private String id, fname, fprice, fdiscount, fdescription, pic, userphone;

    public Favorite() {

    }

    public Favorite(String id, String fname, String fprice, String fdiscount, String fdescription, String pic, String userphone) {
        this.id = id;
        this.fname = fname;
        this.fprice = fprice;
        this.fdiscount = fdiscount;
        this.fdescription = fdescription;
        this.pic = pic;
        this.userphone = userphone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

}
