package xyz.electron.eventcalendar.ui;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

    String metadataJSON = null;
    String aboutJSON = null;

    // Views
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

        View rootView = inflater.inflate(R.layout.content_about, container, false);

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

            Glide.with(getContext()).load(metadata.getIconUrl()).into(event_icon);
            Glide.with(getContext()).load(metadata.getPosterUrl()).into(event_poster);
            event_name.setText(metadata.getEvent_name());
        }

        if (aboutJSON != null){
            EventAboutBean about = gson.fromJson(aboutJSON, EventAboutBean.class);

            event_org_email.setText(about.getOrganiser_contact_email());
            event_address.setText(about.getAddress_of_event());
        }

        return rootView;
    }

}
