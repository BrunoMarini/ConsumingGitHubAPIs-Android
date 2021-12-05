package com.github.timeline.serverCommunication;

import com.github.timeline.Utils.GLog;
import com.github.timeline.serverCommunication.responseJson.Profile;
import com.github.timeline.serverCommunication.responseJson.Repo;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class JsonParser {
    public static final String TAG = JsonParser.class.getSimpleName();

    public static Profile parseProfile(String json) {
        GLog.d(TAG, "getProfileInfo()");
        return (new Gson()).fromJson(json, Profile.class);
    }

    public static ArrayList<Repo> parseRepositories(String json) {
        ArrayList<Repo> repos = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            Gson gson = new Gson();
            for (int i = 0; i < jsonArray.length(); i++) {
                repos.add(gson.fromJson(jsonArray.get(i).toString(), Repo.class));
            }
        } catch (JSONException e) {
            GLog.e(TAG, "Error parsing JSONArray! " + e.getMessage());
            e.printStackTrace();
        }
        return repos;
    }
}
