package xyz.electron.eventcalendar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import xyz.electron.eventcalendar.provider.Contract;
import xyz.electron.eventcalendar.provider.DBHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // TODO: launch only if we have internet connection
        launchMyService();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // View adaptor thing
        ListView listView = (ListView) findViewById(R.id.eventListView);

        Cursor cursor = getContentResolver().query(Contract.SchEntry.CONTENT_URI, null, null, null, null);

        EventsCursorAdapter eventsCursorAdapter = new EventsCursorAdapter(this, cursor);

        listView.setAdapter(eventsCursorAdapter);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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

        } else if (id == R.id.nav_map) {
            // Handle Map Action
            // map point based on latitude/longitude
            // z param is zoom level
            // TODO: Take location from shared pref.
            // Magic number just for testing
             Uri location = Uri.parse("geo:27.9613945,76.4017789?z=20");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
            startActivity(mapIntent);

        } else if (id == R.id.nav_about) {
            // Handle About Action
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Call launchMyService() in the activity
    // to startup the service
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
