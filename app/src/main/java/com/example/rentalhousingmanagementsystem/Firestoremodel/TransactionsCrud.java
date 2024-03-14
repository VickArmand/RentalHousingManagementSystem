package com.example.rentalhousingmanagementsystem.Firestoremodel;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalhousingmanagementsystem.RecyclerviewAdapters.TransactionsAdapter;
import com.example.rentalhousingmanagementsystem.models.Transactions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class TransactionsCrud extends DbConn{
    private final String collectionName = "Transactions";
    private final String [] fields = {"category", "room_id", "credit", "debit", "tenant_id", "payment_mode", "payment_date", "evidence", "status", "created_at", "created_by", "updated_at", "updated_by"};
    private Context context;
    private final String [] status = {"Fully Paid", "Partially Paid", "Not Paid"};
    public TransactionsCrud (Context context)
    {
        this.context = context;
    }

    public void RegisterTransaction(HashMap<String, Object> data){
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
    public void AllTransactions(RecyclerView rv, ProgressDialog pd, String tenantID){
        pd.setCancelable(false);
        pd.setMessage("Fetching Data ...");
        pd.show();
        db.collection(collectionName).whereEqualTo("tenant_id", tenantID).orderBy("payment_date", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                ArrayList<Transactions> data = new ArrayList<>();
                if (value != null) {
                    if (!value.isEmpty()) {
                        for (DocumentSnapshot d : value.getDocuments()) {
                            String category = (String) d.get(fields[0]);
                            String roomId = (String) d.get(fields[1]);
                            String paymentMode = (String) d.get(fields[5]);
                            String evidence = (String) d.get(fields[7]);
                            int credit = Integer.parseInt(String.valueOf(d.get(fields[2])));
                            String tenant_id = (String) d.get(fields[4]);
                            String rental_id = (String) d.get("rental_id");
                            Log.v("Date", d.get(fields[6]).toString());
                            Timestamp t = (Timestamp) d.get(fields[6]);
                            Date paymentDate = t.toDate();
                            String creator = (String) d.get(fields[10]);
                            String updator = (String) d.get(fields[12]);
                            try {
                                Transactions transaction = new Transactions(category, rental_id, roomId, credit, paymentMode, evidence, tenant_id, creator, updator, paymentDate);
                                transaction.setId(d.getId());
                                data.add(transaction);
                            } catch (ParseException e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG);
                            }
                        }
                    }
                    if (data.size() > 0) {
                        rv.setVisibility(View.VISIBLE);
                        TransactionsAdapter rad = new TransactionsAdapter(context, data);
                        rv.setAdapter(rad);
                        rad.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "No " + collectionName + " available", Toast.LENGTH_SHORT).show();
                        rv.setVisibility(View.GONE);
                    }
                }
                pd.dismiss();
            }
    });
    }
    public HashMap<String, Object> GetTransaction(String documentID)
    {
        HashMap<String, Object> data = new HashMap<>();
        DocumentReference transaction = db.collection(collectionName).document(documentID);
        transaction.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
    public void UpdateTransaction(HashMap<String, Object> data, String documentID)
    {
        DocumentReference transaction = db.collection(collectionName).document(documentID);
        transaction.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.getResult().exists())
                transaction.update(data);
            else
                Toast.makeText(context, collectionName + " doesn't exist", Toast.LENGTH_LONG).show();
            Toast.makeText(context, collectionName + " updated successfully", Toast.LENGTH_LONG).show();
        }});
    }
}
