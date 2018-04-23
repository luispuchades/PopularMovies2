package com.luispuchades.popularmovies2.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.luispuchades.popularmovies2.adapters.VideoAdapter;
import com.luispuchades.popularmovies2.MovieGridFragment;
import com.luispuchades.popularmovies2.models.Video;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FetchVideoTask extends AsyncTask<String[], Void, List<Video>> {

    /* Tag for Log Messages */
    private static final String LOG_TAG = FetchVideoTask.class.getName();

    /* Query URL */
    private URL mUrlVideo;
    private String mUrlVideoString;
    private VideoAdapter mVideoAdapter;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Video> doInBackground(String[]... params) {
        String method = params[0][0];
        Map<String, String> mapping = new HashMap<>();

        mapping.put(Constants.getTheMovieDbLanguageQueryParam(), MovieGridFragment
                .getMovieLocale());

        mUrlVideo = NetworkUtilities.buildUrl(method, mapping);

        mUrlVideoString = mUrlVideo.toString();
        Log.i(LOG_TAG, "TEST: doInBackground called");
        Log.d(LOG_TAG, "mUrlVideoString: " + mUrlVideoString);
        if (mUrlVideoString == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of videos.
        return MovieJsonUtils.fetchVideoData(mUrlVideoString);
    }

    @Override
    protected void onPostExecute(List<Video> videos) {
        if (!(videos.isEmpty())){
            mVideoAdapter.setMoviesData(videos);
        }
    }
}
