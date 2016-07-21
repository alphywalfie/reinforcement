package com.example.logan.cameraparsedemo2016;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;

public class FormActivity extends AppCompatActivity {

    Boolean forEdit;
    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        setTitle("Add Disappointment");

        Intent intent = getIntent();
        forEdit = intent.getBooleanExtra("forEdit", false);
        SharedPreferences prefs = getSharedPreferences("remember_me", MODE_PRIVATE);
        user = prefs.getString("userId", null);

        if (forEdit)
        {
            setTitle("Edit Disappointment");
            String title = intent.getStringExtra("title");
            String caption = intent.getStringExtra("caption");
            String photo = intent.getStringExtra("photo");
            EditText et = (EditText) findViewById(R.id.titleText);
            et.setText(title);
            et = (EditText) findViewById(R.id.captionText);
            et.setText(caption);
            if(!photo.isEmpty())
            {
                File disappointmentImage;
                disappointmentImage = new File(photo);
                ImageView imageView = (ImageView) findViewById(R.id.formPic);
                Picasso.with(this).load(disappointmentImage).fit().into(imageView);
            }
        }

    }

    public void submitDisappointment(View v)
    {
        String title;
        String caption;
        String temp;
        String temp2;

        EditText et = (EditText) findViewById(R.id.titleText);
        title = et.getText().toString();
        et = (EditText) findViewById(R.id.captionText);
        caption = et.getText().toString();

        if(title.isEmpty() || caption.isEmpty())
        {
            Toast toast = Toast.makeText(this, "Make sure no field is blank", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            final Intent disappointmentIntent = new Intent();
            disappointmentIntent.putExtra("title", title);
            disappointmentIntent.putExtra("caption", caption);
            disappointmentIntent.putExtra("user", user);
            if (newPhotoTaken) {
                disappointmentIntent.putExtra("photoPath", outputFile.getAbsolutePath());
            }
            if (!forEdit) {
                setResult(1, disappointmentIntent);
                finish();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to edit this item?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                setResult(2, disappointmentIntent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
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
        ImageView imageView = (ImageView) findViewById(R.id.formPic);
        Picasso.with(this).load(outputFile).fit().into(imageView);
        newPhotoTaken = true;
    }
}
