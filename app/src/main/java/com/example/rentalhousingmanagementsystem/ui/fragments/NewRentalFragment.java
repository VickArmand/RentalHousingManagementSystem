package com.example.rentalhousingmanagementsystem.ui.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rentalhousingmanagementsystem.Firestoremodel.Auth;
import com.example.rentalhousingmanagementsystem.databinding.FragmentNewrentalBinding;
import com.example.rentalhousingmanagementsystem.Firestoremodel.RentalsCrud;
import com.example.rentalhousingmanagementsystem.models.Rentals;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class NewRentalFragment extends Fragment {

private FragmentNewrentalBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        NewRentalViewModel newRentalViewModel =
                new ViewModelProvider(this).get(NewRentalViewModel.class);

    binding = FragmentNewrentalBinding.inflate(inflater, container, false);
    View root = binding.getRoot();
    Button addRental = binding.btnaddRental;

    addRental.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String name = binding.txtname.getText().toString();
            String numRooms = binding.txtnumrooms.getText().toString();
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(numRooms))
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            else {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                        Rentals rental;
                        try {
                            rental = new Rentals(name, Integer.parseInt(numRooms), "Open", user, user);
                            HashMap<String, Object> data = new HashMap<String, Object>();
                            data.put("name", Objects.requireNonNull(rental.getName()));
                            data.put("number_of_rooms", Objects.requireNonNull(rental.getNumber_of_rooms()));
                            data.put("status", Objects.requireNonNull(rental.getStatus()));
                            data.put("created_at", Objects.requireNonNull(rental.getCreated_at()));
                            data.put("updated_at", Objects.requireNonNull(rental.getUpdated_at()));
                            new RentalsCrud(getContext()).RegisterRental(data);
                        } catch (ParseException e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);
                        }
                    }
                });
                t.start();
            }
        }
    });
        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}