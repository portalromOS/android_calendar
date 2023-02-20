package com.portal.calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); //For testing night mode theme
        setContentView(R.layout.activity_main);
        CalendarUtils.init();

        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYear);

        updateUI();

    }

    private void updateUI(){
        setMonthViewTxt();
        setWeekDays();
    }

    private void setWeekDays() {
        Resources res = getResources();
        String[] weekDays = res.getStringArray(R.array.weekDays_sm);

        for(int i = 0; i < 7; i++){
            int id = getResources().getIdentifier("day"+i, "id", getApplicationContext().getPackageName());
            TextView tv = (TextView)findViewById(id);
            tv.setText(weekDays[(CalendarUtils.myFirstDayOfWeek+i)%7]);
        }
    }



    private void setMonthViewTxt() {
        monthYearText.setText(CalendarUtils.monthYearFromDate(getResources(), CalendarUtils.selectedDate));
        ArrayList<Integer> daysInMonth = CalendarUtils.daysInMonthArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this, this);

        //definir o layout a ser aplicado na recicledView
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);


        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }



    public void prevMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthViewTxt();
    }
    public void nextMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthViewTxt();
    }
    public void monthYearAction(View view) {

    }

    @Override
    public void onItemClick(int position, int day) {
        if(day > 0){
            /*
            String message = "Selected Date "+ day + " " + CalendarUtils.monthYearFromDate(getResources(), CalendarUtils.selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            */
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.withDayOfMonth(day);
            startActivity(new Intent(this, DayViewActivity.class));
        }
    }
}