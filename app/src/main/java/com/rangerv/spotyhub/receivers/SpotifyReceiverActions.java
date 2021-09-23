package com.rangerv.spotyhub.receivers;

public interface SpotifyReceiverActions {
    void onMetadataChanged(String trackId, String artistName, String albumName, String trackName);
    void onPlaybackStateChanged(boolean playing, int positionInMs);
}
