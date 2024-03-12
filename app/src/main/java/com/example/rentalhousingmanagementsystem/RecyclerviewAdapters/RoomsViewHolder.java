package com.example.rentalhousingmanagementsystem.RecyclerviewAdapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalhousingmanagementsystem.R;

public class RoomsViewHolder  extends RecyclerView.ViewHolder{
    TextView name, cost;
    CardView card;
    ImageView updateRental, deleteRental;
    public RoomsViewHolder(@NonNull View itemView) {
        super(itemView);
        card = itemView.findViewById(R.id.card);
        name = itemView.findViewById(R.id.room_name);
        cost = itemView.findViewById(R.id.cost);
        updateRental = itemView.findViewById(R.id.btnupdate);
        deleteRental = itemView.findViewById(R.id.btndelete);
    }
}
