package com.example.alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

// Sau khi dặt mà thời gian chạy đến thời điểm đó thì nhảy vào class này
public class AlarmReceiver  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("ALRAM", "TIME");
        String data = intent.getStringExtra("extra");
        Log.i("ALARM", data);
        Intent myInent = new Intent(context, Music.class);
        myInent.putExtra("extra", data);
        context.startService(myInent); // Yêu cầu thực hiện cùng lúc
    }
}
