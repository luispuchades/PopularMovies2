package com.luispuchades.popularmovies2.models;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Review object to hold all comments corresponding to the movie fetched from the API. * This
 * class implements the parcelable interface so that its objects can be transferred via Intents.
 */
public class Review implements Parcelable {
    /* Review details layout contains review id, author, content and url */

    /* String for the review Id */
    private String mReviewId;

    /* String for the review author */
    private String mReviewAuthor;

    /* String for the review content*/
    private String mReviewContent;

    /* String for the review url */
    private String mReviewUrl;

    /**
     * Public constructor
     * @param reviewId is a string that contains the review id.
     * @param reviewAuthor is a string that contains the review author.
     * @param reviewContent is a string that contains the review content.
     * @param reviewUrl is a string that contains the review url.   *
     */

    public Review(String reviewId, String reviewAuthor, String reviewContent, String
            reviewUrl) {
        this.mReviewId = reviewId;
        this.mReviewAuthor = reviewAuthor;
        this.mReviewContent = reviewContent;
        this.mReviewUrl = reviewUrl;
    }

    /**
     * Constructor to use when re-constructing object
     * from a parcel
     *
     * @param in a parcel from which to read this object
     */
    private Review(Parcel in) {
        mReviewId = in.readString();
        mReviewAuthor = in.readString();
        mReviewContent = in.readString();
        mReviewUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mReviewId);
        parcel.writeString(mReviewAuthor);
        parcel.writeString(mReviewContent);
        parcel.writeString(mReviewUrl);
    }

    /*********************/
    /* GETTERS & SETTERS */
    /*********************/

    /* REVIEW ID */
    public String getReviewId() {
        return mReviewId;
    }
    public void setReviewId(String reviewId) {
        mReviewId = reviewId;
    }

    /* REVIEW AUTHOR*/
    public String getReviewAuthor() {
        return mReviewAuthor;
    }
    public void setReviewAuthor(String reviewAuthor) {
        mReviewAuthor = reviewAuthor;
    }

    /* REVIEW URL */
    public String getReviewURL() {
        return mReviewUrl;
    }
    public void setReviewUrl(String reviewUrl) {
        mReviewUrl = reviewUrl;
    }

    /* REVIEW CONTENT */
    public String getReviewContent() {
        return mReviewContent;
    }
    public void setReviewContent(String reviewContent){
        mReviewContent = reviewContent;
    }

    /* Parcelable Creator */
    public static final Creator<Review> CREATOR = new Creator<Review>() {

        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}