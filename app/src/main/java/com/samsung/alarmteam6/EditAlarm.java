package com.samsung.alarmteam6;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.samsung.alarmteam6.adapters.DayAdapter;
import com.samsung.alarmteam6.database.WeekOpenHelper;
import com.samsung.alarmteam6.models.Alarm;
import com.samsung.alarmteam6.models.Week;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EditAlarm extends AppCompatActivity {

    final String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    //Thoai-start
    boolean[] mChecked = new boolean[7];
    ImageButton btnTime;
    ImageButton btnCancel;
    Calendar calendar;
    TextView txtShow;
    CircleAlarmTimerView timePicker;
    LinearLayout repeatPicker;
    WeekOpenHelper weekOpenHelper;
    TextView repeatText;
    int hour;
    int minute;
    //Thoai-end
    Date date;
    RecyclerView listDay;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    DayAdapter dayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);

        weekOpenHelper = new WeekOpenHelper(getApplicationContext());

        //Thoai-start;
        btnTime = findViewById(R.id.edit_btn_time);
        btnCancel = findViewById(R.id.edit_btn_cancel);
        txtShow = findViewById(R.id.edit_text_result);
        listDay = findViewById(R.id.edit_day_list_view);
        timePicker = findViewById(R.id.edit_time_picker);
        repeatPicker = findViewById(R.id.edit_lnl_repeat);
        repeatText = findViewById(R.id.edit_txt_repeat);

        Alarm alarm = (Alarm) getIntent().getSerializableExtra("alarm");
        setDefaultTime(alarm);
        System.out.println("Id Edit: " + alarm.getId());
        System.out.println("Edit alarm: " + alarm.getHour() + ":" + alarm.getMinute());
        ArrayList<Week> weeks = weekOpenHelper.getWeekbyAlarm(alarm.getId());
        int numDays = 0;
        for (int i = 0; i < 7; i++) {
            if (weeks.get(i).getStatus() == 1) {
                mChecked[i] = true;
                numDays++;
            } else
                mChecked[i] = false;
        }

        if (numDays == 0) {
            repeatText.setText("Once");
        } else if (numDays == 7) {
            repeatText.setText("Everyday");
        } else if (0 < numDays && numDays < 7) {
            repeatText.setText("Custom");
            listDay.setVisibility(View.VISIBLE);
        }
        //Thoai-end

        dayAdapter = new DayAdapter(this, days, mChecked);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false);
        listDay.setLayoutManager(layoutManager);
        listDay.setAdapter(dayAdapter);

        dayAdapter.notifyDataSetChanged();

        calendar = Calendar.getInstance();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE); // Cho phép truy cập đến báo động của máy (báo thức)
        Intent intent = new Intent(this, AlarmReceiver.class);
        Intent mainIntent = new Intent();


        repeatPicker.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                // Initializing the popup menu and giving the reference as current context
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), repeatPicker);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.repeat_popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String selected = menuItem.getTitle().toString();
                        repeatText.setText(selected);
                        if (selected.equals("Once")) {
                            for (int i = 0; i < 7; i++) {
                                mChecked[i] = false;
                            }
                            listDay.setVisibility(View.INVISIBLE);
                        } else if (selected.equals("Everyday")) {
                            for (int i = 0; i < 7; i++) {
                                mChecked[i] = true;
                            }
                            listDay.setVisibility(View.INVISIBLE);
                        } else if (selected.equals("Custom")) {
                            for (int i = 0; i < 7; i++) {
                                mChecked[i] = false;
                            }
                            listDay.setVisibility(View.VISIBLE);
                        }
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
            }
        });


        timePicker.setOnTimeChangedListener(new CircleAlarmTimerView.OnTimeChangedListener() {
            @Override
            public void start(String starting) {
            }

            @Override
            public void end(String ending) {
                String[] res;
                res = ending.split(":");
                hour = Integer.parseInt(res[0]);
                minute = Integer.parseInt(res[1]);
            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
//                calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
//                intent.putExtra("extra", "on");

                mainIntent.putExtra("hour", hour);
                mainIntent.putExtra("minute", minute);
                mainIntent.putExtra("id", alarm.getId());
                mainIntent.putExtra("status", alarm.getStatus());
                mainIntent.putExtra("day", mChecked);


                setResult(Activity.RESULT_OK, mainIntent);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED, mainIntent);
                finish();
            }
        });

    }

    void setDefaultTime(Alarm alarm) {
        hour = alarm.getHour();
        minute = alarm.getMinute();
        System.out.println("Time Picker: " + minute);
        timePicker.setTime(hour, minute);
    }
}