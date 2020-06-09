package com.example.eventorganising_demo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

public class ReminderReciver extends BroadcastReceiver {
    Vibrator vibrator;
    Context mContext;
    String title;
    private static final String TAG = "ReminderReciver";

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        Log.d(TAG, "onReceive: New Reminder");
        vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(3000);
        try{
            title = intent.getExtras().get("title").toString();
            // title = intent.getStringExtra("title");
            Toast.makeText(context, title, Toast.LENGTH_LONG).show();
        }catch(Exception e){
            e.printStackTrace();
        }
        Notification(context, "New Event Reminder");

    }

    public void Notification(Context context, String message) {

        // Open NotificationView Class on Notification Click
        Intent intent = new Intent(context, MainActivity.class);
        // Send data to NotificationView Class
        intent.putExtra("title", title);
        intent.putExtra("text", title);
        // Open NotificationView.java Activity
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //        NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.drawable.app_icon, "Previous", pIntent).build();
        // Create Notification using NotificationCompat.Builder
        Notification.Builder builder = new Notification.Builder(
                context)
                // Set Icon
                .setSmallIcon(R.mipmap.ic_launcher_round)
                // Set Ticker Message
                .setTicker(message)
                // Set Title
                .setContentTitle(context.getString(R.string.app_name))
                // Set Text
                .setContentText(message)
                // Add an Action Button below Notification
                // Set PendingIntent into Notification
                .setContentIntent(pIntent)
                // Dismiss Notification
                .setAutoCancel(true);

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(38, builder.build());

    }
}
