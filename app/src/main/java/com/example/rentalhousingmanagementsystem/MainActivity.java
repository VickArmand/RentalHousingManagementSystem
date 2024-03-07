package com.example.rentalhousingmanagementsystem;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.rentalhousingmanagementsystem.databinding.ActivityMainBinding;
import com.example.rentalhousingmanagementsystem.Firestoremodel.Auth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private EditText emailTextView, passwordTextView;
    private Button btnLogin;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Auth authObj = new Auth(getApplicationContext());
        currentUser = Auth.getCurrentUser();
        if (currentUser != null)
        {
            clearIntentStack(getApplicationContext(), com.example.rentalhousingmanagementsystem.rentals.class);
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
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            currentUser = authObj.Login(email, password);
                            if (currentUser != null) {
                                clearIntentStack(getApplicationContext(), rentals.class);
                            }
                        }
                    });
                    t.start();
                }
            }
        });
    }
    public void clearIntentStack(Context context, Class cls)
    {
        Intent intent = new Intent(context, cls);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }
    public void nextIntent(Context context, Class cls)
    {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }
}