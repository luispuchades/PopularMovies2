package com.luispuchades.popularmovies2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luispuchades.popularmovies2.utils.Constants;
import com.squareup.picasso.Picasso;


public class MovieActivity extends AppCompatActivity {

    private TextView titleTv;
    private TextView voteAverageTv;
    private TextView releaseDateTv;
    private TextView overviewTv;
    private ImageView posterIv;

    private String mMovieTitle;
    private Double mMovieVoteAverage;
    private String mMovieReleaseDate;
    private String mMovieOverview;
    private String mMoviePosterPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        Movie mIntentExtraMovie = intent.getParcelableExtra(Constants.EXTRA_MOVIE);

        populateUI(mIntentExtraMovie);
        Picasso.with(this)
                .load(Constants.THEMOVIEDB_POSTER_PATH_BASE_URL +
                        Constants.THEMOVIEDB_POSTER_PHONE_SIZE + mMoviePosterPath)
                .into(posterIv);

        setTitle(mMovieTitle);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Movie currentMovie) {

        // Capture of views from movies_list_item.xml
        titleTv = findViewById(R.id.title_tv);
        voteAverageTv = findViewById(R.id.vote_average_tv);
        releaseDateTv = findViewById(R.id.release_date_tv);
        overviewTv = findViewById(R.id.overview_tv);
        posterIv = findViewById(R.id.poster_iv);

        // Set text to views
        mMovieTitle = currentMovie.getMovieTitle();
        mMovieVoteAverage = currentMovie.getMovieVoteAverage();
        mMovieReleaseDate = currentMovie.getMovieReleaseDate();
        mMovieOverview = currentMovie.getMovieOverview();
        mMoviePosterPath = currentMovie.getMoviePosterPath();


        titleTv.setText(checkData(mMovieTitle));

        String voteAverageString = String.valueOf(mMovieVoteAverage);
        voteAverageTv.setText(checkData(voteAverageString));

        releaseDateTv.setText(checkData(mMovieReleaseDate));
        overviewTv.setText(checkData(mMovieOverview));
    }

    private String checkData(String string) {
        if (string.equals("")) {
            return getString(R.string.no_data);
        } else {
            return string;
        }
    }

}