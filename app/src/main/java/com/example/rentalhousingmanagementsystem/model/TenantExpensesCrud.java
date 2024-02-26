package com.example.rentalhousingmanagementsystem.model;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class TenantExpensesCrud extends DbConn{
    Task<QuerySnapshot> RentalExpenses;
    private final String collectionName = "TenantsRentalExpenses";
    private final String [] fields = {"category", "description", "payer", "frequency", "amount", "tenant_id", "deadline", "created_at", "created_by", "updated_at", "updated_by"};
    public void RegisterRentalExpense(HashMap data){
        db.collection(collectionName).add(data);
    }
    public Task<QuerySnapshot> AllRentalExpenses(){
        RentalExpenses = db.collection(collectionName).get();
        return RentalExpenses;
    }
}
