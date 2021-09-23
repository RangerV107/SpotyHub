package com.rangerv.spotifywebapi.model;

public class CurrentlyPlayingObject {
    //public int timestamp;
    public ContextObject context;
    public int progress_ms;
    public TrackObject item;
    public String currently_playing_type;
    public DisallowsObject actions;
    public boolean is_playing;
}
