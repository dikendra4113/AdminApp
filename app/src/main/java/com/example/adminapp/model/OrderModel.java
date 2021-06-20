package com.example.adminapp.model;



public class OrderModel {
    private String catagary;
    private String description;
    private String date;
    private String pname;
    private String price;
    private String quantity;
    private String time;
    private String seatNo;
    private String user_contact;
    private String pid;
    private String paid;
    private String status ;
    private String restaurant_name ;


    public OrderModel(){

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCatagary() {
        return catagary;
    }

    public void setCatagary(String catagary) {
        this.catagary = catagary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public OrderModel(String paid) {
        this.paid = paid;
    }

    public String getPaid() {
        return paid;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public OrderModel(String catagary, String description, String date, String pname, String price, String quantity, String time, String seatNo, String user_contact, String pid, String paid, String status) {
        this.catagary = catagary;
        this.description = description;
        this.date = date;
        this.pname = pname;
        this.price = price;
        this.quantity = quantity;
        this.time = time;
        this.seatNo = seatNo;
        this.user_contact = user_contact;
        this.pid = pid;
        this.paid = paid;
        this.status = status;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public String getUser_contact() {
        return user_contact;
    }

    public void setUser_contact(String user_contact) {
        this.user_contact = user_contact;
    }


}
