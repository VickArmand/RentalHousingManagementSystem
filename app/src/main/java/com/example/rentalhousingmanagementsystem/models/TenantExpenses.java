package com.example.rentalhousingmanagementsystem.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TenantExpenses {
    private final String [] fields = {"category", "description", "payer", "frequency", "amount", "tenant_id", "deadline", "created_at", "created_by", "updated_at", "updated_by"};
    private String category, payer, tenant_id, description, frequency, deadline, created_at, created_by, updated_at, updated_by;
    private int amount;
    public TenantExpenses(String category, String payer, int amount, String tenant_id, String description, String frequency, String deadline, String created_by, String updated_by)
    {
        this.category = category;
        this.amount = amount;
        this.tenant_id = tenant_id;
        this.description = description;
        this.frequency = frequency;
        this.deadline = deadline;
        this.payer = payer;
        this.created_by = created_by;
        this.updated_by = updated_by;
        this.created_at = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        this.updated_at = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
    }

    public String getDeadline() {
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

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
    public void setUpdated_at() {
        this.updated_at =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
    }
}
