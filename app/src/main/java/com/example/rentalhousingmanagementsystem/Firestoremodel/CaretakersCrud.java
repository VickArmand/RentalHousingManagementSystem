package com.example.rentalhousingmanagementsystem.Firestoremodel;

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

import java.util.ArrayList;
import java.util.HashMap;

public class CaretakersCrud extends DbConn{
    private final String collectionName = "Caretakers";
    private final Context context;
    private final String [] fields = {"first_name", "last_name", "national_ID", "email", "contact", "emergency_contact", "status", "created_at", "created_by", "updated_at", "updated_by"};
    private final String [] uniqueFields = {"first_name", "last_name", "national_ID", "email", "contact"};
    private final String [] status = {"Active", "Inactive"};
    public CaretakersCrud(Context context)
    {
        this.context = context;
    }
    public void RegisterCaretaker(HashMap<String, String> data){
        if (CaretakerExists(data))
            Toast.makeText(context, "Caretaker Exists", Toast.LENGTH_SHORT).show();
        else {
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
    }
    public ArrayList<DocumentSnapshot> AllCaretakers(String Rental_id){
        ArrayList<DocumentSnapshot> data = new ArrayList<DocumentSnapshot>();
        Task<QuerySnapshot> caretakers = db.collection(collectionName).whereEqualTo("rental_id", Rental_id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    data.addAll(task.getResult().getDocuments());
                }
            }
        });
        return data;
    }
    public HashMap<String, Object> GetCaretaker(String documentID)
    {
        HashMap<String, Object> data = new HashMap<>();
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
    public Boolean CaretakerExists (HashMap<String, String> data){
        final boolean[] recordExists = {false};
        Task<QuerySnapshot> caretakers = db.collection(collectionName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (recordExists[0])
                            break;
                        else {
                            if (document.get(uniqueFields[0]) == data.get(uniqueFields[0]) && document.get(uniqueFields[1]) == data.get(uniqueFields[1]))
                                recordExists[0] = true;
                            for (int i = 2; i < uniqueFields.length; i++) {
                                if (document.get(uniqueFields[i]) == data.get(uniqueFields[i]))
                                    recordExists[0] = true;
                            }
                        }
                    }
                }
            }
        });
        return recordExists[0];
    }
    public void UpdateCaretaker(HashMap<String, String> data, String documentID)
    {
        DocumentReference caretaker = db.collection(collectionName).document(documentID);
        if (caretaker.get().getResult().exists()) {
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
        caretaker.update("status", status[1]).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void avoid) {
                Toast.makeText(context, collectionName + " deleted successfully", Toast.LENGTH_LONG).show();
            }
        });
    }
}
