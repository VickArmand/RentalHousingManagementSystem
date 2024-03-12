package com.example.rentalhousingmanagementsystem.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rentalhousingmanagementsystem.Firestoremodel.Auth;
import com.example.rentalhousingmanagementsystem.Firestoremodel.DbConn;
import com.example.rentalhousingmanagementsystem.Firestoremodel.TenantsCrud;
import com.example.rentalhousingmanagementsystem.R;
import com.example.rentalhousingmanagementsystem.databinding.FragmentTenantsBinding;
import com.example.rentalhousingmanagementsystem.models.Tenants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TenantsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TenantsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String RENTAL_ID = "rentalID";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String Rental_id;
    private String mParam2;
    private FragmentTenantsBinding binding;

    public TenantsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TenantsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TenantsFragment newInstance(String param1, String param2) {
        TenantsFragment fragment = new TenantsFragment();
        Bundle args = new Bundle();
        args.putString(RENTAL_ID, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Rental_id = getArguments().getString(RENTAL_ID);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tenants, container, false);
        ProgressDialog pd = new ProgressDialog(getContext());
        final RecyclerView rv = view.findViewById(R.id.tenantsrv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        new TenantsCrud(getContext()).AllTenants(rv, pd, Rental_id);
        FloatingActionButton fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertAlert(getContext());
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
    private void insertAlert(Context context)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View subview = inflater.inflate(R.layout.tenants_add, null);
        EditText tenantFirstName = subview.findViewById(R.id.txtfname);
        EditText tenantLastName = subview.findViewById(R.id.txtlname);
        EditText tenantContact = subview.findViewById(R.id.txtcontact);
        EditText tenantEContact = subview.findViewById(R.id.txtecontact);
        Spinner tenantGender = subview.findViewById(R.id.txtgender);
        ArrayAdapter genderAdapter = ArrayAdapter.createFromResource(context, R.array.gender, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        tenantGender.setAdapter(genderAdapter);

        EditText tenantID = subview.findViewById(R.id.txtnationalID);
        EditText tenantEmail = subview.findViewById(R.id.txtemail);
        Spinner tenantRoom = subview.findViewById(R.id.txtroom);
        HashMap<String, String > data = new HashMap<>();
        List<String> val = new ArrayList<>();
        new DbConn().db.collection("Rooms").whereEqualTo("rental_id", Rental_id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("ADD TENANT");
        builder.setView(subview);
        builder.create();
        builder.setPositiveButton("ADD TENANT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String firstName = tenantFirstName.getText().toString();
                String lastName = tenantLastName.getText().toString();
                String contact = tenantContact.getText().toString();
                String gender = tenantGender.getSelectedItem().toString();
                String email = tenantEmail.getText().toString();
                String room = data.get(tenantRoom.getSelectedItem().toString());
                String eContact = tenantEContact.getText().toString();
                String nationalID = tenantID.getText().toString();
                if (TextUtils.isEmpty(firstName) ||TextUtils.isEmpty(lastName) ||TextUtils.isEmpty(contact) ||TextUtils.isEmpty(gender) || TextUtils.isEmpty(email)|| TextUtils.isEmpty(room)|| TextUtils.isEmpty(eContact)|| TextUtils.isEmpty(nationalID))
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                else
                {
                    Tenants tenant = null;
                    TenantsCrud objTenants = new TenantsCrud(context);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    try {
                        tenant = new Tenants(firstName, lastName, gender, nationalID, email, contact, eContact, room, Rental_id, "Available", user.getEmail(), user.getEmail());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    HashMap<String, Object> data = new HashMap<String, Object>();
                    data.put("first_name", Objects.requireNonNull(tenant.getFirstName()));
                    data.put("last_name", Objects.requireNonNull(tenant.getLastName()));
                    data.put("gender", Objects.requireNonNull(tenant.getGender()));
                    data.put("email", Objects.requireNonNull(tenant.getEmail()));
                    data.put("contact", Objects.requireNonNull(tenant.getContact()));
                    data.put("emergency_contact", Objects.requireNonNull(tenant.getEmergencyContact()));
                    data.put("room_id", Objects.requireNonNull(tenant.getRoom_id()));
                    data.put("rental_id", Objects.requireNonNull(tenant.getRental_id()));
                    data.put("national_ID", Objects.requireNonNull(tenant.getNationalID()));
                    data.put("status", Objects.requireNonNull(tenant.getStatus()));
                    data.put("created_at", Objects.requireNonNull(tenant.getCreated_at()));
                    data.put("created_by", Objects.requireNonNull(tenant.getCreated_by()));
                    data.put("updated_by", Objects.requireNonNull(tenant.getUpdated_by()));
                    data.put("updated_at", Objects.requireNonNull(tenant.getUpdated_at()));
                    objTenants.RegisterTenant(data);
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