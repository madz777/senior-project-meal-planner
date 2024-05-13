package com.example.senior_project.ui.meal_plan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MealViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MealViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}