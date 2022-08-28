package com.samsung.team6alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.samsung.team6alarm.adapters.DayAdapter;
import com.samsung.team6alarm.database.AlarmOpenHelper;
import com.samsung.team6alarm.model.Alarm;

import java.util.Calendar;

public class CreateAlarm extends AppCompatActivity {
    Button btnTime;
    TimePicker timePicker;
    Calendar calendar;
    ListView listDay;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    DayAdapter dayAdapter;
    final String days[] = {"Monday", "Tueday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    boolean[] check = new boolean[7];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_alarm);

        listDay = findViewById(R.id.day_list_view);
        btnTime = findViewById(R.id.button_time);
        timePicker = findViewById(R.id.time_picker);
        for(int i = 0; i < 7; i++)
            check[i] = false;

        dayAdapter = new DayAdapter(this, days, check);
        listDay.setAdapter(dayAdapter);
        dayAdapter.notifyDataSetChanged();

        calendar = Calendar.getInstance();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE); // Cho phép truy cập đến báo động của máy (báo thức)
        Intent intent = new Intent(this, AlarmReceiver.class);
        Intent mainIntent = new Intent();

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainIntent.putExtra("hour", timePicker.getCurrentHour());
                mainIntent.putExtra("minute", timePicker.getCurrentMinute());
                mainIntent.putExtra("day", check);

                System.out.println("Length: " + check.length);
                for(int i = 0; i < 7; i++) {
                    System.out.println(i + " " + check[i]);
                }

                setResult(Activity.RESULT_OK, mainIntent);

                finish();
            }
        });
    }
}