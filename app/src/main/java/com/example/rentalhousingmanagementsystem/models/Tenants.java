package com.example.rentalhousingmanagementsystem.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tenants {
    private String id, firstName, lastName, nationalID, gender, email, contact, emergencyContact, room_id, rental_id, status, created_by, updated_by;

    private final String format = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private Date created_at, updated_at;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(format);
    public Tenants(String firstName, String lastName, String gender, String nationalID, String email, String contact, String emergencyContact, String room_id, String rental_id, String status, String created_by, String updated_by) throws ParseException {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationalID = nationalID;
        this.email = email;
        this.contact = contact;
        this.gender = gender;
        this.emergencyContact = emergencyContact;
        this.room_id = room_id;
        this.rental_id = rental_id;
        this.status = status;
        this.created_by = created_by;
        this.updated_by = updated_by;
        this.created_at = dateFormat.parse(dateFormat.format(new Date()));
        this.updated_at = dateFormat.parse(dateFormat.format(new Date()));
    }

    public String getId() {
        return id;
    }
    public String getRental_id() {
        return rental_id;
    }

    public void setRental_id(String rental_id) {
        this.rental_id = rental_id;
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

    public String getGender() {
        return gender;
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

    public void setUpdated_at() throws ParseException {
        this.updated_at = dateFormat.parse(dateFormat.format(new Date()));
    }

    public Date getCreated_at() {
        return created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setId(String id) {
        this.id = id;
    }
}
