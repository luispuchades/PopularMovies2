package com.luispuchades.popularmovies2.utils;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * A listener for the bottom scroll in order to add new data from the service
 *
 * Source:
 * https://catbag.github.io/redux-android-sample/app/build/reports/coverage/debug/
 * br.com.catbag.gifreduxsample.ui/EndlessRecyclerScrollListener.java.html
 */
public abstract class EndlessRecyclerScrollListener extends RecyclerView.OnScrollListener {
    // The minimum amount of items to have below your current scroll position
    // before mLoading more.
    private int mVisibleThreshold = 3;
    // The current offset index of data you have loaded
    private int mCurrentPage = 0;
    // The total number of items in the dataset after the last load
    private int mPreviousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean mLoading = true;
    // Sets the starting page index
    private int mStartingPageIndex = 0;

    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * Constructor for the class
     *
     * @param layoutManager the layout manager that the RecyclerView is using
     * @param currentPage the current page
     */
    protected EndlessRecyclerScrollListener (GridLayoutManager layoutManager, int currentPage) {
        mLayoutManager = layoutManager;
        mVisibleThreshold = mVisibleThreshold * layoutManager.getSpanCount();
        mCurrentPage = currentPage;
    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        /* Check when the user is scrolling down */
        if (dy > 0) {
            int totalItemCount = mLayoutManager.getItemCount();
            int lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager)
                    .findLastVisibleItemPosition();

            // If the total item count is zero and the previous isn't, assume the
            // list is invalidated and should be reset back to initial state
            if (totalItemCount < mPreviousTotalItemCount) {
                mCurrentPage = mStartingPageIndex;
                mPreviousTotalItemCount = totalItemCount;
                if (totalItemCount == 0) {
                    mLoading = true;
                }
            }

            // If it’s still mLoading, we check to see if the dataset count has
            // changed, if so we conclude it has finished mLoading and update the current page
            // number and total item count.
            if (mLoading && (totalItemCount > mPreviousTotalItemCount)) {
                mLoading = false;
                mPreviousTotalItemCount = totalItemCount;
            }

            // If it isn’t currently mLoading, we check to see if we have breached
            // the mVisibleThreshold and need to reload more data.
            // If we do need to reload some more data, we execute onLoadMore to fetch the data.
            // threshold should reflect how many total columns there are too
            if (!mLoading && (lastVisibleItemPosition + mVisibleThreshold) > totalItemCount) {
                mCurrentPage++;
                onLoadMore(mCurrentPage, totalItemCount, view);
                mLoading = true;
            }
        }

    }

    // TODO: CHECK TO APPLY OR ERASE
    /**
     * Call this method whenever performing new searches.
     */
    public void resetState() {
        mCurrentPage = mStartingPageIndex;
        mPreviousTotalItemCount = 0;
        mLoading = true;
    }

    /**
     * Defines the process for actually mLoading more data based on page
     *
     * @param page the page we need to load
     * @param totalItemsCount the total number of items
     * @param view view the view uses
     */
    public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view);
}
