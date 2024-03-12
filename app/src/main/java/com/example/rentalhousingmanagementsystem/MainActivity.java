package com.example.rentalhousingmanagementsystem;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.rentalhousingmanagementsystem.databinding.ActivityMainBinding;
import com.example.rentalhousingmanagementsystem.Firestoremodel.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private EditText emailTextView, passwordTextView;
    private Button btnLogin;
    private Context context;
    protected static FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        context = getApplicationContext();
        currentUser = Auth.getCurrentUser();
        if (currentUser != null)
        {
            clearIntentStack(context, com.example.rentalhousingmanagementsystem.rentals.class);
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        btnLogin = findViewById(R.id.btnlogin);
        emailTextView = findViewById(R.id.txtemail);
        passwordTextView = findViewById(R.id.txtpassword);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = emailTextView.getText().toString();
                password = passwordTextView.getText().toString();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
                    Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                else {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, rentals.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(getApplicationContext(), "Incorrect Email Address or Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                    });
                }
            }
        });
    }
    public void clearIntentStack(Context context, Class cls)
    {
        Intent intent = new Intent(context, cls);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    public void nextIntent(Context context, Class cls)
    {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }
}