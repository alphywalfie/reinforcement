package com.example.logan.cameraparsedemo2016;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class UserView extends AppCompatActivity {

    private File profilePhoto;
    private ProfileAdapter adapter;
    Realm realm = Realm.getDefaultInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);

        TextView un = (TextView) findViewById(R.id.userProfileLabel);
        TextView am = (TextView) findViewById(R.id.userAboutLabel);
        TextView dm = (TextView) findViewById(R.id.userDisappointLabel);

        Intent intent = getIntent();
        String user = intent.getStringExtra("username");
        setTitle(user);
        String about = intent.getStringExtra("about");
        String disappoint = intent.getStringExtra("disappoints");
        SharedPreferences prefs = getSharedPreferences("remember_me", MODE_PRIVATE);
        un.setText(user);
        am.setText(about);
        dm.setText(disappoint);
        if(intent.getStringExtra("profile") != null)
        {
            String photoPath = intent.getStringExtra("profile");
            profilePhoto = new File(photoPath);
            ImageView pp = (ImageView) findViewById(R.id.userProfilePhoto);
            Picasso.with(this).load(profilePhoto).fit().into(pp);
        }

        ListView lv = (ListView) findViewById(R.id.listView2);
        RealmQuery<User> userToQuery = realm.where(User.class);
        userToQuery.equalTo("username", user);
        User result1 = userToQuery.findFirst();
        RealmQuery<Disappointment> query = realm.where(Disappointment.class);
        query.equalTo("user", result1.getUsername());
        RealmResults<Disappointment> userDisappointments = query.findAll();
        adapter = new ProfileAdapter(this, userDisappointments);
        lv.setAdapter(adapter);
    }

    Disappointment disappointmentToEdit;

    public void editDisappointment(View v)
    {
        disappointmentToEdit = (Disappointment) v.getTag();
        Intent intent = new Intent(this, com.example.logan.cameraparsedemo2016.FormActivity.class);
        intent.putExtra("forEdit", true);
        intent.putExtra("title", disappointmentToEdit.getTitle());
        intent.putExtra("caption", disappointmentToEdit.getCaption());
        intent.putExtra("year", disappointmentToEdit.getYear());
        intent.putExtra("month", disappointmentToEdit.getMonth());
        intent.putExtra("date", disappointmentToEdit.getDate());
        if(disappointmentToEdit.getLatitude() != null && disappointmentToEdit.getLongitude() != null)
        {
            intent.putExtra("latitude", disappointmentToEdit.getLatitude());
            intent.putExtra("longitude", disappointmentToEdit.getLongitude());
            intent.putExtra("locationPresent", true);
        }
        intent.putExtra("photo","");
        if (disappointmentToEdit.getFilename() != null)
        {
            intent.putExtra("photo", disappointmentToEdit.getFilename());
        }
        startActivityForResult(intent, 1);
    }

    public void deleteDisappointment(View v)
    {
        final Disappointment d = (Disappointment) v.getTag();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this item?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id){
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                d.deleteFromRealm();
                            }
                        });
                        //realm.commitTransaction();
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void viewDisappointment(View v)
    {
        Disappointment d = (Disappointment) v.getTag();

        if (d != null)
        {
            Intent intent = new Intent(this, com.example.logan.cameraparsedemo2016.DisappointmentView.class);
            intent.putExtra("title", d.getTitle());
            intent.putExtra("user", d.getUser());
            intent.putExtra("caption", d.getCaption());
            if(d.getLongitude() != null)
            {
                intent.putExtra("longitude", d.getLongitude());
                intent.putExtra("latitude", d.getLatitude());
                intent.putExtra("locationPresent", true);
            }
            intent.putExtra("year", d.getYear());
            intent.putExtra("month", d.getMonth());
            intent.putExtra("date", d.getDate());
            if (d.getFilename() != null)
            {
                intent.putExtra("filename", d.getFilename());
            }
            else
            {
                intent.putExtra("filename", "");
            }
            intent.putExtra("id", d.getId());
            intent.putExtra("number", d.getLikes());
            startActivity(intent);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (data != null)
        {
            if (resultCode == 2)
            {
                realm.beginTransaction();
                disappointmentToEdit.setTitle(data.getStringExtra("title"));
                disappointmentToEdit.setCaption(data.getStringExtra("caption"));
                disappointmentToEdit.setYear(data.getIntExtra("year", 0));
                disappointmentToEdit.setMonth(data.getIntExtra("month", 0));
                disappointmentToEdit.setDate(data.getIntExtra("date", 0));
                if (data.getStringExtra("photoPath") != null) {
                    disappointmentToEdit.setFilename(data.getStringExtra("photoPath"));
                }
                Bundle bundle = data.getParcelableExtra("bundle");
                if (bundle != null)
                {
                    LatLng userPosition = bundle.getParcelable("userPosition");
                    Double userPositionLat = userPosition.latitude;
                    Double userPositionLong = userPosition.longitude;
                    disappointmentToEdit.setLatitude(userPositionLat);
                    disappointmentToEdit.setLongitude(userPositionLong);
                }
                RealmQuery<Disappointment> query = realm.where(Disappointment.class);
                query.equalTo("id", disappointmentToEdit.getId());
                RealmResults<Disappointment> result1 = query.findAll();
                realm.copyToRealmOrUpdate(result1);
                realm.commitTransaction();
                adapter.notifyDataSetChanged();
            }
        }
    }
}
