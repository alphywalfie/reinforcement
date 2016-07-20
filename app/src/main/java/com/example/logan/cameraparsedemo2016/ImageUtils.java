package com.example.logan.cameraparsedemo2016;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageUtils {

	// Image utils -- move these out later
	
    public static void resizeSavedBitmap(String path, int maxSide, String dest)
    {
    	System.out.println("Resizing");
    	Bitmap resizedBitmap = resizeBitmap(path, maxSide);
    	saveBitmap(resizedBitmap, dest);
    }
    
    public static void saveBitmap(Bitmap bitmap, String path)
    {
    	try 
    	{
    	     FileOutputStream out = new FileOutputStream(path);
    	     bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
    	     out.flush();
    	     out.close();
    	}
    	catch (Exception e) 
    	{
    	     e.printStackTrace();
    	}
    }
    
    public static Bitmap resizeBitmap(String photoPath, int maxSide) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        boolean landscape = true;
        if (photoW<photoH)
        {
        	// portrait
        	landscape = false;
        }
        
        int scaleFactor = 1;
        if (landscape)
        {
        	scaleFactor = photoW/maxSide;
        }
        else
        {
        	scaleFactor = photoH/maxSide;
        }


        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        return BitmapFactory.decodeFile(photoPath, bmOptions);            
    }	
	
	
	
	public static byte[] getFileByte(String path) throws IOException {
	    ByteArrayOutputStream ous = null;
	    InputStream ios = null;
	    try {
	        byte[] buffer = new byte[4096];
	        ous = new ByteArrayOutputStream();
	        ios = new FileInputStream(path);
	        int read = 0;
	        while ((read = ios.read(buffer)) != -1)
	            ous.write(buffer, 0, read);
	    } finally {
	        try {
	            if (ous != null)
	                ous.close();
	        } catch (IOException e) {
	            // swallow, since not that important
	        }
	        try {
	            if (ios != null)
	                ios.close();
	        } catch (IOException e) {
	            // swallow, since not that important
	        }
	    }
	    return ous.toByteArray();
	}
}
