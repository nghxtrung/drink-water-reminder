package com.example.drink_water_reminder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DrinkLogActivity extends AppCompatActivity {
    ListView listViewDrinkLog;
    String date[] = {"09-04-2022", "10-04-2022", "11-04-2022"};
    List<DrinkLog> drinkLogList = new ArrayList<DrinkLog>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_log);

        drinkLogList.add(new DrinkLog(R.drawable.water, 300, "12:00"));
        drinkLogList.add(new DrinkLog(R.drawable.water, 400, "12:00"));
        drinkLogList.add(new DrinkLog(R.drawable.water, 500, "12:00"));

        listViewDrinkLog = findViewById(R.id.listViewDrinkLog);
        DrinkLogAdapter drinkLogAdapter = new DrinkLogAdapter(this, date, drinkLogList);
        listViewDrinkLog.setAdapter(drinkLogAdapter);
    }

    class DrinkLogAdapter extends BaseAdapter {
        Activity activity;
        String date[];
        List<DrinkLog> drinkLogList;
        LayoutInflater inflater;

        public DrinkLogAdapter(Activity activity, String[] date, List<DrinkLog> drinkLogList) {
            this.activity = activity;
            this.date = date;
            this.drinkLogList = drinkLogList;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return date.length;
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
                v = inflater.inflate(R.layout.listview_drinklog, null);
                TextView drinkLogDateTextView = v.findViewById(R.id.drinkLogDateTextView);
                ListView drinkLogItemListView = v.findViewById(R.id.drinkLogItemListView);

                drinkLogDateTextView.setText(date[i]);
                DrinkLogItemAdapter drinkLogItemAdapter = new DrinkLogItemAdapter(activity, drinkLogList);
                drinkLogItemListView.setAdapter(drinkLogItemAdapter);
                drinkLogItemListView.getLayoutParams().height = caculateHeight(drinkLogItemListView, drinkLogList.size() * 55);
                drinkLogItemListView.setDivider(null);
            }
            return v;
        }

        private int caculateHeight(ListView drinkLogItemListView, int dp) {
            final float scale = drinkLogItemListView.getResources().getDisplayMetrics().density;
            int pixels = (int) (dp * scale + 0.5f);
            return pixels;
        }
    }

    class DrinkLogItemAdapter extends BaseAdapter {
        Activity activity;
        List<DrinkLog> drinkLogList;
        LayoutInflater inflater;

        public DrinkLogItemAdapter(Activity activity, List<DrinkLog> drinkLogList) {
            this.activity = activity;
            this.drinkLogList = drinkLogList;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return drinkLogList.size();
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
                v =inflater.inflate(R.layout.listview_itemdrinklog, null);
                ImageView drinkLogItemImageView = v.findViewById(R.id.drinkLogItemImageView);
                TextView drinkLogItemVolumeTextView = v.findViewById(R.id.drinkLogItemVolumeTextView);
                TextView drinkLogItemTimeTextView = v.findViewById(R.id.drinkLogItemTimeTextView);

                drinkLogItemImageView.setImageResource(drinkLogList.get(i).getImage());
                drinkLogItemVolumeTextView.setText(drinkLogList.get(i).getVolume() + " ml");
                drinkLogItemTimeTextView.setText("12:00");
            }
            return v;
        }
    }
}