package xyz.electron.eventcalendar.ui;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import xyz.electron.eventcalendar.R;
import xyz.electron.eventcalendar.adapters.ScheduleCursorAdapter;
import xyz.electron.eventcalendar.provider.Contract;
import xyz.electron.eventcalendar.ui.DetailActivity;

public class ScheduleFragment extends Fragment {

    private final String TAG = "ScheduleFragment";

    // Views


    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Schedule Fragment");

        View rootView = inflater.inflate(R.layout.content_schedule, container, false);
        // View adaptor thing
        ListView listView = (ListView) rootView.findViewById(R.id.eventListView);

        Cursor cursor = getActivity().getContentResolver().query(Contract.SchEntry.CONTENT_URI, null, null, null, null);
        ScheduleCursorAdapter scheduleCursorAdapter = new ScheduleCursorAdapter(getActivity(), cursor);

        listView.setAdapter(scheduleCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                //open details activity
                Cursor cur = (Cursor) adapter.getItemAtPosition(position);
                cur.moveToPosition(position);

                String eventObjJSON = cur.getString(cur.getColumnIndexOrThrow("schEventObj"));
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("eventObjJSON", eventObjJSON);
                startActivity(intent);
                // Toast.makeText(MainActivity.this, position + " Meooooow " + id, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

}
