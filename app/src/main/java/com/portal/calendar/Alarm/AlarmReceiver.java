package com.portal.calendar.Alarm;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = "";
        String detail = "";

        if(intent != null){
            int eventId = intent.getIntExtra("EVENT_ID", -1);
            title = intent.getStringExtra("EVENT_TITLE");
            detail = intent.getStringExtra("EVENT_DETAIL");
            Log.i("Calendar", title);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

    }
}
