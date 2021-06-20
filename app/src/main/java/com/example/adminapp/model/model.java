package com.example.adminapp.model;

public class model {
    public String catagary;
    public String date;
    public String description;
    public String image;
    public String pid;
    public String pname;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String price;

    public model(String quantity) {
        this.quantity = quantity;
    }

    public String quantity;

    model() {

    }

    public model(String catagary, String date, String description, String image, String pid, String pname, String price) {
        this.catagary = catagary;
        this.date = date;
        this.description = description;
        this.image = image;
        this.pid = pid;
        this.pname = pname;
        this.price = price;
    }

    public String getCatagary() {
        return catagary;
    }

    public void setCatagary(String catagary) {
        this.catagary = catagary;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

