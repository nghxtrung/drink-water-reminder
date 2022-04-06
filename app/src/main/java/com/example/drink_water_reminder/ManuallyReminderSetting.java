package com.example.drink_water_reminder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ManuallyReminderSetting extends AppCompatActivity {

    ListView lv;
    MyAdapter myAdapter;
    ArrayList<RemindersSetting> listClock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manually_reminder_setting);
        lv = findViewById(R.id.list_item);
        listClock = new ArrayList<>();
        listClock.add(new RemindersSetting( 1, 9 , 30 , true ,true ,true,true,true,true,true,true));
        listClock.add(new RemindersSetting( 2, 19 , 30 , true ,true ,true,true,true,true,true,true));
        listClock.add(new RemindersSetting( 3, 20 , 40 , false ,true ,true,true,true,true,true,false));
        myAdapter = new MyAdapter(ManuallyReminderSetting.this,listClock);
        lv.setAdapter(myAdapter);
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        FloatingActionButton floatingActionButtonBack = findViewById(R.id.floatingActionButtonBack);
        floatingActionButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String listStringTime = "";
                for (int i = 0; i < listClock.size(); i++) {
                    listStringTime += listClock.get(i).getHour() +":" + listClock.get(i).getMinute();
                    listStringTime += ",";
                }
                Intent i = new Intent(ManuallyReminderSetting.this,ReminderActivity.class);
                i.putExtra("timeString",listStringTime);
                startActivity(i);
            }
        });

    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ManuallyReminderSetting.this);
        LayoutInflater inflater = getLayoutInflater();
        View view  = inflater.inflate(R.layout.dialog_add_clock,null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        Button buttonCancle = dialog.findViewById(R.id.buttonCancle);
        buttonCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        RemindersSetting remindersSetting = new RemindersSetting();
        EditText editTextTime = dialog.findViewById(R.id.editTextTimeDialog);
        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int gio = calendar.get(Calendar.HOUR_OF_DAY);
                int phut = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog  = new TimePickerDialog(ManuallyReminderSetting.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i,  int i1) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                        calendar.set(0,0,0,i,i1);
                        editTextTime.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                },gio,phut,true);
                timePickerDialog.show();
            }
        });
        CheckBox checkBoxMonday = dialog.findViewById(R.id.checkBoxMonDay);
        CheckBox checkBoxTuesday = dialog.findViewById(R.id.checkBoxTuesday);
        CheckBox checkWednesday = dialog.findViewById(R.id.checkBoxWed);
        CheckBox checkBoxThursday = dialog.findViewById(R.id.checkBoxThurs);
        CheckBox checkBoxFriday = dialog.findViewById(R.id.checkBoxFriday);
        CheckBox checkBoxStaturday = dialog.findViewById(R.id.checkBoxSaturday);
        CheckBox checkBoxSunday = dialog.findViewById(R.id.checkBoxSunday);

        Button buttonAdd = dialog.findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time = editTextTime.getText().toString();
                String[] result = time.split(":");
                remindersSetting.setHour(Integer.parseInt(result[0]));
                remindersSetting.setMinute(Integer.parseInt(result[1]));
                remindersSetting.setId(listClock.size()+1);
                remindersSetting.setMonday(checkBoxMonday.isChecked());
                remindersSetting.setThursday(checkBoxTuesday.isChecked());
                remindersSetting.setWednesday(checkWednesday.isChecked());
                remindersSetting.setThursday(checkBoxThursday.isChecked());
                remindersSetting.setFriday(checkBoxFriday.isChecked());
                remindersSetting.setSaturday(checkBoxStaturday.isChecked());
                remindersSetting.setSunday(checkBoxSunday.isChecked());
                remindersSetting.setStatus(true);
                listClock.add(remindersSetting);
                myAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
    }

}