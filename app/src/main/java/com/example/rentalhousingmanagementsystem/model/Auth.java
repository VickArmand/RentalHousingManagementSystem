package com.example.rentalhousingmanagementsystem.model;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.HashMap;
import java.util.concurrent.Executor;

public class Auth {
    private FirebaseAuth mAuth;
    protected FirebaseUser currentUser;
    protected Auth()
    {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    protected FirebaseUser Login(HashMap data){
        String email = (String) data.get("email");
        String password = (String) data.get("password");
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            currentUser = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
        return currentUser;
    }
    protected FirebaseUser GoogleLogin(){
        Object signInRequest = BeginSignInRequest.builder().setGoogleIdTokenRequestOptions(GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                // Your server's client ID, not your Android client ID.
                .setServerClientId(getString(R.string.default_web_client_id))
                // Only show accounts previously used to sign in.
                .setFilterByAuthorizedAccounts(true)
                .build());
        SignInCredential googleCredential = oneTapClient.getSignInCredentialFromIntent(data);
        String idToken = googleCredential.getGoogleIdToken();
        if (idToken !=  null) {
            // Got an ID token from Google. Use it to authenticate with Firebase.
            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithCredential:success");
                                currentUser = mAuth.getCurrentUser();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.d(TAG, "signInWithCredential:failure", task.getException());
                            }
                        }
                    });
        }
        return currentUser;
    }
    protected void Logout(){
        FirebaseAuth.getInstance().signOut();
    }
    protected boolean[] Register(HashMap data) {
        String email = (String) data.get("email");
        String password = (String) data.get("password");
        final boolean[] success = {false};
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            success[0] = true;
                        } else {
                            Log.d(TAG, "signInWithCredential:failure", task.getException());
                        }
                }
        });
        return success;
    }
}