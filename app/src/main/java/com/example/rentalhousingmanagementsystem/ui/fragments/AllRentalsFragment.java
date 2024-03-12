package com.example.rentalhousingmanagementsystem.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalhousingmanagementsystem.databinding.FragmentAllrentalsBinding;
import com.example.rentalhousingmanagementsystem.Firestoremodel.RentalsCrud;

public class AllRentalsFragment extends Fragment {

private FragmentAllrentalsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        AllRentalsViewModel allRentalsViewModel =
                new ViewModelProvider(this).get(AllRentalsViewModel.class);

    binding = FragmentAllrentalsBinding.inflate(inflater, container, false);
    View root = binding.getRoot();
        ProgressDialog pd = new ProgressDialog(getContext());
        final RecyclerView rv = binding.rentalsrv;
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        new RentalsCrud(getContext()).AllRentals(rv, pd);
        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}