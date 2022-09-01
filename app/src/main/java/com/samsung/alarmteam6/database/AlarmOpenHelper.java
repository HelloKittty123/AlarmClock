package com.samsung.alarmteam6.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.samsung.alarmteam6.models.Alarm;

import java.util.ArrayList;

public class AlarmOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "alarmManager";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME_ALARM = "alarm";
    private static final String KEY_ID = "id";
    private static final String KEY_HOUR = "hour";
    private static final String KEY_MINUTE = "minute";
    private static final String KEY_STATUS = "status";
    private static final int STATUS_ON = 1;
    private static final int STATUS_OF = 0;

    public AlarmOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String create_alarm_table = String
                .format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s INTEGER, %s INTEGER, %s INTEGER)"
                        , TABLE_NAME_ALARM, KEY_ID, KEY_HOUR, KEY_MINUTE, KEY_STATUS);
        sqLiteDatabase.execSQL(create_alarm_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String drop_alarm_table = String
                .format("DROP TABLE IF EXISTS %s", TABLE_NAME_ALARM);
        sqLiteDatabase.execSQL(drop_alarm_table);
        onCreate(sqLiteDatabase);
    }

    public void addAlarm(Alarm alarm) {
//        System.out.println("DATABASE " + alarm.getHour());
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_HOUR, alarm.getHour());
        values.put(KEY_MINUTE, alarm.getMinute());
        values.put(KEY_STATUS, alarm.getStatus());
        db.insert(TABLE_NAME_ALARM, null, values);
        db.close();
    }

    @SuppressLint("Range")
    public Alarm getAlarm(int index) {
        Alarm alarm = new Alarm();
        SQLiteDatabase db = getReadableDatabase();
        String query = String
                .format("SELECT * FROM %s WHERE %s = %s"
                        , TABLE_NAME_ALARM, KEY_ID, index);
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        alarm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
        alarm.setHour(cursor.getInt(cursor.getColumnIndex(KEY_HOUR)));
        alarm.setMinute(cursor.getInt(cursor.getColumnIndex(KEY_MINUTE)));
        return alarm;
    }

    public boolean deleteAlarm(int index) {
        SQLiteDatabase db = getWritableDatabase();
        String query = String
                .format("%s = %s", KEY_ID, index);
        return db.delete(TABLE_NAME_ALARM, query, null) > 0;
    }

    public boolean updateAlarm(Alarm alarm) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_HOUR, alarm.getHour());
        values.put(KEY_MINUTE, alarm.getMinute());
        values.put(KEY_STATUS, alarm.getStatus());
        String id = String.valueOf(alarm.getId());
        String query = String
                .format("%s = %s", KEY_ID, alarm.getId());
        boolean b = 0 < db.update(TABLE_NAME_ALARM, values, KEY_ID + " = ?", new String[]{id});
        return b;
    }

    ;

    @SuppressLint("Range")
    public ArrayList<Alarm> getAll() {
        //Alarm alarm = new Alarm();
        ArrayList<Alarm> alarms = new ArrayList<Alarm>();
        SQLiteDatabase db = getReadableDatabase();
        String query = String
                .format("SELECT * FROM %s"
                        , TABLE_NAME_ALARM);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                //String name = cursor.getString(cursor.getColumnIndex(countyname));
                Alarm alarm = new Alarm();
                alarm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                alarm.setHour(cursor.getInt(cursor.getColumnIndex(KEY_HOUR)));
                alarm.setMinute(cursor.getInt(cursor.getColumnIndex(KEY_MINUTE)));
                alarm.setStatus(cursor.getInt(cursor.getColumnIndex(KEY_STATUS)));
                alarms.add(alarm);
                //list.add(name);
                cursor.moveToNext();
            }
        }
        return alarms;
    }
}
