package com.example.rentalhousingmanagementsystem.Firestoremodel;

import com.google.firebase.firestore.FirebaseFirestore;

public class DbConn {
    public FirebaseFirestore db;
    public DbConn(){
         db = FirebaseFirestore.getInstance();
    }
}
