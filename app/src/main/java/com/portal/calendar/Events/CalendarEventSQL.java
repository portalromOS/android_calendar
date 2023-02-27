package com.portal.calendar.Events;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.portal.calendar.Utils.CalendarUtils;
import com.portal.calendar.R;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class CalendarEventSQL extends SQLiteOpenHelper {
    private Context context;

    private static final int DB_VERSION = 3;

    private static final String TABLE_NAME = "event";

    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_DATE_TIME = "date_time";
    private static final String COL_DETAIL = "detail";
    private static final String COL_ALARM = "alarm";



    public CalendarEventSQL(@Nullable Context context) {
        super(context, CalendarUtils.DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String q = "CREATE TABLE " + TABLE_NAME  + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COL_NAME + " TEXT, " +
                COL_DATE_TIME + " TEXT, " +
                COL_ALARM + " INTEGER, " +
                COL_DETAIL + " TEXT " +
        ");";

        db.execSQL(q);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addOrUpdate(CalendarEventModel model){
        long result = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_NAME, model.name);
        cv.put(COL_DATE_TIME, CalendarUtils.toSQLite(model.date, model.time));
        cv.put(COL_ALARM, model.alarm);
        cv.put(COL_DETAIL, model.detail);

        if(model.hasId()){
            result = db.update(TABLE_NAME, cv, COL_ID+"=?", new String[]{model.id+""});
            if(result == -1){
                CalendarUtils.showMsg(context, R.string.sql_calendarEvents_update);
                return false;
            }
        }
        else{
            result = db.insert(TABLE_NAME, null, cv);
            if(result == -1){
                CalendarUtils.showMsg(context, R.string.sql_calendarEvents_add);
                return false;
            }
        }

        return true;
    }

    public boolean delete(CalendarEventModel model) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(TABLE_NAME, COL_ID+"=?", new String[]{model.id+""});

        if(result == -1){
            CalendarUtils.showMsg(context, R.string.sql_calendarEvents_remove);
            return false;
        }
        return true;
    }



    public ArrayList<CalendarEventModel> getByDay(LocalDate date){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<CalendarEventModel> result = new ArrayList<CalendarEventModel>() ;
        if(db != null){
            String q = "SELECT " + COL_ID  + ", " + COL_NAME  + ", " + COL_DATE_TIME  + ", " + COL_ALARM  + ", " + COL_DETAIL  +
                    " FROM " + TABLE_NAME  +
                    " WHERE " +" date(" + COL_DATE_TIME  + ") = '" + CalendarUtils.toSQLite(date) +"'"+
                    " ORDER BY time(" + COL_DATE_TIME  + ")";

            Cursor cursor = null;

            cursor = db.rawQuery(q, null);

            while(cursor.moveToNext()){

                result.add(new CalendarEventModel(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4)));
            }
        }
        return result;
    }

    public boolean hasEventByDay(LocalDate date){
        SQLiteDatabase db = this.getReadableDatabase();
        boolean result = false;
        if(db != null){
            String q = "SELECT COUNT(" + COL_ID  + ") " +
                    " FROM " + TABLE_NAME  +
                    " WHERE " +" date(" + COL_DATE_TIME  + ") = '" + CalendarUtils.toSQLite(date) +"'";

            Cursor cursor = null;

            cursor = db.rawQuery(q, null);
            cursor.moveToFirst();
            int count= cursor.getInt(0);
            cursor.close();

            result = (count>0);

        }
        return result;
    }
}
