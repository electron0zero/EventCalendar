package xyz.electron.eventcalendar;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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

import xyz.electron.eventcalendar.DataObj.EventMapBean;
import xyz.electron.eventcalendar.DataObj.EventMetadataBean;
import xyz.electron.eventcalendar.provider.Contract;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    // TODO: 17-03-17 Refactor class Names and Stuff
    private final String TAG = "MainActivity";
    SharedPreferences mSettings;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    NavigationView navigationView;

    // DataObj Beans, call initDataObj before using it
    EventMapBean map;
    EventMetadataBean metadata;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_main);
        setSupportActionBar(toolbar);
        // TODO: 17-03-17 re-factor all the constants in a file
        mSettings = getSharedPreferences(MyService.PREFS_NAME, 0);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // get SwipeToRefresh View
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.content_main_str);

        if(isFirstRun()){
            Log.d(TAG, "onCreate: First Run");
            launchMyService();

        } else {
            // init SwipeToRefresh Functionality
            initSwipeToRefresh();
            initDataObj();
            initNavDrawer();
            // init Schedule List View
            initSchedule();
            launchMyService();
        }
        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "custom-event-name".
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("EventCalendar-MyService-Destroyed"));

        // Create an instance of GoogleAPIClient.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Awareness.API)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

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
        Awareness.FenceApi.updateFences(
                mGoogleApiClient,
                new FenceUpdateRequest.Builder()
                        .removeFence(KEY_INSIDE_EVENT_VENUE)
                        .build());

        // unregister Broadcast receivers
        if(mEventFenceBroadcastReceiver != null) {
            try {
                unregisterReceiver(mEventFenceBroadcastReceiver);
            } catch(IllegalArgumentException e) {
                e.printStackTrace();
                Log.e(TAG, "onPause: Attempted to unregister a unregistered receiver");
            }
        }

        Log.d(TAG, "onPause: Fence Destroyed");
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // returns true if it's first run
    private boolean isFirstRun(){
        return mSettings.getBoolean("firstrun", true);
    }

    // sets a SharedPref "firstrun" to false, used after first run is complete
    private void setFirstRun(){
        mSettings.edit().putBoolean("firstrun", false).apply();
        Log.d(TAG, "setFirstRun: Setting first run to false");
    }

    // Our handler for received Intents. This will be called whenever an Intent
    // with an action named "EventCalendar-MyService-Destroyed" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // we have data do something with it.
            if(isFirstRun()){
                setFirstRun();
                Log.d(TAG, "onReceive: do stuff after we have data on first run");
                // init SwipeToRefresh Functionality
                initSwipeToRefresh();
                initDataObj();
                initNavDrawer();
                // init Schedule List View
                initSchedule();
                // Recreate the activity to load new data in NavigationView
                Log.d("test","Recreate");
                // TODO: 02-04-17 Fix this Hack by recreating full NavView on PrefChanged
                MainActivity.this.recreate();
            }
            mSwipeRefreshLayout.setRefreshing(false);
            navigationView.invalidate();
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
                Log.i("main", "Refresh menu item selected");
                // Signal SwipeRefreshLayout to start the progress indicator
                mSwipeRefreshLayout.setRefreshing(true);
                // Start the refresh background task.
                // This method calls setRefreshing(false) when it's finished.
                launchMyService();
                //mSwipeRefreshLayout.setRefreshing(false);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Intent intent;
        switch (item.getItemId()){

            case R.id.nav_schedule:
                // Handle the schedule action
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_sponsors:
                // handle sponsors action
                intent = new Intent(this, SponsorsActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_map:
                // Handle Map Action
                String zoom = "20";
                // map point based on latitude/longitude, z param is zoom level.
                String loc = "geo:" + map.getLatitude() + map.getLongitude() +"?z=" + zoom;
                Uri location = Uri.parse(loc);
                intent = new Intent(Intent.ACTION_VIEW, location);
                // TODO: 02-04-17 handle case when there is no App to handle geo: queries
                startActivity(intent);
                break;

            case R.id.nav_about:
                // Handle About Action
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
        }

        // close drawer when an item is selected
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // HELPER METHODS
    // launchMyService() starts MyService that fetches and updates new data
    public void launchMyService() {
        // make sure we have internet before starting service
        if (isNetworkAvailable()) {
//            Toast.makeText(this,"Refreshing...", Toast.LENGTH_LONG).show();
//            Log.v("Main", "You are online!!!!");
            Intent i = new Intent(this, MyService.class);
            // i.putExtra("foo", "bar");
            // Start the service
            mSwipeRefreshLayout.setRefreshing(true);
            startService(i);
        } else {
            Toast.makeText(this,"Can not Refresh, " +
                    "Check Internet Connection and Try Again",Toast.LENGTH_LONG).show();
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
        Log.d("main", "isNetworkAvailable: " + isConnected);
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
        return ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION )
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(
                MainActivity.this,
                new String[]{ Manifest.permission.ACCESS_FINE_LOCATION },
                REQUEST_PERMISSION_RESULT_CODE );
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
        checkLocationPermission();
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

        mEventFenceBroadcastReceiver = new EventFenceBroadcastReceiver();
        registerReceiver(mEventFenceBroadcastReceiver, new IntentFilter(ACTION_FENCE));

        FenceUpdateRequest.Builder builder = new FenceUpdateRequest.Builder();
        builder.addFence(KEY_INSIDE_EVENT_VENUE, eventFence, fencePendingIntent);

        Awareness.FenceApi.updateFences(mGoogleApiClient, builder.build() );
        Log.d(TAG, "createFence: Fence Created");
    }

    public void initDataObj(){
        // TODO: 02-04-17 check data integrity(CorrectJSON format) and handle it gracefully
        // get JSON Objects from Storage
        String metadataJSON = mSettings.getString("metadata", "");
        String mapJSON = mSettings.getString("map", "");
        // create Gson instance
        Gson gson = new Gson();

        // init vars
        metadata = gson.fromJson(metadataJSON, EventMetadataBean.class);
        map = gson.fromJson(mapJSON, EventMapBean.class);
    }

    public void initNavDrawer(){
        // init Nav Drawer with data from Shared Pref
        View headerView = navigationView.getHeaderView(0);
        // get reference to items in Nav bar
        // TODO: 02-04-17 clean it with view binding libs like ButterKnife
        ImageView nav_poster = (ImageView) headerView.findViewById(R.id.nav_head_poster);
        ImageView nav_icon = (ImageView) headerView.findViewById(R.id.nav_head_icon);
        TextView nav_eventName = (TextView) headerView.findViewById(R.id.nav_head_EventName);

        // populate it
        Glide.with(getApplicationContext()).load(metadata.getPosterUrl()).into(nav_poster);
        Glide.with(getApplicationContext()).load(metadata.getIconUrl()).into(nav_icon);
        nav_eventName.setText(metadata.getEvent_name());
        // TODO: 02-04-17 Fix the click handlers when fixing Reload thing
    }

    public void initSwipeToRefresh(){
        // Setup refresh listener which triggers new data loading
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call mSwipeRefreshLayout.setRefreshing(false)
                // once the work has completed successfully.
                // MyService Will send a Broadcast when it will get all the data
                launchMyService();
            }
        });

    }

    private void initSchedule() {
        // View adaptor thing
        ListView listView = (ListView) findViewById(R.id.eventListView);
        listView.setEmptyView(findViewById(android.R.id.empty));

        Cursor cursor = getContentResolver().query(Contract.SchEntry.CONTENT_URI, null, null, null, null);
        ScheduleCursorAdapter scheduleCursorAdapter = new ScheduleCursorAdapter(this, cursor);

        listView.setAdapter(scheduleCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                //open details activity
                Cursor cur = (Cursor) adapter.getItemAtPosition(position);
                cur.moveToPosition(position);

                String eventObjJSON = cur.getString(cur.getColumnIndexOrThrow("schEventObj"));
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("eventObjJSON", eventObjJSON);
                startActivity(intent);

                Toast.makeText(MainActivity.this, position + " Meooooow " + id, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Google API Callbacks
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected: API connected");
        // create fence
        if(!isFirstRun()){
            createFence();
        }
        // show last know location
        checkLocationPermission();
        // TODO: 02-04-17 Request location update before this
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
            if (mLastLocation != null) {
                Log.d(TAG, "onConnected: lat = " + String.valueOf(mLastLocation.getLatitude()));
                Log.d(TAG, "onConnected: lng = " + String.valueOf(mLastLocation.getLongitude()));
            } else {
                Log.e(TAG, "onConnected: Last location is null");
                Toast.makeText(this, "Can not get last known location,", Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: API connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: API connection failed");
    }

    //  createNotification(56, R.drawable.ic_launcher, "New Message",
//      "There is a new message from Bob!");
    private void createNotification(int nId, String title, String body) {
        // TODO: do not show Notif multiple times, can be tracked by an var set on start and end of activity
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

    public class EventFenceBroadcastReceiver extends BroadcastReceiver {
        private final String TAG = "EventFence";
        @Override
        public void onReceive(Context context, Intent intent) {
            if(TextUtils.equals(ACTION_FENCE, intent.getAction())) {
                FenceState fenceState = FenceState.extract(intent);

                if( TextUtils.equals(KEY_INSIDE_EVENT_VENUE, fenceState.getFenceKey() ) ) {
                    if( fenceState.getCurrentState() == FenceState.TRUE ) {
                        Log.e(TAG, "Fence API Broadcast received");
                        createNotification(1337, metadata.getEvent_name(), map.getNotificationMessage());
                    }
                }
            }
        }
    }

}
