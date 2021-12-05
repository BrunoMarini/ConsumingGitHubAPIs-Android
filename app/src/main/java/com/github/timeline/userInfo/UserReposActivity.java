package com.github.timeline.userInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import com.github.timeline.R;
import com.github.timeline.Utils.Constants;
import com.github.timeline.Utils.GLog;
import com.github.timeline.serverCommunication.JsonParser;
import com.github.timeline.serverCommunication.CustomOkHttpClient;
import com.github.timeline.serverCommunication.responseJson.Repo;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class UserReposActivity extends AppCompatActivity {
    private static final String TAG = UserReposActivity.class.getSimpleName();

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_repos);

        mContext = getApplicationContext();

        Intent intent = getIntent();
        if (!intent.hasExtra(Constants.PROFILE_REPO_URL)) {
            GLog.e(TAG, "User repos URL not found!");
            showAlertMessage(getString(R.string.alert_error_title),
                                getString(R.string.alert_no_repositories_found),
                                    () -> { finish(); return null; });
        }

        loadUserRepos(intent.getStringExtra(Constants.PROFILE_REPO_URL));
    }

    private void loadUserRepos(String url) {
        new Thread(() -> {
            String repos = CustomOkHttpClient.loadUserRepos(mContext, url);
            ArrayList<Repo> reposList = JsonParser.parseRepositories(repos);
            for (Repo current : reposList) {
                populateActivity(current.getName());
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
    private void showAlertMessage(String title, String message, Callable<Void> method) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setPositiveButton("OK", (dialog1, which) -> {
            if (method != null) {
                try {
                    method.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        AlertDialog alertDialog = dialog.create();
        runOnUiThread(alertDialog::show);
    }
}