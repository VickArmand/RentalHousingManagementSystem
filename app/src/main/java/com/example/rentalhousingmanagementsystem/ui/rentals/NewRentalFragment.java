package com.example.rentalhousingmanagementsystem.ui.rentals;

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
import com.example.rentalhousingmanagementsystem.databinding.FragmentNewrentalBinding;
import com.example.rentalhousingmanagementsystem.Firestoremodel.RentalsCrud;

import java.util.HashMap;

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
                        HashMap<String, String> data = new HashMap<String, String>();
                        data.put("name", name);
                        data.put("number_of_rooms", numRooms);
                        new RentalsCrud(getContext()).RegisterRental(data);
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