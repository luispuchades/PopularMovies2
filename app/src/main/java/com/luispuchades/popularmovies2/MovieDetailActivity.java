package com.luispuchades.popularmovies2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.luispuchades.popularmovies2.utils.Constants;

/**
 * Activity that displays the Movie details.
 */
public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(Constants.PARCELABLE_MOVIE_KEY, getIntent()
                    .getParcelableExtra(Constants.PARCELABLE_MOVIE_KEY));

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }
    }
}
