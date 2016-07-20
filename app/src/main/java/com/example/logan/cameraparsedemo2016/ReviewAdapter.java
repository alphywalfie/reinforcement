package com.example.logan.cameraparsedemo2016;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Azi on 30 Jun 2016.
 */
public class ReviewAdapter extends BaseAdapter {

    private Activity context;
    private ArrayList<Review> reviews;
    private File foodImage;

    public ReviewAdapter(Activity a, ArrayList<Review> r)
    {
        context = a;
        reviews = r;
    }

    @Override
    public int getCount() {
        return reviews.size()+1;
    }

    @Override
    public Object getItem(int position) {
        if (position<reviews.size())
            return reviews.get(position);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position<reviews.size()) {
            View v = context.getLayoutInflater().inflate(R.layout.review_row, null);

            TextView name = (TextView) v.findViewById(R.id.nameLabel);
            TextView price = (TextView) v.findViewById(R.id.priceLabel);
            TextView description = (TextView) v.findViewById(R.id.descriptionLabel);
            TextView comment = (TextView) v.findViewById(R.id.commentLabel);
            RatingBar rating = (RatingBar) v.findViewById(R.id.ratingFilled);

            Review r = reviews.get(position);
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
            View v = context.getLayoutInflater().inflate(R.layout.buttons, null);
            return v;
        }
    }
}
