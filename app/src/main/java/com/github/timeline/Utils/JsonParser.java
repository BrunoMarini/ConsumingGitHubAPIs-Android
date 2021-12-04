package com.github.timeline.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {
    public static final String TAG = JsonParser.class.getSimpleName();

    public static class UserProfile {
        public static String getAvatarUrl(String json) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                return jsonObject.getString("avatar_url");
            } catch (JSONException e) {
                GLog.e(TAG, "Error parsing response JSON! " + e.getMessage());
            }
            return null;
        }
    }
}
