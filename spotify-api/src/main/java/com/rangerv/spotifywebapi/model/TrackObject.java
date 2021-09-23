package com.rangerv.spotifywebapi.model;

import androidx.annotation.NonNull;

import java.util.List;

public class TrackObject {
    public SimplifiedAlbumObject album;
    public List<SimplifiedArtistObject> artists = null;
    public List<String> available_markets = null;
    public int disc_number;
    public int duration_ms;
    public boolean explicit;
    public ExternalIdObject external_ids;
    public ExternalUrlObject external_urls;
    public String href;
    public String id;
    public boolean is_local;
    public boolean is_playable;
    public LinkedTrackObject linked_from;
    public String name;
    public int popularity;
    public String preview_url;
    public TrackRestrictionObject restrictions;
    public int track_number;
    public String type;
    public String uri;

}
