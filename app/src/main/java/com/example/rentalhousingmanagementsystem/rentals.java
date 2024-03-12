package com.example.rentalhousingmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.Toast;

import com.example.rentalhousingmanagementsystem.Firestoremodel.Auth;
import com.google.android.material.navigation.NavigationView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.rentalhousingmanagementsystem.databinding.ActivityRentalsBinding;
import com.google.firebase.auth.FirebaseAuth;

public class rentals extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
private ActivityRentalsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     binding = ActivityRentalsBinding.inflate(getLayoutInflater());
     setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarRentals.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_new_rental, R.id.nav_all_rentals)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_rentals);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.rentals, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_rentals);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_rentals);
        if (item.getItemId() == R.id.action_logout) {
            Toast.makeText(this,"Logout Success", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            Log.v("logout", "success");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
//        NavigationUI.onNavDestinationSelected(item, navController) ||
        return super.onOptionsItemSelected(item);
    }
}