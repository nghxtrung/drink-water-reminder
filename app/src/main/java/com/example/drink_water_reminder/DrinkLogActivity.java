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
import android.widget.ProgressBar;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DrinkLogActivity extends AppCompatActivity {
    Connection connection;

    ListView listViewDrinkLog;
    List<DrinkDate> drinkDateList = new ArrayList<DrinkDate>();
    List<DrinkLog> drinkLogList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_log);

        DrinkDatabase drinkDatabase = new DrinkDatabase();
        connection = drinkDatabase.ConnectDatabase();

        if (connection != null) {
            String sqlStatement = "select * from ThoiGian";
            Statement statement = null;
            try {
                statement = connection.createStatement();
                ResultSet set = statement.executeQuery(sqlStatement);
                while (set.next()) {
                    String code = set.getString("MaThoiGian");
                    String date = set.getString("Ngay");
                    int target = set.getInt("MucTieu");
                    drinkDateList.add(new DrinkDate(code, date, target));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        Collections.sort(drinkDateList);

        listViewDrinkLog = findViewById(R.id.listViewDrinkLog);
        DrinkLogAdapter drinkLogAdapter = new DrinkLogAdapter(this, drinkDateList);
        listViewDrinkLog.setAdapter(drinkLogAdapter);
    }

    class DrinkLogAdapter extends BaseAdapter {
        Activity activity;
        List<DrinkDate> drinkDateList;
        List<DrinkLog> drinkLogList;
        LayoutInflater inflater;

        public DrinkLogAdapter(Activity activity, List<DrinkDate> drinkDateList) {
            this.activity = activity;
            this.drinkDateList = drinkDateList;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return drinkDateList.size();
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
                TextView drinkLogSumTextView = v.findViewById(R.id.drinkLogSumTextView);
                ListView drinkLogItemListView = v.findViewById(R.id.drinkLogItemListView);

                drinkLogDateTextView.setText(drinkDateList.get(i).getDate());
                ProgressBar drinkLogDateProgressBar = v.findViewById(R.id.drinkLogDateProgressBar);

                drinkLogList = new ArrayList<DrinkLog>();
                if (connection != null) {
                    String sqlStatement = "select * from ChiTietThoiGian where MaThoiGian = '" + drinkDateList.get(i).getCode() + "'";
                    Statement statement = null;
                    try {
                        statement = connection.createStatement();
                        ResultSet set = statement.executeQuery(sqlStatement);
                        while (set.next()) {
                            String codeTime = set.getString("MaChiTietTG");
                            String time = set.getString("Gio");
                            int volume = set.getInt("LuongNuoc");
                            String dateCode = set.getString("MaThoiGian");
                            int image = set.getInt("Anh");
                            drinkLogList.add(new DrinkLog(codeTime, time, volume, dateCode, image));
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }

                int sumOfVolume = 0;
                for (int j = 0; j < drinkLogList.size(); j++) {
                    sumOfVolume += drinkLogList.get(j).getVolume();
                }
                int percentVolume = Math.round(((float) sumOfVolume/drinkDateList.get(i).getTarget()) * 100);
                drinkLogDateProgressBar.setProgress(percentVolume);

                drinkLogSumTextView.setText(sumOfVolume + " ml");

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
                drinkLogItemTimeTextView.setText(drinkLogList.get(i).getTime());
            }
            return v;
        }
    }
}