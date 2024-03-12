package com.example.rentalhousingmanagementsystem.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transactions {
    private String id, category, tenant_id, evidence, paymentMode, rental_id, room_id, status, created_by, updated_by;
    private int credit, debit;
    private final String format = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private Date created_at, updated_at, payment_date;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(format);
    public Transactions(String category, String rental_id, String room_id, int credit, int debit, String paymentMode, String evidence, String tenant_id, String status, String created_by, String updated_by, Date payment_date) throws ParseException {
        this.tenant_id = tenant_id;
        this.credit = credit;
        this.debit = debit;
        this.paymentMode = paymentMode;
        this.evidence = evidence;
        this.rental_id = rental_id;
        this.room_id = room_id;
        this.status = status;
        this.created_by = created_by;
        this.updated_by = updated_by;
        this.payment_date = payment_date;
        this.category = category;
        this.created_at = dateFormat.parse(dateFormat.format(new Date()));
        this.updated_at = dateFormat.parse(dateFormat.format(new Date()));
    }

    public String getTenant_id() {
        return tenant_id;
    }

    public String getRental_id() {
        return rental_id;
    }

    public void setRental_id(String rental_id) {
        this.rental_id = rental_id;
    }

    public String getCategory() {
        return category;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public void setPayment_date(Date payment_date) {
        this.payment_date = payment_date;
    }

    public void setDebit(int debit) {
        this.debit = debit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTenant_id(String tenant_id) {
        this.tenant_id = tenant_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getRoom_id() {
        return room_id;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreated_at() {
        return created_at;
    }
    public int getCredit() {
        return credit;
    }

    public int getDebit() {
        return debit;
    }

    public String getCreated_by() {
        return created_by;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public Date getPayment_date() {
        return payment_date;
    }
    public void setUpdated_at() throws ParseException {
        this.updated_at = dateFormat.parse(dateFormat.format(new Date()));
    }

    public Date getUpdated_at() {
        return updated_at;
    }
}
