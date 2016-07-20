package com.example.logan.cameraparsedemo2016;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;

/**
 * Created by Azi on 11 Jul 2016.
 */
public class RealmReviewAdapter2 extends RealmBaseAdapter<Review> implements ListAdapter{

    private Activity context;
    private ArrayList<Review> reviews;
    private File foodImage;
    Realm realm = Realm.getDefaultInstance();

    public RealmReviewAdapter2(@NonNull Context context, @Nullable OrderedRealmCollection<Review> data) {
        super(context, data);
    }

    @Override
    public int getCount() {
        return adapterData.size()+1;
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
            View v = inflater.inflate(R.layout.review_row, null);

            TextView name = (TextView) v.findViewById(R.id.nameLabel);
            TextView price = (TextView) v.findViewById(R.id.priceLabel);
            TextView description = (TextView) v.findViewById(R.id.descriptionLabel);
            TextView comment = (TextView) v.findViewById(R.id.commentLabel);
            RatingBar rating = (RatingBar) v.findViewById(R.id.ratingFilled);

            Review r = adapterData.get(position);
            name.setText(r.getFoodName());
            price.setText(String.valueOf(r.getFoodPrice()));
            description.setText(r.getFoodDescription());
            comment.setText(r.getFoodComment());
            rating.setRating(r.getFoodRating());
            if (r.getFoodPhoto() != null)
            {
                foodImage = new File(r.getFoodPhoto());
                ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
                Picasso.with(context).load(foodImage).fit().into(imageView);
            }
            Button db = (Button) v.findViewById(R.id.deleteButton);
            Button eb = (Button) v.findViewById(R.id.editButton);
            db.setTag(r);
            eb.setTag(r);
            return v;
        }
        else
        {
            View v = inflater.inflate(R.layout.buttons, null);
            return v;
        }
    }
}
