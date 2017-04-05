package xyz.electron.eventcalendar.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import xyz.electron.eventcalendar.others.DataObj;
import xyz.electron.eventcalendar.R;


public class ActivitySponsorsAdapter extends ArrayAdapter {

    public ActivitySponsorsAdapter(@NonNull Context context, @NonNull List objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Get the data item for this position
        DataObj.EventScheduleBean.ActivitySponsorsBean activitySponsorsBean
                = (DataObj.EventScheduleBean.ActivitySponsorsBean) getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_activity_sponsor, parent, false);
        }
        // Lookup view for data population
        ImageView image = (ImageView) convertView.findViewById(R.id.activity_spo_image);
        Glide.with(getContext())
                .load(activitySponsorsBean.getImage())
                .placeholder(R.drawable.placeholder)
                .into(image);

        TextView name = (TextView) convertView.findViewById(R.id.activity_spo_name);
        name.setText(activitySponsorsBean.getName());

        TextView url = (TextView) convertView.findViewById(R.id.activity_spo_url);
        url.setText(activitySponsorsBean.getUrl());

        // Return the completed view to render on screen
        return convertView;
    }
}
