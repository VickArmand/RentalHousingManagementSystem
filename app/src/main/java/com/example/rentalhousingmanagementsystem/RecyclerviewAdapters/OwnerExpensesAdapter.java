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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.rentalhousingmanagementsystem.Firestoremodel.OwnerExpensesCrud;
import com.example.rentalhousingmanagementsystem.R;
import com.example.rentalhousingmanagementsystem.Rental_details;
import com.example.rentalhousingmanagementsystem.models.OwnerExpenses;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class OwnerExpensesAdapter extends RecyclerView.Adapter<OwnerExpensesViewHolder> implements Filterable {
    private final Context context;
    private final ArrayList<OwnerExpenses> ownerExpenses;
    private OwnerExpensesCrud objOwnerExpenses;

    public OwnerExpensesAdapter(Context context, ArrayList<OwnerExpenses> ownerExpenses)
    {
        this.context = context;
        this.ownerExpenses = ownerExpenses;
        objOwnerExpenses = new OwnerExpensesCrud(context);
    }

    @NonNull
    @Override
    public OwnerExpensesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_expenses_list_layout, parent, false);
        return new OwnerExpensesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OwnerExpensesViewHolder holder, int position) {
        OwnerExpenses ownerExpense = ownerExpenses.get(position);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Rental_details.class);
                intent.putExtra("ownerExpenseID", ownerExpense.getId());
                context.startActivity(intent);
            }
        });
        holder.category.setText(ownerExpense.getCategory());
        holder.frequency.setText(String.valueOf(ownerExpense.getFrequency()));
        holder.updateRental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                editAlert(ownerExpense);
           }
        });
        holder.deleteRental.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objOwnerExpenses.DeleteOwnerExpense(ownerExpense.getId());
                }
        });
    }
//    private void editAlert(OwnerExpenses ownerExpense)
//    {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View subview = inflater.inflate(R.layout.activity_owner_expense_update, null);
//        Spinner oeCategory = subview.findViewById(R.id.txtcategory);
//        Spinner oeFrequency = subview.findViewById(R.id.txtfrequency);
//        EditText oeCost = subview.findViewById(R.id.txtcost);
//        EditText oeDescription = subview.findViewById(R.id.txtdescription);
//        DatePicker oeDeadline = subview.findViewById(R.id.txtdeadline);
//        if (ownerExpense != null)
//        {
//            oeCategory.setSelection(ownerExpense.getCategory());
//            oeFrequency.setSelection(String.valueOf(ownerExpense.getFrequency()));
//            oeCost.setText(String.valueOf(ownerExpense.getAmount()));
//            oeDescription.setText(String.valueOf(ownerExpense.getDescription()));
//            Date d = ownerExpense.getDeadline();
//            oeDeadline.updateDate(d.getYear(), d.getMonth(), d.getDate());
//        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("UPDATE EXPENSE");
//        builder.setView(subview);
//        builder.create();
//        builder.setPositiveButton("UPDATE EXPENSE", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String category = oeCategory.getText().toString();
//                String frequency = oeFrequency.getText().toString();
//                String description = oeDescription.getText().toString();
//                String cost = oeCost.getText().toString();
//                Date deadline = new Date(oeDeadline.getYear(), oeDeadline.getMonth(), oeDeadline.getDayOfMonth());
//                if (TextUtils.isEmpty(description) || TextUtils.isEmpty(category)|| TextUtils.isEmpty(cost)|| TextUtils.isEmpty(frequency))
//                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show();
//                else
//                {
//                    ownerExpense.setCategory(category);
//                    ownerExpense.setFrequency(frequency);
//                    ownerExpense.setDescription(description);
//                    ownerExpense.setAmount(Integer.parseInt(cost));
//                    ownerExpense.setDeadline(deadline);
//                    try {
//                        ownerExpense.setUpdated_at();
//                    } catch (ParseException e) {
//                        throw new RuntimeException(e);
//                    }
//                    HashMap<String, Object> data = new HashMap<String, Object>();
//                    data.put("category", Objects.requireNonNull(ownerExpense.getCategory()));
//                    data.put("frequency", ownerExpense.getFrequency());
//                    data.put("description", ownerExpense.getDescription());
//                    data.put("deadline", ownerExpense.getDeadline());
//                    data.put("amount", ownerExpense.getAmount());
//                    data.put("updated_at", Objects.requireNonNull(ownerExpense.getUpdated_at()));
//                    objOwnerExpenses.UpdateOwnerExpense(data, ownerExpense.getId());
//                    ((Activity) context).finish();
//                    context.startActivity(((Activity) context).getIntent());
//                }
//            }
//        });
//        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(context, "Task canceled", Toast.LENGTH_SHORT).show();
//            }
//        });
//        builder.show();
//    }

    @Override
    public int getItemCount() {
        return ownerExpenses.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}