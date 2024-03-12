package com.example.rentalhousingmanagementsystem.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Rentals {
    private String id, name, status, created_by, updated_by;
    private final String format = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private Date created_at, updated_at;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(format);
    private int number_of_rooms;
    // new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
    public Rentals(String name, int number_of_rooms, String status, String created_by, String updated_by) throws ParseException {
        this.name = name;
        this.number_of_rooms = number_of_rooms;
        this.status = status;
        this.created_at = dateFormat.parse(dateFormat.format(new Date()));
        this.updated_at = dateFormat.parse(dateFormat.format(new Date()));
        this.created_by = created_by;
        this.updated_by = updated_by;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getNumber_of_rooms() {
        return number_of_rooms;
    }

    public String getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUpdated_at() throws ParseException {
        this.updated_at = dateFormat.parse(dateFormat.format(new Date()));
    }

    public Date getCreated_at() {
        return created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setNumber_of_rooms(int number_of_rooms) {
        this.number_of_rooms = number_of_rooms;
    }
}
