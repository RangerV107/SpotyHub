package com.rangerv.spotifywebapi;


import com.rangerv.spotifywebapi.model.CurrentlyPlayingObject;
import com.rangerv.spotifywebapi.model.PagingObject;
import com.rangerv.spotifywebapi.model.PlaylistTrackObject;
import com.rangerv.spotifywebapi.model.PrivateUserObject;
import com.rangerv.spotifywebapi.model.RecommendationsObject;
import com.rangerv.spotifywebapi.model.SavedTrackObject;
import com.rangerv.spotifywebapi.model.SnapshotId;
import com.rangerv.spotifywebapi.model.TrackObject;
import com.rangerv.spotifywebapi.model.TracksToRemove;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface SpotifyService {

    /**
     * The maximum number of objects to return.
     */
    String LIMIT = "limit";

    /**
     * The index of the first playlist to return. Default: 0 (the first object).
     * Use with limit to get the next set of objects (albums, playlists, etc).
     */
    String OFFSET = "offset";

    /**
     * A comma-separated list of keywords that will be used to filter the response.
     * Valid values are: {@code album}, {@code single}, {@code appears_on}, {@code compilation}
     */
    String ALBUM_TYPE = "album_type";

    /**
     * The country: an ISO 3166-1 alpha-2 country code.
     * Limit the response to one particular geographical market.
     * Synonym to {@link #COUNTRY}
     */
    String MARKET = "market";

    /**
     * Same as {@link #MARKET}
     */
    String COUNTRY = "country";

    /**
     * The desired language, consisting of a lowercase ISO 639 language code
     * and an uppercase ISO 3166-1 alpha-2 country code, joined by an underscore.
     * For example: es_MX, meaning "Spanish (Mexico)".
     */
    String LOCALE = "locale";

    /**
     * Filters for the query: a comma-separated list of the fields to return.
     * If omitted, all fields are returned.
     */
    String FIELDS = "fields";

    /**
     * A timestamp in ISO 8601 format: yyyy-MM-ddTHH:mm:ss. Use this parameter to
     * specify the user's local time to get results tailored for that specific date
     * and time in the day. If not provided, the response defaults to the current UTC time
     */
    String TIMESTAMP = "timestamp";

    String TIME_RANGE = "time_range";






    /**
     * Get detailed profile information about the current user (including the current user’s username).
     * Important! If the user-read-email scope is authorized, the returned JSON will include the email address that was entered when the user created their Spotify account. This email address is unverified; do not assume that the email address belongs to the user.
     *
     * @return The current user
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/#endpoint-get-current-users-profile">Get Current User's Profile</a>
     */
    @GET("me")
    Call<PrivateUserObject> getMe(); //UserPrivate getMe();


    /**
     * Get the object currently being played on the user’s Spotify account.
     *
     * @return The current track
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/#endpoint-get-the-users-currently-playing-track">Get the User's Currently Playing Track</a>
     */
    @GET("me/player/currently-playing")
    Call<CurrentlyPlayingObject> getGetCurrentlyPlayingTrack();


    /**
     * Get a list of the songs saved in the current Spotify user’s “Your Music” library.
     *
     * @return A list of saved tracks
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/#endpoint-get-users-saved-tracks">Get a User’s Saved Tracks</a>
     */
    @GET("me/tracks")
    Call<PagingObject<SavedTrackObject>> getMySavedTracks();


    /**
     * Get a list of the songs saved in the current Spotify user’s “Your Music” library.
     *
     * @return A list of saved tracks
     * @param options Optional parameters.
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/#endpoint-get-users-saved-tracks">Get a User’s Saved Tracks</a>
     */
    @GET("me/tracks")
    Call<PagingObject<SavedTrackObject>> getMySavedTracks(@QueryMap Map<String, Object> options);


    /**
     * Get Spotify catalog information for a single track identified by its unique Spotify ID.
     *
     * @param trackId The Spotify ID for the track.
     * @return Requested track information
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/#endpoint-get-track">Get a Track</a>
     */
    @GET("tracks/{id}")
    Call<TrackObject> getTrack(@Path("id") String trackId);


    /**
     * Get Spotify catalog information for a single track identified by its unique Spotify ID.
     *
     * @param trackId The Spotify ID for the track.
     * @param options Optional parameters.
     * @return Requested track information
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/#endpoint-get-track">Get a Track</a>
     */
    @GET("tracks/{id}")
    Call<TrackObject> getTrack(@Path("id") String trackId, @QueryMap Map<String, Object> options);


    /**
     * Recommendations are generated based on the available information for a given seed entity and matched against similar artists and tracks.
     * If there is sufficient information about the provided seeds, a list of tracks will be returned together with pool size details.
     *
     * @param options Optional parameters.
     * @return Recommendations response object
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/#endpoint-get-recommendations">Get a recommendations</a>
     */
    @GET("recommendations")
    Call<RecommendationsObject> getRecommendations(@QueryMap Map<String, Object> options);


    /**
     * Get full details of the items of a playlist owned by a Spotify user.
     *
     * @param options Optional parameters.
     * @return Items of a playlist.
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/#endpoint-get-playlists-tracks">Get a Playlist's Items</a>
     */
    @GET("playlists/{playlist_id}/tracks")
    Call<PagingObject<PlaylistTrackObject>> getPlaylistsItems(@Path("playlist_id") String playlistId, @QueryMap Map<String, Object> options);


    /**
     * Add one or more items to a user’s playlist.
     *
     * @param playlistId      The playlist's ID
     * @param queryParameters Query parameters
     * @param body            The body parameters
     * @return A snapshot ID (the version of the playlist)
     * @see <a href="https://developer.spotify.com/web-api/add-tracks-to-playlist/">Add Tracks to a Playlist</a>
     */
    @Headers("Content-Type: application/json")
    @POST("playlists/{playlist_id}/tracks")
    Call<SnapshotId> addItemToPlaylist(@Path("playlist_id") String playlistId, @QueryMap Map<String, Object> queryParameters, @Body Map<String, Object> body);


    /**
     * Remove one or more items from a user’s playlist.
     *
     * @param playlistId     The playlist's Id
     * @param tracksToRemove A list of tracks to remove
     * @return A snapshot ID (the version of the playlist)
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/#endpoint-remove-tracks-playlist">Remove Tracks from a Playlist</a>
     */
    @HTTP(method = "DELETE", path = "playlists/{playlist_id}/tracks", hasBody = true)
    Call<SnapshotId> removeItemsFromPlaylist(@Path("playlist_id") String playlistId, @Body TracksToRemove tracksToRemove);


}
