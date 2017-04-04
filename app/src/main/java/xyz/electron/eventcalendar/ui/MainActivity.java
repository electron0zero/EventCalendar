package xyz.electron.eventcalendar.ui;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.FenceState;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.fence.LocationFence;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import xyz.electron.eventcalendar.others.DataObj.EventMapBean;
import xyz.electron.eventcalendar.others.DataObj.EventMetadataBean;
import xyz.electron.eventcalendar.others.FetchDataService;
import xyz.electron.eventcalendar.R;

import static xyz.electron.eventcalendar.others.Helpers.mapThis;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    // TODO: 17-03-17 Refactor class Names and Stuff
    private final String TAG = "MainActivity";
    SharedPreferences mSettings;
    Toolbar toolbar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    NavigationView navigationView;
    View headerView;

    // DataObj Beans, call initDataObj to initialize
    EventMetadataBean metadata = null;
    EventMapBean map = null;


    // Awareness API
    private GoogleApiClient mGoogleApiClient;
    private EventFenceBroadcastReceiver mEventFenceBroadcastReceiver;

    private final static int REQUEST_PERMISSION_RESULT_CODE = 1337;
    public final static String ACTION_FENCE = "action_fence";
    public final static String KEY_INSIDE_EVENT_VENUE = "inside_event_venue";

    // Activity LifeCycle Callbacks
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_main);
        setSupportActionBar(toolbar);

        Fragment fragment = null;
        Class fragmentClass = ScheduleFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_frame, fragment).commit();

        // TODO: 17-03-17 re-factor all the constants in a file
        mSettings = getSharedPreferences(FetchDataService.PREFS_NAME, 0);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        // get SwipeToRefresh View
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh_layout_schedule);
        // Setup refresh listener which triggers new data loading
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call mSwipeRefreshLayout.setRefreshing(false)
                // once the work has completed successfully.
                // FetchDataService Will send a Broadcast when it will get all the data
                launchFetchDataService();
            }
        });

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "custom-event-name".
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("EventCalendar-FetchDataService-Destroyed"));

        // Create an instance of GoogleAPIClient.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Awareness.API)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

        initDataObj();
        initNavigationView();
        launchFetchDataService();
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onPause() {
        unregisterFenceBroadcast();
        super.onPause();
    }


    // Our handler for received Intents. This will be called whenever an Intent
    // with an action named "EventCalendar-FetchDataService-Destroyed" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // we have data do something with it.
            Log.d(TAG, "onReceive: do stuff, we have new data");
            mSwipeRefreshLayout.setRefreshing(false);
            initDataObj();
            initNavigationView();
            // init Schedule List View
        }
    };

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            // Check if user triggered a refresh:
            case R.id.menu_refresh:
                Log.i("menu_schedule", "Refresh menu item selected");
                // Signal SwipeRefreshLayout to start the progress indicator
                mSwipeRefreshLayout.setRefreshing(true);
                // Start the refresh background task.
                // This method calls setRefreshing(false) when it's finished.
                launchFetchDataService();
                //mSwipeRefreshLayout.setRefreshing(false);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.e(TAG, "onNavigationItemSelected: Menu Item Selected");
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;
        int titleId = R.string.app_name;

        if (id == R.id.nav_schedule) {
            fragmentClass = ScheduleFragment.class;
            titleId = R.string.title_activity_main;
        } else if (id == R.id.nav_sponsors) {
            fragmentClass = SponsorsFragment.class;
            titleId = R.string.title_activity_sponsors;
        } else if (id == R.id.nav_map) {
            // Open Maps on new activity
            String zoom = "20";
            if (map != null) {
                mapThis(map.getLatitude(), map.getLongitude(), zoom, getApplicationContext());
            } else {
                // TODO: 04-04-17 Snack bar with no data warning
                Toast.makeText(this, "No Map Data", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_about) {
            fragmentClass = AboutFragment.class;
            titleId = R.string.title_activity_about;
        }

        try {
            if (fragmentClass != null) {
                fragment = (Fragment) fragmentClass.newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fragmentClass != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_frame, fragment).commit();
            toolbar.setTitle(titleId);
        }

        // close drawer when an item is selected
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // HELPER METHODS
    // launchFetchDataService() starts FetchDataService that fetches and updates new data
    public void launchFetchDataService() {
        // make sure we have internet before starting service
        if (isNetworkAvailable()) {
//            Toast.makeText(this,"Refreshing...", Toast.LENGTH_LONG).show();
//            Log.v("Main", "You are online!!!!");
            Intent i = new Intent(this, FetchDataService.class);
            // i.putExtra("foo", "bar");
            // Start the service
            mSwipeRefreshLayout.setRefreshing(true);
            startService(i);
        } else {
            Toast.makeText(this, "Can not Refresh, " +
                    "Check Internet Connection and Try Again", Toast.LENGTH_LONG).show();
            mSwipeRefreshLayout.setRefreshing(false);
//            Log.v("Main", "You are not online!!!!");
        }
    }

    private boolean isNetworkAvailable() {
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = activeNetworkInfo != null &&
                activeNetworkInfo.isConnectedOrConnecting();
        Log.d("menu_schedule", "isNetworkAvailable: " + isConnected);
        return isConnected;
    }

    private boolean checkLocationPermission() {
        if( !hasLocationPermission() ) {
            Log.e(TAG, "Does not have location permission granted");
            requestLocationPermission();
            return false;
        }

        return true;
    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(
                MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSION_RESULT_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_RESULT_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                } else {
                    Log.e(TAG, "Location permission denied.");
                }
            }
        }
    }

    private void createFence() {
        if (map != null) {
            checkLocationPermission();
            // show last know location
            // TODO: 02-04-17 Request location update before this
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                Log.d(TAG, "current Location: lat = " + String.valueOf(mLastLocation.getLatitude()));
                Log.d(TAG, "current Location: lng = " + String.valueOf(mLastLocation.getLongitude()));
            } else {
                Log.e(TAG, "onConnected: Last location is null");
                Toast.makeText(this, "Can not get last known location,", Toast.LENGTH_SHORT).show();
            }
            // fence registration stuff
            Double lat = Double.valueOf(map.getLatitude());
            Double lng = Double.valueOf(map.getLongitude());
            Double rad = Double.valueOf(map.getRadiusInMeters());
            Long dtime = Long.valueOf(map.getTimeForNotificationInSec()) * 1000;
            Log.e(TAG, "=========== Fence Params ===========");
            Log.d(TAG, "createFence: lat = " + lat.toString());
            Log.d(TAG, "createFence: lng = " + lng.toString());
            Log.d(TAG, "createFence: radius = " + rad.toString());
            Log.d(TAG, "createFence: time = " + dtime.toString());
            Log.e(TAG, "====================================");
            AwarenessFence eventFence = LocationFence.in(lat, lng, rad, dtime);

            Intent intent = new Intent(ACTION_FENCE);
            PendingIntent fencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

            FenceUpdateRequest.Builder builder = new FenceUpdateRequest.Builder();
            builder.addFence(KEY_INSIDE_EVENT_VENUE, eventFence, fencePendingIntent);
            registerFenceBroadcast();
            Awareness.FenceApi.updateFences(mGoogleApiClient, builder.build());
            Log.d(TAG, "createFence: Fence Created");

        }
    }

    private void removeFence() {
        Awareness.FenceApi.updateFences(
                mGoogleApiClient,
                new FenceUpdateRequest.Builder()
                        .removeFence(KEY_INSIDE_EVENT_VENUE)
                        .build());
        unregisterFenceBroadcast();
        Log.d(TAG, "removeFence: Fence Destroyed");
    }

    private void registerFenceBroadcast() {
        Log.d(TAG, "registerFenceBroadcast: Registered Fence Broadcast");
        mEventFenceBroadcastReceiver = new EventFenceBroadcastReceiver();
        registerReceiver(mEventFenceBroadcastReceiver, new IntentFilter(ACTION_FENCE));

    }

    private void unregisterFenceBroadcast() {
        // unregister Broadcast receivers
        try {
            unregisterReceiver(mEventFenceBroadcastReceiver);
            mEventFenceBroadcastReceiver = null;
            Log.d(TAG, "unregisterFenceBroadcast: fence broadcast unregistered");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Log.e(TAG, "onPause: Attempted to unregister a unregistered receiver");
        }
    }

    public void initDataObj() {
        // TODO: 02-04-17 check data integrity(CorrectJSON format) and handle it gracefully
        // get JSON Objects from Storage
        String metadataJSON = mSettings.getString("metadata", "");
        String mapJSON = mSettings.getString("map", "");

        // create Gson instance
        Gson gson = new Gson();

        // init vars
        if (metadataJSON != null) {
            metadata = gson.fromJson(metadataJSON, EventMetadataBean.class);
        }
        if (mapJSON != null) {
            map = gson.fromJson(mapJSON, EventMapBean.class);
        }
    }

    public void initNavigationView() {
        // init Nav Drawer with data from Shared Pref
        headerView = navigationView.getHeaderView(0);

        // TODO: 02-04-17 clean it with view binding libs like ButterKnife
        ImageView nav_poster = (ImageView) headerView.findViewById(R.id.nav_head_poster);
        ImageView nav_icon = (ImageView) headerView.findViewById(R.id.nav_head_icon);
        TextView nav_eventName = (TextView) headerView.findViewById(R.id.nav_head_EventName);

        // populate it
        if (metadata != null) {
            Glide.with(getApplicationContext()).load(metadata.getPosterUrl()).into(nav_poster);
            Glide.with(getApplicationContext()).load(metadata.getIconUrl()).into(nav_icon);
            nav_eventName.setText(metadata.getEvent_name());
        }
        navigationView.removeHeaderView(headerView);
        navigationView.addHeaderView(headerView);
        // View is brought to front to set Z-index, spend 2 days for this
        /* Oh my golden time, It was well wasted.
         I wrote hacks over Hacks,
         Monkey Patches over Monkey Patches
         Now I can die in peace */
        navigationView.bringToFront();
    }

    private void createNotification(int nId, String title, String body) {
        // TODO: do not show Notification multiple times
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body);

        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(body));

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(nId, mBuilder.build());
    }

    // Google API Callbacks
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected: API connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        removeFence();
        Log.d(TAG, "onConnectionSuspended: API connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: API connection failed");
    }

    // Awareness API Fence Broadcast receiver
    public class EventFenceBroadcastReceiver extends BroadcastReceiver {
        private final String TAG = "EventFence";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(ACTION_FENCE, intent.getAction())) {
                FenceState fenceState = FenceState.extract(intent);

                if (TextUtils.equals(KEY_INSIDE_EVENT_VENUE, fenceState.getFenceKey())) {
                    if (fenceState.getCurrentState() == FenceState.TRUE) {
                        Log.e(TAG, "Fence API Broadcast received");
                        if (metadata != null && map != null) {
                            createNotification(1337, metadata.getEvent_name(), map.getNotificationMessage());
                        }
                    }
                }
            }
        }
    }

}