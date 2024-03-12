package com.example.rentalhousingmanagementsystem.RecyclerviewAdapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalhousingmanagementsystem.R;

public class RentalsViewHolder extends RecyclerView.ViewHolder {
    TextView name, numRooms;
    CardView card;
    ImageView updateRental, deleteRental;
    public RentalsViewHolder(@NonNull View itemView) {
        super(itemView);
        card = itemView.findViewById(R.id.card);
        name = itemView.findViewById(R.id.rental_name);
        numRooms = itemView.findViewById(R.id.num_rooms);
        updateRental = itemView.findViewById(R.id.btnupdate);
        deleteRental = itemView.findViewById(R.id.btndelete);
    }
}
