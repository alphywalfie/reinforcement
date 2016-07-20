package com.example.logan.cameraparsedemo2016;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {

	private TextView textTargetUri;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		textTargetUri = (TextView) findViewById(R.id.targeturi);
	}

	private File outputFile;
	private File thumbNailFile;
	
	public void takePicture(View view)
	{
	
		File sdCard = Environment.getExternalStorageDirectory();
		File directory = new File (sdCard.getAbsolutePath() + "/CameraTest/");
		directory.mkdirs();
		
		// unique filename based on the time
		String name = String.valueOf(System.currentTimeMillis());
		outputFile = new File(directory, name+".jpg");
		thumbNailFile = new File(directory, name+"_tn.jpg");

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
						textTargetUri.setText(e.getClass().getName());
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
		ImageView imageView = (ImageView) findViewById(R.id.targetimage);
		Picasso.with(this).load(outputFile).fit().into(imageView);
		textTargetUri.setText(outputFile.getAbsolutePath());
	}

	
}
