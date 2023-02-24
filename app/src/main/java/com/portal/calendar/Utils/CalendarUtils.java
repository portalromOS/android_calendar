package com.portal.calendar.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

import com.portal.calendar.R;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarUtils {

    public static final String DB_NAME = "Calendar.db";
    public static DateTimeFormatter sqlDateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
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

    public static String getStringResourceByName(Context context, String idString) {
        Resources r = context.getResources();
        int resId = r.getIdentifier(idString, "string", context.getPackageName());
        String val = r.getString(resId);
        return val;
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
    //region datetime formating
    public static String formDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMMM dd");
        return date.format(formatter);
    }

    public static String formTime(LocalTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }
    //endregion
    //region SQLite
    public static String toSQLite(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }
    public static String toSQLite(LocalTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
        return time.format(formatter);
    }
    public static String toSQLite(LocalDate date, LocalTime time){
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        return dateTime.format(sqlDateTimeformatter);
    }
    public static String toSQLite(LocalDateTime datetime){
        return datetime.format(sqlDateTimeformatter);
    }

    public static LocalDate getSQLiteDate(String date){
        return LocalDate.parse(date, sqlDateTimeformatter);
    }

    public static LocalTime getSQLiteTime(String time){
        return LocalTime.parse(time, sqlDateTimeformatter);
    }

    //endregion

    public static void showMsg(Context context, int msgId) {
        String message = context.getResources().getString(msgId) ;
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
