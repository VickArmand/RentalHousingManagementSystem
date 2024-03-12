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
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalhousingmanagementsystem.Firestoremodel.CaretakersCrud;
import com.example.rentalhousingmanagementsystem.Firestoremodel.RoomsCrud;
import com.example.rentalhousingmanagementsystem.R;
import com.example.rentalhousingmanagementsystem.Rental_details;
import com.example.rentalhousingmanagementsystem.models.Caretakers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class CaretakersAdapter extends RecyclerView.Adapter<CaretakersViewHolder> implements Filterable {
    private final Context context;
    private final ArrayList<Caretakers> caretakers;
    private CaretakersCrud objCaretakers;

    public CaretakersAdapter(Context context, ArrayList<Caretakers> caretakers)
    {
        this.context = context;
        this.caretakers = caretakers;
        objCaretakers = new CaretakersCrud(context);
    }

    @NonNull
    @Override
    public CaretakersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.caretakers_list_layout, parent, false);
        return new CaretakersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CaretakersViewHolder holder, int position) {
        Caretakers caretaker = caretakers.get(position);

        holder.name.setText(String.format("%s %s", caretaker.getFirstName(), caretaker.getLastName()));
//        holder.room.setText((String) new RoomsCrud(context).GetRoom(caretaker.getRoom_id()).get("name"));
        holder.updateRental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAlert(caretaker);
            }
        });
        if (Objects.equals(caretaker.getStatus(), "Active")) {
            holder.deleteRental.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objCaretakers.DeleteCaretaker(caretaker.getId());
                }
            });
        }
    }
    private void editAlert(Caretakers caretaker)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View subview = inflater.inflate(R.layout.activity_caretaker_update, null);
        EditText caretakerContact = subview.findViewById(R.id.txtcontact);
        EditText caretakerID = subview.findViewById(R.id.txtnationalID);
        EditText caretakerEmail = subview.findViewById(R.id.txtemail);
        EditText caretakerRoom = subview.findViewById(R.id.txtroom);
        EditText caretakerEContact = subview.findViewById(R.id.txtecontact);
        if (caretaker != null)
        {
            caretakerContact.setText(caretaker.getContact());
            caretakerID.setText(caretaker.getNationalID());
            caretakerRoom.setText(caretaker.getRoom_id());
            caretakerEmail.setText(caretaker.getEmail());
            caretakerEContact.setText(caretaker.getEmergencyContact());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("UPDATE CARETAKER");
        builder.setView(subview);
        builder.create();
        builder.setPositiveButton("UPDATE CARETAKER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String contact = caretakerContact.getText().toString();
                String email = caretakerEmail.getText().toString();
                String room = caretakerRoom.getText().toString();
                String eContact = caretakerEContact.getText().toString();
                String nationalID = caretakerID.getText().toString();
                if (TextUtils.isEmpty(contact) || TextUtils.isEmpty(email)|| TextUtils.isEmpty(room)|| TextUtils.isEmpty(eContact)|| TextUtils.isEmpty(nationalID))
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                else
                {
                    caretaker.setContact(contact);
                    caretaker.setEmail(email);
                    caretaker.setEmergencyContact(eContact);
                    caretaker.setRoom_id(room);
                    caretaker.setNationalID(nationalID);
                    try {
                        caretaker.setUpdated_at();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    HashMap<String, Object> data = new HashMap<String, Object>();
                    data.put("email", Objects.requireNonNull(caretaker.getEmail()));
                    data.put("contact", Objects.requireNonNull(caretaker.getContact()));
                    data.put("emergency_contact", Objects.requireNonNull(caretaker.getEmergencyContact()));
                    data.put("room_id", Objects.requireNonNull(caretaker.getRoom_id()));
                    data.put("national_ID", Objects.requireNonNull(caretaker.getNationalID()));
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

    @Override
    public int getItemCount() {
        return caretakers.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
