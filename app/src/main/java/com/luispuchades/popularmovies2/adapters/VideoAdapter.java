package com.luispuchades.popularmovies2.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.luispuchades.popularmovies2.R;
import com.luispuchades.popularmovies2.models.Video;
import com.luispuchades.popularmovies2.utils.MovieTrailerCallback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * VideoAdapter manages the Videos RecyclerView in the MovieDetailActivity
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoAdapterViewHolder>{

    /* Tag for Log Messages */
    private static final String LOG_TAG = VideoAdapter.class.getSimpleName();

    private List<Video> mVideoList;

    /**
     * Extraction of Strings
     */
    @BindString(R.string.movie_detail_youtube_thumbnail_service)
    String mVideoDetailYoutubeThumbnail;

    @BindString(R.string.movie_detail_youtube_vendor)
    String mVideoDetailYoutubeVendor;

    @BindString(R.string.movie_detail_youtube_video_link)
    String mVideoDetailYoutubeLink;

    /**
     * Inner class to represent the ViewHolder for the adapter
     */
    public class VideoAdapterViewHolder extends RecyclerView.ViewHolder{

        /**
         * Extraction of Views
         */
        @BindView(R.id.iv_video_thumbnail)
        ImageView mIvVideoThumbnail;

        @BindView(R.id.pb_trailer_loading_indicator)
        public ProgressBar mPbMovieTrailer;

        @BindView(R.id.iv_video_thumbnail_error)
        public ImageView mIvMovieTrailerThumbnailError;

        Context mContext;



        /**
         * Constructor for the ViewHolder
         *
         * @param itemView the view that will be inflated
         */
        public VideoAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    @Override
    public VideoAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int layoutForListItem = R.layout.card_videos;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        boolean shouldAttachtoParentImmediately = false;

        View view = inflater.inflate(layoutForListItem, viewGroup, shouldAttachtoParentImmediately);
        ButterKnife.bind(this, view);

        return new VideoAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoAdapterViewHolder videoAdapterViewholder, int position) {
        final Video video = mVideoList.get(position);

        Picasso.with(videoAdapterViewholder.mContext)
                .load(buildVideoUrl(video.getVideoKey()))
                .into(videoAdapterViewholder.mIvVideoThumbnail, new MovieTrailerCallback
                        (videoAdapterViewholder));

        videoAdapterViewholder.mIvVideoThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent appIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(mVideoDetailYoutubeVendor + video.getVideoKey())
                );

                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(mVideoDetailYoutubeLink + video.getVideoKey())
                );

                try {
                    view.getContext().startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    view.getContext().startActivity(webIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null != mVideoList) {
            int i = mVideoList.size();
            Log.d(LOG_TAG, "Movielist Size: " + i);

            return i;
        }
        return 0;
    }


    // TODO: TRASLADAR A CONSTANTS
    public String buildVideoUrl(String videokey) {
        return mVideoDetailYoutubeThumbnail.replace("#", videokey);
    }

    /**
     * Setter method for assigning the videos got to the mVideoList variable
     *
     * @param videos the list of videos
     */
    public void setMoviesData(List<Video> videos) {
        if (null == videos) {
            mVideoList = videos;
        } else {
            mVideoList.addAll(videos);
        }
        notifyDataSetChanged();
    }


    /**
     * Getter method por getting the Video List
     *
     * @return the Video List
     */
    public List<Video> getVideoList() {
        return mVideoList;
    }

}
