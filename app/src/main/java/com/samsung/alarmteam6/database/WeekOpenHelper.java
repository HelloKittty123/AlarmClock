package com.samsung.alarmteam6.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.samsung.alarmteam6.models.Week;

import java.util.ArrayList;

public class WeekOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "alarmManager";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME_ALARM = "week";
    private static final String KEY_ID = "id";
    private static final String KEY_ALARM_ID = "id_alarm";
    private static final String KEY_DAY = "day";
    private static final String KEY_STATUS = "status";
    private static final int STATUS_ON = 1;
    private static final int STATUS_OF = 0;

    public WeekOpenHelper(@Nullable Context context) {
        super(context, TABLE_NAME_ALARM, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create_alarm_table = String
                .format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %s INTEGER)"
                        , TABLE_NAME_ALARM, KEY_ID, KEY_ALARM_ID, KEY_DAY, KEY_STATUS);
        sqLiteDatabase.execSQL(create_alarm_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addWeek(Week week) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ALARM_ID, week.getId_alarm());
        values.put(KEY_DAY, week.getDay());
        values.put(KEY_STATUS, week.getStatus());
        db.insert(TABLE_NAME_ALARM, null, values);
        db.close();
    }

    @SuppressLint("Range")
    public ArrayList<Week> getWeekbyAlarm(int alarmId) {
        ArrayList<Week> weeks = new ArrayList<Week>();

        SQLiteDatabase db = getReadableDatabase();
        String query = String
                .format("SELECT * FROM %s WHERE %s = %s"
                        , TABLE_NAME_ALARM, KEY_ALARM_ID, alarmId);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                //String name = cursor.getString(cursor.getColumnIndex(countyname));
//                Alarm alarm = new Alarm();
//                alarm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
//                alarm.setHour(cursor.getInt(cursor.getColumnIndex(KEY_HOUR)));
//                alarm.setMinute(cursor.getInt(cursor.getColumnIndex(KEY_MINUTE)));
//                alarms.add(alarm);
                //list.add(name);
                Week week = new Week();
                week.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                week.setId_alarm(cursor.getInt(cursor.getColumnIndex(KEY_ALARM_ID)));
                week.setDay(cursor.getInt(cursor.getColumnIndex(KEY_DAY)));
                week.setStatus(cursor.getInt(cursor.getColumnIndex(KEY_STATUS)));
                weeks.add(week);
                cursor.moveToNext();
            }
        }
        return weeks;
    }

    ;

    public boolean deleteWeek(int alarmId) {
        SQLiteDatabase db = getWritableDatabase();
        String query = String
                .format("%s = %s", KEY_ALARM_ID, alarmId);
        return db.delete(TABLE_NAME_ALARM, query, null) > 0;
    }
}
