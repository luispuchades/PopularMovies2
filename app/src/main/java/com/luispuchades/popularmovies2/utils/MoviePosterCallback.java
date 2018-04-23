package com.luispuchades.popularmovies2.utils;

import android.view.View;

import com.luispuchades.popularmovies2.adapters.MovieAdapter;
import com.squareup.picasso.Callback;


/**
 * Extension od the Callback interface from Picasso
 */
public class MoviePosterCallback extends Callback.EmptyCallback {


    private MovieAdapter.MovieAdapterViewHolder mViewHolder;

    /**
     * Constructor
     *
     * @param viewHolder the ViewHolder of the class at MovieAdapter
     */
    public MoviePosterCallback(MovieAdapter.MovieAdapterViewHolder viewHolder){
        mViewHolder = viewHolder;
    }

    /* TODO: COMPLETE AFTER FINISHING MOVIEADAPTER */
    @Override
    public void onSuccess() {
        mViewHolder.mPbMoviePoster.setVisibility(View.GONE);
        mViewHolder.mTvMoviePosterError.setVisibility(View.GONE);
    }

    @Override
    public void onError() {
        mViewHolder.mPbMoviePoster.setVisibility(View.GONE);
        mViewHolder.mTvMoviePosterError.setRotation(-15);
        mViewHolder.mTvMoviePosterError.setVisibility(View.VISIBLE);
    }
}
