package com.example.rentalhousingmanagementsystem.ui.fragments;

import static android.content.Intent.getIntent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rentalhousingmanagementsystem.Firestoremodel.Auth;
import com.example.rentalhousingmanagementsystem.Firestoremodel.CaretakersCrud;
import com.example.rentalhousingmanagementsystem.R;
import com.example.rentalhousingmanagementsystem.databinding.FragmentCaretakersBinding;
import com.example.rentalhousingmanagementsystem.models.Caretakers;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CaretakersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CaretakersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String RENTAL_ID = "rentalID";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String Rental_ID;
    private String mParam2;
    private FragmentCaretakersBinding binding;

    public CaretakersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CaretakersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CaretakersFragment newInstance(String param1, String param2) {
        CaretakersFragment fragment = new CaretakersFragment();
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
            Rental_ID = getArguments().getString(RENTAL_ID);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_caretakers, container, false);
        ProgressDialog pd = new ProgressDialog(getContext());
        RecyclerView rv = view.findViewById(R.id.caretakersrv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        new CaretakersCrud(getContext()).AllCaretakers(rv, pd, Rental_ID);
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
        EditText caretakerFirstName = subview.findViewById(R.id.txtfname);
        EditText caretakerLastName = subview.findViewById(R.id.txtlname);
        EditText caretakerContact = subview.findViewById(R.id.txtcontact);
        EditText caretakerEContact = subview.findViewById(R.id.txtecontact);
        Spinner caretakerGender = subview.findViewById(R.id.txtgender);
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(context, R.array.gender, R.layout.tenants_add);
        genderAdapter.setDropDownViewResource(R.layout.tenants_add);
        caretakerGender.setAdapter(genderAdapter);
        EditText caretakerID = subview.findViewById(R.id.txtnationalID);
        EditText caretakerEmail = subview.findViewById(R.id.txtemail);
        Spinner caretakerRoom = subview.findViewById(R.id.txtroom);
        ArrayAdapter<CharSequence> roomAdapter = ArrayAdapter.createFromResource(context, R.array.rooms, R.layout.tenants_add);
        roomAdapter.setDropDownViewResource(R.layout.tenants_add);
        caretakerRoom.setAdapter(roomAdapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("ADD CARETAKER");
        builder.setView(subview);
        builder.create();
        builder.setPositiveButton("ADD CARETAKER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nationalID = caretakerID.getText().toString();
                String firstName = caretakerFirstName.getText().toString();
                String lastName = caretakerLastName.getText().toString();
                String contact = caretakerContact.getText().toString();
                String gender = caretakerGender.getSelectedItem().toString();
                String email = caretakerEmail.getText().toString();
                String room = caretakerRoom.getSelectedItem().toString();
                String eContact = caretakerEContact.getText().toString();
                if (TextUtils.isEmpty(firstName) ||TextUtils.isEmpty(lastName) ||TextUtils.isEmpty(contact) ||TextUtils.isEmpty(gender) || TextUtils.isEmpty(email)|| TextUtils.isEmpty(room)|| TextUtils.isEmpty(eContact)|| TextUtils.isEmpty(nationalID))
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                else
                {
                    FirebaseUser user = Auth.getCurrentUser();
                    Caretakers caretaker = null;
                    try {
                        caretaker = new Caretakers(firstName, lastName, gender, nationalID, email, contact, eContact, room, Rental_ID, "Available",user.getEmail(), user.getEmail());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    CaretakersCrud objCaretakers = new CaretakersCrud(context);
                    HashMap<String, Object> data = new HashMap<String, Object>();
                    data.put("first_name", Objects.requireNonNull(caretaker.getFirstName()));
                    data.put("last_name", Objects.requireNonNull(caretaker.getLastName()));
                    data.put("gender", Objects.requireNonNull(caretaker.getGender()));
                    data.put("email", Objects.requireNonNull(caretaker.getEmail()));
                    data.put("contact", Objects.requireNonNull(caretaker.getContact()));
                    data.put("emergency_contact", Objects.requireNonNull(caretaker.getEmergencyContact()));
                    data.put("room_id", Objects.requireNonNull(caretaker.getRoom_id()));
                    data.put("rental_id", Objects.requireNonNull(caretaker.getRental_id()));
                    data.put("national_ID", Objects.requireNonNull(caretaker.getNationalID()));
                    data.put("status", Objects.requireNonNull(caretaker.getStatus()));
                    data.put("created_at", Objects.requireNonNull(caretaker.getCreated_at()));
                    data.put("created_by", Objects.requireNonNull(caretaker.getCreated_by()));
                    data.put("updated_by", Objects.requireNonNull(caretaker.getUpdated_by()));
                    data.put("updated_at", Objects.requireNonNull(caretaker.getUpdated_at()));
                    objCaretakers.UpdateCaretaker(data, caretaker.getId());
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