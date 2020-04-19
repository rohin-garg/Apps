package com.example.coronavirusmapapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Debug;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    ImageButton coronaScreen;
    String username;
    RequestQueue queue;
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_main);

        Drawable mIcon = this.getResources().getDrawable(R.drawable.home);
        mIcon.setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.selectedicon), PorterDuff.Mode.MULTIPLY));

        getSupportActionBar().setTitle("CovidAlert");

        queue = Volley.newRequestQueue(this);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.getController().setZoom(4);

        requestPermissionsIfNecessary(new String[] {
                // if you need to show the current location, uncomment the line below
                Manifest.permission.ACCESS_FINE_LOCATION,
                // WRITE_EXTERNAL_STORAGE is required in order to show the map
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        //items.add(new OverlayItem("Title", "Description", new GeoPoint(0.0d,0.0d))); // Lat/Lon decimal degrees

//the overlay
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        //do something
                        return true;
                    }
                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return false;
                    }
                }, this);
        mOverlay.setFocusItemsOnTap(true);

        map.getOverlays().add(mOverlay);

        username = getIntent().getStringExtra("username");

        getCurrLocation();
        Location c = getCurrLocation();
        if (c != null){
            map.getController().setCenter(new GeoPoint(c));
        }

        coronaScreen = findViewById(R.id.hascorona);
        coronaScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HasCorona.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });



        timer = new Timer();
        timer.schedule(new PLocTimer(), 0, 5000);

        getContLocations();
    }

    protected void getContLocations (){
        String url = "http://192.168.1.24:5000/getcontagiousplaces";
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);
        Log.d("here", "CONTTTTTTTTTTTTTTTTTTTTTTTTTT");
        final Internet it = new Internet(this);
        JsonObjectRequest request = new JsonObjectRequest(url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String result = response.getString("result");
                    if (result.equals("yes")){
                        Toast t = Toast.makeText(getApplicationContext(), "Warning: You may have been exposed to coronavirus. Please take the proper precautions", Toast.LENGTH_LONG);
                        t.setGravity(0,0,0);
                        t.show();
                        Log.d("yes", "yesssssssssssssssssssssssssssssss");
                    } else {
                        Log.d("no", "nooooooooooooooooooooooo");
                    }
                } catch (JSONException e){
                    Log.d("here", "IN CATCH");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Here", "onErrorResponse");
                if (error instanceof TimeoutError) {
                    Log.d("Timeout:", "on response");
                } else if (error instanceof NoConnectionError){
                    Log.d("No connection", "on response");
                    Boolean bo = it.Check();
                    Log.d("Internet Connection", bo.toString());
                    error.printStackTrace();
                } else if (error instanceof AuthFailureError) {
                    error.printStackTrace();
                } else if (error instanceof ServerError) {
                    error.printStackTrace();
                } else if (error instanceof NetworkError) {
                    error.printStackTrace();
                } else if (error instanceof ParseError) {
                    //TODO
                }
            }
        });
        queue.add(request);
    }

    Location lastLoc = null;
    protected void setPlLoc (){
        Log.d("HERE", "SETPLOC");
        MapView map = findViewById(R.id.map);
        map.invalidate();
        Marker startMarker = new Marker(map);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        Location l = getCurrLocation();
        if (l != null && (lastLoc == null || locHash(l) != locHash(lastLoc))) {
            startMarker.setPosition(new GeoPoint(l));
            Log.d("latitude: ", Double.toString(l.getLatitude()));
            Log.d("longitude: ", Double.toString(l.getLongitude()));
            int hash = locHash(l);

            postLocation(l);

            startMarker.setIcon(getResources().getDrawable(R.drawable.marker_default));
            startMarker.setTitle("Current Location: " + username);
            map.getOverlays().add(startMarker);
            Log.d("HERE", "NOT ACTUALLY NULLLLLLL");
            map.getController().setZoom(12);
            map.getController().setCenter(startMarker.getPosition());
        }
        if (l != null){
            lastLoc = l;
        }
        map.invalidate();
    }
    class PLocTimer extends TimerTask {
        public void run () {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setPlLoc();
                }
            });
            Location l = getCurrLocation();
            if (l != null && (lastLoc == null || locHash(l) != locHash(lastLoc))) {
                postLocation(l);
            }
        }
    }
    private Location currLoc;
    protected Location getCurrLocation (){
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            currLoc = location;
                            Log.d("WORKS", "YAAAAAAAAAAAAAAAAAAAAYYYYYY");
                        } else {
                            Log.d("Dsdklfjskldjflksdf", "NULLLLLLLLLLLLLLLL");
                        }
                    }
                });
        return currLoc;
    }

    private int locHash (Location lc) {
        String h =  Double.toString(lc.getLatitude()) + " " + Double.toString(lc.getLongitude());
        return h.hashCode();
    }
    final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public void postLocation (Location l){
        String url = "http://192.168.1.24:5000/loc";

        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("latitude", Double.toString(l.getLatitude()));
        params.put("longitude", Double.toString(l.getLongitude()));
        params.put("hashCode", Integer.toString(locHash(l)));
        params.put("date", Long.toString(l.getTime()));
        /*
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = Calendar.getInstance().getTime();
        params.put("date", dateFormat.format(today));
        Log.d("date", dateFormat.format(today));
         */
        final Internet it = new Internet(this);
        JsonObjectRequest request = new JsonObjectRequest(url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("onResponse", "postloc");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Here", "onErrorResponse");
                if (error instanceof TimeoutError) {
                    Log.d("Timeout:", "on response");
                } else if (error instanceof NoConnectionError){
                    Log.d("No connection", "on response");
                    Boolean bo = it.Check();
                    Log.d("Internet Connection", bo.toString());
                    error.printStackTrace();
                } else if (error instanceof AuthFailureError) {
                    error.printStackTrace();
                } else if (error instanceof ServerError) {
                    error.printStackTrace();
                } else if (error instanceof NetworkError) {
                    error.printStackTrace();
                } else if (error instanceof ParseError) {
                    //TODO
                }
            }
        });
        queue.add(request);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume (){
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}
