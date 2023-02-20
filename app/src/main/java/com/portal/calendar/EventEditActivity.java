package com.portal.calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.portal.calendar.Events.CalendarEvent;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventEditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener  {

    private EditText eventName;
    private TextView eventDate;
    private TextView eventTime;

    private String myEventName;
    private LocalTime myTime;
    private LocalDate myDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        eventName = findViewById(R.id.eventName);
        eventName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                myEventName = String.valueOf(eventName.getText());
            }
        });

        eventDate = findViewById(R.id.eventDate);
        eventTime = findViewById(R.id.eventTime);

        /*
        eventDate.setFirstDayOfWeek(CalendarUtils.myFirstDayOfWeek);
        eventTime.setIs24HourView(true);
        */

        resetForm();
    }
    private void updateUI(){
        eventTime.setText(CalendarUtils.formTime(myTime));
        eventDate.setText(CalendarUtils.formDate(myDate));
    }
    private void resetForm(){
        myEventName = "";
        myDate = CalendarUtils.selectedDate;
        myTime = LocalTime.of( 8,0);
        updateUI();

    }

    public void closeEventAction(View view) {
        //startActivity(new Intent(this, DayViewActivity.class));
        finish();
    }

    public void saveEventAction(View view) {
        CalendarEvent.addEvent(new CalendarEvent(myEventName, myDate, myTime));
        finish();
    }

    public void popUpDatePickerAction(View view) {
        DatePickerDialog dialog = new DatePickerDialog(this, R.style.CustomDatePickerDialog, this, myDate.getYear(), myDate.getMonthValue(), myDate.getDayOfMonth());

        dialog.show();
    }

    public void popUpTimePickerAction(View view) {
        TimePickerDialog dialog = new TimePickerDialog(this, R.style.CustomTimePickerDialog,this, myTime.getHour(), myTime.getMinute(), true);
        dialog.invalidateOptionsMenu();
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        myDate = myDate.withYear(year);
        myDate = myDate.withMonth(monthOfYear);
        myDate = myDate.withDayOfMonth(dayOfMonth);
        updateUI();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        myTime = LocalTime.of( hour,minute);
        updateUI();
    }

}