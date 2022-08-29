package com.samsung.alarmteam6.models;

public class Week {
    private int id;
    private int id_alarm;
    private int day;
    private int status;

    public Week() {
    }

    public Week(int id, int id_alarm, int day, int status) {
        this.id = id;
        this.id_alarm = id_alarm;
        this.day = day;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_alarm() {
        return id_alarm;
    }

    public void setId_alarm(int id_alarm) {
        this.id_alarm = id_alarm;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

