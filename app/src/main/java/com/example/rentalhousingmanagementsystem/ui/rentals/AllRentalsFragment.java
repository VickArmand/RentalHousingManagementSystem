package com.example.rentalhousingmanagementsystem.ui.rentals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalhousingmanagementsystem.databinding.FragmentAllrentalsBinding;

public class AllRentalsFragment extends Fragment {

private FragmentAllrentalsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        AllRentalsViewModel allRentalsViewModel =
                new ViewModelProvider(this).get(AllRentalsViewModel.class);

    binding = FragmentAllrentalsBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        final RecyclerView rentals = binding.rentals;
        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}