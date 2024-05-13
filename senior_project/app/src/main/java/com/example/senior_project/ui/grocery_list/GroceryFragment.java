package com.example.senior_project.ui.grocery_list;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.senior_project.R;
import com.example.senior_project.databinding.FragmentGroceryListBinding;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class GroceryFragment extends Fragment {

    private FragmentGroceryListBinding binding;

    ArrayList<GroceryModel> groceryModels = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GroceryViewModel groceryViewModel =
                new ViewModelProvider(this).get(GroceryViewModel.class);

        binding = FragmentGroceryListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGroceryList;
        groceryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        //INITIALIZE BUTTON FUNCTIONS
        root.findViewById(R.id.pantry_view_button).setOnClickListener(view -> {
            showPantry();
        });

        RecyclerView recyclerView = root.findViewById(R.id.grocery_list_view);

        getGroceries();

        GroceryListAdapter adapter = new GroceryListAdapter(getActivity(), groceryModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void showPantry() {
        Dialog d = new Dialog(requireContext());
        d.setContentView(R.layout.pantry_view);
        d.show();
    }

    private void getGroceries() {
        searchRecipes(findPlannedMeals());
    }

    private ArrayList<String> findPlannedMeals() {
        File folder = new File(requireActivity().getFilesDir(), "meal plans");
        File r = new File(folder, "todaysPlan.json");
        if (!r.exists()) return null;
        ArrayList<String> plannedMeals = new ArrayList<>();

        Object obj;
        try {
            obj = new JSONParser().parse(new FileReader(r.getAbsolutePath()));
            Log.d("success", obj.toString());
            JSONObject jo = (JSONObject) obj;
            plannedMeals.add((String) jo.get("Breakfast"));
            plannedMeals.add((String) jo.get("Lunch"));
            plannedMeals.add((String) jo.get("Dinner"));
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        return plannedMeals;
    }

    @SuppressWarnings("unchecked")
    private void searchRecipes(ArrayList<String> plannedMeals) {
        //Find file in filesys with RecipeName
        File dir = new File(requireActivity().getFilesDir(), "recipes");
        //File r = new File(dir, recipeName + ".json");
        ArrayList<String> ingredientsNeeded = new ArrayList<>();

        File[] files = dir.listFiles();
        if (files != null) {
            try {
                for (File file : files) {
                    if (file.isFile()) {
                        String name = file.getName().replaceFirst("[.][^.]+$", "");
                        for (String meal : plannedMeals) {
                            if (meal.equals(name)) {
                                Object obj;
                                try {
                                    obj = new JSONParser().parse(new FileReader(file.getAbsolutePath()));
                                    Log.d("success", obj.toString());
                                    JSONObject jo = (JSONObject) obj;
                                    JSONArray ingArray = (JSONArray)  jo.get("Ingredients");
                                    assert ingArray != null;
                                    ingArray.forEach( ing -> {ingredientsNeeded.add(parseGroceries( (JSONObject) ing)); });
                                } catch (IOException | ParseException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            checkDupes(ingredientsNeeded);
        }
    }


    private String parseGroceries(JSONObject ingredient) {
        return (String) ingredient.get("Item");
    }

    private void checkDupes(ArrayList<String> groceries) {
        for (String g : groceries) {
            boolean unique = true;
            for (String gCheck : groceries) {
                if (g.equals(gCheck)) {
                    unique = false;
                    break;
                }
            }
            groceryModels.add(new GroceryModel(g));

        }
    }
}