package com.example.logan.cameraparsedemo2016;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import io.realm.Realm;

public class UserList extends AppCompatActivity {

    private RealmUserAdapter adapter;
    Realm realm = Realm.getDefaultInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        setTitle("All Users");

        ListView lv = (ListView) findViewById(R.id.userListView);
        adapter = new RealmUserAdapter(this, realm.where(User.class).findAll());
        lv.setAdapter(adapter);

    }

    public void viewUser(View v)
    {
        User u = (User) v.getTag();
        Intent intent = new Intent(this, com.example.logan.cameraparsedemo2016.UserView.class);
        intent.putExtra("username", u.getUsername());
        intent.putExtra("about", u.getAboutMe());
        intent.putExtra("disappoints", u.getWhatDisappoints());
        if(u.getProfile() != null)
        {
            intent.putExtra("profile", u.getProfile());
        }
        startActivity(intent);
        //launch intent for the view activity
        //put extras = all the thing
        //probably get all the reviews as well
    }
}
