package xyz.electron.eventcalendar;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class EventCalendarWidgetService extends RemoteViewsService{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        // return RemoteViewFactory
        return new EventCalendarWidgetDataProvider(this, intent);
    }
}
