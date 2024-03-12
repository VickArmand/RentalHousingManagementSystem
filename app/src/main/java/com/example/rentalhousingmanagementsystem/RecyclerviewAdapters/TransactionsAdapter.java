package com.example.rentalhousingmanagementsystem.RecyclerviewAdapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalhousingmanagementsystem.Firestoremodel.DbConn;
import com.example.rentalhousingmanagementsystem.Firestoremodel.RoomsCrud;
import com.example.rentalhousingmanagementsystem.Firestoremodel.TransactionsCrud;
import com.example.rentalhousingmanagementsystem.R;
import com.example.rentalhousingmanagementsystem.Rental_details;
import com.example.rentalhousingmanagementsystem.models.Rooms;
import com.example.rentalhousingmanagementsystem.models.Transactions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionViewHolder> implements Filterable {
    private final Context context;
    private final ArrayList<Transactions> transactions;
    private TransactionsCrud objTransactions;

    public TransactionsAdapter(Context context, ArrayList<Transactions> transactions)
    {
        this.context = context;
        this.transactions = transactions;
        objTransactions = new TransactionsCrud(context);
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transactions_list_layout, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transactions transaction = transactions.get(position);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Rental_details.class);
                intent.putExtra("transactionID", transaction.getId());
                context.startActivity(intent);
            }
        });
        holder.paymentDate.setText(String.valueOf(transaction.getPayment_date()));
        holder.credit.setText(String.valueOf(transaction.getCredit()));
        holder.updateRental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAlert(transaction);
            }
        });
    }
    private void editAlert(Transactions transaction)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View subview = inflater.inflate(R.layout.activity_transaction_update, null);
        Spinner transactionCategory = subview.findViewById(R.id.txtcategory);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(context, R.array.transactionCategory, R.layout.activity_transaction_update);
        categoryAdapter.setDropDownViewResource(R.layout.tenants_add);
        transactionCategory.setAdapter(categoryAdapter);
        Spinner transactionMode = subview.findViewById(R.id.txtpaymentmode);
        ArrayAdapter<CharSequence> modeAdapter = ArrayAdapter.createFromResource(context, R.array.rooms, R.layout.tenants_add);
        modeAdapter.setDropDownViewResource(R.layout.tenants_add);
        transactionMode.setAdapter(modeAdapter);
        EditText transactionAmount = subview.findViewById(R.id.txtamount);
        DatePicker transactionDate = subview.findViewById(R.id.txtdeadline);
        if (transaction != null)
        {
//            transactionCategory.setText(transaction.getCategory());
            transactionCategory.setSelection(0);
            transactionAmount.setText(String.valueOf(transaction.getCredit()));
//            transactionMode.setText(transaction.getPaymentMode());
            transactionMode.setSelection(0);
            Date d = transaction.getPayment_date();
            transactionDate.updateDate(d.getYear(), d.getMonth(), d.getDate());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("UPDATE TRANSACTION");
        builder.setView(subview);
        builder.create();
        builder.setPositiveButton("UPDATE TRANSACTION", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String category = transactionCategory.getSelectedItem().toString();
                String amount = transactionAmount.getText().toString();
                String paymentMode = transactionMode.getSelectedItem().toString();
                Date date = new Date(transactionDate.getYear(), transactionDate.getMonth(), transactionDate.getDayOfMonth());
                if (TextUtils.isEmpty(category) || TextUtils.isEmpty(amount) || TextUtils.isEmpty(paymentMode))
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                else
                {
                    transaction.setCategory(category);
                    transaction.setPayment_date(date);
                    transaction.setCredit(Integer.parseInt(amount));
                    final int[] cost = new int[1];
                    FirebaseFirestore db = new DbConn().db;
                    DocumentReference room = db.collection("Rooms").document(transaction.getRoom_id());
                    room.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot doc = task.getResult();
                                if (doc.exists()) {
                                    cost[0] = (int) doc.get("cost");
                                }
                            }
                        }
                    });
                    int debit = cost[0] - transaction.getCredit();
                    transaction.setDebit(debit);
                    transaction.setPaymentMode(paymentMode);
                    try {
                        transaction.setUpdated_at();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    HashMap<String, Object> data = new HashMap<String, Object>();
                    data.put("category", Objects.requireNonNull(transaction.getCategory()));
                    data.put("credit", transaction.getCredit());
                    data.put("debit", transaction.getDebit());
                    data.put("payment_mode", Objects.requireNonNull(transaction.getPaymentMode()));
                    data.put("payment_date", Objects.requireNonNull(transaction.getPayment_date()));
                    data.put("updated_at", Objects.requireNonNull(transaction.getUpdated_at()));
                    objTransactions.UpdateTransaction(data, transaction.getId());
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

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
