package com.example.drink_water_reminder;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class WaterReminderNotificationActionReceiver extends BroadcastReceiver {
    Connection connection;
    List<DrinkAlarm> drinkAlarmList;

    public String generateTimeCode() {
        int count = 0;
        DrinkDatabase drinkDatabase = new DrinkDatabase();
        connection = drinkDatabase.ConnectDatabase();
        if (connection != null) {
            String sqlStatement = "select * from ChiTietThoiGian";
            Statement statement = null;
            try {
                statement = connection.createStatement();
                ResultSet set = statement.executeQuery(sqlStatement);
                while (set.next()) {
                    ++count;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return "CTTG" + (count + 1);
    }

    public String getTimeNow() {
        Date dateNow = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String timeNowString = timeFormat.format(dateNow);
        return timeNowString;
    }

    public String getDateNow() {
        Date dateNow = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateNowString = dateFormat.format(dateNow);
        return dateNowString;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        DrinkDatabase drinkDatabase = new DrinkDatabase();
        connection = drinkDatabase.ConnectDatabase();
        Intent myIntent = new Intent(context, MainActivity.class);
        String action = intent.getAction();
        int noti_id = intent.getIntExtra("noti_id", -1);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        if (action.equals("DRINK")) {
            drinkAlarmList = new ArrayList<DrinkAlarm>();
            if (connection != null) {
                String sqlSelect = "select MaThoiGian from ThoiGian where Ngay = '" + getDateNow() + "'";
                Statement statement = null;
                try {
                    statement = connection.createStatement();
                    ResultSet set = statement.executeQuery(sqlSelect);
                    if (set.next()) {
                        String dateCode = set.getString("MaThoiGian");
                        String sqlInsert = "insert into ChiTietThoiGian (MaChiTietTG, Gio, LuongNuoc, MaThoiGian, Anh) values ('" + generateTimeCode() + "', '" + getTimeNow() + "', " + TestDB.volume + ", '" + dateCode + "', " + TestDB.image + ")";
                        statement.executeUpdate(sqlInsert);
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

            notificationManager.cancel(noti_id);
            context.sendBroadcast(it);
            context.startActivity(myIntent);
        }
        else if(action.equals("SNOOZE")) {
            notificationManager.cancel(noti_id);
            context.sendBroadcast(it);
        }
    }
}
