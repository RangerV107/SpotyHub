package com.rangerv.spotyhub.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.rangerv.spotifywebapi.SpotifyApi;
import com.rangerv.spotifywebapi.SpotifyService;
import com.rangerv.spotifywebapi.model.TrackObject;
import com.rangerv.spotyhub.R;
import com.rangerv.spotyhub.UITest;
import com.rangerv.spotyhub.extensions.Extensions;
import com.rangerv.spotyhub.extensions.KMeansImage;
import com.rangerv.spotyhub.receivers.SpotifyBroadcastReceiver;
import com.rangerv.spotyhub.receivers.SpotifyReceiverActions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;

    private String mAccessToken;
    private SpotifyApi spotifyApi;
    private SpotifyService spotifyService;

    private Button getSimilarButton;
    private Button toTestButton;
    private TextView currentTrackName;
    private TextView currentTrackArtistName;
    private ImageView currentTrackCover;
    private LinearLayout currentPlayLayout;

    SpotifyBroadcastReceiver spotifyBroadcastReceiver;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        mAccessToken = Extensions.loadTextPair(this, "AUTH_TOKEN");
        spotifyApi = new SpotifyApi();
        spotifyApi.setAccessToken(mAccessToken);
        spotifyService = spotifyApi.getService();

        getSimilarButton = findViewById(R.id.button_act_main_get_similar);
        getSimilarButton.setOnClickListener(this);
        toTestButton = findViewById(R.id.button_act_main_to_act_test);
        toTestButton.setOnClickListener(this);

        currentTrackName = findViewById(R.id.text_act_main_track_name);
        currentTrackArtistName = findViewById(R.id.text_act_main_track_artist);
        currentTrackCover = findViewById(R.id.image_act_main_cover);
        currentPlayLayout = findViewById(R.id.linear_layout_act_main_current_play);

        spotifyBroadcastReceiver = new SpotifyBroadcastReceiver(this);

    }

    @Override protected void onResume() {
        super.onResume();
        spotifyBroadcastReceiver.Register(spotifyReceiverActions);
    }


    @Override protected void onPause() {
        super.onPause();
        spotifyBroadcastReceiver.Unregister();
    }


    @Override public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_act_main_get_similar:
                onGetSimilarClicked();
                break;
            case R.id.button_act_main_to_act_test:
                Intent intent = new Intent(MainActivity.this, UITest.class);
                startActivity(intent);
                break;
        }
    }





    private void SetGradient(List<Integer> colors) {
        int[] colors_int = new int[colors.size()];
        for(int c = 0; c < colors.size(); c++){
            colors_int[c] = colors.get(c);
        }
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BL_TR, colors_int);
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        float rad = Extensions.convertDpToPixels(context, 50);
        drawable.setCornerRadii(new float[]
                {0.0f, 0.0f, 0.0f, 0.0f, rad, rad, rad, rad});
        currentPlayLayout.setBackground(drawable);
    }


    SpotifyReceiverActions spotifyReceiverActions = new SpotifyReceiverActions() {
        @Override public void onMetadataChanged(String trackId, String artistName, String albumName, String trackName) {
            GetTrackOnId(trackId.substring(14));
        }
        @Override public void onPlaybackStateChanged(boolean playing, int positionInMs) { }
    };


    private void onGetSimilarClicked() {
        Intent intent = new Intent(MainActivity.this, GetSimilarActivity.class);
        startActivity(intent);
    }


    private void GetTrackOnId(String trackId) {
        spotifyService.getTrack(trackId).enqueue(new Callback<TrackObject>() {
            @Override public void onResponse(Call<TrackObject> call, Response<TrackObject> response) {
                if(response.body() != null) {
                    currentTrackName.setText(response.body().name);
                    currentTrackArtistName.setText(response.body().artists.get(0).name);
                    Glide.with(context)
                            .asBitmap()
                            .load(response.body().album.images.get(1).url)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(new CustomTarget<Bitmap>() {
                                @Override public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    currentTrackCover.setImageBitmap(resource);
                                    new KMeansImage(resource, colors -> { SetGradient(colors); }).execute();
                                }
                                @Override public void onLoadCleared(@Nullable Drawable placeholder) { }
                            });
                }
            }
            @Override public void onFailure(Call<TrackObject> call, Throwable t) { }
        });
    }



}