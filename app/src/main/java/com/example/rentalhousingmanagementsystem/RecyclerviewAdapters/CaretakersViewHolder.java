package com.example.rentalhousingmanagementsystem.RecyclerviewAdapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalhousingmanagementsystem.R;

public class CaretakersViewHolder extends RecyclerView.ViewHolder{
    TextView name, room;
    CardView card;
    ImageView updateRental, deleteRental;
    public CaretakersViewHolder(@NonNull View itemView) {
        super(itemView);
        card = itemView.findViewById(R.id.card);
        name = itemView.findViewById(R.id.fullname);
        room = itemView.findViewById(R.id.room);
        updateRental = itemView.findViewById(R.id.btnupdate);
        deleteRental = itemView.findViewById(R.id.btndelete);
    }
}
