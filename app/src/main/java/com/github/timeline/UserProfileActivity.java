package com.github.timeline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.timeline.Utils.Constants;
import com.github.timeline.Utils.GLog;
import com.github.timeline.Utils.JsonParser.UserProfile;
import com.github.timeline.serverCommunication.CustomOkHttpClient;

import java.io.InputStream;

public class UserProfileActivity extends AppCompatActivity {
    private static final String TAG = UserProfileActivity.class.getSimpleName();

    private static final String FORMAT_LOGIN = "( %s )";
    private static final String FORMAT_NUM_REPOS = "Public repos: %s";

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
        loadAvatar(UserProfile.getAvatarUrl(userProfileJson));

        TextView tvName = findViewById(R.id.tv_name);
        tvName.setText(UserProfile.getName(userProfileJson));

        TextView tvLogin = findViewById(R.id.tv_login);
        tvLogin.setText(String.format(FORMAT_LOGIN, UserProfile.getLogin(userProfileJson)));

        TextView tvPublicRepo = findViewById(R.id.tv_public_repos);
        tvPublicRepo.setText(String.format(FORMAT_NUM_REPOS, UserProfile.getPublicRepo(userProfileJson)));
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