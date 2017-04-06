package xyz.electron.eventcalendar.ui;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import xyz.electron.eventcalendar.others.DataObj.EventMetadataBean;
import xyz.electron.eventcalendar.others.DataObj.EventAboutBean;
import xyz.electron.eventcalendar.others.FetchDataService;
import xyz.electron.eventcalendar.R;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {
    private final String TAG = "AboutFragment";
    SharedPreferences mSettings;
    SwipeRefreshLayout swipeRefreshLayout;

    String metadataJSON = null;
    String aboutJSON = null;

    // Views
    View rootView;
    NestedScrollView scrollView;
    ImageView event_icon;
    ImageView event_poster;
    TextView event_name;
    TextView event_info;
    TextView event_org;
    TextView event_org_phone;
    TextView event_org_email;
    TextView event_address;


    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: About Fragment");

        rootView = inflater.inflate(R.layout.content_about, container, false);

        event_icon = (ImageView) rootView.findViewById(R.id.event_icon);
        event_poster = (ImageView) rootView.findViewById(R.id.event_poster);

        event_name = (TextView) rootView.findViewById(R.id.event_name);
        event_info = (TextView) rootView.findViewById(R.id.eventAbout_info);
        event_org = (TextView) rootView.findViewById(R.id.eventAbout_org);
        event_org_phone = (TextView) rootView.findViewById(R.id.eventAbout_org_phone);
        event_org_email = (TextView) rootView.findViewById(R.id.eventAbout_org_email);
        event_address = (TextView) rootView.findViewById(R.id.eventAbout_address);


        // get Shared preference
        // TODO: handle case when not valid JSON as data
        mSettings = getActivity().getSharedPreferences(FetchDataService.PREFS_NAME, 0);
        metadataJSON = mSettings.getString("metadata", null);
        aboutJSON = mSettings.getString("about", null);
        // Log.d("test", metadataJSON);
        // Log.d("test", aboutJSON);

        Gson gson = new Gson();

        if(metadataJSON != null){
            EventMetadataBean metadata = gson.fromJson(metadataJSON, EventMetadataBean.class);

            Glide.with(getContext())
                    .load(metadata.getIconUrl())
                    .placeholder(R.drawable.placeholder)
                    .into(event_icon);
            Glide.with(getContext())
                    .load(metadata.getPosterUrl())
                    .placeholder(R.drawable.placeholder)
                    .into(event_poster);
            event_name.setText(metadata.getEvent_name());
        }

        if (aboutJSON != null){
            EventAboutBean about = gson.fromJson(aboutJSON, EventAboutBean.class);
            event_info.setText(about.getInfo());
            event_org.setText(about.getOrganiser());
            event_org_phone.setText(about.getOrganiser_contact_phone());
            event_org_email.setText(about.getOrganiser_contact_email());
            event_address.setText(about.getAddress_of_event());
        }

        return rootView;
    }

    @Override
    public void onStart() {
        // Fix the problem caused by Grid View not Being Direct child of SwipeRefreshLayout
        // doing it on Start Ensures that our Host Activity Is
        // created and We can get views from it

        // get The scrolling View in Fragment
        scrollView = (NestedScrollView) rootView.findViewById(R.id.about_view);

        swipeRefreshLayout = (SwipeRefreshLayout) getActivity()
                .findViewById(R.id.swipe_to_refresh_layout);

        // enable by default so user can Refresh even when dragging the screen
        // dragging means "noScrollChange" Occurred and we are at Top already
        // and user is still trying to scroll Up
        swipeRefreshLayout.setEnabled(true);

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                //Log.d("ScrollView","scrollX = "+ scrollX + "scrollY = " + scrollY
                //       +" oldScrollX = " + oldScrollX + "oldScrollY = " + oldScrollY);

                // Checks if we are on Top and enables the SwipeRefreshLayout
                if(scrollY == 0){
                    // Log.d(TAG, "onScrollChange: Y on Zero, Enabled ");
                    swipeRefreshLayout.setEnabled(true);
                } else {
                    // Log.d(TAG, "onScrollChange: Y on Not Zero, Disabled ");
                    swipeRefreshLayout.setEnabled(false);
                }
            }
        });

        super.onStart();
    }
}
