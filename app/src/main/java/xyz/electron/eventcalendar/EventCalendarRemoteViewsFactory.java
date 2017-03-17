package xyz.electron.eventcalendar;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import xyz.electron.eventcalendar.provider.Contract;


public class EventCalendarRemoteViewsFactory implements
        RemoteViewsService.RemoteViewsFactory {
    // TODO: 17-03-17 Rename and refactor Widget class names

    private Context mContext;
    private Cursor mCursor;
    private int mAppWidgetId;


    public EventCalendarRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

    }

    @Override
    public void onCreate() {
        // Since we reload the cursor in onDataSetChanged() which gets called immediately after
        // onCreate(), we do nothing here.
    }

    @Override
    public void onDataSetChanged() {
        // Refresh the cursor
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = mContext.getContentResolver().query(Contract.SchEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        // TODO: 17-03-17 Handle This
        // Get the data for this position from the content provider
        String scheduleJSON = "";
        if (mCursor.moveToPosition(position)) {
            scheduleJSON = mCursor.getString(mCursor.getColumnIndexOrThrow("schEventObj"));
        }
        Gson gson = new Gson();
        DataObj.EventScheduleBean eventScheduleBean =
                gson.fromJson(scheduleJSON, DataObj.EventScheduleBean.class);

        // Fill data in UI
        final int itemId = R.layout.item_widget_event;
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), itemId);
        rv.setTextViewText(R.id.widget_event_name, eventScheduleBean.getName());
        rv.setTextViewText(R.id.widget_event_date, eventScheduleBean.getDate());
        rv.setTextViewText(R.id.widget_event_time, eventScheduleBean.getTime());

        // TODO: Set the click intent

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}