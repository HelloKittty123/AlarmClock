package com.samsung.team6alarm;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.samsung.team6alarm.adapters.AlarmAdapter;
import com.samsung.team6alarm.database.AlarmOpenHelper;
import com.samsung.team6alarm.database.WeekOpenHelper;
import com.samsung.team6alarm.model.Alarm;
import com.samsung.team6alarm.model.Week;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements EventHandler {
    ListView alarmListView;
    ArrayList<Alarm> alarms = new ArrayList<Alarm>();
    AlarmAdapter mAdapter;
    public int mCreateCode = 0;
    public int mEditCode = 1;
    int id = 0;
    int posEdit = 0;
    Button btnAdd;
    Button btnCancel;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    Calendar calendar;
    AlarmOpenHelper alarmOpenHelper;
    WeekOpenHelper weekOpenHelper;
    private static final String NOTIFICATION_ACTION = BuildConfig.APPLICATION_ID + ".NOTIFICATION_ACTION";
    private static final String CHANNEL_ID = "MY_CHANNEL_ID";
    private static final int NOTIFICATION_ID = 0;
    private NotificationManager mNotifyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmOpenHelper = new AlarmOpenHelper(getApplicationContext());
        weekOpenHelper = new WeekOpenHelper(getApplicationContext());
        alarms = alarmOpenHelper.getAll();
        // Gán ID cuối để thêm vào database
        if(alarms.size() > 0)
            id = alarms.get(alarms.size() - 1).getId();
        btnAdd = findViewById(R.id.alarm_add);
        btnCancel = findViewById(R.id.alarm_cancel);
        alarmListView = findViewById(R.id.alarm_list);
        mAdapter = new AlarmAdapter(this, alarms);
        alarmListView.setAdapter(mAdapter);
        Intent createIntent = new Intent(this, CreateAlarm.class);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(createIntent, mCreateCode);
            }
        });
    }

    @Override
    public void onClick(int pos) {
//        edit
        Intent editIntent = new Intent(this, EditAlarm.class);
        Alarm alarm = alarms.get(pos);
        posEdit = pos;
//        System.out.println(alarm.getHour() + ":" + alarm.getMinute());
        editIntent.putExtra("alarm", (Serializable) alarm);
        startActivityForResult(editIntent, mEditCode);

    }

    @Override
    public void onLongClick(int pos) {
//        remove
        alarmOpenHelper.deleteAlarm(alarms.get(pos).getId());
        weekOpenHelper.deleteWeek(alarms.get(pos).getId());
        alarms.remove(pos);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("RQC = " + requestCode + " RSC = " + resultCode + " RSOK = " + Activity.RESULT_OK);
        if(requestCode == mCreateCode) {
            if(resultCode == Activity.RESULT_OK) {
                int hour = data.getIntExtra("hour", 0);
                int minute = data.getIntExtra("minute", 0);
                boolean[] days = data.getBooleanArrayExtra("day");
                Alarm alarm = new Alarm(++id, hour, minute, 1);
                System.out.println("Id: " + id);
                alarmOpenHelper.addAlarm(alarm);
                alarms.add(alarm);
                for(int i = 0; i < days.length; i++) {
                    if(days[i]) {
                        System.out.println(i + "True");
                        weekOpenHelper.addWeek(new Week(0, alarm.getId(), i, 1));
                    } else {
                        System.out.println(i + "False");
                        weekOpenHelper.addWeek(new Week(0, alarm.getId(), i, 0));
                    }
                }
                System.out.println(hour + ":" + minute);
                mAdapter.notifyDataSetChanged();
                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                calendar = Calendar.getInstance();
                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE); // Cho phép truy cập đến báo động của máy (báo thức)
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                mAdapter.setCalendar(calendar);
                Calendar now = Calendar.getInstance();
                now.set(Calendar.HOUR_OF_DAY, hour);
                now.set(Calendar.MILLISECOND, minute);
                System.out.println("CreateCode: " + mCreateCode);

                if (calendar.before(now)) {    //this condition is used for future reminder that means your reminder not fire for past time
                    calendar.add(Calendar.DATE, 7);
                }
                System.out.println(Calendar.HOUR_OF_DAY + Calendar.MINUTE);
                // Tồn tại trong suốt ứng dụng ngay cả khi thoát khỏi ứng dụng
                intent.putExtra("extra", "on");
                System.out.println("hello");
                final int idPen = (int) System.currentTimeMillis(); //Random request code for pendingintent
                pendingIntent = PendingIntent.getBroadcast(
                        MainActivity.this, idPen, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                mAdapter.setPendingIntent(pendingIntent);
//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 7 * 24 * 60 * 60 * 1000, pendingIntent);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        } else if(requestCode == mEditCode) {
            if(resultCode == Activity.RESULT_OK) {
                int idAlarm = data.getIntExtra("id", 0);
                int hour = data.getIntExtra("hour", 0);
                int minute = data.getIntExtra("minute", 0);
                int status = data.getIntExtra("status", 0);
                boolean[] days = data.getBooleanArrayExtra("day");
                Alarm alarm = new Alarm(idAlarm, hour, minute, status);
                System.out.println("Edit Id: " + idAlarm);
                alarmOpenHelper.updateAlarm(alarm);
                alarms.set(posEdit, alarm);

                weekOpenHelper.deleteWeek(idAlarm);
                for(int i = 0; i < days.length; i++) {
                    if(days[i]) {
//                        System.out.println(i + "True");
                        weekOpenHelper.addWeek(new Week(0, alarm.getId(), i, 1));
                    } else {
//                        System.out.println(i + "False");
                        weekOpenHelper.addWeek(new Week(0, alarm.getId(), i, 0));
                    }
                }
                System.out.println(hour + ":" + minute);
                mAdapter.notifyDataSetChanged();
                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                calendar = Calendar.getInstance();
                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE); // Cho phép truy cập đến báo động của máy (báo thức)
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                mAdapter.setCalendar(calendar);
                Calendar now = Calendar.getInstance();
                now.set(Calendar.HOUR_OF_DAY, hour);
                now.set(Calendar.MILLISECOND, minute);

                if (calendar.before(now)) {    //this condition is used for future reminder that means your reminder not fire for past time
                    calendar.add(Calendar.DATE, 7);
                }
                System.out.println(Calendar.HOUR_OF_DAY + Calendar.MINUTE);
                // Tồn tại trong suốt ứng dụng ngay cả khi thoát khỏi ứng dụng
                intent.putExtra("extra", "on");
                System.out.println("hello");
                final int idPen = (int) System.currentTimeMillis(); //Random request code for pendingintent
                pendingIntent = PendingIntent.getBroadcast(
                        MainActivity.this, idPen, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                mAdapter.setPendingIntent(pendingIntent);
//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 7 * 24 * 60 * 60 * 1000, pendingIntent);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
    }


}