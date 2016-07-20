package com.example.logan.cameraparsedemo2016;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

    //private ArrayList<Review> reviews = new ArrayList<>();
    //private ReviewAdapter adapter;
    private RealmReviewAdapter2 adapter;
    private OrderedRealmCollection<Review> reviews;
    Realm realm = Realm.getDefaultInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setTitle("Food Reviews");

        /*Review r = new Review();
        r.setFoodName("Potato");
        r.setFoodPrice(12);
        r.setFoodDescription("Good");
        r.setFoodComment("Yay");
        r.setFoodRating(5);

        reviews.add(r);*/

        ListView lv = (ListView) findViewById(R.id.listView);
        //adapter = new ReviewAdapter (this, reviews);
        adapter = new RealmReviewAdapter2(this, realm.where(Review.class).findAll());
        lv.setAdapter(adapter);
    }

    public void addReview(View v)
    {
        Intent intent = new Intent(this, com.example.logan.cameraparsedemo2016.FormActivity.class);
        intent.putExtra("forEdit", false);
        startActivityForResult(intent, 1);
    }

    Review reviewToEdit;

    public void editReview(View v)
    {
        reviewToEdit = (Review) v.getTag();
        Intent intent = new Intent(this, com.example.logan.cameraparsedemo2016.FormActivity.class);
        intent.putExtra("forEdit", true);
        intent.putExtra("name", reviewToEdit.getFoodName());
        intent.putExtra("price", reviewToEdit.getFoodPrice());
        intent.putExtra("comment", reviewToEdit.getFoodComment());
        intent.putExtra("description", reviewToEdit.getFoodDescription());
        intent.putExtra("rating", reviewToEdit.getFoodRating());
        intent.putExtra("photo","");
        if (reviewToEdit.getFoodPhoto() != null)
        {
            intent.putExtra("photo", reviewToEdit.getFoodPhoto());
        }
        startActivityForResult(intent, 1);
    }

    public void deleteReview(View v)
    {
        realm.beginTransaction();
        final Review r = (Review) v.getTag();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this item?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id){
                        String reviewId = r.getId();
                        RealmQuery<Review> query = realm.where(Review.class);
                        query.equalTo("id", reviewToEdit.getId());
                        RealmResults<Review> result1 = query.findAll();
                        result1.deleteFirstFromRealm();
                        realm.commitTransaction();
                        //reviews.remove(r);
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

    public void deleteAllReviews(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("WARNING: Delete all reviews?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id){
                        realm.beginTransaction();
                        RealmResults<Review> result1 = realm.where(Review.class).findAll();
                        result1.deleteAllFromRealm();
                        realm.commitTransaction();
                        //reviews.clear();
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

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (data != null)
        {
            if(resultCode == 1) {
                Review r = new Review();
                r.setFoodName(data.getStringExtra("name"));
                r.setFoodPrice(data.getFloatExtra("price", 0));
                r.setFoodDescription(data.getStringExtra("description"));
                r.setFoodComment(data.getStringExtra("comment"));
                r.setFoodRating(data.getFloatExtra("rating", 0));
                r.setId(UUID.randomUUID().toString());
                if (data.getStringExtra("photoPath") != null) {
                    r.setFoodPhoto(data.getStringExtra("photoPath"));
                }
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.copyToRealm(r);
                realm.commitTransaction();
                adapter.notifyDataSetChanged();
            }
            else if (resultCode == 2)
            {
                realm.beginTransaction();
                reviewToEdit.setFoodName(data.getStringExtra("name"));
                reviewToEdit.setFoodPrice(data.getFloatExtra("price", 0));
                reviewToEdit.setFoodDescription(data.getStringExtra("description"));
                reviewToEdit.setFoodComment(data.getStringExtra("comment"));
                reviewToEdit.setFoodRating(data.getFloatExtra("rating", 0));
                if (data.getStringExtra("photoPath") != null) {
                    reviewToEdit.setFoodPhoto(data.getStringExtra("photoPath"));
                }
                Realm realm = Realm.getDefaultInstance();
                RealmQuery<Review> query = realm.where(Review.class);
                query.equalTo("id", reviewToEdit.getId());
                RealmResults<Review> result1 = query.findAll();
                realm.copyToRealmOrUpdate(result1);
                realm.commitTransaction();
                adapter.notifyDataSetChanged();
            }
        }
    }
}
