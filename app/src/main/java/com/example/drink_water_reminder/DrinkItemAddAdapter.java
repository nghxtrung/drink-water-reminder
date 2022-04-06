package com.example.drink_water_reminder;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class DrinkItemAddAdapter extends RecyclerView.Adapter<DrinkItemAddAdapter.ViewHolder> {
    Activity activity;
    Integer[] drinkItemAddImageList;

    int row_index = 0;

    public DrinkItemAddAdapter(Activity activity, Integer[] drinkItemAddImageList) {
        this.drinkItemAddImageList = drinkItemAddImageList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View drinkItemView = inflater.inflate(R.layout.drink_item_image, parent, false);
        ViewHolder viewHolder = new ViewHolder(drinkItemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Integer drinkItemAddImage = drinkItemAddImageList[position];
        holder.drinkItemAddImageView.setImageResource(drinkItemAddImage);
        holder.drinkItemAddRVConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });
        if (row_index == position)
            holder.drinkItemAddRVConstraintLayout.setBackgroundColor(Color.parseColor("#e8e8e8"));
        else
            holder.drinkItemAddRVConstraintLayout.setBackgroundColor(Color.WHITE);
    }

    @Override
    public int getItemCount() {
        return drinkItemAddImageList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout drinkItemAddRVConstraintLayout;
        ImageView drinkItemAddImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            drinkItemAddRVConstraintLayout = itemView.findViewById(R.id.drinkItemAddRVConstraintLayout);
            drinkItemAddImageView = itemView.findViewById(R.id.drinkItemAddRVImageView);
        }
    }
}
