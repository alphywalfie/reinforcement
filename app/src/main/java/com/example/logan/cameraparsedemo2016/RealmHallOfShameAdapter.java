package com.example.logan.cameraparsedemo2016;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;

/**
 * Created by lance on 22/07/2016.
 */
public class RealmHallOfShameAdapter extends RealmBaseAdapter<Disappointment> implements ListAdapter {
    private ArrayList<Disappointment> disappointments;
    private File disappointmentImage;
    Realm realm = Realm.getDefaultInstance();
    SharedPreferences prefs = context.getSharedPreferences("remember_me", context.MODE_PRIVATE);

    public RealmHallOfShameAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Disappointment> data) {
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
            View v = inflater.inflate(R.layout.shame_row, null);

            TextView name = (TextView) v.findViewById(R.id.titleText);
            TextView user = (TextView) v.findViewById(R.id.userText);
            ImageView imageView = (ImageView) v.findViewById(R.id.disappointmentImage);

            Disappointment d = adapterData.get(position);
            name.setText(d.getTitle());
            User results1 = realm.where(User.class)
                    .equalTo("username", d.getUser())
                    .findFirst();
            user.setText(results1.getUsername());
            if (d.getFilename() != null)
            {
                disappointmentImage = new File(d.getFilename());
                Picasso.with(context).load(disappointmentImage).fit().into(imageView);
            }
          imageView.setTag(d);
            return v;
        }
        else
        {
            View v = inflater.inflate(R.layout.shame_buttons, null);
            return v;
        }
    }
}
