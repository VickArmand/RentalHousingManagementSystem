package com.example.rentalhousingmanagementsystem.RecyclerviewAdapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalhousingmanagementsystem.R;

public class OwnerExpensesViewHolder  extends RecyclerView.ViewHolder{
    TextView category, frequency;
    CardView card;
    ImageView updateRental, deleteRental;
    public OwnerExpensesViewHolder(@NonNull View itemView) {
        super(itemView);
        card = itemView.findViewById(R.id.card);
        category = itemView.findViewById(R.id.category);
        frequency = itemView.findViewById(R.id.frequency);
        updateRental = itemView.findViewById(R.id.btnupdate);
        deleteRental = itemView.findViewById(R.id.btndelete);
    }
}