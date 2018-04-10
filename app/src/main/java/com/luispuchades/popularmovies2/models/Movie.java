package com.luispuchades.popularmovies2.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.luispuchades.popularmovies2.utils.Constants;


/**
 * Movie object to hold all data related to a movie fetched from the API.
 * This class implements the parcelable interface so that its objects can be transferred via
 * Intents.
 */
public class Movie implements Parcelable {

    /* Movie details layout contains title, release date, movie poster, vote average, and plot
    synopsis. */

    /* int for the movie Id */
    private int mMovieId;

    /* String for the Title */
    private String mMovieTitle;

    /* Double for the User Rating "vote_average"*/
    private Double mMovieVoteAverage;

    /* String for the Release Date */
    private String mMovieReleaseDate;

    /* String for the Plot Synopsis "overview"*/
    private String mMovieOverview;

    /* String for poster_path */
    private String mMoviePosterPath;

    /* String for backdrop_path*/
    private String mMovieBackDropPath;

    /**
     * Public constructor
     *
     * @param movieId is a integer that contains the movie id.
     * @param movieTitle is a string that contains the title of the movie
     * @param voteAverage is a double that contains the average vote for the movie
     * @param movieReleaseDate is a string that contains the release date of the movie
     * @param movieOverview is a string that contains the plot of the movie
     * @param moviePosterPath is a string that contains the path for the movie poster
     * @param movieBackDropPath is a string that contains the path for the movie backdrop
     *
     *
     */

    public Movie(int movieId, String movieTitle,Double voteAverage, String
            movieReleaseDate, String movieOverview,  String moviePosterPath, String
            movieBackDropPath) {
        this.mMovieId = movieId;
        this.mMovieTitle = movieTitle;
        this.mMovieVoteAverage = voteAverage;
        this.mMovieReleaseDate = movieReleaseDate;
        this.mMovieOverview = movieOverview;
        this.mMoviePosterPath = moviePosterPath;
        this.mMovieBackDropPath = movieBackDropPath;
    }

    /**
     * Constructor to use when re-constructing object
     * from a parcel
     *
     * @param in a parcel from which to read this object
     */
    private Movie(Parcel in) {
        mMovieId = in.readInt();
        mMovieTitle = in.readString();
        mMovieVoteAverage = in.readDouble();
        mMovieReleaseDate = in.readString();
        mMovieOverview = in.readString();
        mMoviePosterPath = in.readString();
        mMovieBackDropPath = in.readString();
    }

    /*********************/
    /* GETTERS & SETTERS */
    /*********************/

    /* MOVIE ID */
    public int getmMovieId() {
        return mMovieId;
    }
    public void setMovieId(int movieId) {
        mMovieId = movieId;
    }

    /* TITLE */
    public String getMovieTitle() {
        return mMovieTitle;
    }
    public void setMovieTitle(String movieTitle) {
        mMovieTitle = movieTitle;
    }

    /* VOTE AVERAGE */
    public Double getMovieVoteAverage() {
        return mMovieVoteAverage;
    }
    public void setMovieVoteAverage(Double movieVoteAverage) {
        mMovieVoteAverage = movieVoteAverage;
    }

    /* RELEASE DATE */
    public String getMovieReleaseDate() {
        return mMovieReleaseDate;
    }
    public void setMovieReleaseDate(String movieReleaseDate) {
        mMovieReleaseDate = movieReleaseDate;
    }


    /* OVERVIEW - Plot Synopsis */
    public String getMovieOverview() {
        return mMovieOverview;
    }
    public void setMovieOverview(String movieOverview){
        mMovieOverview = movieOverview;
    }

    /* POSTER PATH */
    public String getMoviePosterPath() {

        return Constants.THEMOVIEDB_POSTER_PATH_BASE_URL +
                Constants.THEMOVIEDB_POSTER_PHONE_SIZE + mMoviePosterPath;
    }
    public void setMoviePosterPath(String moviePosterPath) {
        mMoviePosterPath = moviePosterPath;
    }

    /* BACKDROP PATH */
    public String getMovieBackDropPath() {

        return Constants.THEMOVIEDB_POSTER_PATH_BASE_URL +
                Constants.THEMOVIEDB_BACKDROP_PHONE_SIZE + mMovieBackDropPath;
    }
    public void setMovieBackDropPath(String movieBackDropPath) {
        mMovieBackDropPath = movieBackDropPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mMovieId);
        parcel.writeString(mMovieTitle);
        parcel.writeDouble(mMovieVoteAverage);
        parcel.writeString(mMovieReleaseDate);
        parcel.writeString(mMovieOverview);
        parcel.writeString(mMoviePosterPath);
        parcel.writeString(mMovieBackDropPath);
    }

    /* Parcelable Creator */
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

}
