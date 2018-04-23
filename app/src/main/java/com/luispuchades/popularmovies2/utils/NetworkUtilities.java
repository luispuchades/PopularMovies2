package com.luispuchades.popularmovies2.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public final class NetworkUtilities {

    /* Tag for Log Messages */
    private static final String LOG_TAG = NetworkUtilities.class.getSimpleName();

    /**
     * A method to check if it exists internet connection.
     *
     * @param context the application context
     * @return true or false
     */
    static public boolean isNetworkAvailable(Context context) {

        /* Get a reference to the ConnectivityManager to check state of network connectivity */
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        /* Get details on the currently active default data network */
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
         return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * A method to build the URL with a Map of keys
     *
     * @param method method to append
     * @param params a Map with the key values to append
     * @return the URL for the request to The Movie DB API service.
     */
    public static URL buildUrl(String method, Map<String, String> params) {

        Uri.Builder builder = Uri.parse(Constants.THEMOVIEDB_BASE_URL + method).buildUpon();
        Log.v(LOG_TAG, "Parse url '" + Constants.THEMOVIEDB_BASE_URL + "' with method '" +
                method + "'");

        builder.appendQueryParameter(Constants.THEMOVIEDB_ENDPOINT_API_KEY, Constants
                .THEMOVIEDB_API_KEY);
        Log.v(LOG_TAG, "Append api key");

        for (Map.Entry<String, String> param : params.entrySet()) {

            builder.appendQueryParameter(param.getKey(), param.getValue());
            Log.v(LOG_TAG, "Append param '" + param.getKey() + "' with value '"
                    + param.getValue() + "'");
        }

        Uri uri = builder.build();
        URL url = null;

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(LOG_TAG, "Built URI " + url);

        return url;
    }

}