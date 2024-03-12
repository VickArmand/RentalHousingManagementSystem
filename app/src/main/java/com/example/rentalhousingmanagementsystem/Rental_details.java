package com.example.rentalhousingmanagementsystem;

import static android.content.Intent.getIntent;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.rentalhousingmanagementsystem.ui.main.SectionsPagerAdapter;
import com.example.rentalhousingmanagementsystem.databinding.ActivityRentalDetailsBinding;

public class Rental_details extends AppCompatActivity {

    private ActivityRentalDetailsBinding binding;
    private String rentalID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRentalDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        rentalID = getIntent().getStringExtra("rentalID");
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), rentalID);
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
    }
}