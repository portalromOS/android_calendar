package com.portal.calendar.Alarm;

import java.time.LocalDateTime;

public class AlarmItem {
    int eventId;
    String title;
    String detail;

    public AlarmItem(int eventId, String title, String detail) {
        this.eventId = eventId;
        this.title = title;
        this.detail = detail;
    }
}
