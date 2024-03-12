package com.example.rentalhousingmanagementsystem.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Rooms {
    private String id, name, description, status, rental_id, created_by, updated_by;
    private int cost, max_tenants;
    private final String format = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private Date created_at, updated_at;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(format);
    public Rooms(String name, int cost, String description, int max_tenants, String status, String rental_id, String created_by, String updated_by) throws ParseException {
        this.name = name;
        this.cost = cost;
        this.description = description;
        this.max_tenants = max_tenants;
        this.status = status;
        this.rental_id = rental_id;
        this.created_at = dateFormat.parse(dateFormat.format(new Date()));
        this.updated_at = dateFormat.parse(dateFormat.format(new Date()));
        this.created_by = created_by;
        this.updated_by = updated_by;
    }

    public String getName() {
        return name;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public String getCreated_by() {
        return created_by;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMax_tenants(int max_tenants) {
        this.max_tenants = max_tenants;
    }

    public int getCost() {
        return cost;
    }

    public String getDescription() {
        return description;
    }

    public int getMax_tenants() {
        return max_tenants;
    }

    public String getRental_id() {
        return rental_id;
    }

    public String getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setUpdated_at() throws ParseException {
        this.updated_at = dateFormat.parse(dateFormat.format(new Date()));
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
