package com.luispuchades.popularmovies2.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.luispuchades.popularmovies2.MainActivity;

import java.net.MalformedURLException;
import java.net.URL;

public class ConnectionUnit {

    /* Tag for Log Messages */
    private static final String LOG_TAG = ConnectionUnit.class.getSimpleName();

    static public boolean isNetworkAvailable(Context context) {

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
         return networkInfo != null && networkInfo.isConnected();
    }

    // TODO: CHECK - BORRAR
/*    public static String buildURL(String orderBy) {
        String uri = null;

        Uri baseUri = Uri.parse(Constants.THEMOVIEDB_BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendPath(orderBy);
        uriBuilder.appendQueryParameter(Constants.THEMOVIEDB_ENDPOINT_API_KEY,
                Constants.THEMOVIEDB_API_KEY);

        uri = baseUri.toString();
        Log.v(LOG_TAG, "Built URI " + uri);
        return uri;
    }*/
}
