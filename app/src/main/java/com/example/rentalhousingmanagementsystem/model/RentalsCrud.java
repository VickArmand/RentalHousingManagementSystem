package com.example.rentalhousingmanagementsystem.model;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class RentalsCrud extends DbConn{
    private final String collectionName = "Rentals";
    private final String [] fields = {"name", "number_of_rooms", "status", "created_at", "created_by", "updated_at", "updated_by"};
    Task <QuerySnapshot> rentals;
    public void RegisterRental(HashMap data){
        db.collection(collectionName).add(data);
    }
    public Task<QuerySnapshot> AllRentals(){
        rentals = db.collection(collectionName).get();
        return rentals;
    }
}
