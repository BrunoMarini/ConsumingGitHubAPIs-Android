package com.github.timeline.userInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import com.github.timeline.R;
import com.github.timeline.Utils.Constants;
import com.github.timeline.Utils.GLog;
import com.github.timeline.Utils.JsonParser.UserRepos;
import com.github.timeline.serverCommunication.CustomOkHttpClient;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UserReposActivity extends AppCompatActivity {
    private static final String TAG = UserReposActivity.class.getSimpleName();

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_repos);

        mContext = getApplicationContext();

        Intent intent = getIntent();
        if (!intent.hasExtra(Constants.PROFILE_REPOS_URL)) {
            GLog.e(TAG, "User repos URL not found!");
            finish();
        }

        loadUserRepos(intent.getStringExtra(Constants.PROFILE_REPOS_URL));
    }

    private void loadUserRepos(String url) {
        new Thread(() -> {
            String repos = CustomOkHttpClient.loadUserRepos(mContext, url);
            ArrayList<String> reposList = UserRepos.getAllRepoName(repos);
            for (String current : reposList) {
                populateActivity(current);
            }
        }).start();
    }

    private void populateActivity(String repo) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setStroke(10, Color.WHITE);
        drawable.setColor(Color.BLACK);
        drawable.setCornerRadius(30);

        Button button = new Button(mContext);
        button.setBackground(drawable);
        button.setText(repo);
        button.setTextColor(Color.WHITE);
        button.setTextSize(20);

        LinearLayout ll = findViewById(R.id.ll_repos);

        runOnUiThread(() -> {
            ll.addView(button);
        });
    }

}