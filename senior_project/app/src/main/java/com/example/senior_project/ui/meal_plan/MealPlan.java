package com.example.senior_project.ui.meal_plan;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.senior_project.Listener;
import com.example.senior_project.MainActivity;
import com.example.senior_project.Result;
import com.example.senior_project.Util;

import java.io.File;

public class MealPlan extends MainActivity {

    public void doMealPlan(Context c, Button b, View root) {
        b.setOnClickListener(view -> {
            Log.d("success", "Meal Plan Start");
            Toast.makeText(c, "Generating Meal Plan", Toast.LENGTH_SHORT).show();
            final Listener.GPTCallback<String> callback = Result -> {
                if (Result instanceof com.example.senior_project.Result.Success) {
                    String text = ((Result.Success<String>) Result).data.replace("\\n", "%n").replace("\\", "");
                    Log.d("success", text);
                    Util.saveFile(c, "meal plans", "todaysPlan.json", text);

                    Looper.prepare();
                    Toast.makeText(c, "Meal Plan Generated", Toast.LENGTH_LONG).show();

                } else {
                    // Show error in UI
                }
            };
            Thread thread = new Thread(() -> {
                try {
                    //String query = generateQuery(recipeName);
                    String query = ("Generate a single day's meal plan, consisting of 3 meals based on the following list of recipes. You do not need to use all of the recipes listed, and you do not need to list anything other than the name of the recipe. Recipes: " + recipeParser(c) + "Return the meal plan in JSON format. Please use proper capital lettering. Try to vary what meals you use in sequence, but try to minimize the number of unique ingredients overall. ");
                    String s = Util.queryGPT(query);
                    Log.d("success", query);
                    Result<String> result = new Result.Success<>(s);
                    callback.onComplete(result);
                } catch (Exception e) {
                    //Log.d("success", "Query failed");
                    e.printStackTrace();
                }
            });
            thread.start();
        });
    }

    public String recipeParser(Context c) {
        StringBuilder recipes = new StringBuilder();
        Log.d("success", "parse start");
        File folder = new File(c.getFilesDir(), "recipes");
        File[] files = folder.listFiles();
        if(files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    recipes.append(file.getName().replaceFirst("[.][^.]+$", "")).append(", ");
                } else if (file.isDirectory()) {
                    System.out.println("Directory " + file.getName());
                }
            }
            Log.d("success", recipes.toString());
        }
        return recipes.toString();
    }
}
