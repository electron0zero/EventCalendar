package xyz.electron.eventcalendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.awareness.fence.FenceState;

import static xyz.electron.eventcalendar.MainActivity.ACTION_FENCE;
import static xyz.electron.eventcalendar.MainActivity.KEY_INSIDE_EVENT_VENUE;

public class EventFenceBroadcastReceiver extends BroadcastReceiver {

    private final String TAG = "EventFence";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(TextUtils.equals(ACTION_FENCE, intent.getAction())) {
            FenceState fenceState = FenceState.extract(intent);

            if( TextUtils.equals(KEY_INSIDE_EVENT_VENUE, fenceState.getFenceKey() ) ) {
                if( fenceState.getCurrentState() == FenceState.TRUE ) {
                    Log.e(TAG, "Fence API Broadcast received");
                }
            }
        }
    }
}