package com.samsung.alarmteam6.adapters;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.samsung.alarmteam6.AlarmFragment;
import com.samsung.alarmteam6.AlarmReceiver;
import com.samsung.alarmteam6.EventHandler;
import com.samsung.alarmteam6.R;
import com.samsung.alarmteam6.database.AlarmOpenHelper;
import com.samsung.alarmteam6.database.WeekOpenHelper;
import com.samsung.alarmteam6.models.Alarm;
import com.samsung.alarmteam6.models.Week;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmAdapter extends BaseAdapter {
    private ArrayList<Alarm> mList;
    private Context mContext;
    private EventHandler mHandler;
    private AlarmOpenHelper alarmOpenHelper;
    private WeekOpenHelper weekOpenHelper;
    private Calendar mCalendar;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;

    public AlarmAdapter(Context context, ArrayList<Alarm> alarms, AlarmFragment fragment) {
        mContext = context;
        mList = alarms;
        mHandler = fragment;
        alarmOpenHelper = new AlarmOpenHelper(context);
        weekOpenHelper = new WeekOpenHelper(context);
        mCalendar = Calendar.getInstance();
    }

    public void setCalendar(Calendar calendar) {
        mCalendar = calendar;
    }

    public void setPendingIntent(PendingIntent pendingIntent) {
        this.pendingIntent = pendingIntent;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View newView = view;
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        if (mList.size() > 0) {
            Alarm alarm = mList.get(position);
            System.out.println("Status Alarm" + alarm.getStatus());
            ArrayList<Week> weeks = weekOpenHelper.getWeekbyAlarm(alarm.getId());
            Log.i("minh", "" + weeks.size());
            Log.i("minh", "" + alarm.getId());
            for (Week week : weeks)
                System.out.println(week.getDay() + " " + week.getStatus());
            newView = LayoutInflater.from(mContext).inflate(R.layout.alarm_list_view, viewGroup, false);

            Switch swAlarm = newView.findViewById(R.id.alarm_sw);
            TextView txtAlarm = newView.findViewById(R.id.alarm_txt);
            TextView txtMon = newView.findViewById(R.id.txt_mon);
            TextView txtTue = newView.findViewById(R.id.txt_tue);
            TextView txtWed = newView.findViewById(R.id.txt_Wed);
            TextView txtThur = newView.findViewById(R.id.txt_Thur);
            TextView txtFri = newView.findViewById(R.id.txt_Fri);
            TextView txtSat = newView.findViewById(R.id.txt_Sat);
            TextView txtSun = newView.findViewById(R.id.txt_sun);
            for (int i = 0; i < weeks.size(); i++) {
                if (weeks.get(i).getStatus() == 1) {
                    switch (i) {
                        case 0:
                            System.out.println("Monday");
                            txtMon.setTextColor(Color.parseColor("#4a90e2"));
                            break;
                        case 1:
                            System.out.println("Tuesday");
                            txtTue.setTextColor(Color.parseColor("#4a90e2"));
                            break;
                        case 2:
                            System.out.println("Wednesday");
                            txtWed.setTextColor(Color.parseColor("#4a90e2"));
                            break;
                        case 3:
                            System.out.println("Thursday");
                            txtThur.setTextColor(Color.parseColor("#4a90e2"));
                            break;
                        case 4:
                            System.out.println("Friday");
                            txtFri.setTextColor(Color.parseColor("#4a90e2"));
                            break;
                        case 5:
                            System.out.println("Saturday");
                            txtSat.setTextColor(Color.parseColor("#4a90e2"));
                            break;
                        case 6:
                            System.out.println("Sunday");
                            txtSun.setTextColor(Color.parseColor("#4a90e2"));
                            break;
                    }
                }
            }

            alarmManager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);

            txtAlarm.setText(alarm.getHour() + ":" + (alarm.getMinute() < 10 ? ("0" + alarm.getMinute()) : alarm.getMinute()));
            if (alarm.getStatus() == 1)
                swAlarm.setChecked(true);
            else
                swAlarm.setChecked(false);

            newView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("MANHTINH","onClick");
                    mHandler.onClick(position);
                }
            });

            newView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mHandler.onLongClick(position);
                    return false;
                }
            });

            swAlarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!swAlarm.isChecked()) {
                        intent.putExtra("extra", "off");
                        alarm.setStatus(0);
                        pendingIntent = PendingIntent.getBroadcast(
                                mContext, alarm.getId(), intent, PendingIntent.FLAG_IMMUTABLE);
                        alarmManager.cancel(pendingIntent);
                        //                    mContext.sendBroadcast(intent);
                    } else {
                        intent.putExtra("extra", "on");
                        alarm.setStatus(1);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent);
                    }
                    System.out.println(alarm.getStatus());
                    //                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 7 * 24 * 60 * 60 * 1000, pendingIntent);
                    boolean check = alarmOpenHelper.updateAlarm(alarm);
                }
            });
        }
        return newView;
    }
}

