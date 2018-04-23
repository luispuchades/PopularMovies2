package com.luispuchades.popularmovies2.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.luispuchades.popularmovies2.adapters.ReviewAdapter;
import com.luispuchades.popularmovies2.MovieGridFragment;
import com.luispuchades.popularmovies2.models.Review;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FetchReviewTask extends AsyncTask<String[], Void, List<Review>> {

    /* Tag for Log Messages */
    private static final String LOG_TAG = FetchVideoTask.class.getName();

    /* Query URL */
    private URL mUrlReview;
    private String mUrlReviewString;
    private ReviewAdapter mReviewAdapter;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Review> doInBackground(String[]... params) {
        String method = params[0][0];
        Map<String, String> mapping = new HashMap<>();

        mapping.put(Constants.getTheMovieDbLanguageQueryParam(), MovieGridFragment
                .getMovieLocale());

        mUrlReview = NetworkUtilities.buildUrl(method, mapping);

        mUrlReviewString = mUrlReview.toString();
        Log.i(LOG_TAG, "TEST: doInBackground called");
        Log.d(LOG_TAG, "mUrlVideoString: " + mUrlReviewString);
        if (mUrlReviewString == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of videos.
        return MovieJsonUtils.fetchReviewData(mUrlReviewString);
    }

    @Override
    protected void onPostExecute(List<Review> reviews) {
        if (!(reviews.isEmpty())){
            mReviewAdapter.setReviewsData(reviews);
        }
    }
}