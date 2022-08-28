package com.samsung.team6alarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.samsung.team6alarm.adapters.DayAdapter;
import com.samsung.team6alarm.database.WeekOpenHelper;
import com.samsung.team6alarm.model.Alarm;
import com.samsung.team6alarm.model.Week;

import java.util.ArrayList;
import java.util.Calendar;

public class EditAlarm extends AppCompatActivity {
    Button btnTime;
    TimePicker timePicker;
    Calendar calendar;
    ListView listDay;
    AlarmManager alarmManager;
    WeekOpenHelper weekOpenHelper;
    DayAdapter dayAdapter;
    final String days[] = {"Monday", "Tueday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    boolean[] check = new boolean[7];
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);
        weekOpenHelper = new WeekOpenHelper(getApplicationContext());

        listDay = findViewById(R.id.edit_day_list_view);
        btnTime = findViewById(R.id.edit_button_time);
        timePicker = findViewById(R.id.edit_time_picker);

        Alarm alarm = (Alarm) getIntent().getSerializableExtra("alarm");
        System.out.println("Id Edit: " + alarm.getId());
        System.out.println("Edit alarm: " + alarm.getHour() + ":" + alarm.getMinute());
        ArrayList<Week> weeks = weekOpenHelper.getWeekbyAlarm(alarm.getId());

        for(int i = 0; i < 7; i++) {
            System.out.println(weeks.get(i).getStatus());
        }

//        timePicker.setIs24HourView(false);
        timePicker.setCurrentHour(alarm.getHour());
        timePicker.setCurrentMinute(alarm.getMinute());


        for(int i = 0; i < 7; i++) {
            if(weeks.get(i).getStatus() == 1)
                check[i] = true;
            else
                check[i] = false;
        }

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
                mainIntent.putExtra("id", alarm.getId());
                mainIntent.putExtra("status", alarm.getStatus());
                mainIntent.putExtra("day", check);

                setResult(Activity.RESULT_OK, mainIntent);

                finish();
            }
        });
    }

}