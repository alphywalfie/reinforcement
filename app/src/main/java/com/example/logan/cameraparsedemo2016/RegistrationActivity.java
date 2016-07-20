package com.example.logan.cameraparsedemo2016;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    public void registerUser(View v)
    {
        Realm realm = Realm.getDefaultInstance();

        //get username
        EditText un = (EditText) findViewById(R.id.username);
        String newUsername = un.getText().toString();
        //get email
        EditText em = (EditText) findViewById(R.id.email);
        String email = em.getText().toString();
        //get password
        EditText pw = (EditText) findViewById(R.id.password);
        String newPassword = pw.getText().toString();

        //fix this query
        RealmQuery<User> query = realm.where(User.class);
        query.equalTo("username", newUsername);
        RealmResults<User> result1 = query.findAll();

        if(!result1.isEmpty())
        {
            Toast toast = Toast.makeText(this, "This user already exists", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (!newUsername.isEmpty() && !email.isEmpty() && !newPassword
                .isEmpty())
        {
            realm.beginTransaction();
            User user = new User();
            user.setUsername(newUsername);
            user.setPassword(newPassword);
            realm.copyToRealm(user);
            realm.commitTransaction();
            finish();
        }
        else
        {
            Toast toast = Toast.makeText(this, "ALL FIELDS NEEDED", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
