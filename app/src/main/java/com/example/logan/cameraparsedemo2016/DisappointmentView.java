package com.example.logan.cameraparsedemo2016;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Date;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class DisappointmentView extends AppCompatActivity implements OnMapReadyCallback {

    Realm realm = Realm.getDefaultInstance();
    Disappointment disappointmentToEdit;
    Double latitude;
    Double longitude;
    Boolean locationPresent;
    private RealmDisappointmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disappointment_view);

        Intent intent2 = getIntent();
        TextView tv = (TextView) findViewById(R.id.titleText2);
        tv.setText(intent2.getStringExtra("title"));
        setTitle(intent2.getStringExtra("title"));

        tv = (TextView) findViewById(R.id.userText);
        tv.setText(intent2.getStringExtra("user"));

        tv = (TextView) findViewById(R.id.captionText);
        tv.setText(intent2.getStringExtra("caption"));

        //tv = (TextView) findViewById(R.id.locationText);
        //tv.setText(intent2.getFloatExtra("latitude", 0)+","+intent2.getFloatExtra("longitude", 0));
//
//        tv = (TextView) findViewById(R.id.dateText);
//        tv.setText(intent.getStringExtra("month")+"/"+intent.getStringExtra("date")+"/"+intent.getStringExtra("year"));

        if (!(intent2.getStringExtra("filename").equals("")))
        {
            ImageView iv = (ImageView) findViewById(R.id.disappointmentImage);
            File disappointmentImage = new File(intent2.getStringExtra("filename"));
            Picasso.with(this).load(disappointmentImage).fit().into(iv);
        }

        tv = (TextView) findViewById(R.id.unlikeText);
        tv.setText(intent2.getIntExtra("number", 0)+"people have shamed this");

        latitude = intent2.getDoubleExtra("latitude", 0);
        longitude = intent2.getDoubleExtra("longitude", 0);
        locationPresent = intent2.getBooleanExtra("locationPresent", false);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment2);
        mapFragment.getMapAsync(this);
    }

    public void shame(View v)
    {
        Intent intent2 = getIntent();
        Disappointment d = realm.where(Disappointment.class)
                .equalTo("id", intent2.getStringExtra("id"))
                .findFirst();
        realm.beginTransaction();
        d.setLikes(d.getLikes()+1);
        realm.commitTransaction();

        TextView tv = (TextView) findViewById(R.id.unlikeText);
        tv.setText(d.getLikes()+"people have shamed this");
    }

    //-----------------------------------------------------------MAP METHODS----------------------------------------------------------------------

    private GoogleMap mMap;

    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        // default to normal view
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if(locationPresent)
        {
            LatLng savedLocation = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(savedLocation, 15));
            addNewMarker(savedLocation);
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
}
