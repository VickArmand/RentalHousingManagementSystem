package com.example.rentalhousingmanagementsystem.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TenantExpenses {
    private String id, category, payer, tenant_id, description, frequency, created_by, updated_by;
    private int amount;
    private final String format = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private Date created_at, updated_at, deadline;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(format);
    public TenantExpenses(String category, String payer, int amount, String tenant_id, String description, String frequency, Date deadline, String created_by, String updated_by) throws ParseException {
        this.category = category;
        this.amount = amount;
        this.tenant_id = tenant_id;
        this.description = description;
        this.frequency = frequency;
        this.deadline = deadline;
        this.payer = payer;
        this.created_by = created_by;
        this.updated_by = updated_by;
        this.created_at = dateFormat.parse(dateFormat.format(new Date()));
        this.updated_at = dateFormat.parse(dateFormat.format(new Date()));
    }

    public String getId() {
        return id;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDeadline() {
        return deadline;
    }

    public String getPayer() {
        return payer;
    }

    public String getCategory() {
        return category;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getDescription() {
        return description;
    }

    public int getAmount() {
        return amount;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setTenant_id(String tenant_id) {
        this.tenant_id = tenant_id;
    }

    public String getTenant_id() {
        return tenant_id;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }
    public void setUpdated_at() throws ParseException {
        this.updated_at = dateFormat.parse(dateFormat.format(new Date()));
    }
}
