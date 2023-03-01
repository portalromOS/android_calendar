package com.portal.calendar.CalendarNotification;

import static androidx.core.app.NotificationCompat.CATEGORY_EVENT;
import static androidx.core.app.NotificationCompat.PRIORITY_HIGH;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.portal.calendar.R;
public class NotificationHelper extends ContextWrapper {
    public static final String NOTIFICATION_CHANNEL_DEFAULT_ID = "channelId";
    public static final String NOTIFICATION_CHANNEL_DEFAULT_NAME = "channelName";

    private NotificationManager nManager;

    public NotificationHelper(Context context) {
        super(context);
        createChannels();

    }

    public void createChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            //Needed for versions bigger than 26
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_DEFAULT_ID,
                    NOTIFICATION_CHANNEL_DEFAULT_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLightColor(R.color.highLight);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            getManager().createNotificationChannel(channel);
        }
    }

    public NotificationManager getManager(){
        if(nManager == null)
            nManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        return nManager;
    }

    public NotificationCompat.Builder getChannelNotification(PendingIntent resultPendingIntent,String title, String detail, int icon, String soundName){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_DEFAULT_ID);
        builder.setContentTitle(title);
        builder.setContentText(detail);
        builder.setSmallIcon(icon);
        builder.setPriority(PRIORITY_HIGH);
        builder.setCategory(CATEGORY_EVENT);
        builder.setAutoCancel(true);

        if(soundName != null){
            Uri uri = Uri.parse("android.resource://" + this.getPackageName() + "/raw/"+soundName);
            builder.setSound(uri);
        }

        builder.setContentIntent(resultPendingIntent);
        return builder;
    }
}
