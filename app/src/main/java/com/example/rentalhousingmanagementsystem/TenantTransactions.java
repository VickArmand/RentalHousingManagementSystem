package com.example.rentalhousingmanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rentalhousingmanagementsystem.Firestoremodel.Auth;
import com.example.rentalhousingmanagementsystem.Firestoremodel.DbConn;
import com.example.rentalhousingmanagementsystem.Firestoremodel.TransactionsCrud;
import com.example.rentalhousingmanagementsystem.Firestoremodel.TransactionsCrud;
import com.example.rentalhousingmanagementsystem.models.Transactions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class TenantTransactions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_transactions);
        ProgressDialog pd = new ProgressDialog(this);
        final RecyclerView rv = findViewById(R.id.transactionsrv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        String tenantID = getIntent().getStringExtra("tenantID");
        String rentalID = getIntent().getStringExtra("rentalID");
        String roomID = getIntent().getStringExtra("roomID");
        new TransactionsCrud(this).AllTransactions(rv, pd, tenantID);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertAlert(getApplicationContext(), tenantID, rentalID, roomID);
            }
        });
    }
    private void insertAlert(Context context, String tenantID, String rentalID, String roomID)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View subview = inflater.inflate(R.layout.transactions_add, null);
        Spinner transactionCategory = subview.findViewById(R.id.txtcategory);
        Spinner transactionMode = subview.findViewById(R.id.txtpaymentmode);
        EditText transactionCredit = subview.findViewById(R.id.txtamount);
        DatePicker transactionDate = subview.findViewById(R.id.txtdeadline);
        // Add image
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("ADD CARETAKER");
        builder.setView(subview);
        builder.create();
        builder.setPositiveButton("ADD CARETAKER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String category = transactionCategory.getSelectedItem().toString();
                String mode = transactionMode.getSelectedItem().toString();
                String credit = transactionCredit.getText().toString();
                Date date = new Date(transactionDate.getYear(), transactionDate.getMonth(), transactionDate.getDayOfMonth());

                if (TextUtils.isEmpty(category) ||TextUtils.isEmpty(mode) ||TextUtils.isEmpty(credit))
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                else
                {
                    FirebaseUser user = Auth.getCurrentUser();
                    Transactions transaction = null;
                    final int[] cost = new int[1];
                    FirebaseFirestore db = new DbConn().db;
                    db.collection("Rooms").document(roomID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful())
                            {
                                DocumentSnapshot doc = task.getResult();
                                if (doc.exists()) {
                                    cost[0] = (int) doc.get("cost");
                                }
                            }
                        }
                    });
                    try {
                        transaction = new Transactions(category, rentalID, roomID, Integer.parseInt(credit), (cost[0] - Integer.parseInt(credit)),mode,"", tenantID, "Available",user.getEmail(), user.getEmail(), date);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    TransactionsCrud objTransactions = new TransactionsCrud(context);
                    HashMap<String, Object> data = new HashMap<String, Object>();
                    data.put("category", Objects.requireNonNull(transaction.getCategory()));
                    data.put("rental_id", Objects.requireNonNull(transaction.getRental_id()));
                    data.put("tenant_id", Objects.requireNonNull(transaction.getTenant_id()));
                    data.put("credit", transaction.getCredit());
                    data.put("debit", transaction.getDebit());
                    data.put("room_id", Objects.requireNonNull(transaction.getRoom_id()));
                    data.put("payment_date", Objects.requireNonNull(transaction.getPayment_date()));
                    data.put("payment_mode", Objects.requireNonNull(transaction.getPaymentMode()));
                    data.put("evidence", Objects.requireNonNull(transaction.getEvidence()));
                    data.put("status", Objects.requireNonNull(transaction.getStatus()));
                    data.put("created_at", Objects.requireNonNull(transaction.getCreated_at()));
                    data.put("created_by", Objects.requireNonNull(transaction.getCreated_by()));
                    data.put("updated_by", Objects.requireNonNull(transaction.getUpdated_by()));
                    data.put("updated_at", Objects.requireNonNull(transaction.getUpdated_at()));
                    objTransactions.RegisterTransaction(data);
                    ((Activity) context).finish();
                    context.startActivity(((Activity) context).getIntent());
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Task canceled", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }
}