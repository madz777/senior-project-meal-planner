package com.example.senior_project.ui.grocery_list;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.senior_project.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GroceryListAdapter extends RecyclerView.Adapter<GroceryListAdapter.MyViewHolder> {
    Context context;
    ArrayList<GroceryModel> groceryModels;

    public GroceryListAdapter(Context context, ArrayList<GroceryModel> groceryModels) {
        this.context = context;
        this.groceryModels = groceryModels;
    }

    @NonNull
    @Override
    public GroceryListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.grocery_list_item, parent, false);

        return new GroceryListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryListAdapter.MyViewHolder holder, int position) {
        holder.groceryName.setText(groceryModels.get(position).getGroceryName());
    }

    @Override
    public int getItemCount() {
        return groceryModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView groceryName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            groceryName = itemView.findViewById(R.id.grocery_item_button);
        }
    }
}
