package com.portal.calendar;

import static java.lang.Math.abs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.portal.calendar.Alarm.AlarmItem;
import com.portal.calendar.Alarm.AlarmScheduler;
import com.portal.calendar.Events.CalendarEventModel;
import com.portal.calendar.Events.CalendarEventSQL;
import com.portal.calendar.Utils.CalendarUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class EventEditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener, AdapterView.OnItemSelectedListener
{
    public static String CALENDAR_EVENT_BUNDLE_NAME = "CALENDAR_EVENT_BUNDLE_NAME";
    private int[] alarmValues = new int[]{-1, 0, 1, 8, 24};
    private EditText eventName;
    private TextView eventDate;
    private TextView eventTime;
    private Spinner eventAlarm;
    private TextView eventDetail;

    CalendarEventModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        ImageButton saveIconBtn = findViewById(R.id.saveIconBtn);
        ImageButton deleteIconBtn = findViewById(R.id.deleteIconBtn);

        eventName = findViewById(R.id.eventName);
        eventName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                model.name = String.valueOf(eventName.getText());
            }
        });

        eventDate = findViewById(R.id.eventDate);
        eventTime = findViewById(R.id.eventTime);


        eventAlarm = findViewById(R.id.eventAlarm);
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.eventAlarms, R.layout.custom_spinner_item);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.custom_spinner_item, getAlarmStrings());
        adapter.setDropDownViewResource(R.layout.custom_spinner_item);
        eventAlarm.setAdapter(adapter);
        eventAlarm.setOnItemSelectedListener(this);

        eventDetail = findViewById(R.id.eventDetail);
        eventDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                model.detail = String.valueOf(eventDetail.getText());
            }
        });


        model = null;
        Bundle b = getIntent().getExtras();
        if(b != null){
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                model =  b.getSerializable(CALENDAR_EVENT_BUNDLE_NAME,CalendarEventModel.class) ;
            }else{
                model =  (CalendarEventModel) b.getSerializable(CALENDAR_EVENT_BUNDLE_NAME) ;
            }
        }
        if(model == null){
            model = new CalendarEventModel();
            deleteIconBtn.setVisibility(View.GONE);
            resetForm();
        }
        else{
            saveIconBtn.setVisibility(View.GONE);
            updateUI();
        }
    }

    private ArrayList<String> getAlarmStrings() {
        ArrayList<String> alarmNames = new ArrayList<>();

        for(int i = 0; i < alarmValues.length; i++){
            String stringConst = "eventAlarms_";
            if(alarmValues[i] < 0)
                stringConst += "N"+abs(alarmValues[i]);
            else
                stringConst += alarmValues[i];

            alarmNames.add(CalendarUtils.getStringResourceByName(this, stringConst));
        }
        return alarmNames;
    }

    private int getAlarmPositionByValue(int value){
        for(int i = 0; i < alarmValues.length; i++){
            if(alarmValues[i]==value)
                return i;
        }
        return 0;
    }
    private void updateUI(){
        eventName.setText(model.name);
        eventTime.setText(CalendarUtils.formTime(model.time));
        eventDate.setText(CalendarUtils.formDate(model.date));
        eventAlarm.setSelection(getAlarmPositionByValue(model.alarm),true);
        eventDetail.setText(model.detail);
    }
    private void resetForm(){
        model.name = "";
        model.date = CalendarUtils.selectedDate;
        model.time = LocalTime.of( 8,0);
        model.alarm = -1;
        model.detail = "";
        updateUI();

    }

    public void closeEventAction(View view) {
        finish();
    }

    public void saveEventAction(View view) {
        if(model.isValid()){
            CalendarEventSQL sqlHelper = new CalendarEventSQL(this);
            long result = sqlHelper.addOrUpdate(model);
            if(result>0){
                model.id = result;
                if(model.alarm >= 0){
                    AlarmItem ai = new AlarmItem((int)model.id, model.name, model.detail);
                    AlarmScheduler as = new AlarmScheduler(this);
                    as.schedule(ai, model.getAlarmDateTime());

                    CalendarUtils.showMsg(this, R.string.event_form_alarmAdded);
                }
                finish();
            }
        }
        else{
            CalendarUtils.showMsg(this, R.string.event_form_invalidFields);
        }
    }
    public void deleteEventAction(View view) {
        CalendarEventSQL sqlHelper = new CalendarEventSQL(this);

        if(sqlHelper.delete(model)){

            AlarmItem ai = new AlarmItem((int)model.id, model.name, model.detail);
            AlarmScheduler as = new AlarmScheduler(this);
            as.cancel(ai);

            finish();
        }
    }
    public void popUpDatePickerAction(View view) {
        DatePickerDialog dialog = new DatePickerDialog(this, R.style.CustomDatePickerDialog, this, model.date.getYear(), model.date.getMonthValue(), model.date.getDayOfMonth());
        dialog.show();
    }

    public void popUpTimePickerAction(View view) {
        TimePickerDialog dialog = new TimePickerDialog(this, R.style.CustomTimePickerDialog,this, model.time.getHour(), model.time.getMinute(), true);
        dialog.invalidateOptionsMenu();
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        model.updateDate(year, monthOfYear, dayOfMonth);
        updateUI();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        model.updateTime(hour,minute);
        updateUI();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
        int value = alarmValues[pos];
        model.alarm = value;
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}