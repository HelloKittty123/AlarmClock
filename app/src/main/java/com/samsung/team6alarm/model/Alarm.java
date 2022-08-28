package com.samsung.team6alarm.model;

import java.io.Serializable;

public class Alarm implements Serializable {
    private int id;
    private int hour;
    private int minute;
    private int status;

    public Alarm() {
    }

    public Alarm(int id, int hour, int minute, int status) {
        this.id = id;
        this.hour = hour;
        this.minute = minute;
        this.status = status;
    }

    public Alarm(int hour, int minute, int status) {
        this.hour = hour;
        this.minute = minute;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
