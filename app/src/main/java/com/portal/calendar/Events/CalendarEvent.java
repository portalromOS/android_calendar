package com.portal.calendar.Events;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class CalendarEvent {

    public static ArrayList<CalendarEvent> eventList = new ArrayList<>();
    public String name;
    public LocalDate date;
    public LocalTime time ;

    public CalendarEvent(String name, LocalDate date, LocalTime time) {
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public static ArrayList<CalendarEvent> eventsForDate(LocalDate date){
        ArrayList<CalendarEvent> events = new ArrayList<>();

        for(CalendarEvent event : eventList){
            if(event.date.equals(date)){
                events.add(event);
            }
        }
        return events;
    }

    public static void addEvent(CalendarEvent event){
        eventList.add(event);
    }
}
