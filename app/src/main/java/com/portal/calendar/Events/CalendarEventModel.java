package com.portal.calendar.Events;

import com.portal.calendar.Utils.CalendarUtils;

import java.time.LocalDate;
import java.time.LocalTime;

public class CalendarEventModel {
    public long id;

    public String name;
    public LocalDate date;
    public LocalTime time ;

    public CalendarEventModel(String name, LocalDate date, LocalTime time) {
        this.id = 0;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public CalendarEventModel(long id, String name, String datetime) {
        this.id = id;
        this.name = name;
        this.date = CalendarUtils.getSQLiteDate(datetime);
        this.time = CalendarUtils.getSQLiteTime(datetime);
    }

/** /
    public static ArrayList<CalendarEventModel> eventsForDate(LocalDate date){
        ArrayList<CalendarEventModel> events = new ArrayList<>();

        for(CalendarEventModel event : eventList){
            if(event.date.equals(date)){
                events.add(event);
            }
        }
        return events;
    }

    public static void addEvent(CalendarEventModel event){
        eventList.add(event);
    }
 /**/
}
