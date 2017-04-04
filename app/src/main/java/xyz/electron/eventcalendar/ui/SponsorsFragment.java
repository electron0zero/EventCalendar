package xyz.electron.eventcalendar.ui;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import xyz.electron.eventcalendar.R;
import xyz.electron.eventcalendar.adapters.SponsorsCursorAdapter;
import xyz.electron.eventcalendar.provider.Contract;

public class SponsorsFragment extends Fragment {

    private final String TAG = "SponsorsFragment";

    Cursor cursor;
    SponsorsCursorAdapter sponsorsCursorAdapter;
    // Views
    ListView listView;

    public SponsorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Sponsors Fragment");

        View rootView = inflater.inflate(R.layout.content_sponsors, container, false);
        // View adaptor thing
        listView = (ListView) rootView.findViewById(R.id.sponsorsListView);

        cursor = getActivity().getContentResolver().query(Contract.SpoEntry.CONTENT_URI, null, null, null, null);
        sponsorsCursorAdapter = new SponsorsCursorAdapter(getContext(), cursor);

        listView.setAdapter(sponsorsCursorAdapter);

        return rootView;
    }

}
