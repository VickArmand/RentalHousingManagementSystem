package com.example.rentalhousingmanagementsystem.Firestoremodel;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalhousingmanagementsystem.models.Rentals;
import com.example.rentalhousingmanagementsystem.RecyclerviewAdapters.RentalsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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
            String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
            data.put("status", status[0]);
            data.put("created_at", date);
            data.put("updated_at", date);
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
    public void AllRentals(RecyclerView rv, ProgressDialog pd){
        Task<QuerySnapshot> rentals = db.collection(collectionName).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty())
                {
                    ArrayList<Rentals> data = new ArrayList<>() ;
                    for (DocumentSnapshot d : queryDocumentSnapshots.getDocuments())
                    {
                        String id = d.getId();
                        String name = (String) d.get(fields[0]);
                        int numRooms = Integer.parseInt((String.valueOf(d.get(fields[1]))));
                        String status = (String) d.get(fields[2]);
                        String email = Auth.getCurrentUser().getEmail();
                        try {
                            data.add(new Rentals(d.getId(), name, numRooms, status, email, email));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (data.size() > 0)
                    {
                        rv.setVisibility(View.VISIBLE);
                        RentalsAdapter rad = new RentalsAdapter(context, data);
                        rv.setAdapter(rad);
                        pd.dismiss();
                        rad.notifyDataSetChanged();
                    }
                    else
                    {
                        Toast.makeText(context, "No rentals available", Toast.LENGTH_SHORT).show();
                        rv.setVisibility(View.GONE);
                    }
                }
            }
        });
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
    public void UpdateRental(HashMap<String, Object> data, String documentID)
    {
        DocumentReference rental = db.collection(collectionName).document(documentID);

        rental.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists())
                    rental.update(data);
                else
                    Toast.makeText(context, collectionName + " doesn't exist", Toast.LENGTH_LONG).show();
                Toast.makeText(context, collectionName + " updated successfully", Toast.LENGTH_LONG).show();
            }
        });
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
