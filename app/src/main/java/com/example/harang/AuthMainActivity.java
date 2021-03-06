package com.example.harang;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.HostedUIOptions;
import com.amazonaws.mobile.client.SignInUIOptions;
import com.amazonaws.mobile.client.UserStateDetails;

public class AuthMainActivity extends AppCompatActivity {

    private static final String TAG = AuthMainActivity.class.getSimpleName();

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_main);

        context = this;

        CommonAction.checkSession(this, true);
    }

    private void _openFacebookLogin() {
        //페이스북 로그인
        HostedUIOptions hostedUIOptions = HostedUIOptions.builder()
                .scopes("openid", "email")
                .identityProvider("Facebook")
                .build();

        SignInUIOptions signInUIOptions = SignInUIOptions.builder()
                .hostedUIOptions(hostedUIOptions)
                .build();

        AWSMobileClient.getInstance().showSignIn(this, signInUIOptions, new Callback<UserStateDetails>() {
            @Override
            public void onResult(UserStateDetails details) {
                Log.d(TAG, "onResult: " + details.getUserState());
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError: ", e);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //앱 메인으로 이동한다
        Intent activityIntent = getIntent();
        if (activityIntent.getData() != null &&
                "socialdemoapp".equals(activityIntent.getData().getScheme())) {
            if (AWSMobileClient.getInstance().handleAuthResponse(activityIntent))
                CommonAction.checkSession(this, true);
        }

    }

    public void openLogin(View view) {
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }

    public void openRegistration(View view) {
        Intent intent = new Intent(context, SignUpActivity.class);
        startActivity(intent);
    }

    public void openFacebookLogin(View view) {

        _openFacebookLogin();
    }

}
