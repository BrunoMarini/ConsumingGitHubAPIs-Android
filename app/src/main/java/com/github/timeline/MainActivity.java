package com.github.timeline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.timeline.Utils.Constants;
import com.github.timeline.Utils.GLog;
import com.github.timeline.serverCommunication.CustomOkHttpClient;
import com.github.timeline.userInfo.UserProfileActivity;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.HttpURLConnection;

public class MainActivity extends AppCompatActivity implements Callback {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Context mContext;
    private Callback mCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        mCallback = this;

        EditText etProfile = findViewById(R.id.et_profile_name);

        Button btnSearch = findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String profileName = etProfile.getText().toString();
                if (profileName.isEmpty()) {
                    profileName = "BrunoMarini";
                }
                CustomOkHttpClient.searchForProfile(mContext, mCallback, profileName);
            }
        });
    }

    @Override
    public void onResponse(Response response) throws IOException {
        GLog.d(TAG, "onResponse()");
        if (response.code() == HttpURLConnection.HTTP_OK) {
            Intent intent = new Intent(mContext, UserProfileActivity.class);
            intent.putExtra(Constants.EXTRA_USER_PROFILE_RESPONSE, response.body().string());
            startActivity(intent);
        } else {
            GLog.d(TAG, "Server returned error: " + response.code());
        }
    }

    @Override
    public void onFailure(Request request, IOException e) {
        GLog.d(TAG, "onFailure() " + e.getMessage());
        }
    }