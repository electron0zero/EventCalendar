package xyz.electron.eventcalendar.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.database.ContentObserver;
import android.os.Handler;

import xyz.electron.eventcalendar.R;

public class EventCalendarContentObserver extends ContentObserver {

    private AppWidgetManager mAppWidgetManager;
    private ComponentName mComponentName;
    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public EventCalendarContentObserver(AppWidgetManager appWidgetManager,
                                        ComponentName componentName, Handler handler) {
        super(handler);
        mAppWidgetManager = appWidgetManager;
        mComponentName = componentName;
    }

    @Override
    public void onChange(boolean selfChange) {
        // The data has changed, so notify the widget that the collection view
        // needs to be updated.
        // In response, the factory's onDataSetChanged() will be called which
        // will re-query the cursor for the new data.
        mAppWidgetManager.notifyAppWidgetViewDataChanged(
                mAppWidgetManager.getAppWidgetIds(mComponentName), R.id.widget_list);
    }
}
