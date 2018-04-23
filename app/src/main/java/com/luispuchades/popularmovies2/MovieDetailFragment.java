package com.luispuchades.popularmovies2;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luispuchades.popularmovies2.R;
import com.luispuchades.popularmovies2.adapters.ReviewAdapter;
import com.luispuchades.popularmovies2.adapters.VideoAdapter;
import com.luispuchades.popularmovies2.data.MoviesContract.MovieEntry;
import com.luispuchades.popularmovies2.models.Movie;
import com.luispuchades.popularmovies2.models.Review;
import com.luispuchades.popularmovies2.models.Video;
import com.luispuchades.popularmovies2.utils.Constants;
import com.luispuchades.popularmovies2.utils.FetchReviewTask;
import com.luispuchades.popularmovies2.utils.FetchVideoTask;
import com.luispuchades.popularmovies2.utils.NetworkUtilities;
import com.luispuchades.popularmovies2.utils.SpannableUtilities;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment {

    private Context mContext;
    private Movie mMovie;
    private VideoAdapter mVideoAdapter;
    private ReviewAdapter mReviewAdapter;

    /**
     * Extraction of Views
     */

    @BindView(R.id.collapsing_toolbar_movie_detail)
    CollapsingToolbarLayout mCtlMovie;

    @BindView(R.id.toolbar_movie_detail)
    Toolbar mTMovie;

    @BindView(R.id.backdrop_movie_detail_scrolling_top)
    ImageView mIvMovieBackdrop;

    @BindView(R.id.tv_movie_detail_vote_average)
    TextView mTvMovieVoreAgerage;

    @BindView(R.id.tv_movie_detail_release_date)
    TextView mTvMovieReleaseDate;

    @BindView(R.id.tv_movie_detail_overview)
    TextView mTvMovieOverview;

    @BindView(R.id.rv_videos)
    RecyclerView mRvVideo;

    @BindView(R.id.rv_reviews)
    RecyclerView mRvReview;


    /**
     * Extraction of Strings
     */
    @BindString(R.string.movie_detail_vote_average)
    String mMovieVoteAverage;

    @BindString(R.string.movie_detail_release_date)
    String mMovieReleaseDate;

    @BindString(R.string.movie_detail_overview)
    String mMovieOverview;

    @BindString(R.string.movie_favorite_off_toast_msg)
    String mFavOffToastMsg;

    @BindString(R.string.movie_favorite_on_toast_msg)
    String mFavOnToastMsg;


    public MovieDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getActivity().getApplicationContext();
        mMovie = null;
        if (getArguments().containsKey(Constants.PARCELABLE_MOVIE_KEY)) {
            mMovie = getArguments().getParcelable(Constants.PARCELABLE_MOVIE_KEY);
        }

        if (null != mMovie) {
            View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
            ButterKnife.bind(this, rootView);
            mCtlMovie.setTitle(mMovie.getMovieTitle());

            ((AppCompatActivity) getActivity()).setSupportActionBar(mTMovie);
            if(((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
                setHasOptionsMenu(true);
                ((AppCompatActivity) getActivity()).getSupportActionBar()
                        .setDisplayHomeAsUpEnabled(true);
            }

            Picasso.with(mContext)
                    .load(mMovie.getMovieBackDropPath())
                    .into(mIvMovieBackdrop);

            mTvMovieVoreAgerage.append(SpannableUtilities.getBold(mMovieVoteAverage));
            mTvMovieVoreAgerage.append(mMovie.getMovieVoteAverage());
            mTvMovieReleaseDate.append(SpannableUtilities.getBold(mMovieReleaseDate));
            mTvMovieReleaseDate.append(mMovie.getMovieReleaseDate());
            mTvMovieOverview.append(SpannableUtilities.getBold(mMovieReleaseDate));
            mTvMovieOverview.append(mMovie.getMovieOverview());

            LinearLayoutManager videoLinearLayoutManager = new LinearLayoutManager(mContext);
            videoLinearLayoutManager.setOrientation((LinearLayoutManager.HORIZONTAL));
            mRvVideo.setLayoutManager(videoLinearLayoutManager);

            mRvVideo.setHasFixedSize(true);
            mVideoAdapter = new VideoAdapter();
            mRvVideo.setAdapter(mVideoAdapter);

            LinearLayoutManager reviewLinearLayoutManager = new LinearLayoutManager(mContext);
            mRvReview.setLayoutManager(reviewLinearLayoutManager);

            mRvReview.setHasFixedSize(true);
            mReviewAdapter = new ReviewAdapter();
            mRvReview.setAdapter(mReviewAdapter);

            if(null != savedInstanceState) {
                ArrayList<Video> videoList = savedInstanceState.getParcelable(Constants
                        .BUNDLE_VIDEOS_KEY);
                mVideoAdapter.setMoviesData(videoList);
                ArrayList<Review> reviewList = savedInstanceState.getParcelable(Constants
                        .BUNDlE_REVIEWS_KEY);
                mReviewAdapter.setReviewsData(reviewList);
            } else {
                loadElements(Constants.DETAIL_ELEMENT_VIDEOS, mMovie.getmMovieId());
                loadElements(Constants.DETAIL_ELEMENT_REVIEWS, mMovie.getmMovieId());
            }

            return rootView;
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        List<Video> videoList = mVideoAdapter.getVideoList();
        if(null != videoList) {
            ArrayList<Video> videoArrayList = new ArrayList<>(videoList);
            outState.putParcelableArrayList(Constants.BUNDLE_VIDEOS_KEY, videoArrayList); ;
        }

        List<Review> reviewList = mReviewAdapter.getReviewList();
        if(null != reviewList) {
            ArrayList<Review> reviewArrayList = new ArrayList<>(reviewList);
            outState.putParcelableArrayList(Constants.BUNDLE_VIDEOS_KEY, reviewArrayList); ;
        }
    }

    public void loadElements(String element, int movieId){
        if(NetworkUtilities.isNetworkAvailable(mContext)){
            String method;
            switch (element) {
                case Constants.DETAIL_ELEMENT_VIDEOS:
                    method = Constants.getTheMovieDbMethodVideos(movieId);
                    String[] videos = new String[]{method};
                    new FetchVideoTask().execute(videos);
                    break;
                case Constants.DETAIL_ELEMENT_REVIEWS:
                    method = Constants.getTheMovieDbMethodReviews(movieId);
                    String[] reviews = new String[]{method};
                    new FetchReviewTask().execute(reviews);
                    break;
            }
        }
    }

    /**
     * A method to check id a Movie is flagged as favorite
     *
     * @param movieId the movie ID
     * @return true or false
     */
    private boolean movieIsFavorite(int movieId) {
        boolean favorite = false;
        String[] selectionArgs = {String.valueOf(movieId)};
        Uri uri = MovieEntry.buildFavoriteUriWithMovieId(movieId);
        Cursor cursor = getActivity().getContentResolver().query(uri,
                null,
                MovieEntry.COLUMN_MOVIE_ID + "=?",
                selectionArgs,
                null);
        if(null != cursor && cursor.getCount() != 0) {
            favorite = true;
            cursor.close();
        }
        return favorite;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail, menu);
        if(movieIsFavorite(mMovie.getmMovieId())){
            menu.getItem(0).setIcon(R.drawable.ic_star);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            case R.id.action_favorite:
                if (movieIsFavorite(mMovie.getmMovieId())) {
                    Uri removeFavoriteUri = MovieEntry.buildFavoriteUriWithMovieId
                            (mMovie.getmMovieId());
                    getActivity().getContentResolver().delete(removeFavoriteUri, null, null);
                    Toast.makeText(getActivity().getBaseContext(), mFavOffToastMsg, Toast
                            .LENGTH_LONG).show();
                    item.setIcon(R.drawable.ic_star_border_white);
                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MovieEntry.COLUMN_MOVIE_ID, mMovie.getmMovieId());
                    contentValues.put(MovieEntry.COLUMN_MOVIE_TITLE, mMovie.getMovieTitle());
                    contentValues.put(MovieEntry.COLUMN_MOVIE_RATING, mMovie.getMovieVoteAverage());
                    contentValues.put(MovieEntry.COLUMN_MOVIE_RELEASE_DATE, mMovie
                            .getMovieReleaseDate());
                    contentValues.put(MovieEntry.COLUMN_MOVIE_OVERVIEW, mMovie.getMovieOverview());
                    contentValues.put(MovieEntry.COLUMN_MOVIE_POSTER_PATH, mMovie
                            .getMoviePosterPath());
                    contentValues.put(MovieEntry.COLUMN_MOVIE_BACKDROP_PATH, mMovie
                            .getMovieBackDropPath());
                    Uri favoriteUri = getActivity().getContentResolver().insert(MovieEntry
                            .CONTENT_URI, contentValues);

                    if(null != favoriteUri) {
                        Toast.makeText(getActivity().getBaseContext(), mFavOnToastMsg, Toast
                                .LENGTH_LONG).show();
                        item.setIcon(R.drawable.ic_star);
                    }
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
