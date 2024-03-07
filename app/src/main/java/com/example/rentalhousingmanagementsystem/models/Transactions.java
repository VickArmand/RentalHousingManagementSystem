package com.example.rentalhousingmanagementsystem.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Transactions {
    private String tenant_id, evidence, paymentMode, room_id, status, created_at, created_by, updated_at, updated_by;
    private int credit, debit;
    public Transactions(String room_id, int credit, int debit, String paymentMode, String evidence, String tenant_id, String status, String created_by, String updated_by)
    {
        this.tenant_id = tenant_id;
        this.credit = credit;
        this.debit = debit;
        this.paymentMode = paymentMode;
        this.evidence = evidence;
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

    public String getRoom_id() {
        return room_id;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public void setUpdated_at() {
        this.updated_at =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
    }
}
