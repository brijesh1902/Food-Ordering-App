package com.bpal.myapplication.DBModel;

import java.util.List;

public class Request {

    private String userphone, username, address, total, status, id, paymentmethod, paymentstate;
    private List<Order> foods;

    public Request() {

    }

    public Request(String phone, String name, String address, String total, List<Order> foods, String paymentmethod, String paymentstate, String id) {
        this.userphone = phone;
        this.username = name;
        this.address = address;
        this.total = total;
        this.foods = foods;
        this.status = "0";
        this.id = id;
        this.paymentmethod = paymentmethod;
        this.paymentstate = paymentstate;
    }

    public String getPaymentstate() {
        return paymentstate;
    }

    public void setPaymentstate(String paymentstate) {
        this.paymentstate = paymentstate;
    }

    public String getPaymentmethod() {
        return paymentmethod;
    }

    public void setPaymentmethod(String paymentmethod) {
        this.paymentmethod = paymentmethod;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserphone() {
        return userphone;
    }

    public String getUsername() {
        return username;
    }

    public String getAddress() {
        return address;
    }

    public String getTotal() {
        return total;
    }

    public List<Order> getFoods() {
        return foods;
    }
}
