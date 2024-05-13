package com.example.senior_project.ui.grocery_list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GroceryViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public GroceryViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}