package com.example.rentalhousingmanagementsystem.ui.rentals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.rentalhousingmanagementsystem.databinding.FragmentNewrentalBinding;

public class NewRentalFragment extends Fragment {

private FragmentNewrentalBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        NewRentalViewModel newRentalViewModel =
                new ViewModelProvider(this).get(NewRentalViewModel.class);

    binding = FragmentNewrentalBinding.inflate(inflater, container, false);
    View root = binding.getRoot();
        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}