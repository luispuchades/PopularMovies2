package com.luispuchades.popularmovies2.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.luispuchades.popularmovies2.R;
import com.luispuchades.popularmovies2.MovieGridFragment;
import com.luispuchades.popularmovies2.models.Movie;
import com.luispuchades.popularmovies2.utils.MoviePosterCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    /* Tag for Log Messages */
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private Context mContext;
    private List<Movie> mMovieList;
    private FragmentManager mFragmentManager;
    private String[] mTwoPaneConfigurations;

    /**
     * Extraction of Strings
     */
    @BindString(R.string.selected_configuration)
    String mSelectedConfiguration;

    @BindString(R.string.large)
    String mLarge;

    @BindString(R.string.large_land)
    String mLargeLand;

    @BindString(R.string.xlarge)
    String mXlarge;

    @BindString(R.string.xlarge_land)
    String mXlargeLand;


    /**
     * Include INDEX for table columns
     */
    private static final int INDEX_MOVIE_ID = 0;
    private static final int INDEX_TITLE = 1;
    private static final int INDEX_RATING = 2;
    private static final int INDEX_RELEASE_DATE = 3;
    private static final int INDEX_OVERVIEW = 4;
    private static final int INDEX_POSTER_PATH = 5;
    private static final int INDEX_BACKDROP_PATH = 6;

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder {
        /**
         * Extraction of Views
         */
        @BindView(R.id.cv_movie)
        CardView mCvMovie;

        @BindView(R.id.iv_movie_poster)
        ImageView mIvMoviePoster;

        @BindView(R.id.pb_movie_poster)
        public ProgressBar mPbMoviePoster;

        @BindView(R.id.tv_movie_poster_error)
        public TextView mTvMoviePosterError;

        @BindView(R.id.tv_movie_title)
        TextView mTvMovieTitle;

        /**
         * Constructor to the ViewHolder class
         *
         * @param itemView        view that will be inflated
         * @param fragmentManager manages the fragment to inflate
         */
        MovieAdapterViewHolder(View itemView, FragmentManager fragmentManager) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mTwoPaneConfigurations = new String[]{
                    mLarge, mLargeLand,
                    mXlarge, mXlargeLand,
            };

            mContext = itemView.getContext();
            FragmentManager mFragmentManager = fragmentManager;
        }

        /**
         * Checks if the app is using a two pane layout or not
         *
         * @param selectedConfiguration the selected string of the configuration
         * @return true or false
         */
        private boolean checkTwoPane(String selectedConfiguration) {
            return Arrays.asList(mTwoPaneConfigurations).contains(selectedConfiguration);
        }
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (like ours does) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new ForecastAdapterViewHolder that holds the View for each list item
     */
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // TODO: CHECH IF CONTEXT CAN BE USED mContext
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.card_movies;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup,
                shouldAttachToParentImmediately);

        // TODO: CHECK OPTION OR ERASE
        view.setFocusable(true);

        return new MovieAdapterViewHolder(view,
                ((FragmentActivity) context).getSupportFragmentManager());
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the movie
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param movieAdapterViewHolder The ViewHolder which should be updated to represent the
     *                               contents of the item at the given position in the data set.
     * @param position               The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(final MovieAdapterViewHolder movieAdapterViewHolder, int
            position) {

        // Find the movie at the given position in the list of movies
        final Movie currentMovie = mMovieList.get(position);

        // Capture movie path
        if (currentMovie.getMoviePosterPath() != null) {
            String moviePosterPath = currentMovie.getMoviePosterPath();
            String movieTitle = currentMovie.getMovieTitle();
            Log.d(LOG_TAG, moviePosterPath);

            // TODO: CHECK mContext
            Picasso.with(mContext)
                    .load(moviePosterPath)
                    .resize(mContext.getResources().getInteger(R.integer
                                    .themoviedb_poster_w185_width),
                            mContext.getResources().getInteger(R.integer.themoviedb_poster_w185_height))
                    .placeholder(R.drawable.poster_placeholder)
                    .error(R.drawable.poster_placeholder_error)
                    .into(movieAdapterViewHolder.mIvMoviePoster, new MoviePosterCallback(movieAdapterViewHolder));

            movieAdapterViewHolder.mTvMovieTitle.setText(movieTitle);

            movieAdapterViewHolder.mCvMovie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MovieGridFragment.mMenuMultipleActionsUp.isExpanded()) {
                        MovieGridFragment.mMenuMultipleActionsUp.collapse();
                    }
                }
            });
        }
    }

    /**
     * Method for loading the new cursor into the adapter
     *
     * @param newCursor the new cursor that will be loaded into the adapter.
     */
    public void loadCursorIntoAdapter(Cursor newCursor) {
        if (null != newCursor) {

            newCursor.moveToFirst();

            List<Movie> movieList = new ArrayList<>();

            try {
                do {
                    movieList.add(createMovieFromCursor(newCursor));
                } while (newCursor.moveToNext());
            } finally {
                newCursor.close();
            }
            mMovieList = new ArrayList<>(movieList);
        } else {
            clearMovieList();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (null != mMovieList) {
            int i = mMovieList.size();
            Log.d(LOG_TAG, "Movielist Size: " + i);

            return i;
        }
        return 0;
    }

    /**
     * Reset the Movie List
     */
    public void clearMovieList() {
        if (null != mMovieList) {
            mMovieList.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * Setter method for assigning the movies got to the mMovieList variable
     *
     * @param movies
     */
    public void setMoviesData(List<Movie> movies) {
        if (null == movies) {
            mMovieList = movies;
        } else {
            mMovieList.addAll(movies);
        }
        notifyDataSetChanged();
    }

    /**
     * Getter method por getting the Movie List
     *
     * @return the Movie List
     */
    public List<Movie> getMovieList() {
        return mMovieList;
    }

    private Movie createMovieFromCursor(Cursor cursor) {
        return new Movie(
                cursor.getInt(INDEX_MOVIE_ID),
                cursor.getString(INDEX_TITLE),
                cursor.getString(INDEX_RATING),
                cursor.getString(INDEX_RELEASE_DATE),
                cursor.getString(INDEX_OVERVIEW),
                cursor.getString(INDEX_POSTER_PATH),
                cursor.getString(INDEX_BACKDROP_PATH)
        );
    }

}


