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

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class DisappointmentView extends AppCompatActivity {

    Realm realm = Realm.getDefaultInstance();
    Disappointment disappointmentToEdit;

    private RealmDisappointmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disappointment_view);

        Intent intent2 = getIntent();
        TextView tv = (TextView) findViewById(R.id.titleText2);
        tv.setText(intent2.getStringExtra("title"));

//        tv = (TextView) findViewById(R.id.userText);
//        User results1 = realm.where(User.class)
//                .equalTo("id", intent2.getStringExtra("user"))
//                .findFirst();
//        tv.setText(results1.getUsername());

        tv = (TextView) findViewById(R.id.captionText);
        tv.setText(intent2.getStringExtra("caption"));

//        tv = (TextView) findViewById(R.id.locationText);
//        tv.setText(intent.getStringExtra("location"));
//
//        tv = (TextView) findViewById(R.id.dateText);
//        tv.setText(intent.getStringExtra("month")+"/"+intent.getStringExtra("date")+"/"+intent.getStringExtra("year"));

        if (!(intent2.getStringExtra("filename").equals("")))
        {
            ImageView iv = (ImageView) findViewById(R.id.disappointmentImage);
            File disappointmentImage = new File(intent2.getStringExtra("filename"));
            Picasso.with(this).load(disappointmentImage).fit().into(iv);
        }
    }

    public void editDisappointment(View v)
    {
        Intent intent2 = getIntent();
        disappointmentToEdit = realm.where(Disappointment.class)
                .equalTo("id", intent2.getStringExtra("id"))
                .findFirst();
        Intent intent = new Intent(this, com.example.logan.cameraparsedemo2016.FormActivity.class);
        intent.putExtra("forEdit", true);
        intent.putExtra("title", disappointmentToEdit.getTitle());
        intent.putExtra("caption", disappointmentToEdit.getCaption());
        intent.putExtra("year", disappointmentToEdit.getYear());
        intent.putExtra("month", disappointmentToEdit.getMonth());
        intent.putExtra("date", disappointmentToEdit.getDate());
        intent.putExtra("photo","");
        if (disappointmentToEdit.getFilename() != null)
        {
            intent.putExtra("photo", disappointmentToEdit.getFilename());
        }
        startActivityForResult(intent, 1);
    }

    public void deleteDisappointment(View v)
    {
        Intent intent2 = getIntent();
        final Disappointment d = realm.where(Disappointment.class)
                .equalTo("id", intent2.getStringExtra("id"))
                .findFirst();
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
                        realm.commitTransaction();
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), com.example.logan.cameraparsedemo2016.ListActivity.class);
                        startActivity(intent);
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
