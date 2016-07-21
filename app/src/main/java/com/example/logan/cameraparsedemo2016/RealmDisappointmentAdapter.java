package com.example.logan.cameraparsedemo2016;

import android.app.Activity;
import android.content.Context;
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
 * Created by lance on 21/07/2016.
 */
public class RealmDisappointmentAdapter extends RealmBaseAdapter<Disappointment> implements ListAdapter {
    private Activity context;
    private ArrayList<Disappointment> disappointments;
    private File disappointmentImage;
    Realm realm = Realm.getDefaultInstance();

    public RealmDisappointmentAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Disappointment> data) {
        super(context, data);
    }

    @Override
    public int getCount() {
        return adapterData.size()+1;
    }

    @Override
    public Disappointment getItem(int position) {
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
            View v = inflater.inflate(R.layout.disappointment_row, null);

            TextView name = (TextView) v.findViewById(R.id.titleText);
            TextView user = (TextView) v.findViewById(R.id.userText);
            ImageView imageView = (ImageView) v.findViewById(R.id.imageView3);

            Disappointment d = adapterData.get(position);
            name.setText(d.getTitle());
            User results1 = realm.where(User.class)
                    .equalTo("id", d.getUser())
                    .findFirst();
            user.setText(results1.getUsername());
            if (d.getFilename() != null)
            {
                disappointmentImage = new File(d.getFilename());
                Picasso.with(context).load(disappointmentImage).fit().into(imageView);
            }
            Button db = (Button) v.findViewById(R.id.deleteButton);
            Button eb = (Button) v.findViewById(R.id.editButton);
            db.setTag(d);
            eb.setTag(d);
            return v;
        }
        else
        {
            View v = inflater.inflate(R.layout.buttons, null);
            return v;
        }
    }
}
