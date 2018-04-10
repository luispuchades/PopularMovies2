package com.luispuchades.popularmovies2.models;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Review object to hold all data related to videos corresponding to the movie  fetched from the API.
 * This class implements the parcelable interface so that its objects can be transferred via
 * Intents.
 */
public class Video implements Parcelable {
    /* Review details layout contains video id, iso format (iso_639_1 or iso_3166_1), key, name
    site and size. */

    /* int for the video Id */
    private int mVideoId;

    /* String for the video format iso_639_1 */
    private String mVideoIso_639_1;

    /* String for the video format iso_3166_1 */
    private String mVideoIso_3166_1;

    /* String for the video key */
    private String mVideoKey;

    /* String for the video name */
    private String mVideoName;

    /* String for the video site */
    private String mVideoSite;

    /* String for video size */
    private String mVideoSize;

    /* String for video type*/
    private String mVideoType;

    /**
     * Public constructor
     *  @param videoId is a integer that contains the video id.
     * @param videoIso6391 is a string that contains the video format ISO_639_1
     * @param videoIso31661 is a string that contains the video format ISO_3166_1
     * @param videoKey is a string that contains the video key.
     * @param videoName is a string that contains the video name for the movie.
     * @param videoSite is a string that contains the video site for the movie.
     * @param videoType is a string that contains the video type for the movie.
     *
     */

    public Video(String videoId, String videoIso6391, String videoIso31661, String
            videoKey, String videoName, String videoSite, String videoSize, String videoType) {
        this.mVideoId = videoId;
        this.mVideoIso_639_1 = videoIso6391;
        this.mVideoIso_3166_1 = videoIso31661;
        this.mVideoKey = videoKey;
        this.mVideoName = videoName;
        this.mVideoSite = videoSite;
        this.mVideoSize = videoSize;
        this.mVideoType = videoType;
    }

    /**
     * Constructor to use when re-constructing object
     * from a parcel
     *
     * @param in a parcel from which to read this object
     */
    private Video(Parcel in) {
        mVideoId = in.readInt();
        mVideoIso_639_1 = in.readString();
        mVideoIso_3166_1 = in.readString();
        mVideoKey = in.readString();
        mVideoName = in.readString();
        mVideoSite = in.readString();
        mVideoSize = in.readString();
        mVideoType = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mVideoId);
        parcel.writeString(mVideoIso_639_1);
        parcel.writeString(mVideoIso_3166_1);
        parcel.writeString(mVideoKey);
        parcel.writeString(mVideoName);
        parcel.writeString(mVideoSite);
        parcel.writeString(mVideoSize);
        parcel.writeString(mVideoType);
    }

    /*********************/
    /* GETTERS & SETTERS */
    /*********************/

    /* VIDEO ID */
    public int getVideoId() {
        return mVideoId;
    }
    public void setVideoId(int videoId) {
        mVideoId = videoId;
    }

    /* MOVIE FORMAT ISO 639_1 */
    public String getVideoIso6391() {
        return mVideoIso_639_1;
    }
    public void setVideoIso_639_1(String videoIso6391) {
        mVideoIso_639_1 = videoIso6391;
        }

    /* MOVIE FORMAT ISO 3166_1 */
    public String getVideoIso31661() {
        return mVideoIso_3166_1;
    }
    public void setVideoIso_3166_1(String videoIso31661) {
        mVideoIso_3166_1 = videoIso31661;
    }

    /* VIDEO KEY */
    public String getVideoKey() {
        return mVideoKey;
    }
    public void setVideoKey(String videoKey) {
        mVideoKey = videoKey;
    }

    /* VIDEO NAME */
    public String getVideoName() {
        return mVideoName;
    }
    public void setVideoName(String videoName){
        mVideoName = videoName;
    }

    /* VIDEO SITE */
    public String getVideoSite() {
        return mVideoName;
    }
    public void setVideoSite(String videoSite) {
        mVideoSite = videoSite;
    }

    /* VIDEO SIZE */
    public String getVideoSize() {
        return mVideoName;
    }
    public void setVideoSize(String videoSize){
        mVideoSite = videoSize;
    }

    /* VIDEO TYPE */
    public String getVodeoType() {
        return mVideoType;
    }
    public void setVideoType(String videoType){
        mVideoSite = videoType;
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