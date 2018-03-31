package com.luispuchades.popularmovies2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luispuchades.popularmovies2.utils.Constants;
import com.squareup.picasso.Picasso;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MovieActivity extends AppCompatActivity {

    private String mMovieTitle;
    private String mMoviePosterPath;

    /**
     *****************
     * Binding views *
     *****************
     */

    @BindView(R.id.title_tv)
    TextView titleTv;

    @BindView(R.id.vote_average_tv)
    TextView voteAverageTv;

    @BindView(R.id.release_date_tv)
    TextView releaseDateTv;

    @BindView(R.id.overview_tv)
    TextView overviewTv;

    @BindView(R.id.poster_iv)
    ImageView posterIv;

    /**
     *********************
     * Binding Resources *
     *********************
     */

    @BindString(R.string.no_data)
    String noData;

    @BindString(R.string.detail_error_message)
    String detailErrorMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        // Initiation of ButterKnife to bind views
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        Movie mIntentExtraMovie = null;
        if (intent != null) {
            mIntentExtraMovie = intent.getParcelableExtra(Constants.EXTRA_MOVIE);
        }

        if (mIntentExtraMovie != null) {
            populateUI(mIntentExtraMovie);
        }

        Picasso.with(this)
                .load(Constants.THEMOVIEDB_POSTER_PATH_BASE_URL +
                        Constants.THEMOVIEDB_POSTER_PHONE_SIZE + mMoviePosterPath)
                .into(posterIv);

        setTitle(mMovieTitle);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, detailErrorMessage, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Movie currentMovie) {

        // Set text to views
        mMovieTitle = currentMovie.getMovieTitle();
        Double mMovieVoteAverage = currentMovie.getMovieVoteAverage();
        String mMovieReleaseDate = currentMovie.getMovieReleaseDate();
        String mMovieOverview = currentMovie.getMovieOverview();
        mMoviePosterPath = currentMovie.getMoviePosterPath();

        titleTv.setText(checkData(mMovieTitle));

        String voteAverageString = String.valueOf(mMovieVoteAverage);
        voteAverageTv.setText(checkData(voteAverageString));

        releaseDateTv.setText(checkData(mMovieReleaseDate));
        overviewTv.setText(checkData(mMovieOverview));
    }

    private String checkData(String string) {
        if (string.equals("")) {
            return noData;
        } else {
            return string;
        }
    }

}