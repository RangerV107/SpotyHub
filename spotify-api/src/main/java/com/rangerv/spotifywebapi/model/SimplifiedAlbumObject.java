package com.rangerv.spotifywebapi.model;

import java.util.List;

public class SimplifiedAlbumObject {
    public String albumType;
    public List<SimplifiedArtistObject> artists = null;
    public List<String> available_markets = null;
    public ExternalUrlObject external_urls;
    public String href;
    public String id;
    public List<ImageObject> images = null;
    public String name;
    public String release_date;
    public String release_date_precision;
    public int total_tracks;
    public String type;
    public String uri;
}
