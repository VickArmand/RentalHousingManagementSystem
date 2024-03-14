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
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalhousingmanagementsystem.Firestoremodel.DbConn;
import com.example.rentalhousingmanagementsystem.Firestoremodel.RoomsCrud;
import com.example.rentalhousingmanagementsystem.Firestoremodel.TenantsCrud;
import com.example.rentalhousingmanagementsystem.R;
import com.example.rentalhousingmanagementsystem.Rental_details;
import com.example.rentalhousingmanagementsystem.TenantTransactions;
import com.example.rentalhousingmanagementsystem.models.Tenants;
import com.example.rentalhousingmanagementsystem.models.Tenants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TenantsAdapter extends RecyclerView.Adapter<TenantsViewHolder> implements Filterable {
    private final Context context;
    private final ArrayList<Tenants> tenants;
    private TenantsCrud objTenants;
    private final String rental_ID;

    public TenantsAdapter(Context context, ArrayList<Tenants> tenants, String rental_ID)
    {
        this.context = context;
        this.tenants = tenants;
        this.rental_ID = rental_ID;
        objTenants = new TenantsCrud(context);
    }

    @NonNull
    @Override
    public TenantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tenants_list_layout, parent, false);
        return new TenantsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TenantsViewHolder holder, int position) {
        Tenants tenant = tenants.get(position);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TenantTransactions.class);
                intent.putExtra("tenantID", tenant.getId());
                intent.putExtra("rentalID", tenant.getRental_id());
                intent.putExtra("roomID", tenant.getRoom_id());
                context.startActivity(intent);
            }
        });
        holder.name.setText(String.format("%s %s", tenant.getFirstName(), tenant.getLastName()));
        String room_id = tenant.getRoom_id();
        final String[] room_name = new String[1];
        new DbConn().db.collection("Rooms").document(room_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        room_name[0] = (String) doc.get("name");
                    }
                    else
                        room_name[0] = "None";
                    holder.room.setText(room_name[0]);
                }
            }
        });
        holder.updateRental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAlert(tenant);
            }
        });
        if (Objects.equals(tenant.getStatus(), "Available")) {
            holder.deleteRental.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objTenants.DeleteTenant(tenant.getId());
                }
            });
        }
    }
    private void editAlert(Tenants tenant)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View subview = inflater.inflate(R.layout.activity_caretaker_update, null);
        EditText tenantContact = subview.findViewById(R.id.txtcontact);
        EditText tenantID = subview.findViewById(R.id.txtnationalID);
        EditText tenantEmail = subview.findViewById(R.id.txtemail);
        Spinner tenantRoom = subview.findViewById(R.id.txtroom);
        EditText tenantEContact = subview.findViewById(R.id.txtecontact);
        HashMap<String, String > data = new HashMap<>();
        List<String> val = new ArrayList<>();
        new DbConn().db.collection("Rooms").whereEqualTo("rental_id", rental_ID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QuerySnapshot value = task.getResult();
                if (!value.isEmpty())
                {
                    for (DocumentSnapshot d : value.getDocuments()) {
                        data.put((String) d.get("name"), d.getId());
                        val.add((String) d.get("name"));
                    }
                    ArrayAdapter<String> roomAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, val);
                    roomAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);
                    tenantRoom.setAdapter(roomAdapter);
                    for (Map.Entry<String, String> entry: data.entrySet())
                    {
                        if (Objects.equals(entry.getValue(), tenant.getRoom_id())) {
                            if (tenant != null)
                                tenantRoom.setSelection(val.indexOf(entry.getKey()));
                        }
                    }
                }
            }
        });
        if (tenant != null)
        {
            tenantContact.setText(tenant.getContact());
            tenantID.setText(tenant.getNationalID());
            tenantEmail.setText(tenant.getEmail());
            tenantEContact.setText(tenant.getEmergencyContact());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("UPDATE TENANT");
        builder.setView(subview);
        builder.create();
        builder.setPositiveButton("UPDATE TENANT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String contact = tenantContact.getText().toString();
                String email = tenantEmail.getText().toString();
                String room = data.get(tenantRoom.getSelectedItem().toString());
                String eContact = tenantEContact.getText().toString();
                String nationalID = tenantID.getText().toString();
                if (TextUtils.isEmpty(contact) || TextUtils.isEmpty(email)|| TextUtils.isEmpty(room)|| TextUtils.isEmpty(eContact)|| TextUtils.isEmpty(nationalID))
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                else
                {
                    tenant.setContact(contact);
                    tenant.setEmail(email);
                    tenant.setEmergencyContact(eContact);
                    tenant.setRoom_id(room);
                    tenant.setNationalID(nationalID);
                    try {
                        tenant.setUpdated_at();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    HashMap<String, Object> data = new HashMap<String, Object>();
                    data.put("email", Objects.requireNonNull(tenant.getEmail()));
                    data.put("contact", Objects.requireNonNull(tenant.getContact()));
                    data.put("emergency_contact", Objects.requireNonNull(tenant.getEmergencyContact()));
                    data.put("room_id", Objects.requireNonNull(tenant.getRoom_id()));
                    data.put("national_ID", Objects.requireNonNull(tenant.getNationalID()));
                    data.put("updated_at", Objects.requireNonNull(tenant.getUpdated_at()));
                    objTenants.UpdateTenant(data, tenant.getId());
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
        return tenants.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
