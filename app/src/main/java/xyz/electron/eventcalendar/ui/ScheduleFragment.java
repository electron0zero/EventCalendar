package xyz.electron.eventcalendar.ui;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
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

public class ScheduleFragment extends Fragment implements LoaderCallbacks<Cursor> {

    private final String TAG = "ScheduleFragment";
    // Defines the id of the loader for later reference
    // A unique identifier for this loader. Can be whatever you want.
    public static final int SCHEDULE_LOADER_ID = 2;
    CursorLoader cursorLoader;
    ScheduleCursorAdapter scheduleCursorAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    // Views
    GridView gridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Log.d(TAG, "onCreateView: Schedule Fragment");

        View rootView = inflater.inflate(R.layout.content_schedule, container, false);
        // View adaptor thing
        gridView = (GridView) rootView.findViewById(R.id.eventGridView);
        View emptyView = rootView.findViewById(R.id.empty_schedule);
        gridView.setEmptyView(emptyView);

        scheduleCursorAdapter = new ScheduleCursorAdapter(getActivity(), null);
        gridView.setAdapter(scheduleCursorAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                //open details activity
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        // Initialize the loader with a special ID and the defined callbacks from above
        getLoaderManager().initLoader(SCHEDULE_LOADER_ID, null, this);

        return rootView;
    }

    @Override
    public void onStart() {
        // Fix the problem caused by Grid View not Being Direct child of SwipeRefreshLayout
        // doing it on Start Ensures that our Host Activity Is
        // created and We can get views from it

        swipeRefreshLayout = (SwipeRefreshLayout) getActivity()
                .findViewById(R.id.swipe_to_refresh_layout);

        // Disable mSwipeRefreshLayout when scrolling and not on TOp to tackle the
        // Problem of GridView Not being The direct Child of SwipeRefreshLayout.
        // Hence We can not Scroll Back up in GridViews
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if (gridView != null && gridView.getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = gridView.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = gridView.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                // Log.e(TAG, "onScroll: Enable State " + enable);
                if (gridView.getAdapter().isEmpty()) {
                    // Log.e(TAG, "onStart: gridView empty");
                    swipeRefreshLayout.setEnabled(true);
                }else {
                    swipeRefreshLayout.setEnabled(enable);
                }
            }
        });

        super.onStart();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        cursorLoader = new CursorLoader(getContext(),
                Contract.ScheduleEntry.CONTENT_URI, null, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        scheduleCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        scheduleCursorAdapter.swapCursor(null);
    }

}
