package com.example.rentalhousingmanagementsystem.ui.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewRentalViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public NewRentalViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}