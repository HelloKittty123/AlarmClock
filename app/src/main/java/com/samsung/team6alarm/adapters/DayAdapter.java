package com.samsung.team6alarm.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.Switch;

import com.samsung.team6alarm.AlarmReceiver;
import com.samsung.team6alarm.EventHandler;
import com.samsung.team6alarm.R;
import com.samsung.team6alarm.model.Alarm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DayAdapter extends BaseAdapter {
    private String[] mList;
    private Context mContext;
    private boolean[] mCheck;

    public DayAdapter(Context context, String[] days, boolean[] checkDay) {
        mContext = context;
        mList = new String[days.length];
        for(int i = 0; i < days.length; i++) {
            mList[i] = days[i];
        }
        mCheck = checkDay;
    }

    @Override
    public int getCount() {
        return mList.length;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        View newView = view;
        String day = mList[i];
        newView = LayoutInflater.from(mContext).inflate(R.layout.alarm_list_day, viewGroup, false);
        Switch sw = newView.findViewById(R.id.sw_day);
        sw.setText(day);
        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(sw.isChecked() + " " + i);
                mCheck[i] = sw.isChecked();
                System.out.println("Cheked " + mCheck[i]);
            }
        });

        return newView;
    }
}
