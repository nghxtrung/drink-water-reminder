package com.example.drink_water_reminder;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class WaterReminderNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int notiId = (int) System.currentTimeMillis();
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, notiId, notificationIntent, PendingIntent.FLAG_MUTABLE);

        Intent drinkIntent = new Intent(context, WaterReminderNotificationActionReceiver.class);
        drinkIntent.setAction("DRINK");
        drinkIntent.putExtra("noti_id", notiId);
        PendingIntent pendingIntentDrink = PendingIntent.getBroadcast(context, notiId, drinkIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent snoozeIntent = new Intent(context, WaterReminderNotificationActionReceiver.class);
        snoozeIntent.setAction("SNOOZE");
        snoozeIntent.putExtra("noti_id", notiId);
        PendingIntent pendingIntentSnooze = PendingIntent.getBroadcast(context, notiId, snoozeIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        @SuppressLint("NotificationTrampoline") NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "waterReminder")
                .setContentTitle("Uống nước")
                .setContentText("Đã đến giờ uống nước!")
                .setSmallIcon(R.drawable.ic_bottle_noti)
                .setSound(path)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(contentIntent)
                .addAction(R.drawable.ic_yes,"UỐNG NƯỚC", pendingIntentDrink)
                .addAction(R.drawable.ic_cancel,"TRÌ HOÃN", pendingIntentSnooze);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(notiId, builder.build());
    }
}
