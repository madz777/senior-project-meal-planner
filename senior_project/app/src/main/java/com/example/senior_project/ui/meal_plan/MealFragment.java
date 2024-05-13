package com.example.senior_project.ui.meal_plan;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.senior_project.R;
import com.example.senior_project.databinding.FragmentMealPlanBinding;
import com.example.senior_project.ui.recipe_book.RecipeModel;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MealFragment extends Fragment {

    private FragmentMealPlanBinding binding;

    private Button mealPlanButton;

    ArrayList<MealModel> mealModels = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MealViewModel mealViewModel =
                new ViewModelProvider(this).get(MealViewModel.class);

        binding = FragmentMealPlanBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textMealPlan;
        mealViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        mealPlanButton = root.findViewById(R.id.meal_plan_button);
        MealPlan mealPlan = new MealPlan();
        mealPlan.doMealPlan(getActivity(), mealPlanButton, root);

        RecyclerView recyclerView = root.findViewById(R.id.meal_plan_view);
        getMealPlan();

        MealPlanAdapter adapter = new MealPlanAdapter(getActivity(), mealModels);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getMealPlan() {
        File folder = new File(requireActivity().getFilesDir(), "meal plans");
        File r = new File(folder, "todaysPlan.json");
        if (!r.exists()) return;
        String breakfast;
        String lunch;
        String dinner;

        Object obj;
        try {
            obj = new JSONParser().parse(new FileReader(r.getAbsolutePath()));
            Log.d("success", obj.toString());
            JSONObject jo = (JSONObject) obj;
            breakfast = (String) jo.get("Breakfast");
            //Log.d("success", breakfast);
            lunch = (String) jo.get("Lunch");
            //Log.d("success", lunch);
            dinner = (String) jo.get("Dinner");
            //Log.d("success", dinner);

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        mealModels.add(new MealModel("Breakfast", breakfast));
        mealModels.add(new MealModel("Lunch", lunch));
        mealModels.add(new MealModel("Dinner", dinner));
    }

    public void refreshMealScreen(View root) {

    }
}