package com.samsung.alarmteam6.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.samsung.alarmteam6.AlarmReceiver;
import com.samsung.alarmteam6.EventHandler;
import com.samsung.alarmteam6.R;
import com.samsung.alarmteam6.models.Alarm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {
    private String[] mList;
    private Context mContext;
    private boolean[] mChecked;
    private LayoutInflater inflater;

    public DayAdapter(Context context, String[] days, boolean[] checked) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mList = new String[days.length];
        for (int i = 0; i < days.length; i++) {
            mList[i] = days[i];
        }
        mChecked = checked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = inflater.inflate(R.layout.alarm_list_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtDay.setText(mList[position].substring(0, 3));
        if (mChecked[position]) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.txtDay.setTextColor(mContext.getColor(R.color.P_black));
            }
            holder.txtDay.setBackground(mContext.getDrawable(R.drawable.circle_background_selected));
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.txtDay.setTextColor(mContext.getColor(R.color.P_white));
            }
            holder.txtDay.setBackground(mContext.getDrawable(R.drawable.circle_background));
        }
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDay = itemView.findViewById(R.id.txt_day);
//            if(getAdapterPosition() > -1) {
//                int j = getAdapterPosition();
//                System.out.println("Selected: " + j);
//                if (mChecked[j]) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        txtDay.setTextColor(mContext.getColor(R.color.P_white));
//                    }
//                    txtDay.setBackground(mContext.getDrawable(R.drawable.circle_background));
//                } else {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        txtDay.setTextColor(mContext.getColor(R.color.P_black));
//                    }
//                    txtDay.setBackground(mContext.getDrawable(R.drawable.circle_background_selected));
//                }
//            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = getAdapterPosition();
                    if (mChecked[i]) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            txtDay.setTextColor(mContext.getColor(R.color.P_white));
                        }
                        txtDay.setBackground(mContext.getDrawable(R.drawable.circle_background));
                        mChecked[i] = !mChecked[i];
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            txtDay.setTextColor(mContext.getColor(R.color.P_black));
                        }
                        txtDay.setBackground(mContext.getDrawable(R.drawable.circle_background_selected));
                        mChecked[i] = !mChecked[i];
                    }
                    System.out.println(mChecked[i] + " " + i);
                }
            });
        }
    }


}

