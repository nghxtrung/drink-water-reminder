package com.example.drink_water_reminder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReminderStartandEnd extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_startand_end);
        StringBuilder s = new StringBuilder();
        EditText editTextStart = findViewById(R.id.editTextStart);
        editTextStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int gio = calendar.get(Calendar.HOUR_OF_DAY);
                int phut = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog  = new TimePickerDialog(ReminderStartandEnd.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i,  int i1) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                        calendar.set(0,0,0,i,i1);
                        editTextStart.setText(simpleDateFormat.format(calendar.getTime()));
                        s.append(simpleDateFormat.format(calendar.getTime()));
                        s.append("-");
                    }
                },gio,phut,true);
                timePickerDialog.show();
            }
        });
       EditText editTextEnd = findViewById(R.id.editTextEnd);
        editTextEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int gio = calendar.get(Calendar.HOUR_OF_DAY);
                int phut = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog  = new TimePickerDialog(ReminderStartandEnd.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i,  int i1) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                        calendar.set(0,0,0,i,i1);
                        editTextEnd.setText(simpleDateFormat.format(calendar.getTime()));
                        s.append(simpleDateFormat.format(calendar.getTime()));
                    }
                },gio,phut,true);
                timePickerDialog.show();
            }
        });
        Button buttonOK = findViewById(R.id.buttonOk);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ReminderStartandEnd.this,ReminderActivity.class);
                i.putExtra("startEnd", (Serializable) s);
                startActivity(i);
            }
        });


    }



}