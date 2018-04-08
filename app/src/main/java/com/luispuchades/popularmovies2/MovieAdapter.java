package com.luispuchades.popularmovies2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.luispuchades.popularmovies2.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    /* Tag for Log Messages */
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    // TODO: CHECK
    private Context mContext;
    private List<Movie> mMovieList;
    /*
     * Below, we've defined an interface to handle clicks on items within this Adapter. In the
     * constructor of our MovieAdapter, we receive an instance of a class that has implemented
     * said interface. We store that instance in this variable to call the onClick method whenever
     * an item is clicked in the list.
     */
    private ItemClickHandler mClickHandler;

    public interface ItemClickHandler {
        void onItemClick(Movie movie);
    }

    /**
     * Constructs a new {@link MovieAdapter}.
     *
     * @param context of the app
     * @param moviesList  is the list of movies, which is the data source of the adapter
     */
    // TODO: Comprobar si ArrayList o List !!!
    public MovieAdapter(Context context, List<Movie> moviesList, ItemClickHandler
            clickHandler) {
        mContext = context;
        mMovieList = moviesList;
        mClickHandler = clickHandler;
    }

    // TODO: CHECK - BORRAR
/*    public static class MovieAdapterViewHolder extends RecyclerView.ViewHolder {
        public ImageView posterImage;
        public View mItemView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            posterImage = itemView.findViewById(R.id.grid_movie_item);
        }
    }*/

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

        //TODO: CHECK
        // Context context = viewGroup.getContext();

        boolean shouldAttachToParentImmediately = false;

        View view = LayoutInflater.from(mContext).inflate(R.layout.movies_list_item, viewGroup,
                shouldAttachToParentImmediately);

        // TODO: OPTION
        view.setFocusable(true);

        return new MovieAdapterViewHolder(view);
    }

    // TODO: HASTA AQUI SIGUE PROTOCOLO SUNSHINE

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the movie
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param movieAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {

        Picasso.with(mContext)
                .load(mMovieList.get(position).getMoviePosterPath())
                .resize(mContext.getResources().getInteger(R.integer
                                .themoviedb_poster_w185_width),
                        mContext.getResources().getInteger(R.integer.themoviedb_poster_w185_height))
                .placeholder(R.drawable.poster_placeholder)
                .error(R.drawable.poster_placeholder_error)
                .into(movieAdapterViewHolder.mPosterImage);
    }

    @Override
    public int getItemCount() {

        int i = mMovieList.size();

        Log.d(LOG_TAG, "Movielist Size: " + i);
        return mMovieList.size();
        // TODO: CHECK
/*        if (mMovieList == null) {
            Log.d(LOG_TAG, "mMovieList = null");
            return 0;
        }

        int i = mMovieList.size();

        Log.d(LOG_TAG, "Movielist Size: " + i);
        return mMovieList.size();*/
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        public final ImageView mPosterImage;


        public MovieAdapterViewHolder(View itemView) {
            super(itemView);

            mPosterImage = itemView.findViewById(R.id.grid_movie_item);

            itemView.setOnClickListener(this);
        }


        /**
         * This gets called by the child views during a click. We fetch the date that has been
         * selected, and then call the onClick handler registered with this adapter, passing that
         * date.
         *
         * @param v the View that was clicked
         */
        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();

            Movie movie = mMovieList.get(adapterPosition);

            launchMovieActivity(movie);
        }
    }

    private void launchMovieActivity(Movie movie) {
        //  TODO CHECK:
        //Context context = v.getContext();

        // Create a new intent to view the movie details
        Intent movieIntent = new Intent(mContext, MovieActivity.class);

        // Pass current movie information to the new activity calles MovieActivity
        movieIntent.putExtra(Constants.EXTRA_MOVIE, movie);

        // Send the intent to launch a new activity
        mContext.startActivity(movieIntent);
    }

    /* Reset the Movie List */
    // TODO: CHECK
    public void clear() {
        if (mMovieList != null) {
            mMovieList.clear();
            notifyDataSetChanged();
        }
    }

    public void setMovies (List<Movie> movies) {
        mMovieList = movies;
        notifyDataSetChanged();
    }
}



