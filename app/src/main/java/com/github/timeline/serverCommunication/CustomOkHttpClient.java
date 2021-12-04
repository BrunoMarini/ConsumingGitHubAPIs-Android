package com.github.timeline.serverCommunication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

import com.github.timeline.Utils.Constants;
import com.github.timeline.Utils.GLog;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL;

public class CustomOkHttpClient {
    private static final String TAG = CustomOkHttpClient.class.getSimpleName();

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static boolean searchForProfile(Context context, Callback callback, String profile) {
        if (!isNetworkAvailable(context)) return false;

        HttpUrl url = new HttpUrl.Builder()
                .scheme(Constants.SERVER_SCHEME_HTTPS)
                .host(Constants.SERVER_HOST)
                .addPathSegment(Constants.SERVER_PATH_USER)
                .addPathSegment(profile)
                .build();

        buildAndEnqueueAsyncRequest(url.toString(), callback);
        return true;
    }

    public static InputStream loadAvatar(Context context, String url) {
        if (!isNetworkAvailable(context)) return null;
        try {
            return buildAndEnqueueSyncRequest(url).body().byteStream();
        } catch (IOException | NullPointerException e) {
            GLog.e(TAG, "Error loading image!: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private static Response buildAndEnqueueSyncRequest(String url) {
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();
            return okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            GLog.e(TAG, "Error performing sync request: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private static void buildAndEnqueueAsyncRequest(String url, Callback callback) {
        GLog.d(TAG, "buildAndEnqueueAsyncRequest(): " + url);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(callback);
    }

    private static Boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Network nw = connectivityManager.getActiveNetwork();
            if (nw == null)
                return false;
            NetworkCapabilities netCap = connectivityManager.getNetworkCapabilities(nw);
            if (netCap == null) {
                GLog.d(TAG, "NetworkCapabilities null!");
                return false;
            }

            boolean hasConnection = (netCap.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                        netCap.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                            netCap.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                                                netCap.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
            GLog.d(TAG, "Has network connection: " + hasConnection);
            return hasConnection;
    }
}
