package com.birdicomputers.login;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderReciver extends BroadcastReceiver {

    private String NotificationText;
    private String Notification_ID;

    public ReminderReciver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationText = intent.getStringExtra("notificationText");
        Notification_ID = intent.getStringExtra("notificationID");
        Log.d("NotifText", NotificationText);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Reminders")
                .setSmallIcon(R.drawable.icbell)
                .setContentTitle("Due Now!")
                .setContentText(NotificationText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        try {
            notificationManagerCompat.notify(Integer.parseInt(Notification_ID), builder.build());
        }
        catch (NumberFormatException e){
            Log.d("Reminder :", e.getMessage());
        }

    }
}
