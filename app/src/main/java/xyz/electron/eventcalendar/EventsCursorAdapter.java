package xyz.electron.eventcalendar;


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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class EventsCursorAdapter extends CursorAdapter{

    public EventsCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Find fields to populate in inflated template
        ImageView eventImage = (ImageView) view.findViewById(R.id.event_image);
        TextView eventName = (TextView) view.findViewById(R.id.event_name);
        TextView eventDate = (TextView) view.findViewById(R.id.event_date);
        TextView eventTime = (TextView) view.findViewById(R.id.event_time);

         // Extract properties from cursor
        String eventObjJSON = cursor.getString(cursor.getColumnIndexOrThrow("schEventObj"));

        //convert it to DataObj.EventScheduleBean via GSON
        Gson gson = new Gson();
        DataObj.EventScheduleBean eventScheduleBean =
                            gson.fromJson(eventObjJSON, DataObj.EventScheduleBean.class);

        // Populate fields with extracted properties
        Glide.with(context).load(eventScheduleBean.getImage()).into(eventImage);
        eventName.setText(eventScheduleBean.getName());
        eventDate.setText(eventScheduleBean.getDate());
        eventTime.setText(eventScheduleBean.getTime());
    }
}
