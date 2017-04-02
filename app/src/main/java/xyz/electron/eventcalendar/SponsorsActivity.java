package xyz.electron.eventcalendar;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import xyz.electron.eventcalendar.provider.Contract;

public class SponsorsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsors);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // View adaptor thing
        ListView listView = (ListView) findViewById(R.id.sponsorsListView);

        Cursor cursor = getContentResolver().query(Contract.SpoEntry.CONTENT_URI, null, null, null, null);

        SponsorsCursorAdapter sponsorsCursorAdapter = new SponsorsCursorAdapter(this, cursor);

        listView.setAdapter(sponsorsCursorAdapter);
    }


}
