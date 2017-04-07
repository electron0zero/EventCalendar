package xyz.electron.eventcalendar.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;

import xyz.electron.eventcalendar.others.DataObj;
import xyz.electron.eventcalendar.R;
import xyz.electron.eventcalendar.adapters.ActivitySponsorsAdapter;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        String eventObjJSON = intent.getStringExtra("eventObjJSON");

        //convert it to DataObj.EventScheduleBean via GSON
        Gson gson = new Gson();
        DataObj.EventScheduleBean eventScheduleBean =
                gson.fromJson(eventObjJSON, DataObj.EventScheduleBean.class);

        ImageView image = (ImageView) findViewById(R.id.event_detail_image);
        Glide.with(this)
                .load(eventScheduleBean.getImage())
                .placeholder(R.drawable.placeholder)
                .into(image);

        TextView name = (TextView) findViewById(R.id.event_detail_name);
        name.setText(eventScheduleBean.getName());

        TextView date = (TextView) findViewById(R.id.event_detail_date);
        date.setText(eventScheduleBean.getDate());

        TextView time = (TextView) findViewById(R.id.event_detail_time);
        time.setText(eventScheduleBean.getTime());

        TextView details = (TextView) findViewById(R.id.event_detail_detail);
        details.setText(eventScheduleBean.getDetails());

        TextView phone = (TextView) findViewById(R.id.event_detail_phone);
        phone.setText(eventScheduleBean.getContact_phone());

        TextView email = (TextView) findViewById(R.id.event_detail_email);
        email.setText(eventScheduleBean.getContact_email());

        TextView note = (TextView) findViewById(R.id.event_detail_note);
        note.setText(eventScheduleBean.getSpacial_note());

        TextView loc = (TextView) findViewById(R.id.event_detail_location);
        loc.setText(eventScheduleBean.getEvent_location());

        DataObj.EventScheduleBean.RegisterBean registerBean = eventScheduleBean.getRegister();

        // Registration Card
        TextView reg_required = (TextView) findViewById(R.id.event_detail_reg_title_required);
        if (Boolean.valueOf(registerBean.getIsRequired())) {
            reg_required.setText(R.string.registration_required);
            TextView reg_url = (TextView) findViewById(R.id.event_detail_reg_url);
            reg_url.setText(registerBean.getUrl());
            TextView reg_con = (TextView) findViewById(R.id.event_detail_reg_contact);
            reg_con.setText(registerBean.getContact());
            TextView fees = (TextView) findViewById(R.id.event_detail_reg_fees);
            fees.setText(registerBean.getFees());
        } else {
            reg_required.setText(R.string.registration_not_required);
            CardView cardView = (CardView) findViewById(R.id.event_detail_reg_card);
            if(cardView != null){
                cardView.setVisibility(View.GONE);
            }
        }

        // Sponsors List View
        List<DataObj.EventScheduleBean.ActivitySponsorsBean> activitySponsors
                = eventScheduleBean.getActivitySponsors();
        ActivitySponsorsAdapter activitySponsorsAdapter
                = new ActivitySponsorsAdapter(this, activitySponsors);

        ListView listView = (ListView) findViewById(R.id.event_detail_spo_listView);
        listView.setAdapter(activitySponsorsAdapter);
    }
}
