package com.github.timeline.Utils;

import android.util.Log;

public class GLog {
    private static final String TAG = "[GitHubTimeline]";

    public static void d(String tag, String message) {
        Log.d(TAG, tag + ": " + message);
    }

    public static void e(String tag, String message) {
        Log.e(TAG, tag + ": " + message);
    }
}
