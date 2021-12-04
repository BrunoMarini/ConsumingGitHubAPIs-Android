package com.github.timeline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.github.timeline.Utils.Constants;
import com.github.timeline.Utils.GLog;
import com.github.timeline.Utils.JsonParser;
import com.github.timeline.serverCommunication.CustomOkHttpClient;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL;

public class UserProfileActivity extends AppCompatActivity {
    private static final String TAG = UserProfileActivity.class.getSimpleName();

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mContext = getApplicationContext();

        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(Constants.EXTRA_USER_PROFILE_RESPONSE)) {
            GLog.d(TAG, "Error! Null intent or null response, cancelling");
            finish();
        }

        loadUserInfo(intent.getStringExtra(Constants.EXTRA_USER_PROFILE_RESPONSE));
    }

    private void loadUserInfo(String userProfileJson) {
        GLog.d(TAG, "loadUserInfo()");
        String avatarUrl = JsonParser.UserProfile.getAvatarUrl(userProfileJson);
        loadAvatar(avatarUrl);
    }

    private void loadAvatar(String url) {
        new Thread(() -> {
            InputStream inputStream = CustomOkHttpClient.loadAvatar(mContext, url);
            if (inputStream == null) {
                GLog.e(TAG, "loadAvatar returned null");
                return;
            }
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ImageView avatarImageView = findViewById(R.id.iv_user_avatar);
            runOnUiThread(() -> avatarImageView.setImageBitmap(bitmap));
        }).start();
    }
}