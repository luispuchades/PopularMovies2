package com.luispuchades.popularmovies2.adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.luispuchades.popularmovies2.R;
import com.luispuchades.popularmovies2.models.Review;
import com.luispuchades.popularmovies2.utils.SpannableUtilities;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterVieHolder>{

    /* Tag for Log Messages */
    private static final String LOG_TAG = ReviewAdapter.class.getSimpleName();

    private List<Review> mReviewList;

    @BindString(R.string.movie_detail_review_by)
    String mDetailReviewByLabel;


    class ReviewAdapterVieHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_review_author)
        TextView mTvReviewAuthor;

        @BindView(R.id.tv_review_content)
        TextView mTvReviewContent;

        @BindView(R.id.iv_review_show_more)
        ImageView mIvReviewShowMore;

        Context mContext;


        /**
         * Constructor to the ViewHolder class
         *
         * @param itemView the view that will be inflated
         */
        ReviewAdapterVieHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public ReviewAdapter.ReviewAdapterVieHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int layoutIdForListItem = R.layout.card_reviews;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup,
                shouldAttachToParentImmediately);
        ButterKnife.bind(this, view);

        return new ReviewAdapterVieHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReviewAdapter.ReviewAdapterVieHolder
                                             reviewAdapterVieHolder, int position) {

        Review review = mReviewList.get(position);
        reviewAdapterVieHolder.mTvReviewAuthor.append(SpannableUtilities.getBold
                (mDetailReviewByLabel));
        reviewAdapterVieHolder.mTvReviewAuthor.append(review.getReviewAuthor());
        reviewAdapterVieHolder.mTvReviewContent.setText(review.getReviewContent());
        reviewAdapterVieHolder.mIvReviewShowMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int reviewContentLines = TextViewCompat.getMaxLines(reviewAdapterVieHolder
                        .mTvReviewContent);
                int reviewCollapsedMaxLines = reviewAdapterVieHolder.mContext.getResources()
                        .getInteger(R.integer.review_collapsed_max_lines);
                if(reviewContentLines != reviewCollapsedMaxLines) {
                    reviewAdapterVieHolder.mTvReviewContent.setMaxLines(reviewCollapsedMaxLines);
                    reviewAdapterVieHolder.mIvReviewShowMore
                            .setImageResource(R.drawable.ic_expand_more);
                } else {
                    ObjectAnimator animation = ObjectAnimator.ofInt(
                            reviewAdapterVieHolder.mTvReviewContent,
                            "maxLines",
                            9999
                    );
                    animation.setDuration(1000).start();
                    reviewAdapterVieHolder.mIvReviewShowMore.setImageResource(R.drawable
                            .ic_expand_less);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null != mReviewList) {
            int i = mReviewList.size();
            Log.d(LOG_TAG, "Reviewlist Size: " + i);

            return i;
        }
        return 0;
    }

    /**
     * Setter method for assigning the reviews got to the mReviewList variable
     *
     * @param reviews the list of videos
     */
    public void setReviewsData(List<Review> reviews) {
        if (null == reviews) {
            mReviewList = reviews;
        } else {
            mReviewList.addAll(reviews);
        }
        notifyDataSetChanged();
    }


    /**
     * Getter method por getting the Review List
     *
     * @return the Review List
     */
    public List<Review> getReviewList() {
        return mReviewList;
    }


}
