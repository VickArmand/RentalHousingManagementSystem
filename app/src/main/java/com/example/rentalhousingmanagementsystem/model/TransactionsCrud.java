package com.example.rentalhousingmanagementsystem.model;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class TransactionsCrud extends DbConn{
    Task<QuerySnapshot> transactions;
    private final String collectionName = "Transactions";
    private final String [] fields = {"room_id", "credit", "debit", "tenant_id", "payment_mode", "evidence", "status", "created_at", "created_by", "updated_at", "updated_by"};
    public void RegisterTransaction(HashMap data){
        db.collection(collectionName).add(data);
    }
    public Task<QuerySnapshot> AllTransactions(){
        transactions = db.collection(collectionName).get();
        return transactions;
    }
}
