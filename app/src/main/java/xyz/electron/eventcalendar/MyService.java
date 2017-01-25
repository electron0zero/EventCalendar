package xyz.electron.eventcalendar;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


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
                .url("https://raw.githubusercontent.com/electron0zero/CapstoneProject-AND/master/sample_data.json")
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


        // Parse Rest data and Load that into ContentProvider
    }
}
