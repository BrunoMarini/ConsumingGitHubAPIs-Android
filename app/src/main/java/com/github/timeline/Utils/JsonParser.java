package com.github.timeline.Utils;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

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
            bundle.putString(Constants.PROFILE_REPOS_URL, getPublicRepo(json));
            bundle.putString(Constants.PROFILE_NAME, getName(json));
            bundle.putString(Constants.PROFILE_LOGIN, getLogin(json));
            bundle.putString(Constants.PROFILE_PUBLIC_REPOS, getPublicRepo(json));
            bundle.putString(Constants.PROFILE_BIO, getBio(json));
            return bundle;
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
