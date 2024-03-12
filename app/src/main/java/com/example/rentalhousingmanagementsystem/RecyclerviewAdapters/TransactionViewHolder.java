package com.example.rentalhousingmanagementsystem.RecyclerviewAdapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalhousingmanagementsystem.R;

public class TransactionViewHolder  extends RecyclerView.ViewHolder{
    TextView paymentDate, credit, category;
    CardView card;
    ImageView updateRental;
    public TransactionViewHolder(@NonNull View itemView) {
        super(itemView);
        card = itemView.findViewById(R.id.card);
        category = itemView.findViewById(R.id.category);
        paymentDate = itemView.findViewById(R.id.date);
        credit = itemView.findViewById(R.id.credit);
        updateRental = itemView.findViewById(R.id.btnupdate);
    }
}
