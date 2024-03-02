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

public class TenantsCrud extends DbConn{

    private final String collectionName = "Tenants";
    private final Context context;
    private final String [] fields = {"first_name", "last_name", "national_ID", "email", "contact", "emergency_contact", "room_id", "status", "created_at", "created_by", "updated_at", "updated_by"};
    private final String [] uniqueFields = {"first_name", "last_name", "national_ID", "email", "contact"};
    private final String [] status = {"Available", "Vacated"};
    public TenantsCrud (Context context)
    {
        this.context = context;
    }
    public void RegisterTenant(HashMap<String, String> data){
        if (TenantExists(data))
            Toast.makeText(context.getApplicationContext(), "Tenant Exists", Toast.LENGTH_SHORT).show();
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
    public HashMap<String, DocumentSnapshot> AllTenants(){
        HashMap<String, DocumentSnapshot> data = new HashMap<String, DocumentSnapshot>();
        Task<QuerySnapshot> tenants = db.collection(collectionName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot document : task.getResult().getDocuments()) {
                        data.put(document.getId(), document);
                    }
                }
            }
        });
        return data;
    }
    public HashMap<String, Object> GetTenant(String documentID)
    {
        HashMap<String, Object> data = new HashMap<String, Object>();
        DocumentReference tenant = db.collection(collectionName).document(documentID);
        tenant.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
    public Boolean TenantExists (HashMap<String, String> data){
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
    public void UpdateTenant(HashMap<String, String> data, String documentID)
    {
        DocumentReference tenant = db.collection(collectionName).document(documentID);
        if (tenant.get().getResult().exists()) {
            for (String f : fields) {
                if (data.get(f) != null)
                    tenant.update(f, data.get(f));
            }
        }
        Toast.makeText(context, collectionName + " updated successfully", Toast.LENGTH_LONG).show();
    }
    public void DeleteTenant(String documentID)
    {
        DocumentReference tenant = db.collection(collectionName).document(documentID);
        RoomsCrud objRoom = new RoomsCrud(context);
        String room_id = (String) tenant.get().getResult().get("room_id");
        objRoom.DeleteRoom(room_id);
        tenant.update("room_id", "");
        tenant.update("status", status[1]).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void avoid) {
                Toast.makeText(context, collectionName + " deleted successfully", Toast.LENGTH_LONG).show();
            }
        });
    }
}
