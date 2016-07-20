package com.example.logan.cameraparsedemo2016;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Azi on 9 Jul 2016.
 */
public class ReviewRealmAdapter extends RealmBaseAdapter<Review> implements ListAdapter {

    private Context adapterContext;
    //private RealmList<Review> reviews;
    OrderedRealmCollection<Review> reviews;
    private File foodImage;
    private LayoutInflater inflater;

    public ReviewRealmAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Review> data) {
        super(context, data);
        this.inflater = LayoutInflater.from(context);
        reviews = data;
        adapterContext = context;
        Realm realm = Realm.getDefaultInstance();

    }

    @Override
    public int getCount() {
        return adapterData.size() + 1;
    }

    @Override
    public Review getItem(int position) {
        if (position<adapterData.size())
            return adapterData.get(position);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position<adapterData.size()) {
            inflater.inflate(R.layout.review_row, null);
            TextView name = (TextView) convertView.findViewById(R.id.nameLabel);
            TextView price = (TextView) convertView.findViewById(R.id.priceLabel);
            TextView description = (TextView) convertView.findViewById(R.id.descriptionLabel);
            TextView comment = (TextView) convertView.findViewById(R.id.commentLabel);
            RatingBar rating = (RatingBar) convertView.findViewById(R.id.ratingFilled);

            Review r = adapterData.get(position);
            name.setText(r.getFoodName());
            price.setText(String.valueOf(r.getFoodPrice()));
            description.setText(r.getFoodDescription());
            comment.setText(r.getFoodComment());
            rating.setRating(r.getFoodRating());
            if (r.getFoodPhoto() != null)
            {
                foodImage = new File(r.getFoodPhoto());
                ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
                Picasso.with(context).load(foodImage).fit().into(imageView);
            }
            Button db = (Button) convertView.findViewById(R.id.deleteButton);
            Button eb = (Button) convertView.findViewById(R.id.editButton);
            db.setTag(r);
            eb.setTag(r);
            return convertView;
        }
        else
        {
            inflater.inflate(R.layout.buttons, null);
            return convertView;
        }
    }
}