package com.example.rentalhousingmanagementsystem.RecyclerviewAdapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalhousingmanagementsystem.Firestoremodel.DbConn;
import com.example.rentalhousingmanagementsystem.Firestoremodel.TransactionsCrud;
import com.example.rentalhousingmanagementsystem.R;
import com.example.rentalhousingmanagementsystem.Rental_details;
import com.example.rentalhousingmanagementsystem.models.Transactions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
        Date payment = transaction.getPayment_date();
        if (transaction.getEvidence().length() > 0)
        {
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View subview = inflater.inflate(R.layout.transaction_evidence, null);
                    ImageView iv = subview.findViewById(R.id.evidence);
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images"+transaction.getEvidence());
//                    Picasso.get().load().into(iv);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("TRANSACTION EVIDENCE");
                    builder.setView(subview);
                    builder.create();
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "Task canceled", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                }
            });
        }
        holder.paymentDate.setText("%s-%s-%s".format(String.valueOf(payment.getYear()), payment.getMonth(), payment.getDay()));
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
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(context, R.array.transactionCategory, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        transactionCategory.setAdapter(categoryAdapter);
        Spinner transactionMode = subview.findViewById(R.id.txtpaymentmode);
        ArrayAdapter<CharSequence> modeAdapter = ArrayAdapter.createFromResource(context, R.array.transactionMode, android.R.layout.simple_spinner_item);
        modeAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        transactionMode.setAdapter(modeAdapter);
        EditText transactionAmount = subview.findViewById(R.id.txtamount);
        EditText transactionDate = subview.findViewById(R.id.txtdeadline);
        if (transaction != null)
        {
            ArrayList<Object> category = new ArrayList<>();
            category.add("Rent");
            category.add("Maintenance");
            category.add( "Miscellaneous");
            ArrayList<Object> mode = new ArrayList<>();
            mode.add("Bank");
            mode.add("Cash");
            transactionCategory.setSelection(category.indexOf(transaction.getCategory()));
            transactionAmount.setText(String.valueOf(transaction.getCredit()));
            transactionMode.setSelection(mode.indexOf(transaction.getPaymentMode()));
            Date payment = transaction.getPayment_date();
            transactionDate.setText("%s-%s-%s".format(String.valueOf(payment.getYear()), payment.getMonth(), payment.getDay()));
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("UPDATE TRANSACTION");
        builder.setView(subview);
        builder.create();
        transactionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance();
                int y = c.get(Calendar.YEAR);
                int m = c.get(Calendar.MONTH);
                int d = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        transactionDate.setText(String.format("%s-%s-%s", String.valueOf(year), String.valueOf(month), String.valueOf(dayOfMonth)));
                    }
                },y, m, d);
                datePicker.show();
            }
        });
        builder.setPositiveButton("UPDATE TRANSACTION", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String category = transactionCategory.getSelectedItem().toString();
                String amount = transactionAmount.getText().toString();
                String paymentMode = transactionMode.getSelectedItem().toString();
                String[] dateString = transactionDate.getText().toString().split("-");
                if (TextUtils.isEmpty(category) || TextUtils.isEmpty(amount) || TextUtils.isEmpty(paymentMode))
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                else
                {
                    Date date = new Date(Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]), Integer.parseInt(dateString[2]));
                    transaction.setCategory(category);
                    transaction.setPayment_date(date);
                    transaction.setCredit(Integer.parseInt(amount));
                    final int[] cost = new int[1];
                    FirebaseFirestore db = new DbConn().db;
                    DocumentReference room = db.collection("Rooms").document(transaction.getRoom_id());
                    transaction.setPaymentMode(paymentMode);
                    try {
                        transaction.setUpdated_at();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    HashMap<String, Object> data = new HashMap<String, Object>();
                    data.put("category", Objects.requireNonNull(transaction.getCategory()));
                    data.put("credit", transaction.getCredit());
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
