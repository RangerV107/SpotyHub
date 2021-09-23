package com.rangerv.spotyhub.extensions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.net.Uri;
import android.widget.ImageView;

import com.rangerv.spotyhub.receivers.SpotifyReceiver;
import com.rangerv.spotyhub.receivers.SpotifyReceiverActions;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class Extensions {
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "MainPref";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void saveTextPair(Context context, final String key, final String value) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String loadTextPair(Context context, final String key) {
        return getPrefs(context).getString(key, "unknown");
    }

    public static class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private final String url;
        private final ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }
    }


    public static float convertDpToPixels(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static float convertPixelsToDp(Context context, float pixels) {
        return pixels / context.getResources().getDisplayMetrics().density;
    }



}
