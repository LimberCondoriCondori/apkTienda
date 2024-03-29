package com.example.apktienda1.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class TaskImg extends AsyncTask<String ,String,Bitmap> {
    OnLoadImg onloadImg;
    int index=-1;
    public void setLoadImage(OnLoadImg oli,int index ){
        this.index=index;
        onloadImg=oli;
    }
    @Override
    protected Bitmap doInBackground(String... strings) {
        try{
            URL url=new URL(strings[0]);
            InputStream stream=url.openConnection().getInputStream();
            Bitmap imageBitmap= BitmapFactory.decodeStream(stream);
            return imageBitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        onloadImg.onLoadImg(index,bitmap);

    }
}
