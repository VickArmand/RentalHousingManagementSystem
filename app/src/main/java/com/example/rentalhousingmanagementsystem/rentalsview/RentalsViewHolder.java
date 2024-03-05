package com.example.rentalhousingmanagementsystem.rentalsview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalhousingmanagementsystem.R;

public class RentalsViewHolder extends RecyclerView.ViewHolder {
    TextView name, numRooms;
    ImageView updateRental, deleteRental;
    public RentalsViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.rental_name);
        numRooms = itemView.findViewById(R.id.num_rooms);
        updateRental = itemView.findViewById(R.id.btnupdate);
        deleteRental = itemView.findViewById(R.id.btndelete);
    }
}
