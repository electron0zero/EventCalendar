package xyz.electron.eventcalendar.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    // Increment the version when there is a change in the structure of database
    public static final int DATABASE_VERSION = 1;
    // The name of the database in the filesystem, you can choose this to be anything
    public static final String DATABASE_NAME = "EventCalendarDB.db";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        addScheduleTable(db);
        addSponsorsTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void addScheduleTable(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE " + Contract.ScheduleEntry.TABLE_NAME + " (" +
                        Contract.ScheduleEntry._ID + " INTEGER PRIMARY KEY, " +
                        Contract.ScheduleEntry.COLUMN_NAME + " TEXT NOT NULL);"
        );
    }

    private void addSponsorsTable(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE " + Contract.SponsorsEntry.TABLE_NAME + " (" +
                        Contract.SponsorsEntry._ID + " INTEGER PRIMARY KEY, " +
                        Contract.SponsorsEntry.COLUMN_NAME + " TEXT NOT NULL);"
        );
    }

}
