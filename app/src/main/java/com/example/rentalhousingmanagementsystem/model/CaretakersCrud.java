package com.example.rentalhousingmanagementsystem.model;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class CaretakersCrud extends DbConn{
    Task<QuerySnapshot> caretakers;
    private final String collectionName = "Caretakers";
    private final String [] fields = {"first_name", "last_name", "national_ID", "email", "contact", "emergency_contact", "status", "created_at", "created_by", "updated_at", "updated_by"};
    public void RegisterCaretaker(HashMap data){
        db.collection(collectionName).add(data);
    }
    public Task<QuerySnapshot> AllCaretakers(){
        caretakers = db.collection(collectionName).get();
        return caretakers;
    }
}
