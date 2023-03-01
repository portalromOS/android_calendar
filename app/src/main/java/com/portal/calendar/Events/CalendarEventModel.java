package com.portal.calendar.Events;

import static com.portal.calendar.Events.CalendarEventSQL.*;

import android.database.Cursor;

import com.portal.calendar.Utils.CalendarUtils;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CalendarEventModel implements Serializable {
    public long id;

    public String name;
    public LocalDate date;
    public LocalTime time;

    public String detail;
    public int alarm;

    public boolean allDay;
    public LocalDate dateEnd;
    public LocalTime timeEnd;
    public String alarmSoundName;

    public CalendarEventModel() {
        reset();
    }
    public CalendarEventModel(String name, LocalDate date, LocalTime time, int alarm, String detail,
                              boolean allDay, LocalDate dateEnd, LocalTime timeEnd, String alarmSoundName )
    {
        this.id = 0;
        this.name = (name == null)?"":name;
        this.date = date;
        this.time = time;
        this.alarm = alarm;
        this.detail = detail;

        this.allDay = allDay;
        this.dateEnd = dateEnd;
        this.timeEnd = timeEnd;
        this.alarmSoundName = alarmSoundName;
    }

    public CalendarEventModel(long id,
                              String name, String datetime, int alarm, String detail,
                              boolean allDay, LocalDate dateEnd, LocalTime timeEnd, String alarmSoundName )
    {
        this.id = id;
        this.name = name;
        this.date = CalendarUtils.getSQLiteDate(datetime);
        this.time = CalendarUtils.getSQLiteTime(datetime);
        this.alarm = alarm;
        this.detail = detail;

        this.allDay = allDay;
        this.dateEnd = dateEnd;
        this.timeEnd = timeEnd;
        this.alarmSoundName = alarmSoundName;
    }

    public CalendarEventModel(Cursor cursor )
    {
        reset();
        int indexCol = cursor.getColumnIndex(COL_ID);
        if(indexCol >= 0)
            this.id = cursor.getLong(indexCol);

        indexCol = cursor.getColumnIndex(COL_NAME);
        if(indexCol >= 0)
            this.name = cursor.getString(indexCol);

        indexCol = cursor.getColumnIndex(COL_DETAIL);
        if(indexCol >= 0)
            this.detail = cursor.getString(indexCol);

        indexCol = cursor.getColumnIndex(COL_DATE_TIME);
        if(indexCol >= 0){
            this.date = CalendarUtils.getSQLiteDate(cursor.getString(indexCol));
            this.time = CalendarUtils.getSQLiteTime(cursor.getString(indexCol));
        }

        indexCol = cursor.getColumnIndex(COL_DATE_TIME_END);
        if(indexCol >= 0){
            this.dateEnd = CalendarUtils.getSQLiteDate(cursor.getString(indexCol));
            this.timeEnd = CalendarUtils.getSQLiteTime(cursor.getString(indexCol));
        }

        indexCol = cursor.getColumnIndex(COL_ALL_DAY);
        if(indexCol >= 0)
            this.allDay = (cursor.getInt(indexCol) == 1);

        indexCol = cursor.getColumnIndex(COL_ALARM);
        if(indexCol >= 0)
            this.alarm = cursor.getInt(indexCol);

        indexCol = cursor.getColumnIndex(COL_SOUND_NAME);
        if(indexCol >= 0){
            this.alarmSoundName = cursor.getString(indexCol);
            this.alarmSoundName = (this.alarmSoundName == "")?null:this.alarmSoundName;
        }
    }
    private void reset(){
        id = -1;
        name = "";
        date = LocalDate.now();
        time = LocalTime.now();
        detail = "";
        alarm = -1;
        allDay = false;
        dateEnd = LocalDate.now();
        timeEnd = LocalTime.now();
        alarmSoundName = "";
    }
    public boolean hasId(){
        return (this.id > 0);
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

    public LocalDateTime getAlarmDateTime() {
        if(alarm<0)
            return null;

        LocalDateTime dateTime = LocalDateTime.of(date, time);
        dateTime.minusHours(alarm);
        return dateTime;
    }
}
