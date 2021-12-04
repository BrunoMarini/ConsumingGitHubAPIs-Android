package com.github.timeline.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {
    public static final String TAG = JsonParser.class.getSimpleName();

    public static class UserProfile {
        public static String getAvatarUrl(String json) {
            return getJsonProperty(json, Constants.PROFILE_AVATAR_URL);
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
