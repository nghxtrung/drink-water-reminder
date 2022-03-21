package com.example.drink_water_reminder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
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
                            if(text.getText().equals("Auto Remider")){
                                if(getIntent().hasExtra("timeString")){
                                    String timeString = getIntent().getStringExtra("timeString");
                                    timeStartEnd.setText(timeString);
                                    timeEvery.setVisibility(View.GONE);
                                }
                            }else if(text.getText().equals("MANUALLY SET REMINDERS")){
                                timeStartEnd.setText("10:00-21:00");
                                timeEvery.setVisibility(View.VISIBLE);
                            }
                            cdd.dismiss();
                        }else if(text.getText().equals("Auto Remider")) {
                            View buttonShowManually = findViewById(R.id.buttonManuallySetting);
                            buttonShowManually.setVisibility(View.GONE);
                            View buttonShow = findViewById(R.id.btnShow);
                            buttonShow.setVisibility(View.VISIBLE);
                            text.setText("MANUALLY SET REMINDERS");
                            if(text.getText().equals("Auto Remider")){
                                if(getIntent().hasExtra("timeString")){
                                    String timeString = getIntent().getStringExtra("timeString");
                                    timeStartEnd.setText(timeString);
                                    timeEvery.setVisibility(View.GONE);
                                }
                            }else if(text.getText().equals("MANUALLY SET REMINDERS")){
                                timeStartEnd.setText("10:00-21:00");
                                timeEvery.setVisibility(View.VISIBLE);
                            }
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
    TextView textViewTitle = findViewById(R.id.textViewReminder);
    RelativeLayout relativeLayoutNoti = findViewById(R.id.relativeLayoutNoti);
    Button buttonOffNote = findViewById(R.id.buttonOffnoti);
    Button buttonNotiNote = findViewById(R.id.buttonNotinote);
    Button buttonOffSound = findViewById(R.id.buttonOffSound);
    Button buttonNoti = findViewById(R.id.buttonNoti);
    LinearLayout linearLayoutContent = findViewById(R.id.content);
    buttonNoti.setBackgroundColor(Color.parseColor("#ECECEC"));
    buttonOffNote.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           textViewTitle.setText("Reminder is turned off");
           relativeLayoutNoti.setBackgroundColor(Color.parseColor("#E54646"));
           buttonOffNote.setBackgroundColor(Color.parseColor("#ECECEC"));
           buttonNotiNote.setBackgroundColor(Color.parseColor("#A0A0A0"));
           buttonOffSound.setBackgroundColor(Color.parseColor("#A0A0A0"));
           buttonNoti.setBackgroundColor(Color.parseColor("#A0A0A0"));
           linearLayoutContent.setVisibility(View.GONE);
        }
    });


    buttonNotiNote.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            textViewTitle.setText("No Reminders when you are ahead of schedule");
            relativeLayoutNoti.setBackgroundColor(Color.parseColor("#EC870E"));
            buttonOffNote.setBackgroundColor(Color.parseColor("#A0A0A0"));
            buttonNotiNote.setBackgroundColor(Color.parseColor("#ECECEC"));
            buttonOffSound.setBackgroundColor(Color.parseColor("#A0A0A0"));
            buttonNoti.setBackgroundColor(Color.parseColor("#A0A0A0"));
            linearLayoutContent.setVisibility(View.VISIBLE);

        }
    });


    buttonOffSound.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            textViewTitle.setText("Mute when you are ahead of schedule");
            relativeLayoutNoti.setBackgroundColor(Color.parseColor("#EB7153"));
            buttonOffNote.setBackgroundColor(Color.parseColor("#A0A0A0"));
            buttonNotiNote.setBackgroundColor(Color.parseColor("#A0A0A0"));
            buttonOffSound.setBackgroundColor(Color.parseColor("#ECECEC"));
            buttonNoti.setBackgroundColor(Color.parseColor("#A0A0A0"));
            linearLayoutContent.setVisibility(View.VISIBLE);
        }
    });


    buttonNoti.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            textViewTitle.setText("Auto Reminders");
            relativeLayoutNoti.setBackgroundColor(Color.parseColor("#83C75D"));
            buttonOffNote.setBackgroundColor(Color.parseColor("#A0A0A0"));
            buttonNotiNote.setBackgroundColor(Color.parseColor("#A0A0A0"));
            buttonOffSound.setBackgroundColor(Color.parseColor("#A0A0A0"));
            buttonNoti.setBackgroundColor(Color.parseColor("#ECECEC"));
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
            view  = inflater.inflate(R.layout.layout_progressbar_custom,null);
            builder.setView(view);
            Dialog dialog = builder.create();
            dialog.show();
            ProgressBar progressBar = dialog.findViewById(R.id.progressbarPredict);
            progressBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int current = progressBar.getProgress();
                    if(current > progressBar.getMax()){
                        current = 0;
                    }
                    progressBar.setProgress(current+10);
                    textViewSound.setText(current+"");
                }
            });
            Button buttonProgressCancle = dialog.findViewById(R.id.buttonProgressCancle);
            buttonProgressCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
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


