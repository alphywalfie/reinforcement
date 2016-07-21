package com.example.logan.cameraparsedemo2016;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by Azi on 21 Jul 2016.
 */
public class RealmUserAdapter extends RealmBaseAdapter<User> implements ListAdapter {

    private File userImage;

    public RealmUserAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<User> data) {
        super(context, data);
    }

    @Override
    public int getCount() {
        return adapterData.size()+1;
    }

    @Override
    public User getItem(int position) {
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
            View v = inflater.inflate(R.layout.user_row, null);

            TextView username = (TextView) v.findViewById(R.id.usernameLabel);
            ImageView imageView = (ImageView) v.findViewById(R.id.userListPhoto);
            User u = adapterData.get(position);
            username.setText(u.getUsername());

            if (u.getProfile() != null)
            {
                userImage = new File(u.getProfile());
                Picasso.with(context).load(userImage).fit().into(imageView);
            }

            imageView.setTag(u);
            return v;
        }
        else
        {
            View v = inflater.inflate(R.layout.back_home, null);
            return v;
        }
    }
}
