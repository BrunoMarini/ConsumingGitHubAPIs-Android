package com.github.timeline.userInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.timeline.R;
import com.github.timeline.Utils.Constants;
import com.github.timeline.Utils.GLog;
import com.github.timeline.serverCommunication.JsonParser;
import com.github.timeline.serverCommunication.CustomOkHttpClient;
import com.github.timeline.serverCommunication.responseJson.Profile;

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

        Profile profile = JsonParser.parseProfile(userProfileJson);

        loadAvatar(profile.getAvatar_url());

        TextView tvName = findViewById(R.id.tv_name);
        tvName.setText(profile.getName());

        TextView tvLogin = findViewById(R.id.tv_login);
        tvLogin.setText(String.format(FORMAT_LOGIN, profile.getLogin()));

        TextView tvPublicRepo = findViewById(R.id.tv_public_repos);
        tvPublicRepo.setText(String.format(FORMAT_NUM_REPOS, profile.getPublic_repos()));

        TextView tvBio = findViewById(R.id.tv_bio);
        tvBio.setText(profile.getBio() != null ? profile.getBio() : "This profile does not have a bio");

        if (profile.getPublic_repos() > 0) {
            addSeePublicReposButton(profile.getRepos_url());
        }
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

    private void addSeePublicReposButton(String url) {
        Button button = new Button(mContext);
        button.setText("Check user repos");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UserReposActivity.class);
                intent.putExtra(Constants.PROFILE_REPOS_URL, url);
                startActivity(intent);
            }
        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins(10, 10, 10, 10);
        button.setLayoutParams(params);

        LinearLayout linearLayout = findViewById(R.id.ll_content);
        linearLayout.addView(button);
    }
}