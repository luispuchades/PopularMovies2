package com.luispuchades.popularmovies2.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.luispuchades.popularmovies2.adapters.MovieAdapter;
import com.luispuchades.popularmovies2.MovieGridFragment;
import com.luispuchades.popularmovies2.models.Movie;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FetchMovieTask extends AsyncTask<String[], Void, List<Movie>> {

    /* Tag for Log Messages */
    private static final String LOG_TAG = FetchMovieTask.class.getName();

    /* Query URL */
    private URL mUrlMovie;
    private String mUrlMovieString;
    private MovieAdapter mMovieAdapter;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Movie> doInBackground(String[]... params) {
        String method = params[0][0];
        Map<String, String> mapping = new HashMap<>();

        mapping.put(Constants.getTheMovieDbLanguageQueryParam(), MovieGridFragment
                .getMovieLocale());

        mUrlMovie = NetworkUtilities.buildUrl(method, mapping);

        mUrlMovieString = mUrlMovie.toString();
        Log.i(LOG_TAG, "TEST: doInBackground called");
        Log.d(LOG_TAG, "mUrlVideoString: " + mUrlMovieString);
        if (mUrlMovieString == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of videos.
        return MovieJsonUtils.fetchMovieData(mUrlMovieString);
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        if (!(movies.isEmpty())){
            mMovieAdapter.setMoviesData(movies);
        }
    }
}
