package com.example.logan.cameraparsedemo2016;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        realm = Realm.getDefaultInstance();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // enables the my location button on the upper right
        mMap.setMyLocationEnabled(true);

        // default to normal view
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        // load all previous saved points
        /*RealmResults<Position> positions = realm.where(Position.class).findAll().sort("time", Sort.ASCENDING);
        for (Position pos : positions)
        {
            addOldMarker(pos);
        }*/
    }

// GoogleClient management

    private GoogleApiClient mGoogleApiClient;

    protected synchronized void buildGoogleApiClient() {

        MyGoogleClientListener listener = new MyGoogleClientListener();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(listener)
                .addOnConnectionFailedListener(listener)
                .addApi(LocationServices.API)
                .build();
    }

    class MyGoogleClientListener implements GoogleApiClient.ConnectionCallbacks, OnConnectionFailedListener {
        @Override
        public void onConnected(Bundle arg0) {
            System.out.println("CONNECTED");


            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);

            if (mLastLocation != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 15));

                // update position
                updateMyPosition(mLastLocation);
            }


            // REQUESTING FOR UPDATES
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest,
                    myLocationListener);

        }

        @Override
        public void onConnectionSuspended(int arg0)
        {

        }

        @Override
        public void onConnectionFailed(ConnectionResult arg0)
        {

            System.out.println("CONNECTION FAILED");
        }
    }

    @Override
    public void onStart()
    {
        // initialize the GoogleClient every time the app starts
        if (mGoogleApiClient == null)
        {

            buildGoogleApiClient();
            mGoogleApiClient.connect();
            System.out.println("Connecting...");
        }

        super.onStart();

    }

    @Override
    public void onStop()
    {
        // disconnect the GoogleClient every time the app stops

        if (mGoogleApiClient != null)
        {
            // remove listener
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, myLocationListener);

            mGoogleApiClient.disconnect();
            mGoogleApiClient = null;

            System.out.println("Disonnect...");
        }

        super.onStop();
    }

    public void onDestroy()
    {
        super.onDestroy();

        realm.close();
    }

    // Map management

    private MyLocationListener myLocationListener = new MyLocationListener();

    class MyLocationListener implements LocationListener
    {
        @Override
        public void onLocationChanged(Location arg0)
        {
            // on receiving a location update, update the pin location
            System.out.println("new location: " + arg0);
            updateMyPosition(arg0);
        }
    }

    private void updateMyPosition(Location loc)
    {
        System.out.println("Updating my position");

        try
        {
            // TODO: get the lat/lng of new position
            LatLng position = new LatLng(loc.getLatitude(), loc.getLongitude());

            // System.out.println(entry);

            addNewMarker(position);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    private void addNewMarker(final LatLng pos)
    {
        final Date date =  new Date();
        final String comment = date.toString();


        /*realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Position realmPos = new Position();
                realmPos.fromLatLng(pos);
                realmPos.setTime(date);
                realmPos.setComment(comment);
                realm.copyToRealm(realmPos);
            }
        });*/


        // NOTE: in order to update the UI an operation must occur in the UI
        // thread, this will force the action to occur in the UI thread
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                // Use is diff icon to indicate if the data is sent or not
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(pos);

                markerOptions.title(comment);

                // based on local profile
                //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.green_pin));

                Marker marker = mMap.addMarker(markerOptions);
            }
        });

    }


    /*private void addOldMarker(final Position position)
    {
        // NOTE: in order to update the UI an operation must occur in the UI
        // thread, this will force the action to occur in the UI thread
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                // Use is diff icon to indicate if the data is sent or not
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(position.getLatLng());

                markerOptions.title(position.getComment());

                // based on local profile
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.red_pin));

                Marker marker = mMap.addMarker(markerOptions);
            }
        });

    }*/
}
