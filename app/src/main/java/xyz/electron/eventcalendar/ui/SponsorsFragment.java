package xyz.electron.eventcalendar.ui;

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
import android.widget.GridView;

import xyz.electron.eventcalendar.R;
import xyz.electron.eventcalendar.adapters.SponsorsCursorAdapter;
import xyz.electron.eventcalendar.provider.Contract;

public class SponsorsFragment extends Fragment implements LoaderCallbacks<Cursor>{

    private final String TAG = "SponsorsFragment";
    // Defines the id of the loader for later reference
    // A unique identifier for this loader. Can be whatever you want.
    public static final int SPONSORS_LOADER_ID = 1;
    CursorLoader cursorLoader;
    SponsorsCursorAdapter sponsorsCursorAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    // Views
    GridView gridView;
    View rootView;

    public SponsorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Sponsors Fragment");

        rootView = inflater.inflate(R.layout.content_sponsors, container, false);
        // View adaptor thing
        gridView = (GridView) rootView.findViewById(R.id.sponsorsGridView);
        View emptyView = rootView.findViewById(R.id.empty_sponsors);
        gridView.setEmptyView(emptyView);
        // set a null Cursor, CursorLoader Will Load the data at when available
        sponsorsCursorAdapter = new SponsorsCursorAdapter(getContext(), null);
        gridView.setAdapter(sponsorsCursorAdapter);

        // Initialize the loader with a special ID and the defined callbacks from above
        getLoaderManager().initLoader(SPONSORS_LOADER_ID, null, this);

        return rootView;
    }

    @Override
    public void onStart() {
        // Fix the problem caused by Grid View not Being Direct child of SwipeRefreshLayout
        // doing it on Start Ensures that our Host Activity Is
        // created and We can get views from it

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
                if (gridView != null && gridView.getChildCount() > 0) {
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

        super.onStart();
    }

    @Override
    public void onResume() {
        // handle case when we have No content in Grid View aka Empty View case
        if (gridView.getAdapter().isEmpty()) {
            Log.d(TAG, "onResume: SponsorsFragment gridView empty");
            swipeRefreshLayout.setEnabled(true);
        }
        super.onResume();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        cursorLoader = new CursorLoader(getContext(),
                Contract.SponsorsEntry.CONTENT_URI, null, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        sponsorsCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        sponsorsCursorAdapter.swapCursor(null);
    }
}
