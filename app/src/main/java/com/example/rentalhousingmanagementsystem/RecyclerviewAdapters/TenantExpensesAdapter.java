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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalhousingmanagementsystem.Firestoremodel.TenantExpensesCrud;
import com.example.rentalhousingmanagementsystem.R;
import com.example.rentalhousingmanagementsystem.Rental_details;
import com.example.rentalhousingmanagementsystem.models.TenantExpenses;
import com.example.rentalhousingmanagementsystem.models.TenantExpenses;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class TenantExpensesAdapter extends RecyclerView.Adapter<TenantExpensesViewHolder> implements Filterable {
    private final Context context;
    private final ArrayList<TenantExpenses> tenantExpenses;
    private TenantExpensesCrud objTenantExpenses;

    public TenantExpensesAdapter(Context context, ArrayList<TenantExpenses> tenantExpenses)
    {
        this.context = context;
        this.tenantExpenses = tenantExpenses;
        objTenantExpenses = new TenantExpensesCrud(context);
    }

    @NonNull
    @Override
    public TenantExpensesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tenant_expenses_list_layout, parent, false);
        return new TenantExpensesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TenantExpensesViewHolder holder, int position) {
        TenantExpenses tenantExpense = tenantExpenses.get(position);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Rental_details.class);
                intent.putExtra("tenantExpenseID", tenantExpense.getId());
                context.startActivity(intent);
            }
        });
        holder.category.setText(tenantExpense.getCategory());
        holder.payer.setText(String.valueOf(tenantExpense.getPayer()));
        holder.updateRental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                editAlert(tenantExpense);
            }
        });
        holder.deleteRental.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objTenantExpenses.DeleteTenantExpense(tenantExpense.getId());
                }
            });
    }
//    private void editAlert(TenantExpenses tenantExpense)
//    {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View subview = inflater.inflate(R.layout.activity_tenant_expense_update, null);
//        Spinner teCategory = subview.findViewById(R.id.txtcategory);
//        Spinner teFrequency = subview.findViewById(R.id.txtfrequency);
//        Spinner tePayer = subview.findViewById(R.id.txtfrequency);
//        Spinner teTenant = subview.findViewById(R.id.txtfrequency);
//        EditText teCost = subview.findViewById(R.id.txtcost);
//        EditText teDescription = subview.findViewById(R.id.txtdescription);
//        DatePicker teDeadline = subview.findViewById(R.id.txtdeadline);
//        if (tenantExpense != null)
//        {
//            teCategory.setSelection(tenantExpense.getCategory());
//            teFrequency.setSelection(String.valueOf(tenantExpense.getFrequency()));
//            tePayer.setSelection(String.valueOf(tenantExpense.getPayer()));
//            teTenant.setSelection(String.valueOf(tenantExpense.getTenant_id()));
//            teCost.setText(String.valueOf(tenantExpense.getAmount()));
//            teDescription.setText(String.valueOf(tenantExpense.getDescription()));
//            Date d = tenantExpense.getDeadline();
//            teDeadline.updateDate(d.getYear(), d.getMonth(), d.getDate());
//        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("UPDATE EXPENSE");
//        builder.setView(subview);
//        builder.create();
//        builder.setPositiveButton("UPDATE EXPENSE", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String category = teCategory.getText().toString();
//                String frequency = teFrequency.getText().toString();
//                String payer = tePayer.getText().toString();
//                String tenant = teTenant.getText().toString();
//                String description = teDescription.getText().toString();
//                String cost = teCost.getText().toString();
//                Date deadline = new Date(teDeadline.getYear(), teDeadline.getMonth(), teDeadline.getDayOfMonth());
//                if (TextUtils.isEmpty(description) || TextUtils.isEmpty(category)|| TextUtils.isEmpty(cost)|| TextUtils.isEmpty(frequency))
//                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show();
//                else
//                {
//                    tenantExpense.setCategory(category);
//                    tenantExpense.setFrequency(frequency);
//                    tenantExpense.setPayer(payer);
//                    tenantExpense.setTenant_id(tenant);
//                    tenantExpense.setDescription(description);
//                    tenantExpense.setAmount(Integer.parseInt(cost));
//                    tenantExpense.setDeadline(deadline);
//                    try {
//                        tenantExpense.setUpdated_at();
//                    } catch (ParseException e) {
//                        throw new RuntimeException(e);
//                    }
//                    HashMap<String, Object> data = new HashMap<String, Object>();
//                    data.put("category", Objects.requireNonNull(tenantExpense.getCategory()));
//                    data.put("frequency", tenantExpense.getFrequency());
//                    data.put("description", tenantExpense.getDescription());
//                    data.put("payer", tenantExpense.getPayer());
//                    data.put("tenant_id", tenantExpense.getTenant_id());
//                    data.put("deadline", tenantExpense.getDeadline());
//                    data.put("amount", tenantExpense.getAmount());
//                    data.put("updated_at", Objects.requireNonNull(tenantExpense.getUpdated_at()));
//                    objTenantExpenses.UpdateTenantExpense(data, tenantExpense.getId());
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
        return tenantExpenses.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
