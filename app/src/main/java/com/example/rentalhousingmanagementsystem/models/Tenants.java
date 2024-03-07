package com.example.rentalhousingmanagementsystem.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Tenants {
    private String firstName, lastName, nationalID, email, contact, emergencyContact, room_id, status, created_at, created_by, updated_at, updated_by;
    public Tenants(String firstName, String lastName, String nationalID, String email, String contact, String emergencyContact, String room_id, String status, String created_by, String updated_by)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationalID = nationalID;
        this.email = email;
        this.contact = contact;
        this.emergencyContact = emergencyContact;
        this.room_id = room_id;
        this.status = status;
        this.created_by = created_by;
        this.updated_by = updated_by;
        this.created_at = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        this.updated_at = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
    }

    public String getStatus() {
        return status;
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

    public String getRoom_id() {
        return room_id;
    }

    public String getEmergencyContact() {
        return emergencyContact;
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

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setUpdated_at() {
        this.updated_at =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
    }
}
