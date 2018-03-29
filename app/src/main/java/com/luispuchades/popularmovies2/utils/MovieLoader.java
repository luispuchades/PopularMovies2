package com.luispuchades.popularmovies2.utils;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.luispuchades.popularmovies2.Movie;

import java.util.List;

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {

    /* Tag for Log Messages */
    private static final String LOG_TAG = MovieLoader.class.getName();

    /* Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link MovieLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public MovieLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "TEST: onStartedLoading called");
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Movie> loadInBackground() {

        Log.i(LOG_TAG, "TEST: loadInBackground called");
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of movies.
        return MovieJsonUtils.fetchMovieData(mUrl);
    }
}
