package com.example.drink_water_reminder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        timeStartEnd.setText(TestDB.timeStart + "-" + TestDB.timeEnd);
    TextView textViewTitle = findViewById(R.id.textViewReminder);
    RelativeLayout relativeLayoutNoti = findViewById(R.id.relativeLayoutNoti);
    ImageView buttonOffNote = findViewById(R.id.buttonOffnoti);
    ImageView buttonNoti = findViewById(R.id.buttonNoti);
    LinearLayout linearLayoutContent = findViewById(R.id.content);
    buttonOffNote.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           textViewTitle.setText("Reminder is turned off");
           relativeLayoutNoti.setBackgroundColor(Color.parseColor("#EE5C49"));
           linearLayoutContent.setVisibility(View.GONE);
        }
    });


    buttonNoti.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            textViewTitle.setText("Auto Reminders");
            relativeLayoutNoti.setBackgroundColor(Color.parseColor("#51d06d"));
            linearLayoutContent.setVisibility(View.VISIBLE);
        }
    });

    LinearLayout linearLayoutReminderSound = findViewById(R.id.layout_reminder_sound);
    TextView textViewSound = findViewById(R.id.textView8);
    linearLayoutReminderSound.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ReminderActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            view = inflater.inflate(R.layout.volume_alert,null);
            SeekBar seekBar = view.findViewById(R.id.seekBarVolumn);
            seekBar.setProgress(0);
            seekBar.incrementProgressBy(10);
            seekBar.setMax(100);

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progress = progress / 10;
                    progress = progress * 10;
                    textViewSound.setText(String.valueOf(progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            builder.setView(view);
            builder.setTitle("Reminder volume")
                    .setPositiveButton("OK", null)
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    });

    LinearLayout linearLayoutCustomSound =findViewById(R.id.linnerLayoutCustomSound);
    TextView textCustomSound = findViewById(R.id.textView10);
    linearLayoutCustomSound.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ReminderActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            view  = inflater.inflate(R.layout.dialog_custom_sound,null);
            builder.setView(view);
            Dialog dialog = builder.create();
            dialog.show();
            TextView textViewClassic = dialog.findViewById(R.id.textViewClassic);
            TextView textViewShort = dialog.findViewById(R.id.textViewShort);
            TextView textViewStyle = dialog.findViewById(R.id.textViewStyle);
            textViewClassic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textCustomSound.setText("Classic");
                    dialog.dismiss();
                }
            });
            textViewShort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textCustomSound.setText("Short");
                    dialog.dismiss();
                }
            });
            textViewStyle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textCustomSound.setText("Sound Style");
                    dialog.dismiss();
                }
            });
        }
    });
    Switch  switchReminderSound = findViewById(R.id.switch1);
    switchReminderSound.setChecked(true);
    switchReminderSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if(switchReminderSound.isChecked()==true){
                linearLayoutReminderSound.setVisibility(View.VISIBLE);
                linearLayoutCustomSound.setVisibility(View.VISIBLE);
            } else if(switchReminderSound.isChecked()==false){
                linearLayoutReminderSound.setVisibility(View.GONE);
                linearLayoutCustomSound.setVisibility(View.GONE);
            }
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
                showIntervalAlert(true, "");
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

    private void showIntervalAlert(boolean checkError, String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReminderActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View intervalTimeAlertView = inflater.inflate(R.layout.interval_alert, null);
        builder.setView(intervalTimeAlertView);
        EditText intervalTimeEditText = intervalTimeAlertView.findViewById(R.id.intervalTimeEditText);
        if (!checkError) {
            intervalTimeEditText.setError(error);
            intervalTimeEditText.requestFocus();
        }
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (validateIntervalTime(intervalTimeEditText).equals("")) {
                    timeEvery.setText("Every " + intervalTimeEditText.getText().toString() + " minutes");
                } else {
                    String error = validateIntervalTime(intervalTimeEditText);
                    showIntervalAlert(false, error);
                }
            }
        })
        .setNegativeButton("Cancel", null);
        builder.show();
    }

    private String validateIntervalTime(EditText editText) {
        try {
            int value = Integer.parseInt(editText.getText().toString());
            if (value <= 0)
                return "Bắt buộc lớn 0";
            else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                try {
                    Date dateStart = simpleDateFormat.parse(TestDB.timeStart);
                    Date dateEnd = simpleDateFormat.parse(TestDB.timeEnd);
                    long diffMs = dateEnd.getTime() - dateStart.getTime();
                    long diffSec = diffMs / 1000;
                    long min = diffSec / 60;
                    if (value > min)
                        return "Bắt buộc nhỏ hơn";
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return "";
        } catch (NumberFormatException e) {
            return "Bắt buộc nhập số";
        }
    }
}


