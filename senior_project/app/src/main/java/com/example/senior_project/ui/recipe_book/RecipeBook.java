package com.example.senior_project.ui.recipe_book;

import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.senior_project.Listener;
import com.example.senior_project.MainActivity;
import com.example.senior_project.Result;
import com.example.senior_project.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RecipeBook extends MainActivity implements Listener {

    public void doRecipeFromName(Context c, Button b) {
        //Log.d("success", "Generation Start");
        Util.newInputDialog(c, b, this, "Generate New Recipe", "Generate", "Cancel");
    }

    public void onNameInput(String recipeName, Context c) {
        final Listener.GPTCallback<String> callback = Result -> {
            if (Result instanceof com.example.senior_project.Result.Success) {
                String text = ((Result.Success<String>) Result).data.replace("\\n", "%n").replace("\\", "");
                Log.d("success", text);
                Util.saveFile(c, "recipes", recipeName + ".json", text);

                Looper.prepare();
                Toast.makeText(c, "Recipe Generated", Toast.LENGTH_SHORT).show();

            } else {
                // Show error in UI
            }
        };

        Thread thread = new Thread(() -> {
            try {
                //String query = generateQuery(recipeName);
                String query = ("Write a recipe, with ingredients and instructions to make " + recipeName + ", making it as simple and cost-effective to construct as possible. Return the recipe in JSON format. Specifically, identify the recipe's Name, Ingredients organized by Item and Quantity, and Instructions organized by Step and Instruction");
                String s = Util.queryGPT(query.replace("\n", " ").replace("\\", ""));
                Result<String> result = new Result.Success<>(s);
                callback.onComplete(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
}