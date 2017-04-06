package xyz.electron.eventcalendar.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import xyz.electron.eventcalendar.others.DataObj;
import xyz.electron.eventcalendar.R;
import xyz.electron.eventcalendar.provider.Contract;


public class SponsorsCursorAdapter extends CursorAdapter {

    public SponsorsCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_sponsor, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Find fields to populate in inflated template
        ImageView sponsorsImage = (ImageView) view.findViewById(R.id.sponsors_image);
        TextView sponsorsName = (TextView) view.findViewById(R.id.sponsors_name);
        TextView sponsorsType = (TextView) view.findViewById(R.id.sponsors_type);
        TextView sponsorsUrl = (TextView) view.findViewById(R.id.sponsors_url);

        // Extract properties from cursor
        String sponsorsDataObjJSON = cursor.getString(cursor.
                getColumnIndexOrThrow(Contract.SponsorsEntry.COLUMN_NAME));

        //convert it to DataObj.EventScheduleBean via GSON
        Gson gson = new Gson();
        DataObj.EventSponsorsBean eventSponsorsBean =
                gson.fromJson(sponsorsDataObjJSON, DataObj.EventSponsorsBean.class);

        // Populate fields with extracted properties
        Glide.with(context)
                .load(eventSponsorsBean.getImage())
                .placeholder(R.drawable.placeholder)
                .into(sponsorsImage);

        sponsorsName.setText(eventSponsorsBean.getName());
        sponsorsType.setText(eventSponsorsBean.getType());
        sponsorsUrl.setText(eventSponsorsBean.getUrl());
    }
}
