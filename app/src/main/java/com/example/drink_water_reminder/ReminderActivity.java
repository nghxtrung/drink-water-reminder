package com.example.drink_water_reminder;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class ReminderActivity extends AppCompatActivity {

    TimePicker _timePickerDialog;
    TextView timeEvery;
    TextView timeStartEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        timeEvery = findViewById(R.id.textView4);
        timeStartEnd = findViewById(R.id.textViewStartandEnd);
        if(getIntent().hasExtra("time")){
            String timeEvertuRemider = getIntent().getStringExtra("time");
            timeEvery.setText(timeEvertuRemider);
        }
        else{
            timeEvery.setText("Every 30 minutes");
        }
        if(getIntent().hasExtra("startEnd")){
            String timeStartEndRemider = getIntent().getStringExtra("startEnd");
            timeStartEnd.setText(timeStartEndRemider);
        }
        else{
            timeStartEnd.setText("10:00-21:00");
        }

        LinearLayout linearLayout = findViewById(R.id.lineManually);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogClass cdd=new CustomDialogClass(ReminderActivity.this);
                cdd.show();
                cdd.yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView text = findViewById(R.id.textView5);
                        if(text.getText().equals("MANUALLY SET REMINDERS")){
                            View buttonShowManually = findViewById(R.id.buttonManuallySetting);
                            buttonShowManually.setVisibility(View.VISIBLE);
                            View buttonShow = findViewById(R.id.btnShow);
                            text.setText("Auto Remider");
                            buttonShow.setVisibility(View.GONE);
                            cdd.dismiss();
                        }else if(text.getText().equals("Auto Remider")) {
                            View buttonShowManually = findViewById(R.id.buttonManuallySetting);
                            buttonShowManually.setVisibility(View.GONE);
                            View buttonShow = findViewById(R.id.btnShow);
                            buttonShow.setVisibility(View.VISIBLE);
                            text.setText("MANUALLY SET REMINDERS");
                            cdd.dismiss();
                        }
                    }
                });
            }
        });

    Button buttonSettingManully = findViewById(R.id.buttonManuallySetting);
    buttonSettingManully.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(ReminderActivity.this,ManuallyReminderSetting.class);
            startActivity(i);
        }
    });



    }

    public void showPopup(View v) {
        Menu optionsMenu;
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.optionmenu_item, popup.getMenu());
        popup.show();
        optionsMenu = popup.getMenu();
        MenuItem item = optionsMenu.findItem(R.id.itemSetUpPicker);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(ReminderActivity.this,ReminderIntervalActivity.class);
                startActivity(intent);
                return true;
            }
        });
        MenuItem item1 = optionsMenu.findItem(R.id.itemSetUpStartandEnd);
        item1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(ReminderActivity.this,ReminderStartandEnd.class);
                startActivity(intent);
                return true;
            }
        });

    }








}


