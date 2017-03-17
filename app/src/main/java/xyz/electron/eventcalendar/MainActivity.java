package xyz.electron.eventcalendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.Objects;

import xyz.electron.eventcalendar.provider.Contract;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // TODO: 17-03-17 Refactor class Names and Stuff
    SharedPreferences mSettings;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // TODO: 17-03-17 re-factor all the constants in a file
        mSettings = getSharedPreferences(MyService.PREFS_NAME, 0);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main)
        navigationView.setNavigationItemSelectedListener(this);

        // init Nav Drawer with data from Shared Pref
        // get metadata JSON object from Shared pref.
        String metadata = mSettings.getString("metadata", "");
        View headerView = navigationView.getHeaderView(0);
        if (!Objects.equals(metadata, "")) {
            //we got metadata JSON Object use it and launch map
            // convert it to DataObj via GSON
            Gson gson = new Gson();
            DataObj.EventMetadataBean eventMetadataBean =
                    gson.fromJson(metadata, DataObj.EventMetadataBean.class);

            // get reference to items in Nav bar
            ImageView nav_poster = (ImageView) headerView.findViewById(R.id.nav_head_poster);
            ImageView nav_icon = (ImageView) headerView.findViewById(R.id.nav_head_icon);
            TextView nav_eventName = (TextView) headerView.findViewById(R.id.nav_head_EventName);

            // populate it
             Glide.with(getApplicationContext()).load(eventMetadataBean.getPosterUrl()).into(nav_poster);
             Glide.with(getApplicationContext()).load(eventMetadataBean.getIconUrl()).into(nav_icon);
             nav_eventName.setText(eventMetadataBean.getEvent_name());

        }

        // get SwipeToRefresh View
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.content_main_str);
        // Setup refresh listener which triggers new data loading
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call mSwipeRefreshLayout.setRefreshing(false)
                // once the work has completed successfully.
                launchMyService();
            }
        });


        // View adaptor thing
        ListView listView = (ListView) findViewById(R.id.eventListView);
        listView.setEmptyView(findViewById(android.R.id.empty));

        Cursor cursor = getContentResolver().query(Contract.SchEntry.CONTENT_URI, null, null, null, null);
        ScheduleCursorAdapter scheduleCursorAdapter = new ScheduleCursorAdapter(this, cursor);

        listView.setAdapter(scheduleCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter,View view, int position, long id){
                //open details activity
                Cursor cur = (Cursor) adapter.getItemAtPosition(position);
                cur.moveToPosition(position);

                String eventObjJSON = cur.getString(cur.getColumnIndexOrThrow("schEventObj"));
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("eventObjJSON", eventObjJSON);
                startActivity(intent);

                Toast.makeText(MainActivity.this, position +  " Meooooow " + id, Toast.LENGTH_SHORT).show();
            }
        });

        // TODO: get data on first start or tell user to refresh via empty view

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
        int id = item.getItemId();

        if (id == R.id.nav_schedule) {
            // Handle the schedule action
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_sponsors) {
            // handle sponsors action
            Intent intent = new Intent(this, SponsorsActivity.class);
            startActivity(intent);

        } else {
            if (id == R.id.nav_map) {
                // Handle Map Action
                // get Shared preference
                String map = mSettings.getString("map", "");
                if (!Objects.equals(map, "")) {
                    //we got map JSON Object use it and launch map
                    // convert it to DataObj via GSON
                    Gson gson = new Gson();
                    DataObj.EventMapBean eventMapBean =
                            gson.fromJson(map, DataObj.EventMapBean.class);
                    String zoom = "20";
                    // map point based on latitude/longitude
                    // z param is zoom level.
                    String loc = "geo:" + eventMapBean.getLocation() +"?z=" + zoom;
                    Uri location = Uri.parse(loc);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            } else if (id == R.id.nav_about) {
                // Handle About Action
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);

            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // launchMyService() starts MyService that fetches and updates new data
    public void launchMyService() {
        // make sure we have internet before starting service
        if (isNetworkAvailable()) {
            Toast.makeText(this,"Refreshing...", Toast.LENGTH_LONG).show();
//            Log.v("Main", "You are online!!!!");
            Intent i = new Intent(this, MyService.class);
            // i.putExtra("foo", "bar");
            // Start the service
            startService(i);
        } else {
            Toast.makeText(this,"Can not Refresh, " +
                    "Check Internet Connection and Try Again",Toast.LENGTH_LONG).show();
//            Log.v("Main", "You are not online!!!!");
        }
        // TODO: 17-03-17 use a broadcast receiverr to call this function
        // when we finished with service (send localBroadcast in service's onDestroy())
        mSwipeRefreshLayout.setRefreshing(false);
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

}
