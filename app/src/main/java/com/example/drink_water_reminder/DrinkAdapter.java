package com.example.drink_water_reminder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class DrinkAdapter extends BaseAdapter {

    Activity activity;
    List<DrinkAlarm> drinkAlarmList;

    LayoutInflater inflater;

    public DrinkAdapter(Activity activity, List<DrinkAlarm> drinkAlarmList) {
        this.activity = activity;
        this.drinkAlarmList = drinkAlarmList;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return drinkAlarmList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            v = inflater.inflate(R.layout.drink_alarm_item, null);
            ImageView drinkAlarmImageView = v.findViewById(R.id.drinkAlarmImageView);
            TextView volumeTextView = v.findViewById(R.id.volumeTextView);
            TextView drinkTimeTextView = v.findViewById(R.id.drinkTimeTextView);
            drinkAlarmImageView.setImageResource(drinkAlarmList.get(i).getImage());
            if (drinkAlarmList.get(i).getVolume() > 0) {
                volumeTextView.setVisibility(View.VISIBLE);
                volumeTextView.setText(drinkAlarmList.get(i).getVolume() + " ml");
            } else
                volumeTextView.setVisibility(View.GONE);
            drinkTimeTextView.setText(drinkAlarmList.get(i).getTime());
        }
        return v;
    }
}
