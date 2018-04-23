package com.luispuchades.popularmovies2.utils;

import android.view.View;

import com.luispuchades.popularmovies2.adapters.VideoAdapter;
import com.squareup.picasso.Callback;

public class MovieTrailerCallback extends Callback.EmptyCallback {

    private VideoAdapter.VideoAdapterViewHolder mViewHolder;

    /**
     * Constructor
     *
     * @param viewHolder the ViewHolder of the class at MovieAdapter
     */
    public MovieTrailerCallback(VideoAdapter.VideoAdapterViewHolder viewHolder){
        mViewHolder = viewHolder;
    }

    /* TODO: COMPLETE AFTER FINISHING MOVIEADAPTER */
    @Override
    public void onSuccess() {
        mViewHolder.mPbMovieTrailer.setVisibility(View.GONE);
        mViewHolder.mIvMovieTrailerThumbnailError.setVisibility(View.GONE);
    }

    @Override
    public void onError() {
        mViewHolder.mPbMovieTrailer.setVisibility(View.GONE);
        mViewHolder.mIvMovieTrailerThumbnailError.setVisibility(View.VISIBLE);
    }
}
