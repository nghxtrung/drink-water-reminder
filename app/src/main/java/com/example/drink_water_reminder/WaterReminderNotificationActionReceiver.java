package com.example.drink_water_reminder;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WaterReminderNotificationActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myIntent = new Intent(context, MainActivity.class);
        String action = intent.getAction();
        int noti_id = intent.getIntExtra("noti_id", -1);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        if (action.equals("DRINK")) {
            notificationManager.cancel(noti_id);
            context.sendBroadcast(it);
            TestDatabase.addData(new DrinkAlarm(R.drawable.ic_bottle_1, 300, "13:30"));
            context.startActivity(myIntent);
        }
        else if(action.equals("SNOOZE")) {
            notificationManager.cancel(noti_id);
            context.sendBroadcast(it);
        }
    }
}
