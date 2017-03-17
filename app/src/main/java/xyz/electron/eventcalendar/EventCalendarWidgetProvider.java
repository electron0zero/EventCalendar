package xyz.electron.eventcalendar;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.widget.RemoteViews;

import xyz.electron.eventcalendar.provider.Contract;

/**
 * Implementation of App Widget functionality.
 */
public class EventCalendarWidgetProvider extends AppWidgetProvider {

    public static String CLICK_ACTION = "xyz.electron.eventcalendar.widget.CLICK";

    private static HandlerThread sWorkerThread;
    private static Handler sWorkerQueue;
    private static EventCalendarContentObserver sDataObserver;

    public EventCalendarWidgetProvider() {
        sWorkerThread = new HandlerThread("EventCalendarWidgetHandler");
        sWorkerThread.start();
        sWorkerQueue = new Handler(sWorkerThread.getLooper());
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        // Update each of the widgets with the remote adapter
        for (int i = 0; i < appWidgetIds.length; ++i) {
            RemoteViews layout = buildLayout(context, appWidgetIds[i]);
            appWidgetManager.updateAppWidget(appWidgetIds[i], layout);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        final ContentResolver r = context.getContentResolver();
        if (sDataObserver == null) {
            final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            final ComponentName cn = new ComponentName(context, EventCalendarWidgetProvider.class);
            sDataObserver = new EventCalendarContentObserver(mgr, cn, sWorkerQueue);
            r.registerContentObserver(Contract.SchEntry.CONTENT_URI, true, sDataObserver);
        }
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context ctx, Intent intent) {
//        final String action = intent.getAction();
//        if (action.equals(CLICK_ACTION)) {
//            final String symbol = intent.getStringExtra(DetailGraphActivity.SELECTED_SYMBOL);
//
//            Intent i = new Intent(ctx, DetailGraphActivity.class);
//            i.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
//            i.putExtra(DetailGraphActivity.SELECTED_SYMBOL, symbol);
//            ctx.startActivity(i);
//        }
        super.onReceive(ctx, intent);
    }

    private RemoteViews buildLayout(Context context, int appWidgetId) {
        RemoteViews rv;

        final Intent intent = new Intent(context, EventCalendarRemoteViewsService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        rv = new RemoteViews(context.getPackageName(), R.layout.event_calendar_widget);
        rv.setRemoteAdapter(R.id.widget_list, intent);

        final Intent onClickIntent = new Intent(context, EventCalendarWidgetProvider.class);
        onClickIntent.setAction(EventCalendarWidgetProvider.CLICK_ACTION);
        onClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        onClickIntent.setData(Uri.parse(onClickIntent.toUri(Intent.URI_INTENT_SCHEME)));
        final PendingIntent onClickPendingIntent = PendingIntent.getBroadcast(context, 0,
                onClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setPendingIntentTemplate(R.id.widget_list, onClickPendingIntent);

        return rv;
    }

}

