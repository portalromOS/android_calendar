package com.portal.calendar;

import android.content.res.Resources;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarUtils {

    //-1 - Monday 6 - Sunday
    public static int myFirstDayOfWeek = 0;
    public static LocalDate selectedDate;
    public static LocalDate today;

    public static boolean initiated = false;

    public static void init() {
        if(!initiated){
            CalendarUtils.selectedDate = LocalDate.now();
            CalendarUtils.today = LocalDate.now();
            initiated = true;
        }
    }
    public static ArrayList<Integer> daysInMonthArray(LocalDate date) {
        ArrayList<Integer> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = CalendarUtils.selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();//1-7
        dayOfWeek -= 1;//0-6

        //Fill array starting monday with the days in month;
        for(int i = 0; i < 42; i++){
            if(i < dayOfWeek || i >= daysInMonth + dayOfWeek){
                daysInMonthArray.add(0);
            }
            else{
                daysInMonthArray.add(i - dayOfWeek + 1);//O primeiro dia é dia 1
            }
        }

        //if starting day is not monday remove leading 0s in array if available, or add the necessary 0
        if(myFirstDayOfWeek > 0){
            if(dayOfWeek >= myFirstDayOfWeek){//Remover 0 disponiveis na array
                for(int i = 0; i < myFirstDayOfWeek; i++){
                    daysInMonthArray.remove(0);
                }
            }
            else{

                for(int i = 0; i < 7 - myFirstDayOfWeek; i++){
                    daysInMonthArray.add(0, 0);
                }
            }
        }

        return daysInMonthArray;
    }

    public static String monthYearFromDate(Resources res, LocalDate date){
        String result;

        String[] months = res.getStringArray(R.array.months);

        int monthId = date.getMonthValue();//starts 1
        result = months[monthId-1]+" "+date.getYear();
        return result;
    }

    public static String formDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        return date.format(formatter);
    }

    public static String formTime(LocalTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }
}
