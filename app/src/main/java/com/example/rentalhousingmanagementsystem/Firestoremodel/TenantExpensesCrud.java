package com.example.rentalhousingmanagementsystem.Firestoremodel;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalhousingmanagementsystem.RecyclerviewAdapters.TenantExpensesAdapter;
import com.example.rentalhousingmanagementsystem.models.TenantExpenses;
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

public class TenantExpensesCrud extends DbConn{
    private final String collectionName = "TenantsRentalExpenses";
    private final String [] fields = {"category", "description", "payer", "frequency", "amount", "tenant_id", "deadline", "created_at", "created_by", "updated_at", "updated_by"};
    private Context context;
    public TenantExpensesCrud (Context context)
    {
        this.context = context;
    }

    public void RegisterTenantExpense(HashMap<String, Object> data){
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
    public void AllTenantExpenses(RecyclerView rv, ProgressDialog pd){
        pd.setCancelable(false);
        pd.setMessage("Fetching Data ...");
        pd.show();
        db.collection(collectionName).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                ArrayList<TenantExpenses> data = new ArrayList<>() ;
                if (value != null) {
                    if (!value.isEmpty()) {
                        for (DocumentSnapshot d : value.getDocuments()) {
                            String category = (String) d.get(fields[0]);
                            String description = (String) d.get(fields[1]);
                            String payer = (String) d.get(fields[2]);
                            String frequency = (String) d.get(fields[3]);
                            int amount = Integer.parseInt(String.valueOf(d.get(fields[4])));
                            String tenant_id = (String) d.get(fields[5]);
                            Date deadline = (Date) d.get(fields[6]);
                            String creator = (String) d.get(fields[8]);
                            String updator = (String) d.get(fields[11]);
                            try {
                                TenantExpenses tenantExpense = new TenantExpenses(category, payer, amount, tenant_id, description, frequency, deadline, creator, updator);
                                tenantExpense.setId(d.getId());
                                data.add(tenantExpense);
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
                if (data.size() > 0)
                {
                    rv.setVisibility(View.VISIBLE);
                    TenantExpensesAdapter rad = new TenantExpensesAdapter(context, data);
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
    public HashMap<String, Object> GetTenantExpense(String documentID)
    {
        HashMap<String, Object> data = new HashMap<>();
        DocumentReference tenantExpense = db.collection(collectionName).document(documentID);
        tenantExpense.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
    public void UpdateTenantExpense(HashMap<String, Object> data, String documentID)
    {
        DocumentReference tenantExpense = db.collection(collectionName).document(documentID);
        tenantExpense.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.getResult().exists())
                tenantExpense.update(data);
            else
                Toast.makeText(context, collectionName + " doesn't exist", Toast.LENGTH_LONG).show();
            Toast.makeText(context, collectionName + " updated successfully", Toast.LENGTH_LONG).show();
        }});
    }
    public void DeleteTenantExpense(String documentID)
    {
        DocumentReference tenantExpense = db.collection(collectionName).document(documentID);
        tenantExpense.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void avoid) {
                Toast.makeText(context, collectionName + " deleted successfully", Toast.LENGTH_LONG).show();
            }
        });
    }
}
