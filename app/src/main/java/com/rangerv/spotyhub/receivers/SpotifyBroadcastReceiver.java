package com.rangerv.spotyhub.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

public class SpotifyBroadcastReceiver {

    Context context;
    BroadcastReceiver spotifyReceiver;

    public SpotifyBroadcastReceiver(Context context) {
        this.context = context;
    }

    public void Register(SpotifyReceiverActions actions) {
        spotifyReceiver = new SpotifyReceiver(actions);

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.spotify.music.playbackstatechanged");
        filter.addAction("com.spotify.music.metadatachanged");
        filter.addAction("com.spotify.music.queuechanged");
        context.registerReceiver(spotifyReceiver, filter);
    }

    public void Unregister() {
        if(spotifyReceiver != null) {
            context.unregisterReceiver(spotifyReceiver);
        }
    }

    public BroadcastReceiver getSpotifyReceiver(){
        return spotifyReceiver;
    }
}
