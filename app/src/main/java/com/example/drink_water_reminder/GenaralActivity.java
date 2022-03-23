package com.example.drink_water_reminder;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class GenaralActivity extends AppCompatActivity {
    ListView lvGenaral;
    ArrayList GenaralList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genaral);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//action return to menu toolbar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.title_toolbar)));

        lvGenaral = (ListView) findViewById(R.id.listViewDrinkLog);
        GenaralList = new ArrayList<String>();

        GenaralList.add("Weight & Goal");
        GenaralList.add("Cup Capacity units");
        GenaralList.add("Weight units");
        GenaralList.add("Date format");
        GenaralList.add("Use 24-hour clock");
        ArrayAdapter adapter = new ArrayAdapter(
                GenaralActivity.this,
                android.R.layout.simple_list_item_1,GenaralList
        );
        lvGenaral.setAdapter(adapter);
    }
}