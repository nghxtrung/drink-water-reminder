package com.example.drink_water_reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Connection connection;

    List<DrinkAlarm> drinkAlarmList;

    DrinkAdapter drinkAdapter;
    GridView drinkGridView;
    TextView drinkTargetTextView;
    ProgressBar drinkProgressBar;
    int sumOfVolume = 0;
    int drinkTarget = 0;
    int yearDrink, monthDrink, dayDrink;
    int hourDrinkDetail, minuteDrinkDetail;
    ImageView drinkNowImageView;
    RecyclerView drinkNowRecyclerView, drinkAddRecyclerView;
    DrinkItemNowAdapter drinkItemNowAdapter;
    DrinkItemAddAdapter drinkItemAddAdapter;
    List<DrinkNow> drinkNowList;

    public void setWaterReminderNotification(int idWaterReminderNotification, String drinkTimeString) {
        Intent wrnIntent = new Intent(MainActivity.this, WaterReminderNotificationReceiver.class);
        PendingIntent wrnPendingIntent = PendingIntent.getBroadcast(MainActivity.this, idWaterReminderNotification, wrnIntent, PendingIntent.FLAG_MUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Date dateNow = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateNowString = dateFormat.format(dateNow);
        String drinkDateTimeString = drinkTimeString + ":01 " + dateNowString;
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        Date drinkDateTime = null;
        try {
            drinkDateTime = dateTimeFormat.parse(drinkDateTimeString);
            long drinkMillis = drinkDateTime.getTime();
            alarmManager.set(AlarmManager.RTC_WAKEUP, drinkMillis, wrnPendingIntent);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void unsetWaterReminderNotification(int idWaterReminderNotification) {
        Intent wrnIntent = new Intent(MainActivity.this, WaterReminderNotificationReceiver.class);
        PendingIntent wrnPendingIntent = PendingIntent.getBroadcast(MainActivity.this, idWaterReminderNotification, wrnIntent, PendingIntent.FLAG_MUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(wrnPendingIntent);
    }

    public String caculateTime(String time, int interval) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, interval);
        String newTime = simpleDateFormat.format(calendar.getTime());
        return newTime;
    }

    public List<DrinkAlarmTest> generateListTime(String timeStart, String timeEnd, int interval) {
        List<DrinkAlarmTest> drinkAlarmTestList = new ArrayList<DrinkAlarmTest>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        int i = 1;
        String nextTime = caculateTime(timeStart, interval);
        boolean check = true;
        while (check) {
            int value = 0;
            try {
                value = simpleDateFormat.parse(nextTime).compareTo(simpleDateFormat.parse(timeEnd));
                if (value <= -1 || value == 0) {
                    drinkAlarmTestList.add(new DrinkAlarmTest(i, nextTime));
                    nextTime = caculateTime(nextTime, interval);
                    i++;
                }
                else
                    check = false;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return drinkAlarmTestList;
    }

    public String getDateNow() {
        Date dateNow = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateNowString = dateFormat.format(dateNow);
        return dateNowString;
    }

    public String getTimeNow() {
        Date dateNow = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String timeNowString = timeFormat.format(dateNow);
        return timeNowString;
    }

    public String generateDateCode() {
        int count = 0;
        DrinkDatabase drinkDatabase = new DrinkDatabase();
        connection = drinkDatabase.ConnectDatabase();
        int maxNumber = 1;
        if (connection != null) {
            String sqlStatement = "select top(1) MaThoiGian from ThoiGian order by MaThoiGian desc";
            Statement statement = null;
            try {
                statement = connection.createStatement();
                ResultSet set = statement.executeQuery(sqlStatement);
                if (set.next()) {
                    String maxCode = set.getString("MaThoiGian");
                    maxNumber = Integer.parseInt(maxCode.substring(2)) + 1;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return "TG" + maxNumber;
    }

    public String generateTimeCode() {
        int count = 0;
        DrinkDatabase drinkDatabase = new DrinkDatabase();
        connection = drinkDatabase.ConnectDatabase();
        int maxNumber = 1;
        if (connection != null) {
            String sqlStatement = "select top(1) MaChiTietTG from ChiTietThoiGian order by MaChiTietTG desc";
            Statement statement = null;
            try {
                statement = connection.createStatement();
                ResultSet set = statement.executeQuery(sqlStatement);
                if (set.next()) {
                    String maxCode = set.getString("MaChiTietTG");
                    maxNumber = Integer.parseInt(maxCode.substring(4)) + 1;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return "CTTG" + maxNumber;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("waterReminder", "Water Reminder Channel", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        String timeNowString = getTimeNow();

        List<DrinkAlarmTest> drinkAlarmTestList = generateListTime(TestDB.timeStart, TestDB.timeEnd, TestDB.interval);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        int value = 0;
        try {
            for (DrinkAlarmTest drinkAlarmTest : drinkAlarmTestList) {
                value = simpleDateFormat.parse(drinkAlarmTest.getTime()).compareTo(simpleDateFormat.parse(timeNowString));
                if (value >= 1)
                    setWaterReminderNotification(drinkAlarmTest.getId(), drinkAlarmTest.getTime());
                else
                    unsetWaterReminderNotification(drinkAlarmTest.getId());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
//                .setTitle("Done")
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(MainActivity.this,"Get Started!",Toast.LENGTH_LONG).show();
//                        dialogInterface.dismiss();
//                    }
//                });
//        LayoutInflater inflater = getLayoutInflater();
//        View welcomeAlertView = inflater.inflate(R.layout.alert_welcome, null);
//        builder.setView(welcomeAlertView);
//        builder.show();

        Toolbar mainToolBar = findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolBar);
        getSupportActionBar().setTitle("Drink Water Reminder");
        mainToolBar.setNavigationIcon(R.drawable.ic_bell);
        mainToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ReminderActivity.class);
                startActivity(intent);
            }
        });
        mainToolBar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.ic_calendar));

        String dateNowString = getDateNow();

        drinkAlarmList = new ArrayList<DrinkAlarm>();
        DrinkDatabase drinkDatabase = new DrinkDatabase();
        connection = drinkDatabase.ConnectDatabase();
        if (connection != null) {
            String sqlStatement = "select * from ThoiGian inner join ChiTietThoiGian on ThoiGian.MaThoiGian = ChiTietThoiGian.MaThoiGian where Ngay = '" + dateNowString +"'";
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
                    DrinkAlarm drinkAlarm = new DrinkAlarm(codeTime, time, volume, dateCode, image);
                    drinkAlarmList.add(drinkAlarm);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
//        drinkAlarmList.add(new DrinkAlarm("0", "14:00", 0, "0", R.drawable.ic_bell_color));


        drinkProgressBar = findViewById(R.id.drinkTargetProgressBar);
        drinkTargetTextView = findViewById(R.id.drinkTargetTextView);
        if (connection != null) {
            String sqlStatement = "select MucTieu from ThoiGian where Ngay = '" + dateNowString + "'";
            Statement statement = null;
            try {
                statement = connection.createStatement();
                ResultSet set = statement.executeQuery(sqlStatement);
                if (!set.next()) {
                    setDrinkTarget(drinkTargetTextView, drinkProgressBar, drinkAlarmList, 2000);
                    String sqlInsert = "insert into ThoiGian (MaThoiGian, Ngay, MucTieu) values ('" + generateDateCode() + "', '" + dateNowString + "', 2000)";
                    statement.executeUpdate(sqlInsert);
                } else {
                    drinkTarget = set.getInt("MucTieu");
                    setDrinkTarget(drinkTargetTextView, drinkProgressBar, drinkAlarmList, drinkTarget);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        findViewById(R.id.drinkTargetGroup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDrinkTargetAlert(drinkTargetTextView, drinkProgressBar, sumOfVolume, true, "");
            }
        });

        drinkGridView = findViewById(R.id.drinkGridView);
        Collections.sort(drinkAlarmList);
        drinkAdapter = new DrinkAdapter(this, drinkAlarmList);
        drinkGridView.setAdapter(drinkAdapter);
        drinkGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                if (position < drinkAlarmList.size() - 1)
                    showDrinkDetailAlert(drinkAlarmList, position, true, "", "");
            }
        });

        ImageView addImageView = findViewById(R.id.addImageView);
        addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connection != null) {
                    String sqlStatement = "select MaThoiGian, MucTieu from ThoiGian where Ngay = '" + dateNowString + "'";
                    Statement statement = null;
                    try {
                        statement = connection.createStatement();
                        ResultSet set = statement.executeQuery(sqlStatement);
                        String dateCode;
                        String timeCode = generateTimeCode();
                        if (!set.next()) {
                            dateCode = generateDateCode();
                            setDrinkTarget(drinkTargetTextView, drinkProgressBar, drinkAlarmList, 2000);
                            String sqlInsertDate = "insert into ThoiGian (MaThoiGian, Ngay, MucTieu) values ('" + dateCode + "', '" + dateNowString + "', 2000)";
                            statement.executeUpdate(sqlInsertDate);
                            String sqlInsertTime = "insert into ChiTietThoiGian (MaChiTietTG, Gio, LuongNuoc, MaThoiGian, Anh) values ('" + timeCode + "', '" + getTimeNow() + "', " + TestDB.volume + ", '" + dateCode + "', " + TestDB.image + ")";
                            statement.executeUpdate(sqlInsertTime);
                        } else {
                            dateCode = set.getString("MaThoiGian");
                            String sqlInsertTime = "insert into ChiTietThoiGian (MaChiTietTG, Gio, LuongNuoc, MaThoiGian, Anh) values ('" + timeCode + "', '" + getTimeNow() + "', " + TestDB.volume + ", '" + dateCode + "', " + TestDB.image + ")";
                            Toast.makeText(MainActivity.this, sqlInsertTime, Toast.LENGTH_SHORT).show();
                            statement.executeUpdate(sqlInsertTime);
                        }
                        drinkAlarmList.add(new DrinkAlarm(timeCode, getTimeNow(), TestDB.volume, dateCode, TestDB.image));
                        Collections.sort(drinkAlarmList);
                        drinkAdapter = new DrinkAdapter(MainActivity.this, drinkAlarmList);
                        drinkGridView.setAdapter(drinkAdapter);
                        setDrinkTarget(drinkTargetTextView, drinkProgressBar, drinkAlarmList, drinkTarget);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        });

        drinkNowList = new ArrayList<DrinkNow>();
        drinkNowList.add(new DrinkNow(R.drawable.ic_bottle_1, 300));
        drinkNowList.add(new DrinkNow(R.drawable.ic_bottle_2, 400));
        drinkNowList.add(new DrinkNow(R.drawable.ic_bottle_3, 600));
        drinkNowList.add(new DrinkNow(R.drawable.ic_bottle_4, 700));
        drinkNowList.add(new DrinkNow(R.drawable.ic_bottle_5, 1000));
        drinkNowImageView = findViewById(R.id.drinkNowImageView);
        drinkNowImageView.setImageResource(TestDB.image);
        drinkNowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDrinkNowAlert();
            }
        });
    }

    private void setDrinkTarget(TextView drinkTargetTextView, ProgressBar drinkProgressBar, List<DrinkAlarm> drinkAlarmList, int drinkTarget) {
        sumOfVolume = 0;
        for (int i = 0; i < drinkAlarmList.size() - 1; i++) {
            sumOfVolume += drinkAlarmList.get(i).getVolume();
        }
        int percentVolume = Math.round(((float) sumOfVolume/drinkTarget) * 100);
        drinkProgressBar.setProgress(percentVolume);
        drinkTargetTextView.setText(sumOfVolume + "/" + drinkTarget + "ml");
    }

    private void showDrinkTargetAlert(TextView drinkTargetTextView, ProgressBar drinkProgressBar, int sumOfVolume, boolean checkError, String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View drinkTargetAlertView = inflater.inflate(R.layout.drink_target_alert, null);
        EditText drinkTargetEditText = drinkTargetAlertView.findViewById(R.id.drinkTargetEditText);
        if (!checkError) {
            drinkTargetEditText.setError(error);
            drinkTargetEditText.requestFocus();
        }
        drinkTargetEditText.setText(drinkTarget + "");
        builder.setView(drinkTargetAlertView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (validateTargetVolume(drinkTargetEditText).equals("")) {
                    drinkTargetTextView.setText(sumOfVolume + "/" + drinkTargetEditText.getText().toString() + "ml");
                    int volume = Integer.parseInt(drinkTargetEditText.getText().toString());
                    int percentVolume = Math.round(((float) sumOfVolume/volume) * 100);
                    drinkProgressBar.setProgress(percentVolume);
                    drinkTarget = volume;
                } else {
                    String error = validateTargetVolume(drinkTargetEditText);
                    showDrinkTargetAlert(drinkTargetTextView, drinkProgressBar, sumOfVolume, false, error);
                }
            }
        });
        builder.show();
    }

    private void showDrinkDetailAlert(List<DrinkAlarm> drinkAlarmList, int position, boolean checkError, String error, String currentTimeSet) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View drinkDetailAlertView = inflater.inflate(R.layout.drink_detail_alert, null);
        builder.setView(drinkDetailAlertView);
        TextView drinkTimeDetailTextView = drinkDetailAlertView.findViewById(R.id.drinkTimeDetailTextView);
        EditText drinkVolumeEditText = drinkDetailAlertView.findViewById(R.id.drinkVolumeEditText);
        drinkVolumeEditText.setText(Integer.toString(drinkAlarmList.get(position).getVolume()));
        drinkTimeDetailTextView.setText(drinkAlarmList.get(position).getTime());
        if (!checkError) {
            drinkTimeDetailTextView.setText(currentTimeSet);
            drinkVolumeEditText.setError(error);
            drinkVolumeEditText.requestFocus();
        }
        drinkTimeDetailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeItemAlert(drinkTimeDetailTextView);
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (validateVolume(drinkVolumeEditText).equals("")) {
                    String time = drinkTimeDetailTextView.getText().toString();
                    int volume = Integer.parseInt(drinkVolumeEditText.getText().toString());

                    if (connection != null) {
                        String sqlUpdate = "update ChiTietThoiGian set gio = '" + time + "', LuongNuoc = " + volume + " where MaChiTietTG = '" + drinkAlarmList.get(position).getTimeCode() + "'";
                        Statement statement = null;
                        try {
                            statement = connection.createStatement();
                            statement.executeUpdate(sqlUpdate);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }

                    drinkAlarmList.get(position).setTime(time);
                    drinkAlarmList.get(position).setVolume(volume);
                    Collections.sort(drinkAlarmList);
                    drinkAdapter = new DrinkAdapter(MainActivity.this, drinkAlarmList);
                    drinkGridView.setAdapter(drinkAdapter);
                } else {
                    String currentTimeSet = drinkTimeDetailTextView.getText().toString();
                    String error = validateVolume(drinkVolumeEditText);
                    showDrinkDetailAlert(drinkAlarmList, position, false, error, currentTimeSet);
                }
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        ImageView copyImageView = drinkDetailAlertView.findViewById(R.id.copyImageView);
        copyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int image = drinkAlarmList.get(position).getImage();
                String time = drinkAlarmList.get(position).getTime();
                int volume = drinkAlarmList.get(position).getVolume();
                String dateCode = drinkAlarmList.get(position).getDateCode();

                if (connection != null) {
                    String sqlInsert = "insert into ChiTietThoiGian (MaChiTietTG, Gio, LuongNuoc, MaThoiGian, Anh) values ('" + generateTimeCode() + "', '" + time + "', " + volume + ", '" + dateCode + "', " + image + ")";
                    Statement statement = null;
                    try {
                        statement = connection.createStatement();
                        statement.executeUpdate(sqlInsert);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }

                drinkAlarmList.add(new DrinkAlarm(generateTimeCode(), time, volume, dateCode, image));
                Collections.sort(drinkAlarmList);
                drinkAdapter = new DrinkAdapter(MainActivity.this, drinkAlarmList);
                drinkGridView.setAdapter(drinkAdapter);
                setDrinkTarget(drinkTargetTextView, drinkProgressBar, drinkAlarmList, drinkTarget);
                alertDialog.dismiss();
            }
        });
        ImageView deleteImageView = drinkDetailAlertView.findViewById(R.id.deleteImageView);
        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String timeCode = drinkAlarmList.get(position).getTimeCode();
                if (connection != null) {
                    String sqlDelete = "delete from ChiTietThoiGian where MaChiTietTG = '" + timeCode + "'";
                    Statement statement = null;
                    try {
                        statement = connection.createStatement();
                        statement.executeUpdate(sqlDelete);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }

                drinkAlarmList.remove(position);
                Collections.sort(drinkAlarmList);
                drinkAdapter = new DrinkAdapter(MainActivity.this, drinkAlarmList);
                drinkGridView.setAdapter(drinkAdapter);
                setDrinkTarget(drinkTargetTextView, drinkProgressBar, drinkAlarmList, drinkTarget);
                alertDialog.dismiss();
            }
        });
    }

    private String validateTargetVolume(EditText editText) {
        try {
            int value = Integer.parseInt(editText.getText().toString());
            if (value <= 0)
                return "Bắt buộc lớn 0";
            else if (value > 2145)
                return "Bắt buộc nhỏ hơn";
            return "";
        } catch (NumberFormatException e) {
            return "Bắt buộc nhập số";
        }
    }

    private String validateVolume(EditText editText) {
        try {
            int value = Integer.parseInt(editText.getText().toString());
            if (value <= 0)
                return "Bắt buộc lớn 0";
            return "";
        } catch (NumberFormatException e) {
            return "Bắt buộc nhập số";
        }
    }

    private String validateVolumeAddDrinkNow(EditText editText, int image) {
        try {
            int value = Integer.parseInt(editText.getText().toString());
            if (value <= 0)
                return "Bắt buộc lớn 0";
            else if (!checkDuplicateDrinkNow(image, value))
                return "Bắt buộc không được trùng";
            return "";
        } catch (NumberFormatException e) {
            return "Bắt buộc nhập số";
        }
    }

    private boolean checkDuplicateDrinkNow(int image, int volume) {
        boolean check = true;
        for (DrinkNow drinkNow : drinkNowList) {
            if (drinkNow.getImage() == image && drinkNow.getVolume() == volume) {
                check = false;
                break;
            }
        }
        return check;
    }

    private void showTimeItemAlert(TextView drinkTimeDetailTextView) {
        String drinkTimeDetail = drinkTimeDetailTextView.getText().toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        try {
            Date date = simpleDateFormat.parse(drinkTimeDetail);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            hourDrinkDetail = calendar.get(Calendar.HOUR_OF_DAY);
            minuteDrinkDetail = calendar.get(Calendar.MINUTE);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TimePickerDialog.OnTimeSetListener drinkDetailOTSListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Calendar calendar = Calendar.getInstance();
                int hourNow = calendar.get(Calendar.HOUR_OF_DAY);
                int minuteNow = calendar.get(Calendar.MINUTE);
                if (selectedHour > hourNow || (selectedHour == hourNow && selectedMinute > minuteNow)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Thông báo")
                            .setMessage("Vui lòng chọn thời gian nhỏ hơn")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    showTimeItemAlert(drinkTimeDetailTextView);
                                }
                            });
                    builder.show();
                } else {
                    hourDrinkDetail = selectedHour;
                    minuteDrinkDetail = selectedMinute;
                    String formatHour = hourDrinkDetail < 10 ? ("0" + hourDrinkDetail) : ("" + hourDrinkDetail);
                    String formatMinute = minuteDrinkDetail < 10 ? ("0" + minuteDrinkDetail) : ("" + minuteDrinkDetail);
                    drinkTimeDetailTextView.setText(formatHour + ":" + formatMinute);
                }
            }
        };
        TimePickerDialog drinkDetailTPD = new TimePickerDialog(MainActivity.this,
                drinkDetailOTSListener, hourDrinkDetail, minuteDrinkDetail, true);
        drinkDetailTPD.show();
    }

    private void showDrinkNowAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View addListDrinkAlertView = inflater.inflate(R.layout.list_drink_now_alert, null);
        builder.setView(addListDrinkAlertView);
        drinkItemNowAdapter = new DrinkItemNowAdapter(MainActivity.this, drinkNowList);
        LinearLayoutManager drinkItemNowLLM = new LinearLayoutManager(MainActivity.this,
                LinearLayoutManager.HORIZONTAL, false);
        drinkNowRecyclerView = addListDrinkAlertView.findViewById(R.id.drinkItemNowRecyclerView);
        drinkNowRecyclerView.setAdapter(drinkItemNowAdapter);
        drinkNowRecyclerView.setLayoutManager(drinkItemNowLLM);
        builder.setPositiveButton("Chọn", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TestDB.indexNow = drinkItemNowAdapter.row_index;
                TestDB.image = drinkNowList.get(drinkItemNowAdapter.row_index).getImage();
                TestDB.volume = drinkNowList.get(drinkItemNowAdapter.row_index).getVolume();
                drinkNowImageView.setImageResource(TestDB.image);
            }
        })
        .setNegativeButton("Xoá", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                drinkNowList.remove(drinkItemNowAdapter.row_index);
                drinkItemNowAdapter.notifyItemRemoved(drinkItemNowAdapter.row_index);
                showDrinkNowAlert();
            }
        })
        .setNeutralButton("Thêm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showDrinkAddAlert(true, "");
            }
        });
        builder.show();
    }

    private void showDrinkAddAlert(boolean checkError, String error) {
        LayoutInflater inflater = getLayoutInflater();
        View listDrinkNowAlertView = inflater.inflate(R.layout.add_list_drink_alert, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(listDrinkNowAlertView);
        Integer[] drinkItemAddImageList = {R.drawable.ic_bottle_1, R.drawable.ic_bottle_2,
                R.drawable.ic_bottle_3, R.drawable.ic_bottle_4,
                R.drawable.ic_bottle_5};
        drinkItemAddAdapter = new DrinkItemAddAdapter(MainActivity.this, drinkItemAddImageList);
        LinearLayoutManager drinkItemAddLLM = new LinearLayoutManager(MainActivity.this,
                LinearLayoutManager.HORIZONTAL, false);
        drinkAddRecyclerView = listDrinkNowAlertView.findViewById(R.id.drinkItemAddRecyclerView);
        drinkAddRecyclerView.setAdapter(drinkItemAddAdapter);
        drinkAddRecyclerView.setLayoutManager(drinkItemAddLLM);
        EditText drinkVolumeItemEditText = listDrinkNowAlertView.findViewById(R.id.drinkVolumeItemEditText);
        if (!checkError) {
            drinkVolumeItemEditText.setError(error);
            drinkVolumeItemEditText.requestFocus();
        }
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int image = drinkItemAddImageList[drinkItemAddAdapter.row_index];
                if (validateVolumeAddDrinkNow(drinkVolumeItemEditText, image).equals("")) {
                    String volume = ((TextView) listDrinkNowAlertView.findViewById(R.id.drinkVolumeItemEditText)).getText().toString();
                    drinkNowList.add(new DrinkNow(image, Integer.parseInt(volume)));
                    drinkItemNowAdapter.notifyItemInserted(drinkNowList.size() - 1);
                    closeKeyboard(listDrinkNowAlertView);
                    showDrinkNowAlert();
                } else {
                    String error = validateVolumeAddDrinkNow(drinkVolumeItemEditText, image);
                    showDrinkAddAlert(false, error);
                }
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showDrinkNowAlert();
            }
        });
        builder.show();
    }

    private void closeKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void showDrinkDateAlert() {
        Calendar calendar = Calendar.getInstance();
        yearDrink = calendar.get(Calendar.YEAR);
        monthDrink = calendar.get(Calendar.MONTH);
        dayDrink = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date = simpleDateFormat.parse(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                    if (date.after(new Date())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Thông báo")
                                .setMessage("Vui lòng chọn thời gian nhỏ hơn")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        showDrinkDateAlert();
                                    }
                                });
                        builder.show();
                    } else {
                        yearDrink = selectedYear;
                        monthDrink = selectedMonth + 1;
                        dayDrink = selectedDay;
                        String day = dayDrink < 10 ? "0" + dayDrink : dayDrink + "";
                        String month = monthDrink < 10 ? "0" + monthDrink : monthDrink + "";
                        if (dayDrink == calendar.get(Calendar.DAY_OF_MONTH) &&
                            monthDrink == (calendar.get(Calendar.MONTH) + 1) &&
                            yearDrink == calendar.get(Calendar.YEAR))
                            getSupportActionBar().setTitle("Menu");
                        else if (dayDrink == (calendar.get(Calendar.DAY_OF_MONTH) - 1) &&
                                monthDrink == (calendar.get(Calendar.MONTH) + 1) &&
                                yearDrink == calendar.get(Calendar.YEAR))
                            getSupportActionBar().setTitle("Yesterday");
                        else {
                            getSupportActionBar().setTitle(day + "/" + month + "/" + yearDrink);
                        }
                        String customDateSelected = day + "/" + month + "/" + yearDrink;
                        getDrinkList(customDateSelected);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, onDateSetListener,
                yearDrink, monthDrink, dayDrink);
        datePickerDialog.show();
    }

    public void getDrinkList(String date) {
        drinkAlarmList = new ArrayList<DrinkAlarm>();
        if (connection != null) {
            String sqlStatement = "select * from ThoiGian inner join ChiTietThoiGian on ThoiGian.MaThoiGian = ChiTietThoiGian.MaThoiGian where Ngay = '" + date +"'";
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
                    DrinkAlarm drinkAlarm = new DrinkAlarm(codeTime, time, volume, dateCode, image);
                    drinkAlarmList.add(drinkAlarm);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        drinkGridView = findViewById(R.id.drinkGridView);
        Collections.sort(drinkAlarmList);
        drinkAdapter = new DrinkAdapter(this, drinkAlarmList);
        drinkGridView.setAdapter(drinkAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drinkLog:
                Intent intent = new Intent(MainActivity.this, DrinkLogActivity.class);
                startActivity(intent);
                return true;
            case R.id.today:
                getSupportActionBar().setTitle("Drink Water Reminder");
                getDrinkList(getDateNow());
                return true;
            case R.id.yesterday:
                getSupportActionBar().setTitle("Yesterday");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, -1);
                calendar.getTime();
                String yesterdayString = simpleDateFormat.format(calendar.getTime());
                getDrinkList(yesterdayString);
                return true;
            case R.id.customDate:
                showDrinkDateAlert();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}