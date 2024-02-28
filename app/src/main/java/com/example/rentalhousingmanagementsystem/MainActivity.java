package com.example.rentalhousingmanagementsystem;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.rentalhousingmanagementsystem.databinding.ActivityMainBinding;
import com.example.rentalhousingmanagementsystem.model.Auth;


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
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
                    Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                else {
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            new Auth(getApplicationContext()).Login(email, password);
                        }
                    });
                    t.start();
                }
            }
        });
    }
}