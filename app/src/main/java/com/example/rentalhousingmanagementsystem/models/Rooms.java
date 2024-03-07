package com.example.rentalhousingmanagementsystem.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Rooms {
    private String name, description, status, rental_id, created_at, created_by, updated_at, updated_by;
    private int cost, max_tenants;
    public Rooms(String name, int cost, String description, int max_tenants, String status, String rental_id, String created_by, String updated_by)
    {
        this.name = name;
        this.cost = cost;
        this.description = description;
        this.max_tenants = max_tenants;
        this.status = status;
        this.rental_id = rental_id;
        this.created_at = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        this.updated_at = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        this.created_by = created_by;
        this.updated_by = updated_by;
    }

    public String getName() {
        return name;
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

    public void setUpdated_at() {
        this.updated_at = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
