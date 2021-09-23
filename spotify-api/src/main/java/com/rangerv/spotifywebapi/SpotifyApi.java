package com.rangerv.spotifywebapi;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpotifyApi {

    public static final String SPOTIFY_WEB_API_ENDPOINT = "https://api.spotify.com/v1/";
    private final SpotifyService mSpotifyService;
    private String mAccessToken;
    public SpotifyApiFunctions apiFunctions;


    public SpotifyApi() {
        mSpotifyService = init();
        apiFunctions = new SpotifyApiFunctions(mSpotifyService);
    }

    private SpotifyService init() {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .addInterceptor(new HeaderInterceptor())
                //.addInterceptor(logInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SPOTIFY_WEB_API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit.create(SpotifyService.class);
    }

    public SpotifyApi setAccessToken(String accessToken) {
        mAccessToken = accessToken;
        apiFunctions.setAccessToken(accessToken);
        return this;
    }

    public SpotifyService getService() {
        return mSpotifyService;
    }

    public class HeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer " + mAccessToken)
                    .build();
            Response response = chain.proceed(request);
            return response;
        }
    }
}
