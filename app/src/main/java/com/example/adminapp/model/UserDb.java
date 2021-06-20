package com.example.adminapp.model;

public class UserDb {
    private String restaurant_phone,password,restaurant_owner,email;

    public UserDb(){

    }

    public String getRestaurant_phone() {
        return restaurant_phone;
    }

    public void setRestaurant_phone(String restaurant_phone) {
        this.restaurant_phone = restaurant_phone;
    }

    public String getRestaurant_owner() {
        return restaurant_owner;
    }

    public void setRestaurant_owner(String restaurant_owner) {
        this.restaurant_owner = restaurant_owner;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserDb(String restaurant_phone, String password, String restaurant_owner, String email) {
        this.restaurant_phone = restaurant_phone;
        this.password = password;
        this.restaurant_owner = restaurant_owner;
        this.email = email;
    }
}
