package com.samsung.alarmteam6.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.samsung.alarmteam6.BuildConfig;
import com.samsung.alarmteam6.MainActivity;
import com.samsung.alarmteam6.R;

public class Music extends Service {
    MediaPlayer mediaPlayer;
    int id;
    private static final String NOTIFICATION_ACTION = BuildConfig.APPLICATION_ID + ".NOTIFICATION_ACTION";
    private static final String CHANNEL_ID = "MY_CHANNEL_ID";
    private static final int NOTIFICATION_ID = 0;
    private NotificationManager mNotifyManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Music", "Music on");
        System.out.println("Music on");

        createNotifyChannel();

        // Register Broadcast
        NotifyBroadcast notifyBroadcast = new NotifyBroadcast();
        IntentFilter intentFilter = new IntentFilter(NOTIFICATION_ACTION);
        registerReceiver(notifyBroadcast, intentFilter);

        String data = intent.getStringExtra("extra");
        if(data.equals("on"))
            id = 1;
        else
            id = 0;

        if(id == 1) {
            mediaPlayer = MediaPlayer.create(this, R.raw.r2d2);
            mediaPlayer.start();
            sendNotification();
        } else {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }


        return START_NOT_STICKY;
    }

    private void createNotifyChannel() {
        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // create notification channel
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "My channel",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableVibration(true);

            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    private NotificationCompat.Builder getNotifyBuilder() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent notifyIntent = PendingIntent.getActivity(this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("My notification")
                .setContentText("Hello!")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(notifyIntent);

        return notifyBuilder;
    }

    private void sendNotification() {
        NotificationCompat.Builder notifyBuilder = getNotifyBuilder();

        Intent intent = new Intent(NOTIFICATION_ACTION);
        PendingIntent updateIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        // Create button action
        notifyBuilder.addAction(R.drawable.ic_launcher_background, "Cancel", updateIntent);

        Notification notification = notifyBuilder.build();
        mNotifyManager.notify(NOTIFICATION_ID, notification);
    }

    private void cancelNotification() {
        mNotifyManager.cancel(NOTIFICATION_ID);
        mediaPlayer.stop();
        mediaPlayer.reset();
    }

    private void updateNotification() {
        NotificationCompat.Builder notifyBuilder = getNotifyBuilder();
        notifyBuilder.setContentText("Hello again!");
        Notification notification = notifyBuilder.build();
        mNotifyManager.notify(NOTIFICATION_ID, notification);
    }

    public class NotifyBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            cancelNotification();
        }
    }
}
