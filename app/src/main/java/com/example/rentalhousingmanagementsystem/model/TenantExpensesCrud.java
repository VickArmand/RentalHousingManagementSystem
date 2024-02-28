package com.example.rentalhousingmanagementsystem.model;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class TenantExpensesCrud extends DbConn{
    private final String collectionName = "TenantsRentalExpenses";
    private final String [] fields = {"category", "description", "payer", "frequency", "amount", "tenant_id", "deadline", "created_at", "created_by", "updated_at", "updated_by"};
    private Context context;
    public void RegisterTenantExpense(HashMap data){
        db.collection(collectionName).add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(context, collectionName + " registered successfully", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,  "Error registering " + collectionName, Toast.LENGTH_LONG).show();
            }
        });
    }
    public HashMap AllTenantExpenses(){
        HashMap data = new HashMap();
        Task<QuerySnapshot> tenantExpenses = db.collection(collectionName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        data.put(document.getId(), document.getData());
                    }
                }
            }
        });
        return data;
    }
    public HashMap GetTenantExpense(String documentID)
    {
        HashMap data = new HashMap();
        DocumentReference tenantExpense = db.collection(collectionName).document(documentID);
        tenantExpense.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        for (String f : fields) {
                            data.put(f, doc.get(f));
                        }
                    }
                }
            }
        });
        return data;
    }
    public void UpdateTenantExpense(HashMap data, String documentID)
    {
        DocumentReference tenantExpense = db.collection(collectionName).document(documentID);
        if (tenantExpense != null) {
            for (String f : fields) {
                if (data.get(f) != null)
                    tenantExpense.update(f, data.get(f));
            }
        }
        Toast.makeText(context, collectionName + " updated successfully", Toast.LENGTH_LONG).show();
    }
    public void DeleteTenantExpense(String documentID)
    {
        DocumentReference tenantExpense = db.collection(collectionName).document(documentID);
        tenantExpense.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void avoid) {
                Toast.makeText(context, collectionName + " deleted successfully", Toast.LENGTH_LONG).show();
            }
        });
    }
}
