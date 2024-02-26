package com.example.rentalhousingmanagementsystem.model;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoomsCrud extends DbConn{
    Task<QuerySnapshot> rooms;
    private final String [] fields = {"name", "cost", "description", "status", "tenant_id", "rental_id", "created_at", "created_by", "updated_at", "updated_by"};
    private final String collectionName = "Rooms";
    public void RegisterRoom(HashMap data){
        db.collection(collectionName).add(data);
    }
    public Task<QuerySnapshot> AllRooms(){
        rooms = db.collection(collectionName).get();
        return rooms;
    }
}