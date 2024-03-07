package com.example.rentalhousingmanagementsystem.Firestoremodel;

import com.google.firebase.firestore.FirebaseFirestore;

public class DbConn {
    protected FirebaseFirestore db;
    protected DbConn(){
         db = FirebaseFirestore.getInstance();
    }
}
