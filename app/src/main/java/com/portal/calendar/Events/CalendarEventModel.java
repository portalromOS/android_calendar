package com.portal.calendar.Events;

import com.portal.calendar.Utils.CalendarUtils;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class CalendarEventModel implements Serializable {
    public long id;

    public String name;
    public LocalDate date;
    public LocalTime time ;
    public String detail;
    public int alarm;

    public CalendarEventModel() {
        id = 0;
        name = "";
        date = LocalDate.now();
        time = LocalTime.now();
        detail = "";
        alarm = -1;
    }
    public CalendarEventModel(String name, LocalDate date, LocalTime time, int alarm, String detail) {
        this.id = 0;
        this.name = (name == null)?"":name;
        this.date = date;
        this.time = time;
        this.alarm = alarm;
        this.detail = detail;
    }

    public CalendarEventModel(long id, String name, String datetime, int alarm, String detail) {
        this.id = id;
        this.name = name;
        this.date = CalendarUtils.getSQLiteDate(datetime);
        this.time = CalendarUtils.getSQLiteTime(datetime);
        this.alarm = alarm;
        this.detail = detail;
    }

    public boolean hasId(){
        return (this.id != 0);
    }

    public void updateDate(int year, int monthOfYear, int dayOfMonth) {
        date = date.withYear(year);
        date = date.withMonth(monthOfYear);
        date = date.withDayOfMonth(dayOfMonth);
    }

    public void updateTime(int hour, int minute) {
         time = LocalTime.of( hour,minute);
    }
    public boolean isValid() {
        boolean result = true;
        if(date == null)
            result = false;
        if(time == null)
            result = false;
        return result;
    }

}
