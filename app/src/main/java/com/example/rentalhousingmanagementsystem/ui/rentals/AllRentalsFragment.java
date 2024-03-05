package com.example.rentalhousingmanagementsystem.ui.rentals;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalhousingmanagementsystem.databinding.FragmentAllrentalsBinding;
import com.example.rentalhousingmanagementsystem.model.RentalsCrud;
import com.example.rentalhousingmanagementsystem.rentalsview.RentalsAdapter;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class AllRentalsFragment extends Fragment {

private FragmentAllrentalsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        AllRentalsViewModel allRentalsViewModel =
                new ViewModelProvider(this).get(AllRentalsViewModel.class);

    binding = FragmentAllrentalsBinding.inflate(inflater, container, false);
    View root = binding.getRoot();
        final RecyclerView rv = binding.rentalsrv;
        ArrayList<DocumentSnapshot> rentals = new RentalsCrud(getContext()).AllRentals();
        Log.v("record size", ""+rentals.size());
        if (rentals.size() > 0)
        {
            Toast.makeText(getContext(), "Rentals available", Toast.LENGTH_SHORT);
            RentalsAdapter rad = new RentalsAdapter(getContext(), rentals);
            rv.setVisibility(View.VISIBLE);
            rv.setLayoutManager(new LinearLayoutManager(getActivity()));
            rv.setAdapter(rad);
        }
        else
        {
            rv.setVisibility(View.GONE);
            Toast.makeText(getContext(), "No rentals available", Toast.LENGTH_SHORT);
        }
        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}