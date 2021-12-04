package com.github.timeline.Utils;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonParser {
    public static final String TAG = JsonParser.class.getSimpleName();

    public static class UserProfile {
        public static String getAvatarUrl(String json) {
            return getJsonProperty(json, Constants.PROFILE_AVATAR_URL);
        }

        public static String getReposUrl(String json) {
            return getJsonProperty(json, Constants.PROFILE_REPOS_URL);
        }

        public static String getName(String json) {
            return getJsonProperty(json, Constants.PROFILE_NAME);
        }

        public static String getLogin(String json) {
            return getJsonProperty(json, Constants.PROFILE_LOGIN);
        }

        public static String getPublicRepo(String json) {
            return getJsonProperty(json, Constants.PROFILE_PUBLIC_REPOS);
        }

        public static String getBio(String json) {
            return getJsonProperty(json, Constants.PROFILE_BIO);
        }

        public static Bundle getAllInfo(String json) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.PROFILE_AVATAR_URL, getAvatarUrl(json));
            bundle.putString(Constants.PROFILE_REPOS_URL, getReposUrl(json));
            bundle.putString(Constants.PROFILE_NAME, getName(json));
            bundle.putString(Constants.PROFILE_LOGIN, getLogin(json));
            bundle.putString(Constants.PROFILE_PUBLIC_REPOS, getPublicRepo(json));
            bundle.putString(Constants.PROFILE_BIO, getBio(json));
            return bundle;
        }
    }

    public static class UserRepos {
        public static String getName(String json) {
            return getJsonProperty(json, Constants.REPO_NAME);
        }

        public static ArrayList<String> getAllRepoName(String json) {
            ArrayList<String> repoNames = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    repoNames.add(getName(jsonArray.get(i).toString()));
                }
            } catch (JSONException e) {
                GLog.e(TAG, "Error parsing JSONArray! " + e.getMessage());
                e.printStackTrace();
            }
            return repoNames;
        }
    }

    private static String getJsonProperty(String json, String property) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.getString(property);
        } catch (JSONException e) {
            GLog.e(TAG, "Error parsing JSON! " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
