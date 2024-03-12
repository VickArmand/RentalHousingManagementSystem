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
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalhousingmanagementsystem.Firestoremodel.RoomsCrud;
import com.example.rentalhousingmanagementsystem.R;
import com.example.rentalhousingmanagementsystem.Rental_details;
import com.example.rentalhousingmanagementsystem.models.Rooms;
import com.example.rentalhousingmanagementsystem.models.Rooms;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsViewHolder> implements Filterable {
    private final Context context;
    private final ArrayList<Rooms> rooms;
    private RoomsCrud objRooms;

    public RoomsAdapter(Context context, ArrayList<Rooms> rooms)
    {
        this.context = context;
        this.rooms = rooms;
        objRooms = new RoomsCrud(context);
    }

    @NonNull
    @Override
    public RoomsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rooms_list_layout, parent, false);
        return new RoomsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomsViewHolder holder, int position) {
        Rooms room = rooms.get(position);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Rental_details.class);
                intent.putExtra("roomID", room.getId());
                context.startActivity(intent);
            }
        });
        holder.name.setText(room.getName());
        holder.cost.setText(String.valueOf(room.getCost()));
        holder.updateRental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAlert(room);
            }
        });
        if (Objects.equals(room.getStatus(), "Open")) {
            holder.deleteRental.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objRooms.DeleteRoom(room.getId());
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
                    objRooms.RestoreRoom(room.getId());
                }
            });
        }
    }
    private void editAlert(Rooms room)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View subview = inflater.inflate(R.layout.activity_room_update, null);
        EditText rCost = subview.findViewById(R.id.txtcost);
        EditText rTenants = subview.findViewById(R.id.txttenants);
        EditText rDescription = subview.findViewById(R.id.txtdescription);
        if (room != null)
        {
            rDescription.setText(room.getDescription());
            rTenants.setText(String.valueOf(room.getMax_tenants()));
            rCost.setText(String.valueOf(room.getCost()));
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("UPDATE ROOM");
        builder.setView(subview);
        builder.create();
        builder.setPositiveButton("UPDATE ROOM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String cost = rCost.getText().toString();
                String tenants = rTenants.getText().toString();
                String description = rDescription.getText().toString();
                if (TextUtils.isEmpty(cost) || TextUtils.isEmpty(tenants) || TextUtils.isEmpty(description))
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                else
                {
                    room.setCost(Integer.parseInt(cost));
                    room.setDescription(description);
                    room.setMax_tenants(Integer.parseInt(tenants));
                    try {
                        room.setUpdated_at();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    HashMap<String, Object> data = new HashMap<String, Object>();
                    data.put("cost", room.getCost());
                    data.put("max_tenants", room.getMax_tenants());
                    data.put("description", room.getDescription());
                    data.put("updated_at", Objects.requireNonNull(room.getUpdated_at()));
                    objRooms.UpdateRoom(data, room.getId());
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
        return rooms.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
