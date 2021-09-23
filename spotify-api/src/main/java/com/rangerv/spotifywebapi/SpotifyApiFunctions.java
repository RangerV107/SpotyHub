package com.rangerv.spotifywebapi;

import android.util.Log;

import com.rangerv.spotifywebapi.model.TrackObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpotifyApiFunctions {

    private final SpotifyService mSpotifyService;
    private String mAccessToken;

    public SpotifyApiFunctions(SpotifyService spotifyService){
        mSpotifyService = spotifyService;
    }

    public void setAccessToken(String accessToken) {
        mAccessToken = accessToken;
    }


    public String SpotyLog(String operationName, String token, int code, String otherInfo) {
//        200	OK - The request has succeeded. The client can read the result of the request in the body and the headers of the response.
//        201	Created - The request has been fulfilled and resulted in a new resource being created.
//        202	Accepted - The request has been accepted for processing, but the processing has not been completed.
//        204	No Content - The request has succeeded but returns no message body.
//        304	Not Modified. See Conditional requests.
//        400	Bad Request - The request could not be understood by the server due to malformed syntax. The message body will contain more information; see Response Schema.
//        401	Unauthorized - The request requires user authentication or, if the request included authorization credentials, authorization has been refused for those credentials.
//        403	Forbidden - The server understood the request, but is refusing to fulfill it.
//        404	Not Found - The requested resource could not be found. This error can be due to a temporary or permanent condition.
//        429	Too Many Requests - Rate limiting has been applied.
//        500	Internal Server Error. You should never receive this error because our clever coders catch them all … but if you are unlucky enough to get one, please report it to us through a comment at the bottom of this page.
//        502	Bad Gateway - The server was acting as a gateway or proxy and received an invalid response from the upstream server.
//        503	Service Unavailable - The server is currently unable to handle the request due to a temporary condition which will be alleviated after some delay. You can choose to resend the request again.

        String codeInf = "...";
        switch (code) {
            case 200: codeInf = "OK"; break;
            case 201: codeInf = "Created"; break;
            case 202: codeInf = "Accepted"; break;
            case 204: codeInf = "No Content"; break;
            case 304: codeInf = "Not Modified"; break;
            case 400: codeInf = "Bad Request"; break;
            case 401: codeInf = "Unauthorized"; break;
            case 403: codeInf = "Forbidden"; break;
            case 404: codeInf = "Not Found"; break;
            case 429: codeInf = "Too Many Requests"; break;
            case 500: codeInf = "Internal Server Error"; break;
            case 502: codeInf = "Bad Gateway"; break;
            case 503: codeInf = "Service Unavailable"; break;
        }

//        Log.d("SpotifyServiceLog",
//                "Operation: " + operationName + "\n" +
//                "Token: " + token.substring(0, 5) + "...\n" +
//                "Code: " + code + " - " + codeInf + "\n\n"
//        );

        StringBuffer strBuffer = new StringBuffer();
        strBuffer.append("Operation: " + operationName + "\n");
        strBuffer.append("Token: " + token.substring(0, 5) + "...\n");
        strBuffer.append("Code: " + code + " - " + codeInf + "\n");
        if(!otherInfo.equals("")) {
            strBuffer.append("\n");
            strBuffer.append(otherInfo + "\n");
        }
        strBuffer.append("----------------------------------" + "\n\n");
        return strBuffer.toString();
    }




//    /**
//     * Get Spotify catalog information for a single track identified by its unique Spotify ID.
//     *
//     * @param trackId The Spotify ID for the track.
//     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/#endpoint-get-track">Get a Track</a>
//     */
//    public void GetTrackOnId(String trackId, Callback<TrackObject> callback) {
//        mSpotifyService.getTrack(trackId).enqueue(callback);
//    }
//
//    public void GetTrackOnId(String trackId) {
//        mSpotifyService.getTrack(trackId).enqueue(new Callback<TrackObject>() {
//            @Override
//            public void onResponse(Call<TrackObject> call, Response<TrackObject> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<TrackObject> call, Throwable t) {
//
//            }
//        });
//    }







}
