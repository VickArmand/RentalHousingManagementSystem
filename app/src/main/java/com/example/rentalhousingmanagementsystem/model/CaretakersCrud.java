package com.example.rentalhousingmanagementsystem.model;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class CaretakersCrud extends DbConn{
    private final String collectionName = "Caretakers";
    private Context context;
    private final String [] fields = {"first_name", "last_name", "national_ID", "email", "contact", "emergency_contact", "status", "created_at", "created_by", "updated_at", "updated_by"};
    public void RegisterCaretaker(HashMap data){
        db.collection(collectionName).add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Toast.makeText(context, collectionName + " registered successfully", Toast.LENGTH_LONG).show();
            }
        });
    }
    public HashMap AllCaretakers(){
        HashMap data = new HashMap();
        Task<QuerySnapshot> caretakers = db.collection(collectionName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
    public HashMap GetCaretaker(String documentID)
    {
        HashMap data = new HashMap();
        DocumentReference caretaker = db.collection(collectionName).document(documentID);
        caretaker.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
    public void UpdateCaretaker(HashMap data, String documentID)
    {
        DocumentReference caretaker = db.collection(collectionName).document(documentID);
        if (caretaker != null) {
            for (String f : fields) {
                if (data.get(f) != null)
                    caretaker.update(f, data.get(f));
            }
        }
        Toast.makeText(context, collectionName + " updated successfully", Toast.LENGTH_LONG).show();
    }
    public void DeleteCaretaker(String documentID)
    {
        DocumentReference caretaker = db.collection(collectionName).document(documentID);
        caretaker.update("status", "Vacated").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void avoid) {
                Toast.makeText(context, collectionName + " deleted successfully", Toast.LENGTH_LONG).show();
            }
        });
    }
}
