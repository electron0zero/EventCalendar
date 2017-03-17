package xyz.electron.eventcalendar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

public class AboutActivity extends AppCompatActivity {

    SharedPreferences mSettings;
    String metadata;
    String about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get Shared preference
        // TODO: handle case when we get Nothing(null/None) in metadata and about
        mSettings = getSharedPreferences(MyService.PREFS_NAME, 0);
        metadata = mSettings.getString("metadata", "Not Found");
        about = mSettings.getString("about", "Not Found");
        // Log.d("test", metadata);
        // Log.d("test", about);

        // convert it to DataObj via GSON
        Gson gson = new Gson();
        DataObj.EventMetadataBean eventMetadataBean =
                gson.fromJson(metadata, DataObj.EventMetadataBean.class);
        DataObj.EventAboutBean eventAboutBean =
                gson.fromJson(about, DataObj.EventAboutBean.class);

        // get Views
        ImageView event_icon = (ImageView) findViewById(R.id.event_icon);
        ImageView event_poster = (ImageView) findViewById(R.id.event_poster);
        TextView event_name = (TextView) findViewById(R.id.event_name);
        TextView event_info = (TextView) findViewById(R.id.eventAbout_info);
        TextView event_org = (TextView) findViewById(R.id.eventAbout_org);
        TextView event_org_phone = (TextView) findViewById(R.id.eventAbout_org_phone);
        TextView event_org_email = (TextView) findViewById(R.id.eventAbout_org_email);
        TextView event_address = (TextView) findViewById(R.id.eventAbout_address);

        // populate it
        Glide.with(getApplicationContext()).load(eventMetadataBean.getIconUrl()).into(event_icon);
        Glide.with(getApplicationContext()).load(eventMetadataBean.getPosterUrl()).into(event_poster);
        event_name.setText(eventMetadataBean.getEvent_name());
        event_info.setText(eventAboutBean.getInfo());
        event_org.setText(eventAboutBean.getOrganiser());
        event_org_phone.setText(eventAboutBean.getOrganiser_contact_phone());
        event_org_email.setText(eventAboutBean.getOrganiser_contact_email());
        event_address.setText(eventAboutBean.getAddress_of_event());
    }

}
