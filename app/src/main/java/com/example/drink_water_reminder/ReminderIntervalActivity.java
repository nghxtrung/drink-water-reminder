package com.example.drink_water_reminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.Serializable;

public class ReminderIntervalActivity extends AppCompatActivity {

    TimePicker timePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_interval);
        Button button = findViewById(R.id.buttonSelect);
        timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickTime();
            }
        });
    }

    public void pickTime(){
        StringBuilder s = new StringBuilder();
        s.append(timePicker.getCurrentHour()*60 + ":");
        s.append(timePicker.getCurrentMinute());
        Intent intent = new Intent(ReminderIntervalActivity.this,ReminderActivity.class);
        intent.putExtra("time","Every " + s + "minutes");
        startActivity(intent);
        Toast.makeText(ReminderIntervalActivity.this,s,Toast.LENGTH_SHORT).show();
    }

}