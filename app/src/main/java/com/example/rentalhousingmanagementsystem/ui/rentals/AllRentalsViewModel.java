package com.example.rentalhousingmanagementsystem.ui.rentals;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AllRentalsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AllRentalsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}