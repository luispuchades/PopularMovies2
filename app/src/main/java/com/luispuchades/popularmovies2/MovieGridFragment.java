package com.luispuchades.popularmovies2;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.luispuchades.popularmovies2.adapters.MovieAdapter;
import com.luispuchades.popularmovies2.data.MoviesContract.MovieEntry;
import com.luispuchades.popularmovies2.models.Movie;
import com.luispuchades.popularmovies2.utils.Constants;
import com.luispuchades.popularmovies2.utils.EndlessRecyclerScrollListener;
import com.luispuchades.popularmovies2.utils.FetchMovieTask;
import com.luispuchades.popularmovies2.utils.NetworkUtilities;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieGridFragment extends Fragment implements
        View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    /* Tag for Log Messages */
    private static final String LOG_TAG = MovieGridFragment.class.getSimpleName();

    private Context mContext;
    private EndlessRecyclerScrollListener mScrollListener;
    private int mPage;
    private int mSorting;
    private static String mMovieLocale;
    private int mPosition = RecyclerView.NO_POSITION;
    private static MovieAdapter mMovieAdapter;
    private static final int ID_FAVORITES_LOADER = 69;

    public static FloatingActionsMenu mMenuMultipleActionsUp;

    /**
     * Extraction of Views
     */
    @BindView(R.id.rv_posters)
    RecyclerView mRecyclerView;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mPbLoadingIndicator;

    @BindView(R.id.tv_error_message_display)
    TextView mErrorMessageDisplay;

    @BindView(R.id.sr_swipe_container)
    SwipeRefreshLayout mSrSwipeContainer;

    @BindView(R.id.button_popular)
    FloatingActionButton mFabButtonPopular;

    @BindView(R.id.button_rated)
    FloatingActionButton mFabButtonRated;

    @BindView(R.id.button_favorites)
    FloatingActionButton mFabButtonFavorites;

    /**
     * Extraction of Strings
     */
    @BindString(R.string.settings_sorting_key)
    String mSettingsSortingKey;

    @BindString(R.string.settings_order_by_default)
    String mSettingsSortingDefault;

    @BindString(R.string.settings_order_by_most_popular_value)
    String mSettingsSortingPopularValue;

    @BindString(R.string.settings_order_by_top_rated_value)
    String mSettingsSortingRatedValue;

    @BindString(R.string.settings_favorites_value)
    String mSettingsSortingFavoritesValue;

    @BindString(R.string.settings_locale_key)
    String mSettingsLocaleKey;

    @BindString(R.string.settings_locale_default)
    String mSettingsLocaleDefault;


    public MovieGridFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Boolean errorShown = false;
        if (null != savedInstanceState) {
            errorShown = savedInstanceState.getBoolean(Constants.BUNDLE_ERROR_KEY);
        }

        // Inflate the layout for this fragment
        View rootView =
                inflater.inflate(R.layout.fragment_movie_grid, container, false);

        ButterKnife.bind(this, rootView);
        mContext = getContext();
        setUpSharedPreferences();

        if (null != savedInstanceState && !errorShown) {
            mPage = savedInstanceState.getInt(Constants.BUNDLE_PAGE_KEY);
            mSorting = savedInstanceState.getInt(Constants.BUNDLE_SORTING_KEY);
        } else {
            mPage = 1;
        }

        final int columns = getResources().getInteger(R.integer.grid_columns);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, columns,
                GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter();
        mRecyclerView.setAdapter(mMovieAdapter);

        if (mSorting != Constants.SORTING_FAVORITES) {
            mScrollListener = new EndlessRecyclerScrollListener(gridLayoutManager, mPage) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    mPage = page;
                    loadCards();
                }
            };

            mRecyclerView.addOnScrollListener(mScrollListener);
        }

        mSrSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mErrorMessageDisplay.setVisibility(View.INVISIBLE);
                clearGridView();
                loadCards();
            }
        });

        mSrSwipeContainer.setColorSchemeResources(R.color.colorAccent);

        mMenuMultipleActionsUp = rootView.findViewById(R.id.multiple_actions_up);
        mFabButtonPopular.setOnClickListener(this);
        mFabButtonRated.setOnClickListener(this);
        mFabButtonFavorites.setOnClickListener(this);

        if (null != savedInstanceState && !errorShown) {
            ArrayList<Movie> movieList = savedInstanceState.getParcelableArrayList(Constants
                    .BUNDLE_MOVIES_KEY);
            mMovieAdapter.setMoviesData(movieList);
        } else {
            loadCards();
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        List<Movie> movieList = mMovieAdapter.getMovieList();

        if (null != movieList) {
            outState.putParcelableArrayList(Constants.BUNDLE_MOVIES_KEY, new
                    ArrayList<Parcelable>(movieList));
            outState.putInt(Constants.BUNDLE_PAGE_KEY, mPage);
            outState.putInt(Constants.BUNDLE_SORTING_KEY, mSorting);
        } else if (mErrorMessageDisplay.isShown()) {
            outState.putBoolean(Constants.BUNDLE_ERROR_KEY, true);
        }
    }

    /**
     * setupSharedPreferences is the method that sets the different options within the
     * SharedPreferences
     */
    private void setUpSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences
                (mContext);

        String sorting = sharedPreferences.getString(mSettingsSortingKey, mSettingsLocaleDefault);

        if (sorting.equals(mSettingsSortingPopularValue)) {
            mSorting = Constants.SORTING_POPULAR;
        } else if (sorting.equals(mSettingsSortingRatedValue)) {
            mSorting = Constants.SORTING_RATED;
        } else if (sorting.equals(mSettingsSortingFavoritesValue)) {
            mSorting = Constants.SORTING_FAVORITES;
        }

        mMovieLocale = sharedPreferences.getString(mSettingsLocaleKey, mSettingsLocaleDefault);

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    }

    /**
     * Popular Movies 2 will handle the Floating Action Buttons from the clicks got through onClick.
     *
     * @param view the view associated to the fragment
     */
    @Override
    public void onClick(View view) {

        mErrorMessageDisplay.setVisibility(View.INVISIBLE);

        clearGridView();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences
                (mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        switch (view.getId()) {
            case R.id.button_popular:
                mSorting = Constants.SORTING_POPULAR;
                editor.putString(mSettingsSortingKey, mSettingsSortingPopularValue);
                break;

            case R.id.button_rated:
                mSorting = Constants.SORTING_RATED;
                editor.putString(mSettingsSortingKey, mSettingsSortingRatedValue);
                break;

            case R.id.button_favorites:
                mSorting = Constants.SORTING_FAVORITES;
                editor.putString(mSettingsSortingKey, mSettingsSortingFavoritesValue);
                break;

            default:
                break;
        }

        editor.apply();
        loadCards();
        mMenuMultipleActionsUp.collapse();
    }

    public void loadCards() {
        if (null != mSrSwipeContainer && mSrSwipeContainer.isRefreshing()) {
            mSrSwipeContainer.setRefreshing(false);
        }

        if (NetworkUtilities.isNetworkAvailable(mContext)) {
            switch (mSorting) {
                case Constants.SORTING_POPULAR:
                    new FetchMovieTask().execute(
                            new String[]{
                                    Constants.getTheMovieDbMethodPopular(),
                                    String.valueOf(mPage)
                            }
                    );
                    break;

                case Constants.SORTING_RATED:
                    new FetchMovieTask().execute(
                            new String[]{
                                    Constants.getTheMovieDbMethodTopRated(),
                                    String.valueOf(mPage)
                            }
                    );
                    break;

                case Constants.SORTING_FAVORITES:
                    LoaderManager loaderManager = getActivity().getSupportLoaderManager();
                    if (null != loaderManager.getLoader(ID_FAVORITES_LOADER)) {
                        loaderManager.initLoader(ID_FAVORITES_LOADER, null, this);
                    } else {
                        loaderManager.restartLoader(ID_FAVORITES_LOADER, null, this);
                    }
                    break;
            }
        } else {
            showErrorMessage(mContext, R.string.error_no_connectivity);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_FAVORITES_LOADER:
                Uri favoriteMovieUri = MovieEntry.CONTENT_URI;

                return new CursorLoader(mContext,
                        favoriteMovieUri,
                        Constants.FAVORITE_MOVIES_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("CursorLoader nor implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (null != cursor && cursor.getCount() != 0) {
            mMovieAdapter.loadCursorIntoAdapter(cursor);

            if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        } else {
            showErrorMessage(mContext, R.string.error_no_favorites);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.loadCursorIntoAdapter(null);
    }

    /**
     * Reset the GridView properties and adapter
     */
    public void clearGridView() {
        switch (mSorting) {
            case Constants.SORTING_POPULAR:
            case Constants.SORTING_RATED:
                mScrollListener.resetState();
                mPage = 1;
                mMovieAdapter.clearMovieList();
                break;
            case Constants.SORTING_FAVORITES:
                mMovieAdapter.loadCursorIntoAdapter(null);
        }
    }

    /**
     * Loads the error message into the TextView to show the message
     *
     * @param context   the current context
     * @param messageId the id of the error message
     */
    public void showErrorMessage(Context context, int messageId) {
        mErrorMessageDisplay.setText(context.getText(messageId));
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    /**
     * Getter method for getting the Movie Locale
     *
     * @return
     */
    public static String getMovieLocale() {
        return mMovieLocale;
    }

}