package com.example.logan.cameraparsedemo2016;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //remember me functionality
        SharedPreferences prefs = getSharedPreferences("remember_me", MODE_PRIVATE);
        Boolean c = prefs.getBoolean("remember", false);

        if(c)
        {
            String p = prefs.getString("password", "");
            String u = prefs.getString("username", "");

            View x = findViewById(R.id.loginText);
            EditText un = (EditText) x;
            un.setText(u);

            x = findViewById(R.id.passwordText);
            EditText pw = (EditText) x;
            pw.setText(p);

            x = findViewById(R.id.checkBox);
            CheckBox cb = (CheckBox) x;
            cb.setChecked(c);
        }
    }

    public void login(View v)
    {
        Realm realm = Realm.getDefaultInstance();

        View x = findViewById(R.id.loginText);
        EditText un = (EditText) x;

        x = findViewById(R.id.passwordText);
        EditText pw = (EditText) x;

        x = findViewById(R.id.checkBox);
        CheckBox cb = (CheckBox) x;

        String loginUsername = un.getText().toString();
        String loginPassword = pw.getText().toString();
        Boolean checky = cb.isChecked();

        //fix this query
        RealmQuery<User> query = realm.where(User.class);
        query.equalTo("username", loginUsername);
        query.equalTo("password", loginPassword);
        User result1 = query.findFirst();

        //fix this logic check
        if(!(result1 == null))
        {
            SharedPreferences prefs = getSharedPreferences("remember_me", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            if (checky)
            {
                editor.putString("username",loginUsername);
                editor.putString("password",loginPassword);
                editor.putBoolean("remember", checky);
            }
            editor.putString("userId", result1.getId());
            editor.commit();
            Intent intent = new Intent(this, com.example.logan.cameraparsedemo2016.ListActivity.class);
            startActivity(intent);
        }
        else
        {
            Toast toast = Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void goToRegisterScreen(View v)
    {
        Intent intent = new Intent(this, com.example.logan.cameraparsedemo2016.RegistrationActivity.class);
        startActivity(intent);
    }
}
