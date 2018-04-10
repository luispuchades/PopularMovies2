package com.luispuchades.popularmovies2;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.luispuchades.popularmovies2.models.Movie;
import com.luispuchades.popularmovies2.utils.ConnectionUnit;
import com.luispuchades.popularmovies2.utils.Constants;
import com.luispuchades.popularmovies2.utils.MovieLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Movie>>,
        MovieAdapter.ItemClickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener {

    /* Tag for Log Messages */
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    /**
     * Constant value for the movie loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int MOVIE_LOADER_ID = 1;

    private RecyclerView mRecyclerView;
    /** Adapter for the list of movies */
    private MovieAdapter mMovieAdapter;
    private List<Movie> mMovies;

    // TODO: CHECK - BORRAR
    // private ProgressBar mLoadingIndicator;

    // TODO: CHECK
    private int mPosition = RecyclerView.NO_POSITION;


    /**
     *****************
     * Binding views *
     *****************
     */

    @BindView(R.id.empty_view)
    TextView mEmptyStateTextView;

    @BindView(R.id.loading_indicator)
    View mLoadingIndicator;

    // GridView for movie posters
    // @BindView(R.id.movies_rv)
    // RecyclerView mRecyclerView;

    /**
     *********************
     * Binding Resources *
     *********************
     */

    @BindString(R.string.no_internet_connection)
    String noInternetConnection;

    @BindString(R.string.no_movies)
    String noMovies;

    @BindString(R.string.settings_order_by_key)
    String settingsOrderByKey;

    @BindString(R.string.settings_order_by_default)
    String settingsOrderByDefault;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initiation of ButterKnife to bind views
        ButterKnife.bind(this);

        mRecyclerView = findViewById(R.id.movies_rv);

        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         *
         * Please note: This so called "ProgressBar" isn't a bar by default. It is more of a
         * circle.
         */
        mLoadingIndicator = findViewById(R.id.loading_indicator);

        // TODO: CHECK
        // mLayoutManager = new GridLayoutManager();

        // If the list of movies is empty then setEmptyView
        // TODO: CHECK
        // mRecyclerView.setEmptyView(mEmptyStateTextView);

        /*
         * A GridLayoutManager is responsible for measuring and positioning item views within a
         * RecyclerView into a grid list.
         *
         * The third parameter (shouldReverseLayout) should be true if you want to reverse your
         * layout. Generally, this is only true with horizontal lists that need to support a
         * right-to-left layout.
         */
        // TODO: CHECK
/*        GridLayoutManager LayoutManager = new GridLayoutManager(this, 2,
                GridLayoutManager.VERTICAL, false);*/
        int columnsNumber = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, columnsNumber);

        /* setLayoutManager associates the LayoutManager we created above with our RecyclerView */
        mRecyclerView.setLayoutManager(gridLayoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         *
         * Use setHasFixedSize(true) on mRecyclerView to designate that all items in the list
         * will have the same size
         */
        mRecyclerView.setHasFixedSize(true);

        /*
         * The MovieAdapter is responsible for linking our movies data with the Views that
         * will end up displaying our movie data.
         *
         * Although passing in "this" twice may seem strange, it is actually a sign of separation
         * of concerns, which is best programming practice. The MovieAdapter requires an
         * Android Context (which all Activities are) as well as an onClickHandler. Since our
         * MainActivity implements the MovieAdapter MovieOnClickHandler interface, "this"
         * is also an instance of that type of handler.
         */
        mMovieAdapter = new MovieAdapter(this, new ArrayList<Movie>(),this);

        // Set the adapter on the {@link GridView}
        // so the list can be populated in the user interface
        /* Setting the adapter attaches it to the RecyclerView in our layout. */

        /*
         * Set the adapter on the {@link GridView}
         * so the list can be populated in the user interface
         * Setting the adapter attaches it to the RecyclerView in our layout.
         */
        mRecyclerView.setAdapter(mMovieAdapter);

/*        if ( savedInstanceState == null || !savedInstanceState.containsKey("Movies")) {
            mMovies = null;
            loadMovieData();
        } else {
            mMovies = savedInstanceState.getParcelableArrayList("Movies");
            showMovieData();
            mMovieAdapter.setMovies(mMovies);
        }*/

        // TODO: CHECK
        showMovieLoading();

        /*
         * Ensures a loader is initialized and active. If the loader doesn't already exist, one is
         * created and (if the activity/fragment is currently started) starts the loader. Otherwise
         * the last created loader is re-used.
         */

        // TODO: CHECK - BORRAR
        // getLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);

        // TODO: HASTA AQUI IMPLEMENTACION DE RECYCLERVIEW CORRECTA

        // Obtain a reference to the SharedPreferences file for this app
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // And register to be notified of preference changes
        // So we know when the user has adjusted the query settings
        prefs.registerOnSharedPreferenceChangeListener(this);

        // TODO: CHECK
        // Set an item click listener on the GridView, which sends an intent to a new activity
        // with detailed information about the choosen film
/*
        mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current movie that was clicked on
                Movie currentMovie = mAdapter.getItem(position);

                launchMovieActivity(currentMovie);
            }
        });
*/

        // If there is a network connection, fetch data
        if (ConnectionUnit.isNetworkAvailable(this)){
            // Get a reference to the LoaderManager, in order to interact with loaders.
            // TODO: CHECK
            LoaderManager loaderManager = getLoaderManager();

            // android.app.LoaderManager loaderManager = getLoaderManager();
            //LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            // TODO: CHECK - BORRAR
            // loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
            loadMovieData();
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            mLoadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(noInternetConnection);
        }
    }

    /**
     * This method calls showMovieData() then run the AsycnTask to load the data from the API.
     */
    // TODO: CHECK
    private void loadMovieData() {
        showMovieData();

        LoaderManager loaderManager = getLoaderManager();

        loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
        // new FetchMovieDataTask().execute();
    }

/*    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("Movies", (ArrayList<? extends Parcelable>) mMovies);
    }*/

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(settingsOrderByKey)) {
            //Clear the ListView as a new query will be kicked off
            //mAdapter.clear();

            //Hide the empty state text view as the loading indicator will be displayed.
            mEmptyStateTextView.setVisibility(View.GONE);

            //Show the loading indicator while new data is being fetched
            mLoadingIndicator.setVisibility(View.VISIBLE);

            //Restart the loader to require THEMOVIEDB as the query settings have been updated
            getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
            //getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
        }
    }


    @Override
    public Loader<List<Movie>> onCreateLoader(int loaderId, Bundle args) {

        switch (loaderId) {

            case MOVIE_LOADER_ID:
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(this);

                String orderBy = sharedPreferences.getString(
                        settingsOrderByKey,
                        settingsOrderByDefault
                );

                Uri baseUri = Uri.parse(Constants.THEMOVIEDB_BASE_URL);
                Uri.Builder uriBuilder = baseUri.buildUpon();

                uriBuilder.appendPath(orderBy);
                uriBuilder.appendQueryParameter(Constants.THEMOVIEDB_ENDPOINT_API_KEY,
                        Constants.THEMOVIEDB_API_KEY);

                Log.d(LOG_TAG, "CHECK Movieloader");
                return new MovieLoader(this, uriBuilder.toString());

            default:
                throw new RuntimeException("Loader Not Implemented; " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        // TODO: CHECK
        // Hide loading indicator because the data has been loaded
        // View loadingIndicator = findViewById(R.id.loading_indicator);
        // loadingIndicator.setVisibility(View.GONE);

        // TODO: CHECK
        // Set empty state text to display "No movies found."
        // mEmptyStateTextView.setText(noMovies);

        // TODO: CHECK
        // Clear the adapter of previous movie data
        // mAdapter.clear();

        // TODO: CHECK
        // If there is a valid list of {@link Movie}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
/*        if (movies != null && !movies.isEmpty()) {
            Log.d(LOG_TAG, "CHECK_1:");
            // TODO: CHECK
            // mAdapter.addAll(movies);
        }*/

        // TODO: ADD MOVIES
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);
        if (movies.size() != 0) showMovieData();
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        /*
         * Since this Loader's data is now invalid, we need to clear the Adapter that is
         * displaying the data.
         * Loader reset, so we can clear out our existing data.
         */
         // mMovieAdapter.clear();
    }

    /**
     * This method will make the View for the movie data visible and hide the error message and
     * loading indicator.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't need to check whether
     * each view is currently visible or invisible.
     */
    private void showMovieData() {
        /* First, hide the loading indicator */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Finally, make sure the movie is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the loading indicator visible and hide the movie View and error
     * message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't need to check whether
     * each view is currently visible or invisible.
     */
    private void showMovieLoading() {
        /* Then, hide the movie data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
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

    @Override
    public void onClick(Movie movie) {

    }
}
