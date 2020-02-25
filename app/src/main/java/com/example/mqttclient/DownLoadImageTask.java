package com.example.mqttclient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    public DownLoadImageTask(ImageView bmImage){
        this.bmImage=bmImage;
    }



    @Override
    protected Bitmap doInBackground(String... url) {
        String urlDisplay=url[0];
        Bitmap mIcon=null;
        try{
            InputStream in = new URL(urlDisplay).openStream();
            mIcon= BitmapFactory.decodeStream(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mIcon;
    }

    protected void onPostExecute(Bitmap result){
        bmImage.setImageBitmap(result);
    }

}