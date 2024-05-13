package com.example.senior_project.ui.meal_plan;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.senior_project.R;
import com.example.senior_project.ui.recipe_book.RecipeModel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MealPlanAdapter extends RecyclerView.Adapter<MealPlanAdapter.MyViewHolder> {
    Context context;
    ArrayList<MealModel> mealModels;

    public MealPlanAdapter(Context c, ArrayList<MealModel> a) {
        this.context = c;
        this.mealModels = a;
    }
    @NonNull
    @Override
    public MealPlanAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater i = LayoutInflater.from(context);
        View v = i.inflate(R.layout.meal_plan_list_item, parent, false);

        return new MealPlanAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MealPlanAdapter.MyViewHolder holder, int position) {

        String name = mealModels.get(position).getRecipeName();
        //Log.d("success", name);
        holder.recipeButton.setText(name);
        holder.mealName.setText(mealModels.get(position).getMealName());

        holder.recipeButton.setOnClickListener(view -> {
            try {
                showRecipe(context, name);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mealModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        Button recipeButton;
        TextView mealName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeButton = itemView.findViewById(R.id.list_item_button);
            mealName = itemView.findViewById(R.id.meal_name_text);
        }
    }

    @SuppressWarnings("unchecked")
    private void showRecipe(Context c, String recipeName) throws ParseException, IOException {
        Dialog d = new Dialog(c);
        d.setContentView(R.layout.recipe_view);
        d.show();

        //Find file in filesys with RecipeName
        File dir = new File(context.getFilesDir(), "recipes");
        File r = new File(dir, recipeName + ".json");
        StringBuilder ingredients = new StringBuilder();
        StringBuilder steps = new StringBuilder();

        Object obj;
        try {
            obj = new JSONParser().parse(new FileReader(r.getAbsolutePath()));
            Log.d("success", obj.toString());
            JSONObject jo = (JSONObject) obj;
            JSONArray ingArray = (JSONArray)  jo.get("Ingredients");

            assert ingArray != null;
            ingArray.forEach( ing -> {ingredients.append(parseIngredients( (JSONObject) ing)); });

            JSONArray stepsArray = (JSONArray) jo.get("Instructions");
            assert stepsArray != null;
            stepsArray.forEach( step -> {steps.append(parseInstructions( (JSONObject) step)); });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //ingredients = (String) jo.get("Ingredients");
        //String instructions = (String) jo.get("Instructions");

        TextView tv1 = d.findViewById(R.id.recipe_view_name);
        tv1.setText(recipeName);
        TextView tv2 = d.findViewById(R.id.recipe_view_ingredients);
        tv2.setText(ingredients);
        TextView tv3 = d.findViewById(R.id.recipe_view_steps);
        tv3.setText(steps);
    }

    private static String parseIngredients(JSONObject ingredient) {
        StringBuilder s = new StringBuilder();
        Log.d("success", ingredient.toString());

        //Get ingredient name
        String item = (String) ingredient.get("Item");

        //Get ingredient quantity
        String quantity = (String) ingredient.get("Quantity");

        s.append(item + " x " + quantity);

        Log.d("success", s.toString());
        return s.toString().concat("\n");
    }

    private static String parseInstructions(JSONObject instruction) {
        StringBuilder s = new StringBuilder();
        Log.d("success", instruction.toString());

        //Get ingredient name
        String step = (String) instruction.get("Step");

        //Get ingredient quantity
        String text = (String) instruction.get("Instruction");

        s.append(step + ". " + text);

        Log.d("success", s.toString());
        return s.toString().concat("\n");
    }


}
