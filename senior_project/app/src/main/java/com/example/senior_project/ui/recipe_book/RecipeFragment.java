package com.example.senior_project.ui.recipe_book;

import android.app.Dialog;
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
import com.example.senior_project.databinding.FragmentRecipeBookBinding;

import java.io.File;
import java.util.ArrayList;

public class RecipeFragment extends Fragment {

    private FragmentRecipeBookBinding binding;
    private Button recipeButton;

    ArrayList<RecipeModel> recipeModels = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RecipeViewModel recipeViewModel =
                new ViewModelProvider(this).get(RecipeViewModel.class);

        binding = FragmentRecipeBookBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textRecipeBook;
        recipeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        recipeButton = root.findViewById(R.id.gen_recipe_button);
        RecipeBook b = new RecipeBook();
        b.doRecipeFromName(getActivity(), recipeButton);

        root.findViewById(R.id.view_recipes_button).setOnClickListener(view -> {
            showRecipeBook();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void showRecipeBook() {
        Dialog d = new Dialog(requireActivity());
        d.setContentView(R.layout.recipes_list);
        d.show();

        RecyclerView r = d.findViewById(R.id.recipe_book_list);

        getRecipes();

        RecipeBookAdapter c = new RecipeBookAdapter(getActivity(), recipeModels);

        r.setAdapter(c);
        r.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void getRecipes() {
        Log.d("success", "parse start");
        File folder = new File(requireActivity().getFilesDir(), "recipes");
        File[] files = folder.listFiles();
        if(files != null) {
            try {
                for (File file : files) {
                    boolean unique = true;
                    if (file.isFile()) {
                        String name = file.getName().replaceFirst("[.][^.]+$", "");
                        for (RecipeModel recipe : recipeModels) {
                            Log.d("success", recipe.getRecipeName());
                            if (name.equals(recipe.getRecipeName())) {
                                unique = false;
                                break;
                            }
                        }
                        Log.d("success", file.getName() + " " + unique);
                        if (unique) recipeModels.add(new RecipeModel(name));
                    } else {
                        Log.d("success", "failure");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}