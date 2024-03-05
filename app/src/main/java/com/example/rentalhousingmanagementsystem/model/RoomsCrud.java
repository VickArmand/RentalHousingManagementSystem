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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class RoomsCrud extends DbConn{
    private final String [] fields = {"name", "cost", "description", "max_tenants", "status", "rental_id", "created_at", "created_by", "updated_at", "updated_by"};
    private final String [] uniqueFields = {"name", "rental_id"};
    private final String collectionName = "Rooms";
    private final Context context;
    private final String [] status = {"Occupied", "Vacant"};
    public RoomsCrud (Context context)
    {
        this.context = context;
    }
    public void RegisterRoom(HashMap<String, String>  data){
        // Status must be Open
        if (RoomExists(data))
            Toast.makeText(context.getApplicationContext(), "Room Exists", Toast.LENGTH_SHORT).show();
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
    public ArrayList<DocumentSnapshot> AllRooms(String Rental_id){
        ArrayList<DocumentSnapshot> data = new ArrayList<>();
        Task<QuerySnapshot> rooms = db.collection(collectionName).whereEqualTo("rental_id", Rental_id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    data.addAll(task.getResult().getDocuments());
                }
            }
        });
        return data;
    }
    public HashMap<String, Object> GetRoom(String documentID)
    {
        HashMap<String, Object> data = new HashMap<String, Object>();
        DocumentReference room = db.collection(collectionName).document(documentID);
        room.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
    public Boolean RoomExists (HashMap<String, String> data){
        final boolean[] recordExists = {false};
        ArrayList<DocumentSnapshot> rooms = AllRooms(data.get(uniqueFields[1]));
        for (DocumentSnapshot room: rooms)
        {
            if (recordExists[0])
                break;
            if (room.get(uniqueFields[0]) == data.get(uniqueFields[0]))
                recordExists[0] = true;
        }
        return recordExists[0];
    }
    public void UpdateRoom(HashMap<String, String> data, String documentID)
    {
        DocumentReference room = db.collection(collectionName).document(documentID);
        if (room.get().getResult().exists()) {
            for (String f : fields) {
                if (data.get(f) != null)
                    room.update(f, data.get(f));
            }
        }
        Toast.makeText(context, collectionName + " updated successfully", Toast.LENGTH_LONG).show();
    }
    public void DeleteRoom(String documentID)
    {
        // Status must be Vacant
        DocumentReference room = db.collection(collectionName).document(documentID);
        room.update("status", status[1]).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void avoid) {
                Toast.makeText(context, collectionName + " deleted successfully", Toast.LENGTH_LONG).show();
            }
        });
    }
}