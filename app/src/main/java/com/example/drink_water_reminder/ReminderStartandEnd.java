package com.example.drink_water_reminder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReminderStartandEnd extends AppCompatActivity {

    int hourEnd, minuteEnd,hourStart,minuteStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_startand_end);
        EditText editTextStart = findViewById(R.id.editTextStart);
        EditText editTextEnd = findViewById(R.id.editTextEnd);
        editTextStart.setInputType(InputType.TYPE_NULL);
        editTextStart.setText(TestDB.timeStart);
        editTextEnd.setText(TestDB.timeEnd);
        editTextStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeStartAlert(editTextStart, editTextEnd);
            }
        });
       editTextEnd.setInputType(InputType.TYPE_NULL);
        editTextEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeEndAlert(editTextStart,editTextEnd);
            }
        });
        Button buttonOK = findViewById(R.id.buttonOk);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ReminderStartandEnd.this,ReminderActivity.class);
                startActivity(i);
            }
        });
    }

    private void showTimeStartAlert(EditText editTextStart, EditText editTextEnd) {
        final Calendar calendar = Calendar.getInstance();
        int gio = calendar.get(Calendar.HOUR_OF_DAY);
        int phut = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog  = new TimePickerDialog(ReminderStartandEnd.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i,  int i1) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                String timeEnd = editTextEnd.getText().toString();
                try {
                    Date date = simpleDateFormat.parse(timeEnd);
                    calendar.setTime(date);
                    hourEnd = calendar.get(Calendar.HOUR_OF_DAY);
                    minuteEnd = calendar.get(Calendar.MINUTE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (i > hourEnd || (i == hourEnd && i1 > minuteEnd)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReminderStartandEnd.this)
                            .setTitle("Thông báo")
                            .setMessage("Vui lòng chọn thời gian bắt đầu nhỏ hơn thời gian kết thúc")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    showTimeStartAlert(editTextStart, editTextEnd);
                                }
                            });
                    builder.show();
                } else {
                    String hour = i < 10 ? ("0" + i) : Integer.toString(i);
                    String minute = i1 < 10 ? ("0" + i1) : Integer.toString(i1);
                    calendar.set(0,0,0,i,i1);
                    editTextStart.setText(hour + ":" + minute);
                    TestDB.timeStart = hour + ":" + minute;
                }
            }
        },gio,phut,true);
        timePickerDialog.show();
    }

    private void showTimeEndAlert(EditText editTextStart, EditText editTextEnd) {
        final Calendar calendar = Calendar.getInstance();
        int gio = calendar.get(Calendar.HOUR_OF_DAY);
        int phut = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog  = new TimePickerDialog(ReminderStartandEnd.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i,  int i1) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                String timeStart = editTextStart.getText().toString();
                try {
                    Date date = simpleDateFormat.parse(timeStart);
                    calendar.setTime(date);
                    hourStart = calendar.get(Calendar.HOUR_OF_DAY);
                    minuteStart = calendar.get(Calendar.MINUTE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (i < hourStart || (i == hourStart && i1 <= minuteStart)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReminderStartandEnd.this)
                            .setTitle("Thông báo")
                            .setMessage("Vui lòng chọn thời gian kết thúc lớn hơn thời gian bắt đầu")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    showTimeEndAlert(editTextStart, editTextEnd);
                                }
                            });
                    builder.show();
                } else {
                    String hour = i < 10 ? ("0" + i) : Integer.toString(i);
                    String minute = i1 < 10 ? ("0" + i1) : Integer.toString(i1);
                    calendar.set(0,0,0,i,i1);
                    editTextEnd.setText(hour + ":" + minute);
                    TestDB.timeEnd = hour + ":" + minute;
                }
            }
        },gio,phut,true);
        timePickerDialog.show();
    }
}