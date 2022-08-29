package com.samsung.alarmteam6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.animation.ObjectAnimator;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.samsung.alarmteam6.services.Music;

public class TimePicker extends AppCompatActivity {
    private int hour, minute, second;
    private long time;
    ImageButton btnResume, btnStop, btnPause;
    TextView timeView, totalView;
    ImageView icanchor;
    ObjectAnimator animator;
    CountDownTimer countDownTimer;
    MediaPlayer mediaPlayer;
    private static final String NOTIFICATION_ACTION = BuildConfig.APPLICATION_ID + ".NOTIFICATION_ACTION";
    private static final String CHANNEL_ID = "MY_CHANNEL_ID";
    private static final int NOTIFICATION_ID = 0;
    private NotificationManager mNotifyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);

        mediaPlayer = MediaPlayer.create(this, R.raw.r2d2);
        createNotifyChannel();

        // Register Broadcast
        NotifyBroadcast notifyBroadcast = new NotifyBroadcast();
        IntentFilter intentFilter = new IntentFilter(NOTIFICATION_ACTION);
        registerReceiver(notifyBroadcast, intentFilter);

        hour = getIntent().getIntExtra("hour", 0);
        minute = getIntent().getIntExtra("minute",0);
        second = getIntent().getIntExtra("second",0);
        btnResume = findViewById(R.id.btnResume);
        btnPause = findViewById(R.id.btnPause);
        btnStop = findViewById(R.id.btnStop);
        timeView = findViewById(R.id.timeView);
        icanchor = findViewById(R.id.icanchor);
        totalView =findViewById(R.id.total_time);
        totalView.setText("Total time is " +hour +" hours "+minute+" minutes "+second+" seconds");
        time = hour*3600000+minute*60000+second*1000;
        btnResume.setVisibility(View.GONE);
        animator = ObjectAnimator.ofFloat(icanchor,"rotation",0,360);
        timeView.setText(returnTime(time));
        animator.setDuration(time);
        animator.setRepeatCount(0xffffffff);
        animator.setRepeatMode(ObjectAnimator.RESTART);
        animator.start();
        countDownTimer = new CountDownTimer(time,1000) {
            @Override
            public void onTick(long l) {
                timeView.setText(returnTime(l));
                hour   = (int)(l /3600000);
                minute = (int)(l - hour*3600000)/60000;
                second = (int)(l - hour*3600000- minute*60000)/1000;
                System.out.println(second);
            }

            @Override
            public void onFinish() {
                timeView.setText("done!");
                mediaPlayer.start();
                sendNotification();
                animator.end();

            }
        };
        countDownTimer.start();
        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time = hour*3600000+minute*60000+second*1000;
                btnPause.setVisibility(View.VISIBLE);
                btnResume.setVisibility(View.GONE);
                animator.resume();
                countDownTimer = new CountDownTimer(time,1000) {
                    @Override
                    public void onTick(long l) {
                        timeView.setText(returnTime(l));
                        hour   = (int)(l /3600000);
                        minute = (int)(l - hour*3600000)/60000;
                        second = (int)(l - hour*3600000- minute*60000)/1000;
                    }

                    @Override
                    public void onFinish() {
                        timeView.setText("done!");
                        animator.end();

                    }
                };
                countDownTimer.start();

            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnResume.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.GONE);
                animator.pause();
                countDownTimer.cancel();
            }
        });

    }
    public String returnTime(long time){
        int h   = (int)(time /3600000);
        int m = (int)(time - h*3600000)/60000;
        int s= (int)(time - h*3600000- m*60000)/1000;
        String t =(h < 10 ? "0"+h: h)+":"+(m < 10 ? "0"+m: m)+":"+ (s < 10 ? "0"+s: s);
        return t;
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
                PendingIntent.FLAG_UPDATE_CURRENT);
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
        PendingIntent updateIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
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

    public class NotifyBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            cancelNotification();
        }
    }
}