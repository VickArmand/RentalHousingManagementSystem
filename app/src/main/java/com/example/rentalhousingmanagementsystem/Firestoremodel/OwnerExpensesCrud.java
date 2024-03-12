package com.example.rentalhousingmanagementsystem.Firestoremodel;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalhousingmanagementsystem.RecyclerviewAdapters.OwnerExpensesAdapter;
import com.example.rentalhousingmanagementsystem.models.OwnerExpenses;
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
import java.util.Date;
import java.util.HashMap;

public class OwnerExpensesCrud extends DbConn{
    private final String collectionName = "OwnersRentalExpenses";
    private final String [] fields = {"category", "description", "frequency", "amount", "deadline", "created_at", "created_by", "updated_at", "updated_by"};
    private Context context;
    public OwnerExpensesCrud (Context context)
    {
        this.context = context;
    }

    public void RegisterOwnerExpense(HashMap<String, Object> data){
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
    public void AllOwnerExpenses(RecyclerView rv, ProgressDialog pd){
        pd.setCancelable(false);
        pd.setMessage("Fetching Data ...");
        pd.show();
        db.collection(collectionName).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                ArrayList<OwnerExpenses> data = new ArrayList<>();
                if (value != null) {
                    if (!value.isEmpty()) {
                        for (DocumentSnapshot d : value.getDocuments()) {
                            String category = (String) d.get(fields[0]);
                            String description = (String) d.get(fields[1]);
                            String frequency = (String) d.get(fields[2]);
                            int amount = Integer.parseInt(String.valueOf(d.get(fields[3])));
                            Date deadline = (Date) d.get(fields[4]);
                            String creator = (String) d.get(fields[6]);
                            String updator = (String) d.get(fields[8]);
                            try {
                                OwnerExpenses ownerExpense = new OwnerExpenses(category, amount, description, frequency, deadline, creator, updator);
                                ownerExpense.setId(d.getId());
                                data.add(ownerExpense);
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
                if (data.size() > 0) {
                    rv.setVisibility(View.VISIBLE);
                    OwnerExpensesAdapter rad = new OwnerExpensesAdapter(context, data);
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
    public HashMap<String, Object> GetOwnerExpense(String documentID)
    {
        HashMap<String, Object> data = new HashMap<>();
        DocumentReference ownerExpense = db.collection(collectionName).document(documentID);
        ownerExpense.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
    public void UpdateOwnerExpense(HashMap<String, Object> data, String documentID)
    {
        DocumentReference ownerExpense = db.collection(collectionName).document(documentID);
        ownerExpense.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.getResult().exists())
                ownerExpense.update(data);
            else
                Toast.makeText(context, collectionName + " doesn't exist", Toast.LENGTH_LONG).show();
            Toast.makeText(context, collectionName + " updated successfully", Toast.LENGTH_LONG).show();
        }});
    }
    public void DeleteOwnerExpense(String documentID)
    {
        DocumentReference ownerExpense = db.collection(collectionName).document(documentID);
        ownerExpense.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void avoid) {
                Toast.makeText(context, collectionName + " deleted successfully", Toast.LENGTH_LONG).show();
            }
        });
    }
}
