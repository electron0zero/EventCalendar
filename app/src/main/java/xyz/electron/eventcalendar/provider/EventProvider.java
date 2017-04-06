package xyz.electron.eventcalendar.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class EventProvider extends ContentProvider {

    // Use an int for each URI we will run, this represents the different queries
    private static final int SCH = 100;
    private static final int SCH_ID = 101;
    private static final int SPO = 200;
    private static final int SPO_ID = 201;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private DBHelper mDBHelper;

    /**
     * Builds a UriMatcher that is used to determine witch database request is being made.
     */
    public static UriMatcher buildUriMatcher(){
        String content = Contract.CONTENT_AUTHORITY;

        // All paths to the UriMatcher have a corresponding code to return
        // when a match is found (the ints above).
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(content, Contract.PATH_SCHEDULE, SCH);
        matcher.addURI(content, Contract.PATH_SCHEDULE + "/#", SCH_ID);
        matcher.addURI(content, Contract.PATH_SPONSORS, SPO);
        matcher.addURI(content, Contract.PATH_SPONSORS + "/#", SPO_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDBHelper = new DBHelper(getContext());
        return true;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch(sUriMatcher.match(uri)){
            case SCH:
                return Contract.ScheduleEntry.CONTENT_TYPE;
            case SCH_ID:
                return Contract.ScheduleEntry.CONTENT_ITEM_TYPE;
            case SPO:
                return Contract.SponsorsEntry.CONTENT_TYPE;
            case SPO_ID:
                return Contract.SponsorsEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        Cursor retCursor;

        switch(sUriMatcher.match(uri)){
            case SCH:
                retCursor = db.query(
                        Contract.ScheduleEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case SCH_ID:
                long _id = ContentUris.parseId(uri);
                retCursor = db.query(
                        Contract.ScheduleEntry.TABLE_NAME,
                        projection,
                        Contract.ScheduleEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case SPO:
                retCursor = db.query(
                        Contract.SponsorsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case SPO_ID:
                _id = ContentUris.parseId(uri);
                retCursor = db.query(
                        Contract.SponsorsEntry.TABLE_NAME,
                        projection,
                        Contract.SponsorsEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set the notification URI for the cursor to the one passed into the function. This
        // causes the cursor to register a content observer to watch for changes that happen to
        // this URI and any of it's descendants. By descendants, we mean any URI that begins
        // with this path.
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        long _id;
        Uri returnUri;

        switch(sUriMatcher.match(uri)){
            case SCH:
                _id = db.insert(Contract.ScheduleEntry.TABLE_NAME, null, values);
                if(_id > 0){
                    returnUri =  Contract.ScheduleEntry.buildSchUri(_id);
                } else{
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            case SPO:
                _id = db.insert(Contract.SponsorsEntry.TABLE_NAME, null, values);
                if(_id > 0){
                    returnUri = Contract.SponsorsEntry.buildSpoUri(_id);
                } else{
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Use this on the URI passed into the function to notify any observers that the uri has
        // changed.
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int rows; // Number of rows effected

        switch(sUriMatcher.match(uri)){
            case SCH:
                rows = db.delete(Contract.ScheduleEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case SPO:
                rows = db.delete(Contract.SponsorsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Because null could delete all rows:
        if(selection == null || rows != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int rows;

        switch(sUriMatcher.match(uri)){
            case SCH:
                rows = db.update(Contract.ScheduleEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case SPO:
                rows = db.update(Contract.SponsorsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rows != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }


}
