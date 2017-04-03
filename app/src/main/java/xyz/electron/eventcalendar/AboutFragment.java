package xyz.electron.eventcalendar;


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

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {
    private final String TAG = "AboutFragment";
    SharedPreferences mSettings;
    String metadata;
    String about;

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
        // TODO: handle case when we get Nothing(null/None) in metadata and about
        mSettings = getActivity().getSharedPreferences(FetchDataService.PREFS_NAME, 0);
        metadata = mSettings.getString("metadata", "Not Found");
        about = mSettings.getString("about", "Not Found");
        // Log.d("test", metadata);
        // Log.d("test", about);

        // convert it to DataObj via GSON
        Gson gson = new Gson();
        DataObj.EventMetadataBean eventMetadataBean =
                gson.fromJson(metadata, DataObj.EventMetadataBean.class);
        DataObj.EventAboutBean eventAboutBean =
                gson.fromJson(about, DataObj.EventAboutBean.class);

        // populate it
        Glide.with(getContext()).load(eventMetadataBean.getIconUrl()).into(event_icon);
        Glide.with(getContext()).load(eventMetadataBean.getPosterUrl()).into(event_poster);
        event_name.setText(eventMetadataBean.getEvent_name());
        event_info.setText(eventAboutBean.getInfo());
        event_org.setText(eventAboutBean.getOrganiser());
        event_org_phone.setText(eventAboutBean.getOrganiser_contact_phone());
        event_org_email.setText(eventAboutBean.getOrganiser_contact_email());
        event_address.setText(eventAboutBean.getAddress_of_event());

        return rootView;
    }

}
