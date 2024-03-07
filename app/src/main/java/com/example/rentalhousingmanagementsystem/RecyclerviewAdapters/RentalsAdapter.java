package com.example.rentalhousingmanagementsystem.RecyclerviewAdapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalhousingmanagementsystem.MainActivity;
import com.example.rentalhousingmanagementsystem.R;
import com.example.rentalhousingmanagementsystem.models.Rentals;
import com.example.rentalhousingmanagementsystem.Firestoremodel.RentalsCrud;
import com.example.rentalhousingmanagementsystem.rentalUpdate;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class RentalsAdapter extends RecyclerView.Adapter<RentalsViewHolder> implements Filterable {
    private final Context context;
    private final ArrayList<Rentals> rentals;
    private RentalsCrud objRentals;

    public RentalsAdapter(Context context, ArrayList<Rentals> rentals)
    {
        this.context = context;
        this.rentals = rentals;
        objRentals = new RentalsCrud(context);
    }

    @NonNull
    @Override
    public RentalsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rental_list_layout, parent, false);
        return new RentalsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RentalsViewHolder holder, int position) {
        Rentals rental = rentals.get(position);
        holder.name.setText(rental.getName());
        holder.numRooms.setText(String.valueOf(rental.getNumber_of_rooms()));
        holder.updateRental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAlert(rental);
            }
        });
        if (Objects.equals(rental.getStatus(), "Open")) {
            holder.deleteRental.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objRentals.DeleteRental(rental.getId());
                }
            });
        }
        else
        {
            ImageView update = holder.deleteRental;
            update.setImageResource(R.drawable.restore);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(70, 70);
            update.setLayoutParams(lp);
            holder.deleteRental.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objRentals.ReopenRental(rental.getId());
                }
            });
        }
    }
    private void editAlert(Rentals rental)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View subview = inflater.inflate(R.layout.activity_rental_update, null);
        EditText rentalName = subview.findViewById(R.id.txtname);
        EditText rentalRooms = subview.findViewById(R.id.txtnumrooms);
        if (rental != null)
        {
            rentalName.setText(rental.getName());
            rentalRooms.setText(String.valueOf(rental.getNumber_of_rooms()));
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("UPDATE RENTAL");
        builder.setView(subview);
        builder.create();
        builder.setPositiveButton("UPDATE RENTAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = rentalName.getText().toString();
                String rooms = rentalRooms.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(rooms))
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                else
                {
                    rental.setName(name);
                    rental.setNumber_of_rooms(Integer.parseInt(rooms));
                    try {
                        rental.setUpdated_at();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    HashMap<String, Object> data = new HashMap<String, Object>();
                    data.put("name", Objects.requireNonNull(rental.getName()));
                    data.put("number_of_rooms", rental.getNumber_of_rooms());
                    data.put("updated_at", Objects.requireNonNull(rental.getUpdated_at()));
                    objRentals.UpdateRental(data, rental.getId());
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
        return rentals.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
