package com.portal.calendar.MonthDay;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.portal.calendar.Events.CalendarEventModel;
import com.portal.calendar.Events.CalendarEventSQL;
import com.portal.calendar.R;
import com.portal.calendar.Utils.CalendarUtils;

import java.time.LocalDate;
import java.util.ArrayList;

public class MonthDayViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
    public final TextView dayOfMonth_txt;
    public final ImageView dayOfMonth_event_notification;
    private final MonthDayAdapter.OnItemListener onItemListener;

    private static  CalendarEventSQL helperSQL;
    private int day;
    public MonthDayViewHolder(@NonNull View itemView, MonthDayAdapter.OnItemListener onItemListener) {
        super(itemView);
        dayOfMonth_txt = itemView.findViewById(R.id.cellEventTextView);
        dayOfMonth_event_notification= itemView.findViewById(R.id.cellEventNotification);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    public void setDayOfMonth(Context context, int day){
        this.day = day;
        dayOfMonth_event_notification.setVisibility(View.GONE);
        if(day>0) {
            dayOfMonth_txt.setText(day + "");

            LocalDate date = LocalDate.of(CalendarUtils.selectedDate.getYear(), CalendarUtils.selectedDate.getMonth(), day);


            if(getHelperSQL(context).hasEventByDay(date)){
                dayOfMonth_event_notification.setVisibility(View.VISIBLE);
            }

            //ArrayList<CalendarEventModel> eventsOfTheDay = getHelperSQL(context).getEventsByDayMonthView(date);


        }
        else {
            dayOfMonth_txt.setText("");
        }
    }

    private CalendarEventSQL getHelperSQL(Context context){
        if(helperSQL == null){
            helperSQL = new CalendarEventSQL(context);
        }
        return helperSQL;
    }

    @Override
    public void onClick(View view) {
        onItemListener.onItemClick(getAdapterPosition(), day);
    }
}
