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
import java.util.Map;
import java.util.Objects;

public class RentalsCrud extends DbConn{
    private final String collectionName = "Rentals";
    private final String [] fields = {"name", "number_of_rooms", "status", "created_at", "created_by", "updated_at", "updated_by"};
    private final String [] uniqueFields = {"name"};
    private final String [] status = {"Open", "Closed"};

    private final Context context;
    public RentalsCrud (Context context)
    {
        this.context = context;
    }
    public void RegisterRental(HashMap<String, String> data){
        if (RentalExists(data))
            Toast.makeText(context.getApplicationContext(), "Rental Exists", Toast.LENGTH_SHORT).show();
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
    public HashMap<String, DocumentSnapshot> AllRentals(){
        HashMap<String, DocumentSnapshot> data = new HashMap<>();
        Task<QuerySnapshot> rentals = db.collection(collectionName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
    public HashMap<String, Object> GetRental(String documentID)
    {
        HashMap<String, Object> data = new HashMap<>();
        DocumentReference rental = db.collection(collectionName).document(documentID);
        rental.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
    public Boolean RentalExists (HashMap<String, String> data){
        final boolean[] recordExists = {false};
        Task<QuerySnapshot> rentals = db.collection(collectionName).whereEqualTo(uniqueFields[0], data.get(uniqueFields[0])).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (recordExists[0])
                            break;
                        else {
                            recordExists[0] = true;
                        }
                    }
                }
            }
        });
        return recordExists[0];
    }
    public void UpdateRental(HashMap<String, String> data, String documentID)
    {
        DocumentReference rental = db.collection(collectionName).document(documentID);
        if (rental.get().getResult().exists()) {
            for (String f : fields) {
                if (data.get(f) != null) {
                    if (Objects.equals(f, "status")) {
                        if (Objects.equals(data.get("status"), status[1]) && rental.get().getResult().get("status") != status[1])
                            DeleteRental(documentID);
                        else if (Objects.equals(data.get("status"), status[0]) && rental.get().getResult().get("status") != status[0])
                            ReopenRental(documentID);
                    }
                    else
                        rental.update(f, data.get(f));
                }
            }
        }
        Toast.makeText(context, collectionName + " updated successfully", Toast.LENGTH_LONG).show();
    }
    public void ReopenRental(String documentID)
    {
        DocumentReference rental = db.collection(collectionName).document(documentID);
        rental.update("status", status[0]).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void avoid) {
//                RoomsCrud objRoom = new RoomsCrud(context);
//                HashMap<String, DocumentSnapshot> rooms = objRoom.AllRooms(documentID);
//                HashMap<String, String> data = new HashMap<String, String>();
//                data.put("status", status[1]);
//                for (String roomId : rooms.keySet())
//                    objRoom.UpdateRoom(data, roomId);
                Toast.makeText(context, collectionName + " updated successfully", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void DeleteRental(String documentID)
    {
        DocumentReference rental = db.collection(collectionName).document(documentID);
        rental.update("status", status[1]).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void avoid) {
//                RoomsCrud objRoom = new RoomsCrud(context);
//                HashMap <String, DocumentSnapshot> rooms = objRoom.AllRooms(documentID);
//                HashMap<String, String> data = new HashMap<String, String>();
//                data.put("status", "Vacant");
//                for (String roomId : rooms.keySet())
//                    objRoom.UpdateRoom(data, roomId);
                Toast.makeText(context, collectionName + " deleted successfully", Toast.LENGTH_LONG).show();
            }
        });
    }
}
