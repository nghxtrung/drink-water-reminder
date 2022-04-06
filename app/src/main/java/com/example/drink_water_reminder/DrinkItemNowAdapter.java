package com.example.drink_water_reminder;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.invoke.ConstantCallSite;
import java.util.List;

public class DrinkItemNowAdapter extends RecyclerView.Adapter<DrinkItemNowAdapter.ViewHolder> {
    Activity activity;
    List<DrinkNow> drinkNowList;

    int row_index = TestDB.indexNow;

    public DrinkItemNowAdapter(Activity activity, List<DrinkNow> drinkNowList) {
        this.activity = activity;
        this.drinkNowList = drinkNowList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View drinkItemView = inflater.inflate(R.layout.drink_item_image_text, parent, false);
        ViewHolder viewHolder = new ViewHolder(drinkItemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int drinkItemNowImage = drinkNowList.get(position).getImage();
        int drinkItemNowVolume = drinkNowList.get(position).getVolume();
        holder.drinkItemNowImageView.setImageResource(drinkItemNowImage);
        holder.drinkItemNowVolumeTextView.setText(drinkItemNowVolume + " ml");
        holder.drinkItemNowRVConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });
        if (row_index == position)
            holder.drinkItemNowRVConstraintLayout.setBackgroundColor(Color.parseColor("#e8e8e8"));
        else
            holder.drinkItemNowRVConstraintLayout.setBackgroundColor(Color.WHITE);
    }

    @Override
    public int getItemCount() {
        return drinkNowList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout drinkItemNowRVConstraintLayout;
        ImageView drinkItemNowImageView;
        TextView drinkItemNowVolumeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            drinkItemNowRVConstraintLayout = itemView.findViewById(R.id.drinkItemNowRVConstraintLayout);
            drinkItemNowImageView = itemView.findViewById(R.id.drinkItemNowRVImageView);
            drinkItemNowVolumeTextView = itemView.findViewById(R.id.drinkItemNowVolumeTextView);
        }
    }
}
