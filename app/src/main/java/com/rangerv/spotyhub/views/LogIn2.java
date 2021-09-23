package com.rangerv.spotyhub.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.net.Uri;
import android.widget.Toast;

import com.rangerv.spotyhub.R;
import com.rangerv.spotyhub.extensions.Extensions;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import okhttp3.Call;

public class LogIn2 extends AppCompatActivity {

    private static String CLIENT_ID = "";
    private static String REDIRECT_URI = "";
    private static String SCOPES = "";
    public static final int AUTH_TOKEN_REQUEST_CODE = 0x10;
    public static final int AUTH_CODE_REQUEST_CODE = 0x11;

    private String mAccessToken;
    private String mAccessCode;
    private Call mCall;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in2);
        mContext = this;

        LogIn();
    }

    @Override
    protected void onDestroy() {
        cancelCall();
        super.onDestroy();
    }

    private void LogIn() {
        Resources res = getResources();
        CLIENT_ID = res.getString(R.string.CLIENT_ID);
        REDIRECT_URI = res.getString(R.string.REDIRECT_URI);
        SCOPES = res.getString(R.string.SCOPES);

        getRequestToken();
    }

    public void getRequestCode() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.CODE);
        AuthorizationClient.openLoginActivity(this, AUTH_CODE_REQUEST_CODE, request);
    }

    public void getRequestToken() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN);
        AuthorizationClient.openLoginActivity(this, AUTH_TOKEN_REQUEST_CODE, request);
    }

    private AuthorizationRequest getAuthenticationRequest(AuthorizationResponse.Type type) {
        return new AuthorizationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
                .setShowDialog(false)
                .setScopes(new String[]{ SCOPES })
                .setCampaign("your-campaign-token")
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);

//        if (AUTH_TOKEN_REQUEST_CODE == requestCode) {
//            mAccessToken = response.getAccessToken();
//        } else if (AUTH_CODE_REQUEST_CODE == requestCode) {
//            mAccessCode = response.getCode();
//        }

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AUTH_TOKEN_REQUEST_CODE:
                    mAccessToken = response.getAccessToken();
                    break;
                case AUTH_CODE_REQUEST_CODE:
                    mAccessCode = response.getCode();
                    break;
            }
        }
        else { Toast.makeText(this, "Wrong result", Toast.LENGTH_SHORT).show(); }

        Intent intent = new Intent(LogIn2.this, MainActivity.class);
        //intent.putExtra(AUTH_TOKEN, mAccessToken);
        startActivity(intent);
        Extensions.saveTextPair(mContext,"AUTH_TOKEN", mAccessToken);
        LogIn2.this.finish();
    }

    private Uri getRedirectUri() {
        return Uri.parse(REDIRECT_URI);
    }

    private void cancelCall() {
        if (mCall != null) {
            mCall.cancel();
        }
    }

}