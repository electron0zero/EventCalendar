package xyz.electron.eventcalendar;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

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
        TextView eventName = (TextView) view.findViewById(R.id.event_name);
        // Extract properties from cursor
        String eventObj = cursor.getString(cursor.getColumnIndexOrThrow("schEventObj"));
        // Populate fields with extracted properties
        eventName.setText(eventObj.toString());
    }
}
