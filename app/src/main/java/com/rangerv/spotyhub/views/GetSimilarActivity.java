package com.rangerv.spotyhub.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rangerv.spotifywebapi.SpotifyApi;
import com.rangerv.spotifywebapi.SpotifyService;
import com.rangerv.spotifywebapi.model.CurrentlyPlayingObject;
import com.rangerv.spotifywebapi.model.PagingObject;
import com.rangerv.spotifywebapi.model.PlaylistTrackObject;
import com.rangerv.spotifywebapi.model.RecommendationsObject;
import com.rangerv.spotifywebapi.model.SnapshotId;
import com.rangerv.spotifywebapi.model.TrackObject;
import com.rangerv.spotifywebapi.model.TrackToRemove;
import com.rangerv.spotifywebapi.model.TracksToRemove;
import com.rangerv.spotyhub.R;
import com.rangerv.spotyhub.extensions.Extensions;
import com.rangerv.spotyhub.receivers.SpotifyBroadcastReceiver;
import com.rangerv.spotyhub.receivers.SpotifyReceiverActions;
import com.rangerv.spotyhub.views.adapters.TrackAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GetSimilarActivity extends AppCompatActivity implements View.OnClickListener {

    private String mAccessToken;
    private SpotifyApi spotifyApi;
    private SpotifyService spotifyService;

    private TextView scrollTxt;
    private TextView trackNameTxt;
    private TextView trackArtistNameTxt;
    private ImageView trackImg;
    private Button addToSimilarPlaylistButton;
    private TrackAdapter trackAdapter;

    private SpotifyBroadcastReceiver spotifyBroadcastReceiver;

    private TrackObject currentTrack;
    private RecommendationsObject currentRecommendationTracks;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_similar);

        mAccessToken = Extensions.loadTextPair(this, "AUTH_TOKEN");
        spotifyApi = new SpotifyApi();
        spotifyApi.setAccessToken(mAccessToken);
        spotifyService = spotifyApi.getService();

        trackNameTxt = findViewById(R.id.text_act_get_similar_track_name);
        trackArtistNameTxt = findViewById(R.id.text_act_get_similar_track_artist);
        trackImg = findViewById(R.id.image_act_get_similar_cover);
        scrollTxt = findViewById(R.id.text_act_get_similar_scroll_text);
        addToSimilarPlaylistButton = findViewById(R.id.button_act_get_similar_add_to_similar_playlist);
        addToSimilarPlaylistButton.setOnClickListener(this);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.Rlist_saved_track);
        trackAdapter = new TrackAdapter();
        recyclerView.setAdapter(trackAdapter);

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
            case R.id.button_act_get_similar_add_to_similar_playlist:
                //SaveRecommendationsToPlaylist("77NUfffLAZAgT8hbS8zXSM");
                //GetPlaylistItems("7bHxcG6aYgZYE9uYYJyDVD");
                UpdateSimilarPlaylist("77NUfffLAZAgT8hbS8zXSM");
                break;
        }
    }



    SpotifyReceiverActions spotifyReceiverActions = new SpotifyReceiverActions() {
        @Override public void onMetadataChanged(String trackId, String artistName, String albumName, String trackName) {

            spotifyService.getTrack(trackId.substring(14)).enqueue(new Callback<TrackObject>() {
                @Override public void onResponse(Call<TrackObject> call, Response<TrackObject> response) {
                    if(response.body() != null) {

                        scrollTxt.append(spotifyApi.apiFunctions.SpotyLog("GetTrackOnId", mAccessToken, response.code(),
                                "Global track name: " + response.body().name + "\n" + "Artist name: " + response.body().artists.get(0).name));

                        currentTrack = response.body();

                        trackNameTxt.setText(response.body().name);
                        trackArtistNameTxt.setText(response.body().artists.get(0).name);
                        new Extensions.ImageLoadTask(response.body().album.images.get(1).url, trackImg).execute();
                    }
                    GetRecommendations();
                }
                @Override public void onFailure(Call<TrackObject> call, Throwable t) { }
            });
        }
        @Override public void onPlaybackStateChanged(boolean playing, int positionInMs) { }
    };



    private void GetTrackOnId(String trackId) {
        spotifyService.getTrack(trackId).enqueue(new Callback<TrackObject>() {
            @Override public void onResponse(Call<TrackObject> call, Response<TrackObject> response) {
                if(response.body() != null) {
                    scrollTxt.append(spotifyApi.apiFunctions.SpotyLog("GetTrackOnId", mAccessToken, response.code(),
                            "Global track name: " + response.body().name + "\n" + "Artist name: " + response.body().artists.get(0).name));

                    trackNameTxt.setText(response.body().name);
                    trackArtistNameTxt.setText(response.body().artists.get(0).name);
                    new Extensions.ImageLoadTask(response.body().album.images.get(1).url, trackImg).execute();
                }
            }
            @Override public void onFailure(Call<TrackObject> call, Throwable t) { }
        });
    }


    private void GetCurrentlyPlayingTrack() {
        spotifyService.getGetCurrentlyPlayingTrack().enqueue(new Callback<CurrentlyPlayingObject>() {
            @Override public void onResponse(Call<CurrentlyPlayingObject> call, Response<CurrentlyPlayingObject> response) {
                scrollTxt.append(spotifyApi.apiFunctions.SpotyLog("GetCurrentlyPlayingTrack", mAccessToken, response.code(), ""));

                if(response.body() != null) {
                    Log.d("SpotifyReceiver","GetCurrentlyPlayingTrack: " + response.body().item.name);
                    trackNameTxt.setText(response.body().item.name);
                    trackArtistNameTxt.setText(response.body().item.artists.get(0).name);
                    new Extensions.ImageLoadTask(response.body().item.album.images.get(1).url, trackImg).execute();
                }
            }

            @Override public void onFailure(Call<CurrentlyPlayingObject> call, Throwable t) {
                scrollTxt.setText(t.getMessage());
            }
        });
    }


    private void GetRecommendations(){
        Map<String, Object> queryParameters = new HashMap<String, Object>();
        queryParameters.put("limit", 20);
        queryParameters.put("seed_tracks", currentTrack.id);

        //Log.d("GetRecommendations", currentTrack.id);
        spotifyService.getRecommendations(queryParameters).enqueue(new Callback<RecommendationsObject>() {
            @Override public void onResponse(Call<RecommendationsObject> call, Response<RecommendationsObject> response) {
                //Log.d("GetRecommendations", response.body().tracks.get(0).name);
                trackAdapter.setData(response.body().tracks);
                currentRecommendationTracks = response.body();
            }
            @Override public void onFailure(Call<RecommendationsObject> call, Throwable t) { }
        });
    }


    private void SaveRecommendationsToPlaylist(String playlistId){
        Map<String, Object> queryParameters = new HashMap<String, Object>();
        queryParameters.put("position", 0);

        StringBuilder stringBuilder = new StringBuilder(1000);
        for(TrackObject track: currentRecommendationTracks.tracks){
            stringBuilder.append("spotify:track:" + track.id + ",");
        }
        stringBuilder.setLength(stringBuilder.length() - 1);
        //scrollTxt.append(stringBuilder.toString() + "\n\n");
        queryParameters.put("uris", stringBuilder.toString());

        Map<String, Object> body = new HashMap<String, Object>();
        //scrollTxt.append(currentRecommendationTracks.tracks.get(0).name + " : " + currentRecommendationTracks.tracks.get(0).id + "\n\n");

        spotifyService.addItemToPlaylist(playlistId, queryParameters, body).enqueue(new Callback<SnapshotId>() {
            @Override public void onResponse(Call<SnapshotId> call, Response<SnapshotId> response) {
                scrollTxt.append(spotifyApi.apiFunctions.SpotyLog("SaveRecommendationsToPlaylist", mAccessToken, response.code(),""));
            }
            @Override public void onFailure(Call<SnapshotId> call, Throwable t) { }
        });
    }


    private void GetPlaylistItems(String playlistId) {
        Map<String, Object> queryParameters = new HashMap<String, Object>();

        spotifyService.getPlaylistsItems(playlistId, queryParameters).enqueue(new Callback<PagingObject<PlaylistTrackObject>>() {
            @Override public void onResponse(Call<PagingObject<PlaylistTrackObject>> call, Response<PagingObject<PlaylistTrackObject>> response) {
                scrollTxt.append(spotifyApi.apiFunctions.SpotyLog("GetPlaylistItems", mAccessToken, response.code(),""));
                if(response.body() != null) {
                    List<TrackObject> tracks = new ArrayList<>();
                    for (PlaylistTrackObject track : response.body().items) {
                        tracks.add(track.track);
                    }
                    trackAdapter.setData(tracks);
                }

            }
            @Override public void onFailure(Call<PagingObject<PlaylistTrackObject>> call, Throwable t) { }
        });
    }


    private void UpdateSimilarPlaylist(String similarPlaylistId) {
        Map<String, Object> queryParameters = new HashMap<String, Object>();
        queryParameters.put("limit", 100);

        spotifyService.getPlaylistsItems(similarPlaylistId, queryParameters).enqueue(new Callback<PagingObject<PlaylistTrackObject>>() {
            @Override public void onResponse(Call<PagingObject<PlaylistTrackObject>> call, Response<PagingObject<PlaylistTrackObject>> response) {
                if(response.body() != null) {

                    List<TrackToRemove> tracks = new ArrayList<>();
                    for (PlaylistTrackObject track : response.body().items) {
                        TrackToRemove t = new TrackToRemove();
                        t.uri = track.track.uri;
                        tracks.add(t);
                    }
                    TracksToRemove tracksToRemove = new TracksToRemove();
                    tracksToRemove.tracks = tracks;

                    spotifyService.removeItemsFromPlaylist(similarPlaylistId, tracksToRemove).enqueue(new Callback<SnapshotId>() {
                        @Override public void onResponse(Call<SnapshotId> call, Response<SnapshotId> response) {
                            if(response.body() != null) {
                                //SpotyLog("removeItemsFromPlaylist", mAccessToken, response.code(),"");
                            }
                        }
                        @Override public void onFailure(Call<SnapshotId> call, Throwable t) {

                        }
                    });

                    SaveRecommendationsToPlaylist(similarPlaylistId);

                }
            }
            @Override public void onFailure(Call<PagingObject<PlaylistTrackObject>> call, Throwable t) {

            }
        });
    }



}