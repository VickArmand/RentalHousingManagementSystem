package com.example.rentalhousingmanagementsystem.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Owners {
    private String firstName, lastName, nationalID, email, contact, created_at, created_by, updated_at, updated_by;
    public Owners(String firstName, String lastName, String nationalID, String email, String contact, String created_by, String updated_by)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationalID = nationalID;
        this.email = email;
        this.contact = contact;
        this.created_by = created_by;
        this.updated_by = updated_by;
        this.created_at = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        this.updated_at = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
    }

    public String getFirstName() {
        return firstName;
    }

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getCreated_by() {
        return created_by;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public String getContact() {
        return contact;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setUpdated_at() {
        this.updated_at =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
    }
}
