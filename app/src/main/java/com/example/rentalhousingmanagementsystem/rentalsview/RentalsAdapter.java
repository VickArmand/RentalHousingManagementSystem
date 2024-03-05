package com.example.rentalhousingmanagementsystem.rentalsview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalhousingmanagementsystem.R;
import com.example.rentalhousingmanagementsystem.model.RentalsCrud;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class RentalsAdapter extends RecyclerView.Adapter<RentalsViewHolder> implements Filterable {
    private final Context context;
    private final ArrayList<DocumentSnapshot> rentals;
    private RentalsCrud objRentals;

    RentalsAdapter(Context context, ArrayList<DocumentSnapshot> rentals)
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
        final DocumentSnapshot rental = rentals.get(position);
        holder.name.setText((CharSequence) rental.get("name"));
        holder.numRooms.setText((CharSequence) rental.get("number_of_rooms"));
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
