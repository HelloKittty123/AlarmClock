package com.samsung.alarmteam6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.samsung.alarmteam6.adapters.DayAdapter;

import java.util.Calendar;
import java.util.Date;

public class CreateAlarm extends AppCompatActivity {

    //Thoai-start
    boolean[] mChecked = new boolean[7];
    ImageButton btnTime;
    ImageButton btnCancel;
    Calendar calendar;
    TextView txtShow;
    CircleAlarmTimerView timePicker;
    LinearLayout repeatPicker;
    TextView repeatText;
    int hour;
    int minute;
    Date date;
    //Thoai-end

    RecyclerView listDay;
    AlarmManager alarmManager;
    DayAdapter dayAdapter;
    final String days[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_alarm);

        //Thoai-start;
        btnTime = findViewById(R.id.btn_time);
        btnCancel = findViewById(R.id.btn_cancel);
        txtShow = findViewById(R.id.text_result);
        listDay = findViewById(R.id.day_list_view);
        timePicker = findViewById(R.id.time_picker);
        repeatPicker = findViewById(R.id.lnl_repeat);
        repeatText = findViewById(R.id.txt_repeat);
        setDefaultTime();
        for (int i = 0; i < 7; i++) {
            mChecked[i] = false;
            repeatText.setText("Once");
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
                        }
                        if (selected.equals("Everyday")) {
                            for (int i = 0; i < 7; i++) {
                                mChecked[i] = true;
                            }
                            listDay.setVisibility(View.INVISIBLE);
                        }
                        if (selected.equals("Custom")) {
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
                mainIntent.putExtra("hour", hour);
                mainIntent.putExtra("minute", minute);
                mainIntent.putExtra("day", mChecked);

                setResult(Activity.RESULT_OK, mainIntent);
                System.out.println("Created: ");
                for(int i = 0; i < 7; i++) {
                    System.out.println(i + " " + mChecked[i]);
                }

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

    void setDefaultTime() {
        date = new Date();
        hour = date.getHours();
        minute = date.getMinutes();
        timePicker.setTime(hour, minute);
    }
}