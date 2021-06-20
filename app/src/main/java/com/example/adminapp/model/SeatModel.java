package com.example.adminapp.model;

public class SeatModel {
    private String availability;
    private String tableNo;
    public SeatModel() {

    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public SeatModel(String availability, String tableNo) {
        this.availability = availability;
        this.tableNo = tableNo;
    }
}
