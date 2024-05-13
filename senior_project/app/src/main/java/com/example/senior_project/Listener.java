package com.example.senior_project;

import android.content.Context;

public interface Listener {
    void onNameInput(String recipeName, Context c);

    interface GPTCallback<T> {
        void onComplete(Result<T> Result);
    }
}