package com.example.rentalhousingmanagementsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class TenantTransactions extends AppCompatActivity {
    Uri imageUri;
    String photoUrl = "";
    Context context = TenantTransactions.this;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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
                insertAlert(context, tenantID, rentalID, roomID);
            }
        });
    }
    private void insertAlert(Context context, String tenantID, String rentalID, String roomID)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View subview = inflater.inflate(R.layout.transactions_add, null);
        Spinner transactionCategory = subview.findViewById(R.id.txtcategory);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(context, R.array.transactionCategory, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        transactionCategory.setAdapter(categoryAdapter);
        Spinner transactionMode = subview.findViewById(R.id.txtpaymentmode);
        ArrayAdapter<CharSequence> modeAdapter = ArrayAdapter.createFromResource(context, R.array.transactionMode, android.R.layout.simple_spinner_item);
        modeAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        transactionMode.setAdapter(modeAdapter);
        EditText transactionCredit = subview.findViewById(R.id.txtamount);
        EditText transactionDate = subview.findViewById(R.id.txtdeadline);
        Button uploadImage = subview.findViewById(R.id.btnuploadimage);
        // Add image
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("ADD TRANSACTION");
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
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        builder.setPositiveButton("ADD TRANSACTION", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String category = transactionCategory.getSelectedItem().toString();
                String mode = transactionMode.getSelectedItem().toString();
                String credit = transactionCredit.getText().toString();
                String[] dateString = transactionDate.getText().toString().split("-", 3);
                FirebaseFirestore db = new DbConn().db;
                if (TextUtils.isEmpty(category) ||TextUtils.isEmpty(mode) ||TextUtils.isEmpty(credit) || dateString.length == 0)
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                else
                {
                    uploadImageFn(category, dateString, rentalID, roomID, credit, tenantID, mode);
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
    private void selectImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }
    private void uploadImageFn(String category, String []dateString, String rentalID,String roomID, String credit, String tenantID, String mode)
    {
        if (imageUri != null) {
            final String format = "yyyy-MM-dd_HH:mm:ss";
            final SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            String filename = dateFormat.format(new Date());
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/" + filename);
            storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            photoUrl = uri.toString();
                        }
                    });
                    Toast.makeText(context, "Image upload success", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Image upload failed", Toast.LENGTH_SHORT).show();
                }
            });

        }
        Date date = new Date(Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]), Integer.parseInt(dateString[2]));
        Toast.makeText(context, String.format("YMD %s", date), Toast.LENGTH_SHORT).show();

        Transactions transaction = null;
        try {
            assert user != null;
            transaction = new Transactions(category, rentalID, roomID, Long.parseLong(credit), mode, photoUrl, tenantID,  user.getEmail(), user.getEmail(), date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        TransactionsCrud objTransactions = new TransactionsCrud(context);
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("category", Objects.requireNonNull(transaction.getCategory()));
        data.put("rental_id", Objects.requireNonNull(transaction.getRental_id()));
        data.put("tenant_id", Objects.requireNonNull(transaction.getTenant_id()));
        data.put("credit", transaction.getCredit());
        data.put("room_id", Objects.requireNonNull(transaction.getRoom_id()));
        data.put("payment_date", Objects.requireNonNull(transaction.getPayment_date()));
        data.put("payment_mode", Objects.requireNonNull(transaction.getPaymentMode()));
        data.put("evidence", Objects.requireNonNull(transaction.getEvidence()));
        data.put("created_at", Objects.requireNonNull(transaction.getCreated_at()));
        data.put("created_by", Objects.requireNonNull(transaction.getCreated_by()));
        data.put("updated_by", Objects.requireNonNull(transaction.getUpdated_by()));
        data.put("updated_at", Objects.requireNonNull(transaction.getUpdated_at()));
        objTransactions.RegisterTransaction(data);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && data.getData() != null) {
            imageUri = data.getData();
        }
    }
}