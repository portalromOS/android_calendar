package com.portal.calendar.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class AlarmScheduler implements  AlarmSchedulerInterface{
    private Context context;
    private AlarmManager manager;

    public AlarmScheduler(Context context) {
        this.context = context;
        manager = context.getSystemService(AlarmManager.class);
    }

    @Override
    public void schedule(AlarmItem item, LocalDateTime dateTime) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("EVENT_TITLE", item.title);
        intent.putExtra("EVENT_DETAIL", item.detail);
        intent.putExtra("EVENT_ID", item.eventId);
        manager.setExactAndAllowWhileIdle(
            manager.RTC_WAKEUP,
            dateTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            PendingIntent.getBroadcast(
                context,
                item.eventId,
                intent,
                PendingIntent.FLAG_MUTABLE
            )
        );
    }

    @Override
    public void cancel(AlarmItem item) {
        manager.cancel(
            PendingIntent.getBroadcast(
                context,
                item.eventId,
                new Intent(context, AlarmReceiver.class),
                PendingIntent.FLAG_MUTABLE
            )
        );

    }
}
