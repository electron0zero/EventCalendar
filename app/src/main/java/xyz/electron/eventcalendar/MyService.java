package xyz.electron.eventcalendar;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import xyz.electron.eventcalendar.provider.Contract;


public class MyService extends IntentService {

    public static final String PREFS_NAME = "MyPrefsFile";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public MyService() {
        // Used to name the worker thread, important only for debugging.
        super("MyServiceThread");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String responseBody = null;

        // Query Network
        // should be a singleton
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://rawgit.com/electron0zero/EventCalendar/master/public-assets/eventdata.json")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            responseBody = response.body().string();

            Log.d("test", "onHandleIntent: " + responseBody);

        } catch (IOException e) {
            e.printStackTrace();
        }
        // parse JSON
        JSONObject respo = null;
        try {
            respo = new JSONObject(responseBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // TODO: 14-03-17
        // Check if we have updated JSON from network, if yes update
        // Content Provider and shared pref. otherwise do nothing
        // check update_number in eventMetadata to if JSON is updated or not

        // eventMetadata
        JSONObject metadata = null;
        try {
            metadata = respo.getJSONObject("eventMetadata");
            // We need an Editor object to make preference changes.
            // All objects are from android.context.Context
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("metadata", metadata.toString());
            // Commit the edits!
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // save metadata to shared pref
        Log.d("test", "onHandleIntent: " + metadata.toString());

        // eventMap
        JSONObject map = null;
        try {
            map = respo.getJSONObject("eventMap");
            // We need an Editor object to make preference changes.
            // All objects are from android.context.Context
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("map", map.toString());
            // Commit the edits!
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // save map to shared pref
        Log.d("test", "onHandleIntent: " + map.toString());

        // eventAbout
        JSONObject about = null;
        try {
            about = respo.getJSONObject("eventAbout");
            // We need an Editor object to make preference changes.
            // All objects are from android.context.Context
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("about", about.toString());
            // Commit the edits!
            editor.commit();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // save about to shared pref
        Log.d("test", "onHandleIntent: " + about.toString());

        // eventSchedule
        JSONArray schedule = null;
        try {
            schedule = respo.getJSONArray("eventSchedule");
            // delete old records from content provider
            int mRowsDeleted = 0;
            mRowsDeleted = getContentResolver().delete(Contract.SchEntry.CONTENT_URI,
                        null, null);
            // add new records to content provider
            JSONObject item = null;
            for (int i = 0; i < schedule.length(); i++){
                item = schedule.getJSONObject(i);
                ContentValues mContentValues = new ContentValues();
                Uri mNewUri;

                mContentValues.put(Contract.SchEntry.COLUMN_NAME, item.toString());
                // update each item in ContentProvider
                mNewUri = getContentResolver().insert(Contract.SchEntry.CONTENT_URI,
                        mContentValues);
                Log.d("test", "onHandleIntent: New URI after insert " + mNewUri);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        Log.d("test", "onHandleIntent: " + schedule.toString());

        JSONArray sponsors = null;
        try {
            sponsors = respo.getJSONArray("eventSponsors");

            //delete old records
            int mRowsDeleted = 0;
            mRowsDeleted = getContentResolver().delete(Contract.SpoEntry.CONTENT_URI,
                    null, null);

            // add new rows

            JSONObject item = null;
            for (int i = 0; i < sponsors.length(); i++){
                item = sponsors.getJSONObject(i);

                ContentValues mContentValues = new ContentValues();
                Uri mNewUri;

                mContentValues.put(Contract.SpoEntry.COLUMN_NAME, item.toString());
                // update each item in ContentProvider

                mNewUri = getContentResolver().insert(Contract.SpoEntry.CONTENT_URI,
                        mContentValues);
                Log.d("test", "onHandleIntent: New URI after insert " + mNewUri);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        Log.d("test", "onHandleIntent: " + sponsors.toString());

        // Parse data and Load that into ContentProvider done
    }
}
