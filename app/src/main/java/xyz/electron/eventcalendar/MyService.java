package xyz.electron.eventcalendar;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
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
                .url("https://gist.githubusercontent.com/electron0zero/88783beb4d666eb2cc56d4dd669e887b/raw/99e6ca10a530a3f4000446f2cdb635134f4300e9/IGN_test_JSON")
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

        JSONObject metadata = null;
        try {
            metadata = respo.getJSONObject("eventMetadata");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // save metadata to shared pref
        Log.d("test", "onHandleIntent: " + metadata.toString());

        JSONObject map = null;
        try {
            map = respo.getJSONObject("eventMap");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // save map to shared pref
        Log.d("test", "onHandleIntent: " + map.toString());

        JSONObject about = null;
        try {
            about = respo.getJSONObject("eventAbout");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // save about to shared pref
        Log.d("test", "onHandleIntent: " + about.toString());

        JSONArray schedule = null;
        try {
            schedule = respo.getJSONArray("eventSchedule");
            //delete old records
            int mRowsDeleted = 0;
            mRowsDeleted = getContentResolver().delete(Contract.SchEntry.CONTENT_URI,
                        null, null);

            // then add new records
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
