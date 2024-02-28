package com.example.rentalhousingmanagementsystem;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import com.example.rentalhousingmanagementsystem.controller.AuthController;
import com.example.rentalhousingmanagementsystem.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private EditText emailTextView, passwordTextView;
    private Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new AuthController().LoginController(email, password);
                    }
                });
            }
        });
    }
}