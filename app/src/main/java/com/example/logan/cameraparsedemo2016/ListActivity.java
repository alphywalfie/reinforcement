package com.example.logan.cameraparsedemo2016;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

public class ListActivity extends AppCompatActivity {

    private RealmDisappointmentAdapter adapter;
    private OrderedRealmCollection<Disappointment> disappointments;
    Realm realm = Realm.getDefaultInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setTitle("Wall of Shame");

        ListView lv = (ListView) findViewById(R.id.listView);
        adapter = new RealmDisappointmentAdapter(this, realm.where(Disappointment.class).findAll());
        lv.setAdapter(adapter);
    }

    public void addDisappointment(View v)
    {
        SharedPreferences prefs = getSharedPreferences("remember_me", MODE_PRIVATE);
        Intent intent = new Intent(this, com.example.logan.cameraparsedemo2016.FormActivity.class);
        intent.putExtra("forEdit", false);
        startActivityForResult(intent, 1);
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
                        realm.commitTransaction();
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

    public void viewUsers(View v)
    {
        Intent intent = new Intent(this, com.example.logan.cameraparsedemo2016.UserList.class);
        startActivity(intent);
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
//        intent.putExtra("location", d.getLocation());
//        intent.putExtra("year", d.getYear());
//        intent.putExtra("month", d.getMonth());
//        intent.putExtra("date", d.getDate());
            if (d.getFilename() != null)
            {
                intent.putExtra("filename", d.getFilename());
            }
            else
            {
                intent.putExtra("filename", "");
            }
            intent.putExtra("id", d.getId());
            startActivity(intent);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (data != null)
        {
            if(resultCode == 1) {
                RealmQuery<User> query = realm.where(User.class);
                User currentUser = query.findFirst();
                Disappointment d = new Disappointment();
                d.setTitle(data.getStringExtra("title"));
                d.setUser(data.getStringExtra("user"));
                d.setYear(data.getIntExtra("year", 0));
                d.setMonth(data.getIntExtra("month", 0));
                d.setDate(data.getIntExtra("date", 0));
                d.setCaption(data.getStringExtra("caption"));
                d.setId(UUID.randomUUID().toString());
                if (data.getStringExtra("photoPath") != null) {
                    d.setFilename(data.getStringExtra("photoPath"));
                }
                realm.beginTransaction();
                realm.copyToRealm(d);
                realm.commitTransaction();
                adapter.notifyDataSetChanged();
            }
            else if (resultCode == 2)
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
