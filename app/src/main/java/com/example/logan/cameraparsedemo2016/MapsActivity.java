package com.example.logan.cameraparsedemo2016;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // enables the my location button on the upper right
        mMap.setMyLocationEnabled(true);
        // default to normal view
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnMapClickListener(this);
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

    @Override
    public void onMapClick(LatLng latLng) {
        Location location = new Location("Etc");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        updateMyPosition(location);
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
            /*mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);*/
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

            System.out.println("Disconnect...");
        }

        super.onStop();
    }

    public void onDestroy()
    {
        super.onDestroy();
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

    LatLng userPosition;
    private void updateMyPosition(Location loc)
    {
        System.out.println("Updating my position");

        try
        {
            // TODO: get the lat/lng of new position
            LatLng position = new LatLng(loc.getLatitude(), loc.getLongitude());
            userPosition = position;
            // System.out.println(entry);
            mMap.clear();
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

    public void searchLocation(View v)
    {
        EditText search = (EditText) findViewById(R.id.locationSearch);
        String location = search.getText().toString();
        List<Address> addressList = null;
        if(location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(addressList != null || addressList.size() != 0) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }
            else
            {
                Toast toast = Toast.makeText(this, "No such location found", Toast.LENGTH_SHORT);
            }
        }
    }

    public void setUserLocation(View v)
    {
        Intent intent = getIntent();
        Bundle args = new Bundle();
        args.putParcelable("userPosition", userPosition);
        intent.putExtra("bundle", args);
        setResult(1, intent);
        finish();
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
