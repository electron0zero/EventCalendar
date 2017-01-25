package xyz.electron.eventcalendar.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    // Increment the version when there is a change in the structure of database
    public static final int DATABASE_VERSION = 1;
    // The name of the database in the filesystem, you can choose this to be anything
    public static final String DATABASE_NAME = "eventCal.db";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        addSchTable(db);
        addSpoTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void addSchTable(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE " + Contract.SchEntry.TABLE_NAME + " (" +
                        Contract.SchEntry._ID + " INTEGER PRIMARY KEY, " +
                        Contract.SchEntry.COLUMN_NAME + " TEXT NOT NULL);"
        );
    }

    private void addSpoTable(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE " + Contract.SpoEntry.TABLE_NAME + " (" +
                        Contract.SpoEntry._ID + " INTEGER PRIMARY KEY, " +
                        Contract.SpoEntry.COLUMN_NAME + " TEXT NOT NULL);"
        );
    }

}
