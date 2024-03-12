package com.example.rentalhousingmanagementsystem.Firestoremodel;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalhousingmanagementsystem.RecyclerviewAdapters.CaretakersAdapter;
import com.example.rentalhousingmanagementsystem.models.Caretakers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

public class CaretakersCrud extends DbConn{
    private final String collectionName = "Caretakers";
    private final Context context;
    private final String [] fields = {"first_name", "last_name", "gender", "national_ID", "email", "contact", "emergency_contact", "room_id", "rental_id","status", "created_at", "created_by", "updated_at", "updated_by"};
    private final String [] uniqueFields = {"first_name", "last_name", "national_ID", "email", "contact"};
    private final String [] status = {"Active", "Inactive"};
    public CaretakersCrud(Context context)
    {
        this.context = context;
    }
    public void RegisterCaretaker(HashMap<String, Object> data){
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
    public void AllCaretakers(RecyclerView rv, ProgressDialog pd, String Rental_id){
        pd.setCancelable(false);
        pd.setMessage("Fetching Data ...");
        pd.show();
        db.collection(collectionName).whereEqualTo("rental_id", Rental_id).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                ArrayList<Caretakers> data = new ArrayList<>();
                if (!value.isEmpty()) {
                    for (DocumentSnapshot d : value.getDocuments()) {
                        String firstName = (String) d.get("first_name");
                        String lastName = (String) d.get("last_name");
                        String gender = (String) d.get("gender");
                        String nationalID = (String) d.get("national_ID");
                        String email = (String) d.get("email");
                        String contact = (String) d.get("contact");
                        String eContact = (String) d.get("emergency_contact");
                        String room_id = (String) d.get("room_id");
                        String rental_id = (String) d.get("rental_id");
                        String status = (String) d.get("status");
                        String creator = (String) d.get("created_by");
                        String updator = (String) d.get("updated_by");
                        try {
                            Caretakers caretaker = new Caretakers(firstName, lastName, gender, nationalID, email, contact, eContact, room_id, rental_id, status, creator, updator);
                            caretaker.setId(d.getId());
                            data.add(caretaker);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                if (data.size() > 0) {
                    rv.setVisibility(View.VISIBLE);
                    CaretakersAdapter rad = new CaretakersAdapter(context, data);
                    rv.setAdapter(rad);
                    rad.notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "No "+collectionName+" available", Toast.LENGTH_SHORT).show();
                    rv.setVisibility(View.GONE);
                }
                pd.dismiss();
            }
        });
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
    public Boolean CaretakerExists (HashMap<String, Object> data){
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
    public void UpdateCaretaker(HashMap<String, Object> data, String documentID)
    {
        DocumentReference caretaker = db.collection(collectionName).document(documentID);
        caretaker.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.getResult().exists())
                caretaker.update(data);
            else
                Toast.makeText(context, collectionName + " doesn't exist", Toast.LENGTH_LONG).show();
            Toast.makeText(context, collectionName + " updated successfully", Toast.LENGTH_LONG).show();
        }});
    }
    public void DeleteCaretaker(String documentID)
    {
        HashMap<String, Object> data = new HashMap<>();
        data.put("status", status[1]);
        data.put("room_id", null);
        DocumentReference caretaker = db.collection(collectionName).document(documentID);
        caretaker.update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void avoid) {
                Toast.makeText(context, collectionName + " deleted successfully", Toast.LENGTH_LONG).show();
            }
        });
    }
}
