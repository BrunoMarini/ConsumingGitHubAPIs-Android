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

        Bundle userInfoBundle = UserProfile.getAllInfo(userProfileJson);

        loadAvatar(userInfoBundle.getString(Constants.PROFILE_AVATAR_URL));

        TextView tvName = findViewById(R.id.tv_name);
        tvName.setText(userInfoBundle.getString(Constants.PROFILE_NAME));

        TextView tvLogin = findViewById(R.id.tv_login);
        tvLogin.setText(String.format(FORMAT_LOGIN, userInfoBundle.getString(Constants.PROFILE_LOGIN)));

        TextView tvPublicRepo = findViewById(R.id.tv_public_repos);
        int publicRepos = Integer.parseInt(userInfoBundle.getString(Constants.PROFILE_PUBLIC_REPOS));
        tvPublicRepo.setText(String.format(FORMAT_NUM_REPOS, String.valueOf(publicRepos)));

        TextView tvBio = findViewById(R.id.tv_bio);
        String bio = userInfoBundle.getString(Constants.PROFILE_BIO);
        tvBio.setText(bio != null ? bio : "This profile does not have a bio");

        if (publicRepos > 0) {
            addSeePublicReposButton(userInfoBundle.getString(Constants.PROFILE_REPOS_URL));
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