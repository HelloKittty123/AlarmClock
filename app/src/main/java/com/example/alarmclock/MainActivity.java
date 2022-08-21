package com.example.alarmclock;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    Button btnTime;
    Button btnCancel;
    TimePicker timePicker;
    TextView txtShow;
    Calendar calendar;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTime = findViewById(R.id.button_time);
        btnCancel = findViewById(R.id.button_cancel);
        txtShow = findViewById(R.id.text_result);
        timePicker = findViewById(R.id.time_picker);
        calendar = Calendar.getInstance();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE); // Cho phép truy cập đến báo động của máy (báo thức)
        Intent intent = new Intent(this, AlarmReceiver.class);

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                intent.putExtra("extra", "on");
                // Tồn tại trong suốt ứng dụng ngay cả khi thoát khỏi ứng dụng
                pendingIntent = PendingIntent.getBroadcast(
                        MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                txtShow.setText("Giờ bạn đặt là: " + String.valueOf(timePicker.getCurrentHour()) +
                        ":" + String.valueOf(timePicker.getCurrentMinute()));
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtShow.setText("Bạn đã hủy đặt giờ");
                alarmManager.cancel(pendingIntent);
                intent.putExtra("extra", "off");
                sendBroadcast(intent);
            }
        });
    }
}