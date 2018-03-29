package com.luispuchades.popularmovies2;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.luispuchades.popularmovies2.utils.Constants;
import com.luispuchades.popularmovies2.utils.MovieLoader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Movie>>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    /* Tag for Log Messages */
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    /**
     * Constant value for the movie loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int MOVIE_LOADER_ID = 1;

    /** Adapter for the list of movies */
    private MovieAdapter mAdapter;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    /** GridView for movie posters*/
    private GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link GridView} in the layout
        mGridView = findViewById(R.id.movies_gv);

        // If the list of movies is empty then setEmptyView
        mEmptyStateTextView = findViewById(R.id.empty_view);
        mGridView.setEmptyView(mEmptyStateTextView);

        mAdapter = new MovieAdapter(this, new ArrayList<Movie>());

        // Set the adapter on the {@link GridView}
        // so the list can be populated in the user interface
        mGridView.setAdapter(mAdapter);

        // Obtain a reference to the SharedPreferences file for this app
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // And register to be notified of preference changes
        // So ww know when the user has adjusted the query settings
        prefs.registerOnSharedPreferenceChangeListener(this);

        // Set an item click listener on the GridView, which sends an intent to a new activity
        // with detailed information about the choosen film
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current movie that was clicked on
                Movie currentMovie = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                // Uri movieUri = Uri.parse(currentMovie.getMoviePosterPath());
                launchMovieActivity(currentMovie);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    private void launchMovieActivity(Movie movie) {
        // Create a new intent to view the movie details
        Intent movieIntent = new Intent(getApplicationContext(), MovieActivity.class);

        // Pass current movie information to the new activity calles MovieActivity
        movieIntent.putExtra(Constants.EXTRA_MOVIE, movie);

        // Send the intent to launch a new activity
        startActivity(movieIntent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.settings_order_by_key))) {
            //Clear the ListView as a new query will be kicked off
            mAdapter.clear();

            //Hide the empty state text view as the loading indicator will be displayed.
            mEmptyStateTextView.setVisibility(View.GONE);

            //Show the loading indicator while new data is being fetched
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.VISIBLE);

            //Restart the loader to require THEMOVIEDB as the query settings have been updated
            getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
        }
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle args) {

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);

        String orderBy = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(Constants.THEMOVIEDB_BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendPath(orderBy);
        uriBuilder.appendQueryParameter(Constants.THEMOVIEDB_ENDPOINT_API_KEY,
                Constants.THEMOVIEDB_API_KEY);

        return new MovieLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No movies found."
        mEmptyStateTextView.setText(R.string.no_movies);


        // Clear the adapter of previous movie data
        mAdapter.clear();

        // If there is a valid list of {@link Movie}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (movies != null && !movies.isEmpty()) {
            Log.d(LOG_TAG, "Aquí llega 1");
            mAdapter.addAll(movies);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.main, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if( id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
