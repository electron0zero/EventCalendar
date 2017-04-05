package xyz.electron.eventcalendar.ui;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import xyz.electron.eventcalendar.R;
import xyz.electron.eventcalendar.adapters.ScheduleCursorAdapter;
import xyz.electron.eventcalendar.provider.Contract;

public class ScheduleFragment extends Fragment {

    private final String TAG = "ScheduleFragment";
    SwipeRefreshLayout swipeRefreshLayout;
    // Views
    GridView gridView;

    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Schedule Fragment");

        View rootView = inflater.inflate(R.layout.content_schedule, container, false);
        // View adaptor thing
        gridView = (GridView) rootView.findViewById(R.id.eventGridView);
        View emptyView = rootView.findViewById(R.id.empty_schedule);
        gridView.setEmptyView(emptyView);

        Cursor cursor = getActivity().getContentResolver().query(Contract.SchEntry.CONTENT_URI, null, null, null, null);
        ScheduleCursorAdapter scheduleCursorAdapter = new ScheduleCursorAdapter(getActivity(), cursor);
        scheduleCursorAdapter.notifyDataSetChanged();

        gridView.setAdapter(scheduleCursorAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        // Fix the problem crated by Grid View not Being Direct child of SwipeRefreshLayout
        swipeRefreshLayout = (SwipeRefreshLayout) getActivity()
                .findViewById(R.id.swipe_to_refresh_layout);

        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if(gridView != null && gridView.getChildCount() > 0){
                    // check if the first item of the list is visible
                    boolean firstItemVisible = gridView.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = gridView.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                // Log.d(TAG, "onScroll: Enable SwipeToRefreshLayout : " + enable);
                swipeRefreshLayout.setEnabled(enable);
            }
        });


        return rootView;
    }

}
