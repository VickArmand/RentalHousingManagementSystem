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
import android.widget.EditText;
import android.widget.Toast;

import com.example.rentalhousingmanagementsystem.Firestoremodel.Auth;
import com.example.rentalhousingmanagementsystem.Firestoremodel.RoomsCrud;
import com.example.rentalhousingmanagementsystem.R;
import com.example.rentalhousingmanagementsystem.databinding.FragmentRoomsBinding;
import com.example.rentalhousingmanagementsystem.models.Rooms;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoomsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoomsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String RENTAL_ID = "rentalID";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String Rental_ID;
    private String mParam2;
    private FragmentRoomsBinding binding;

    public RoomsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RoomsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RoomsFragment newInstance(String param1, String param2) {
        RoomsFragment fragment = new RoomsFragment();
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
        View view = inflater.inflate(R.layout.fragment_rooms, container, false);
        ProgressDialog pd = new ProgressDialog(getContext());
        final RecyclerView rv = view.findViewById(R.id.roomsrv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        new RoomsCrud(getContext()).AllRooms(rv, pd, Rental_ID);
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
        View subview = inflater.inflate(R.layout.rooms_add, null);
        EditText rName = subview.findViewById(R.id.txtrname);
        EditText rCost = subview.findViewById(R.id.txtcost);
        EditText rTenants = subview.findViewById(R.id.txttenants);
        EditText rDescription = subview.findViewById(R.id.txtdescription);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("ADD ROOM");
        builder.setView(subview);
        builder.create();
        builder.setPositiveButton("ADD ROOM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = rName.getText().toString();
                String cost = rCost.getText().toString();
                String tenants = rTenants.getText().toString();
                String description = rDescription.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(cost) || TextUtils.isEmpty(tenants) || TextUtils.isEmpty(description))
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                else
                {
                    FirebaseUser user = Auth.getCurrentUser();
                    Rooms room = null;
                    try {
                        room = new Rooms(name, Integer.parseInt(cost), description, Integer.parseInt(tenants), "Vacant", Rental_ID, user.getEmail(), user.getEmail());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    RoomsCrud objRooms = new RoomsCrud(context);

                    HashMap<String, Object> data = new HashMap<String, Object>();
                    data.put("name", room.getName());
                    data.put("cost", room.getCost());
                    data.put("status", room.getStatus());
                    data.put("rental_id", room.getRental_id());
                    data.put("max_tenants", room.getMax_tenants());
                    data.put("description", room.getDescription());
                    data.put("created_at", Objects.requireNonNull(room.getCreated_at()));
                    data.put("created_by", Objects.requireNonNull(room.getCreated_by()));
                    data.put("updated_by", Objects.requireNonNull(room.getUpdated_by()));
                    data.put("updated_at", Objects.requireNonNull(room.getUpdated_at()));
                    objRooms.RegisterRoom(data);
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