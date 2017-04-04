package xyz.electron.eventcalendar.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v7.widget.CardView;
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
import xyz.electron.eventcalendar.others.Helpers;


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
        String spoDataObjJSON = cursor.getString(cursor.getColumnIndexOrThrow("spoDataObj"));

        //convert it to DataObj.EventScheduleBean via GSON
        Gson gson = new Gson();
        DataObj.EventSponsorsBean eventSponsorsBean =
                gson.fromJson(spoDataObjJSON, DataObj.EventSponsorsBean.class);

        // Populate fields with extracted properties
        Glide.with(context).load(eventSponsorsBean.getImage()).into(sponsorsImage);

        sponsorsName.setText(eventSponsorsBean.getName());
        sponsorsType.setText(eventSponsorsBean.getType());
        sponsorsUrl.setText(eventSponsorsBean.getUrl());

//        View item = view.findViewById(R.id.activity_sponsor_item);
//        item.setBackgroundColor(Color.parseColor(Helpers.getRandomColor()));
    }
}
