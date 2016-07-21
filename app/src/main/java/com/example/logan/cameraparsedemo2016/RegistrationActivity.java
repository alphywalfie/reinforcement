package com.example.logan.cameraparsedemo2016;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmList;
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
        //get about me
        EditText am = (EditText) findViewById(R.id.aboutMeText);
        String newAbout = am.getText().toString();
        //get disappointsMost
        EditText dm = (EditText) findViewById(R.id.disappointMostText);
        String newDisappointMost = dm.getText().toString();

        //fix this query
        RealmQuery<User> query = realm.where(User.class);
        query.equalTo("username", newUsername);
        RealmResults<User> result1 = query.findAll();
        RealmList<Disappointment> userDisappointments = new RealmList<Disappointment>();
        if(!result1.isEmpty())
        {
            Toast toast = Toast.makeText(this, "This user already exists", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (!newUsername.isEmpty() && !email.isEmpty() && !newPassword
                .isEmpty() &&!newAbout.isEmpty() && !newDisappointMost.isEmpty())
        {
            realm.beginTransaction();
            User user = new User();
            user.setUsername(newUsername);
            user.setPassword(newPassword);
            user.setAboutMe(newAbout);
            user.setWhatDisappoints(newDisappointMost);
            user.setId(UUID.randomUUID().toString());
            if (outputFile != null)
            {
                user.setProfile(outputFile.getAbsolutePath());
            }
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

    public void cancelMethod(View v)
    {
        finish();
    }

    //-----------------------------------------------------------Camera Methods---------------------------------------------------------

    private File outputFile;
    private File thumbNailFile;
    private String photoPath;
    private Boolean newPhotoTaken = false;

    public void takePicture(View view)
    {

        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File (sdCard.getAbsolutePath() + "/CameraTest/");
        directory.mkdirs();

        // unique filename based on the time
        photoPath = String.valueOf(System.currentTimeMillis());
        outputFile = new File(directory, photoPath+".jpg");
        thumbNailFile = new File(directory, photoPath+"_tn.jpg");

        Uri outputFileUri = Uri.fromFile(outputFile);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK)
        {
            processPicture();
        }
    }


    class PictureProcessThread implements Runnable
    {
        @Override
        public void run()
        {
            try
            {
                // this is potentially time consuming and should be done in a thread

                // rescale the picture from the full size output file which is assumed to now be
                // in outputFile

                // create rescale 640
                // create thumbnail rescale 50

                ImageUtils.resizeSavedBitmap(outputFile.getAbsolutePath(), 100, thumbNailFile.getAbsolutePath());
                ImageUtils.resizeSavedBitmap(outputFile.getAbsolutePath(), 640, outputFile.getAbsolutePath());



                // UI updates must be done via the UI thread NOT here, this will
                // cause the Runnable to occur in the UI thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        updateImageView();
                    }
                });

            }
            catch(final Exception e)
            {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }


    public void processPicture()
    {
        new Thread(new PictureProcessThread()).start();

    }

    private void updateImageView()
    {
        // place ImageView with Picasso
        ImageView imageView = (ImageView) findViewById(R.id.imageView2);
        Picasso.with(this).load(outputFile).fit().into(imageView);
        newPhotoTaken = true;
    }
}
