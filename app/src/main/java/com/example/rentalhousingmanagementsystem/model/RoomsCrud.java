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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoomsCrud extends DbConn{
    private final String [] fields = {"name", "cost", "description", "status", "tenant_id", "rental_id", "created_at", "created_by", "updated_at", "updated_by"};
    private final String collectionName = "Rooms";
    private Context context;
    public void RegisterRoom(HashMap data){
        db.collection(collectionName).add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Toast.makeText(context, collectionName + " registered successfully", Toast.LENGTH_LONG).show();
            }
        });
    }
    public HashMap AllRooms(){
        HashMap data = new HashMap();
        Task<QuerySnapshot> rooms = db.collection(collectionName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
    public HashMap GetRoom(String documentID)
    {
        HashMap data = new HashMap();
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
    public void UpdateRoom(HashMap data, String documentID)
    {
        DocumentReference room = db.collection(collectionName).document(documentID);
        if (room != null) {
            for (String f : fields) {
                if (data.get(f) != null)
                    room.update(f, data.get(f));
            }
        }
        Toast.makeText(context, collectionName + " updated successfully", Toast.LENGTH_LONG).show();
    }
    public void DeleteRoom(String documentID)
    {
        DocumentReference room = db.collection(collectionName).document(documentID);
        room.update("status", "Vacant").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void avoid) {
                Toast.makeText(context, collectionName + " deleted successfully", Toast.LENGTH_LONG).show();
            }
        });
    }
}