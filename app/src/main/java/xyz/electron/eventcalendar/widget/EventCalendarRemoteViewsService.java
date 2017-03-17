package xyz.electron.eventcalendar.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class EventCalendarRemoteViewsService extends RemoteViewsService{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        // return RemoteViewFactory
        return new EventCalendarRemoteViewsFactory(this, intent);
    }
}
