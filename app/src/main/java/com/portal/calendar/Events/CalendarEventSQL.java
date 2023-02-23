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

    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "event";

    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_DATE_TIME = "date_time";





    public CalendarEventSQL(@Nullable Context context) {
        super(context, CalendarUtils.DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String q = "CREATE TABLE " + TABLE_NAME  + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COL_NAME + " TEXT, " +
                COL_DATE_TIME + " TEXT " +
        ");";

        db.execSQL(q);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean add(String name, LocalDate date, LocalTime time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_NAME, name);
        cv.put(COL_DATE_TIME, CalendarUtils.toSQLite(date, time));
        long result = db.insert(TABLE_NAME, null, cv);

        if(result == -1){
            String message = context.getResources().getString(R.string.sql_calendarEvents_add) ;
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public ArrayList<CalendarEventModel> getByDay(LocalDate date){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<CalendarEventModel> result = new ArrayList<CalendarEventModel>() ;
        if(db != null){
            String q = "SELECT * FROM " + TABLE_NAME  + " WHERE " +
                    " date(" + COL_DATE_TIME  + ") = '" + CalendarUtils.toSQLite(date) +"'"+
                    " ORDER BY time(" + COL_DATE_TIME  + ")";

            Cursor cursor = null;

            cursor = db.rawQuery(q, null);

            while(cursor.moveToNext()){
                result.add(new CalendarEventModel(cursor.getLong(0), cursor.getString(1), cursor.getString(2)));
            }
        }
        return result;
    }
}
