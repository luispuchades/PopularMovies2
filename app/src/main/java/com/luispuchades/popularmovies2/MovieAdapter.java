package com.luispuchades.popularmovies2;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.luispuchades.popularmovies2.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    /* Tag for Log Messages */
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    // TODO: CHECK
    private List<Movie> mMovieList;
    private Context mContext;

    /**
     * Constructs a new {@link MovieAdapter}.
     *
     * @param context of the app
     * @param movies  is the list of movies, which is the data source of the adapter
     */
    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        mContext = context;
        mMovieList = movies;
    }

    public static class MovieAdapterViewHolder extends RecyclerView.ViewHolder {
        public ImageView posterImage;
        public View mItemView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            posterImage = itemView.findViewById(R.id.grid_movie_item);
        }
    }

    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int
            viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_list_item,
                parent, false);

        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieAdapterViewHolder holder, final int position) {
        holder.mItemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Context context = v.getContext();

                Movie movie = mMovieList.get(position);

                launchMovieActivity(movie);
            }
        });

        Picasso.with(mContext)
                .load(mMovieList.get(position).getMoviePosterPath())
                .resize(mContext.getResources().getInteger(R.integer
                                .themoviedb_poster_w185_width),
                        mContext.getResources().getInteger(R.integer.themoviedb_poster_w185_height))
                .placeholder(R.drawable.poster_placeholder)
                .error(R.drawable.poster_placeholder_error)
                .into(holder.posterImage);

    }

    @Override
    public int getItemCount() {

        int i = mMovieList.size();

        Log.d(LOG_TAG, "Movielist Size: " + i);
        return mMovieList.size();
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

}



