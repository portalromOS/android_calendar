package com.portal.calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.portal.calendar.Events.CalendarEvent;
import com.portal.calendar.Events.CalendarEventsAdapter;

import java.util.ArrayList;

public class DayViewActivity extends AppCompatActivity {

    private ListView calendarEventListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);

        calendarEventListView = findViewById(R.id.calendarEventListView);

        updateUI();
        setEventAdapter();
    }
    @Override
    protected void onResume() {
        super.onResume();
        setEventAdapter();
    }

    private void setEventAdapter() {
        ArrayList<CalendarEvent> dailyEvents = CalendarEvent.eventsForDate(CalendarUtils.selectedDate);
        CalendarEventsAdapter ceAdapter = new CalendarEventsAdapter(getApplicationContext(), dailyEvents);
        calendarEventListView.setAdapter(ceAdapter);
    }

    private void updateUI() {
        setMonthViewTxt();
        setDayTxt();
    }
    private void setDayTxt() {
        TextView day_txt = (TextView)findViewById(R.id.day);

        String[] weekDays = getResources().getStringArray(R.array.weekDays);
        day_txt.setText( weekDays[CalendarUtils.selectedDate.getDayOfWeek().getValue() - 1] + " " + CalendarUtils.selectedDate.getDayOfMonth());
    }

    private void setMonthViewTxt() {
        TextView monthYear_txt = (TextView)findViewById(R.id.monthYear);
        monthYear_txt.setText(CalendarUtils.monthYearFromDate(getResources(), CalendarUtils.selectedDate));
    }
    public void closeDayAction(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void prevDayAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusDays(1);
        updateUI();
    }

    public void nextDayAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusDays(1);
        updateUI();
    }

    public void addEventAction(View view) {
        startActivity(new Intent(this, EventEditActivity.class));
    }
}