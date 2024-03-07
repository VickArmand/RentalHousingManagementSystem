package com.example.rentalhousingmanagementsystem.Firestoremodel;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
public class Auth {
    // Initialize Firebase Auth
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    protected static FirebaseUser currentUser;
    private final Context context;
    public Auth(Context context)
    {
        currentUser = mAuth.getCurrentUser();
        this.context = context;
    }
    public static FirebaseUser getCurrentUser()
    {
        return currentUser;
    }
    public FirebaseUser Login(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.v("AuthLogin", "signInWithCredential:success");
                            currentUser = mAuth.getCurrentUser();
                            Log.v("CurrentUser", "Value "+currentUser.toString());
                            Toast.makeText(context,"Login success", Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.v("AuthLogin", "signInWithCredential:failure" + task.getException());
                            Log.v("CurrentUser", "Value "+currentUser.toString());
                            currentUser = null;
                            Log.v("CurrentUser", "Value "+currentUser.toString());
                            Toast.makeText(context,"Incorrect Email Address or Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return currentUser;
    }
    public void GoogleLogin(){
//        Object signInRequest = BeginSignInRequest.builder().setGoogleIdTokenRequestOptions(GoogleIdTokenRequestOptions.builder()
//                .setSupported(true)
//                // Your server's client ID, not your Android client ID.
//                .setServerClientId(getString(R.string.default_web_client_id))
//                // Only show accounts previously used to sign in.
//                .setFilterByAuthorizedAccounts(true)
//                .build());
//        SignInCredential googleCredential = oneTapClient.getSignInCredentialFromIntent(data);
//        String idToken = googleCredential.getGoogleIdToken();
//        if (idToken !=  null) {
//            // Got an ID token from Google. Use it to authenticate with Firebase.
//            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
//            mAuth.signInWithCredential(firebaseCredential)
//                    .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                // Sign in success, update UI with the signed-in user's information
//                                Log.d(TAG, "signInWithCredential:success");
//                                currentUser = mAuth.getCurrentUser();
//                            } else {
//                                // If sign in fails, display a message to the user.
//                                Log.d(TAG, "signInWithCredential:failure", task.getException());
//                            }
//                        }
//                    });
//        }
    }
    public void Logout(){
        FirebaseAuth.getInstance().signOut();
    }
    public void Register(HashMap data) {
        String email = (String) data.get("email");
        String password = (String) data.get("password");
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                        } else {
                            Log.d(TAG, "signInWithCredential:failure", task.getException());
                        }
                }
        });
    }
}