package com.portal.calendar;

import static java.lang.Math.abs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
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

import com.portal.calendar.Alarm.AlarmItem;
import com.portal.calendar.Alarm.AlarmScheduler;
import com.portal.calendar.Events.CalendarEventModel;
import com.portal.calendar.Events.CalendarEventSQL;
import com.portal.calendar.Utils.CalendarUtils;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

public class EventEditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener, AdapterView.OnItemSelectedListener
{
    //public static String CALENDAR_EVENT_BUNDLE_NAME = "CALENDAR_EVENT_BUNDLE_NAME";
    public static String CALENDAR_EVENT_BUNDLE_EVENT_ID="CALENDAR_EVENT_BUNDLE_EVENT_ID";

    private MediaPlayer mediaPlayer;
    private ArrayList<String> alarmSoundValues = new ArrayList<>(Arrays.asList("", "alarm_clock_beep", "data_scaner", "rooster_crowing_in_the_morning"));
    private ArrayList<Integer> alarmValues = new ArrayList<>(Arrays.asList(-1, 0, 1, 8, 24));
    private EditText eventName;
    private TextView eventDate;
    private TextView eventTime;
    private Spinner eventAlarm;

    private View formDivAlarmSound;
    private Spinner eventAlarmSound;

    private ImageButton playSoundIconBtn;
    private TextView eventDetail;


    private CalendarEventSQL sqlHelper;
    CalendarEventModel model = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        setupGui();

        sqlHelper = new CalendarEventSQL(this);

        long eventId = getIntent().getLongExtra(CALENDAR_EVENT_BUNDLE_EVENT_ID, -1);
        if(eventId >= 0)
            model = sqlHelper.getById((int)eventId);

        /*
        Bundle b = getIntent().getExtras();
        if(b != null){
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                model =  b.getSerializable(CALENDAR_EVENT_BUNDLE_NAME,CalendarEventModel.class) ;
            }else{
                model =  (CalendarEventModel) b.getSerializable(CALENDAR_EVENT_BUNDLE_NAME) ;
            }
        }
        */

        if(model == null){
            model = new CalendarEventModel();
            findViewById(R.id.deleteIconBtn).setVisibility(View.GONE);
            resetForm();
        }
        else{
            findViewById(R.id.saveIconBtn).setVisibility(View.GONE);
            updateUI();
        }
    }
    private void setupGui(){

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


        formDivAlarmSound = findViewById(R.id.formDivAlarmSound);

        eventAlarmSound = findViewById(R.id.eventAlarmSound);
        ArrayAdapter<String> adapterSound = new ArrayAdapter<String>(
                this,
                R.layout.custom_spinner_item,
                CalendarUtils.getStringResourcesArray(this, alarmSoundValues, "alarmSound_")
        );
        adapter.setDropDownViewResource(R.layout.custom_spinner_item);
        eventAlarmSound.setAdapter(adapterSound);
        eventAlarmSound.setOnItemSelectedListener(this);

        playSoundIconBtn = findViewById(R.id.playSoundIconBtn);

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
    }
    private ArrayList<String> getAlarmStrings() {
        ArrayList<String> alarmNames = new ArrayList<>();

        for (Integer val:alarmValues) {
            String stringConst = "eventAlarms_";
            stringConst += (val < 0) ? "N"+abs(val) : val;
            alarmNames.add(CalendarUtils.getStringResourceByName(this, stringConst));
        }
        return alarmNames;
    }

    private void updateUI(){
        eventName.setText(model.name);
        eventTime.setText(CalendarUtils.formTime(model.time));
        eventDate.setText(CalendarUtils.formDate(model.date));
        eventAlarm.setSelection(CalendarUtils.getListPositionByValue(alarmValues, model.alarm),true);
        eventDetail.setText(model.detail);

        if(model.alarm == -1)
            formDivAlarmSound.setVisibility(View.GONE);
        else
            formDivAlarmSound.setVisibility(View.VISIBLE);


        if(model.alarmSoundName == "")
            playSoundIconBtn.setVisibility(View.GONE);
        else
            playSoundIconBtn.setVisibility(View.VISIBLE);

        eventAlarmSound.setSelection(CalendarUtils.getListPositionByValue(alarmSoundValues, model.alarmSoundName),true);


    }
    private void resetForm(){
        model.name = "";
        model.date = CalendarUtils.selectedDate;
        model.time = LocalTime.of( 8,0);
        model.alarm = -1;
        model.alarmSoundName = "";
        model.detail = "";
        updateUI();

    }
    public void toggleSoundAction(View view) {
        if(model.alarmSoundName != ""){
            int resId = getResources().getIdentifier("raw/"+model.alarmSoundName, null, this.getPackageName());

            if (mediaPlayer != null){
                if(mediaPlayer.isPlaying()){
                    stopSoundAction(playSoundIconBtn);
                    return;
                }
                else{
                    mediaPlayer.release();
                }
            }

            mediaPlayer = MediaPlayer.create(view.getContext(), resId);
            mediaPlayer.start();
            playSoundIconBtn.setImageResource(R.drawable.baseline_stop_circle_24);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopSoundAction(playSoundIconBtn);
                }
            });
        }
    }
    public void stopSoundAction(View view) {
        if(mediaPlayer != null){
            mediaPlayer.stop();
            playSoundIconBtn.setImageResource(R.drawable.baseline_play_circle_outline_24);
        }
    }
    public void closeEventAction(View view) {
        finish();
    }

    public void saveEventAction(View view) {
        if(model.isValid()){
            long result = sqlHelper.addOrUpdate(model);
            if(result>0){
                model.id = result;
                if(model.alarm >= 0){
                    AlarmItem ai = new AlarmItem((int)model.id, model.name, model.detail, model.alarmSoundName);
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
        if(sqlHelper.delete(model)){

            AlarmItem ai = new AlarmItem((int)model.id, model.name, model.detail, model.alarmSoundName);
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
        if(parent == eventAlarm){
            int value = alarmValues.get(pos);
            model.alarm = value;
        }
        else{
            String value = alarmSoundValues.get(pos);
            model.alarmSoundName = value;
            stopSoundAction(playSoundIconBtn);
        }
        updateUI();
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}