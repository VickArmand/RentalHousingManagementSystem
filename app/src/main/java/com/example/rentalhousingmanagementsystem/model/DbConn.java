package com.example.rentalhousingmanagementsystem.model;

import com.google.firebase.firestore.FirebaseFirestore;

public class DbConn {
    protected FirebaseFirestore db;
    protected DbConn(){
         db = FirebaseFirestore.getInstance();
    }
}
