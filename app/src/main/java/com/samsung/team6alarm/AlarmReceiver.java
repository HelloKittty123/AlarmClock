package com.samsung.team6alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.samsung.team6alarm.model.Music;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("ALRAM", "TIME");
        String data = intent.getStringExtra("extra");
        Log.i("ALARM", data);
        System.out.println(data);
        Intent myInent = new Intent(context, Music.class);
        myInent.putExtra("extra", data);
        context.startService(myInent); // Yêu cầu thực hiện cùng lúc
    }
}
