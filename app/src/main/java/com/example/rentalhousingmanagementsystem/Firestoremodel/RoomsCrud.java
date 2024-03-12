package com.example.rentalhousingmanagementsystem.Firestoremodel;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalhousingmanagementsystem.RecyclerviewAdapters.RoomsAdapter;
import com.example.rentalhousingmanagementsystem.models.Rooms;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
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
    public void RegisterRoom(HashMap<String, Object>  data){
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
    public void AllRooms(RecyclerView rv, ProgressDialog pd, String Rental_id){
        pd.setCancelable(false);
        pd.setMessage("Fetching Data ...");
        pd.show();
         db.collection(collectionName).whereEqualTo("rental_id", Rental_id).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                ArrayList<Rooms> data = new ArrayList<>() ;
                if (value != null) {
                    if (!value.isEmpty()) {
                        for (DocumentSnapshot d : value.getDocuments()) {
                            String name = (String) d.get(fields[0]);
                            int cost = Integer.parseInt((String.valueOf(d.get(fields[1]))));
                            int tenants = Integer.parseInt((String.valueOf(d.get(fields[3]))));
                            String description = (String) d.get(fields[2]);
                            String status = (String) d.get(fields[2]);
                            String rental_id = (String) d.get(fields[5]);
                            String creator = (String) d.get(fields[7]);
                            String updator = (String) d.get(fields[9]);
                            try {
                                Rooms room = new Rooms(name, cost, description, tenants, status, rental_id, creator, updator);
                                room.setId(d.getId());
                                data.add(room);
                            } catch (ParseException e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
                if (data.size() > 0)
                {
                    rv.setVisibility(View.VISIBLE);
                    RoomsAdapter rad = new RoomsAdapter(context, data);
                    rv.setAdapter(rad);
                    rad.notifyDataSetChanged();
                }
                else
                {
                    Toast.makeText(context, "No "+collectionName+" available", Toast.LENGTH_SHORT).show();
                    rv.setVisibility(View.GONE);
                }
                pd.dismiss();
            }
        });
    }
    public DocumentSnapshot GetRoom(String documentID)
    {
        final DocumentSnapshot[] data = new DocumentSnapshot[1];
        DocumentReference room = db.collection(collectionName).document(documentID);
        room.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        data[0] = doc;
                    }
                }
            }
        });
        return data[0];
    }
    public Boolean RoomExists (HashMap<String, Object> data){
        final boolean[] recordExists = {false};
        db.collection(collectionName).whereEqualTo(uniqueFields[1], data.get(uniqueFields[1])).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot room: task.getResult().getDocuments())
                    {
                        if (recordExists[0])
                            break;
                        if (room.get(uniqueFields[0]) == data.get(uniqueFields[0]))
                            recordExists[0] = true;
                    }
                }
            }
        });

        return recordExists[0];
    }
    public void UpdateRoom(HashMap<String, Object> data, String documentID)
    {
        DocumentReference room = db.collection(collectionName).document(documentID);
        room.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.getResult().exists())
                room.update(data);
            else
                Toast.makeText(context, collectionName + " doesn't exist", Toast.LENGTH_LONG).show();
            Toast.makeText(context, collectionName + " updated successfully", Toast.LENGTH_LONG).show();
        }});
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
    public void RestoreRoom(String documentID)
    {
        // Status must be Vacant
        DocumentReference room = db.collection(collectionName).document(documentID);
        room.update("status", status[0]).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void avoid) {
                Toast.makeText(context, collectionName + " restored successfully", Toast.LENGTH_LONG).show();
            }
        });
    }
}