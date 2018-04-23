package com.luispuchades.popularmovies2.utils;

import com.luispuchades.popularmovies2.BuildConfig;
import com.luispuchades.popularmovies2.data.MoviesContract.MovieEntry;

/**
 * This class updates the app constants.
 * Created by luisp on 11/03/2018.
 */

public class Constants {

    /* Create URL for the movie path*/
    public static final String THEMOVIEDB_POSTER_PATH_BASE_URL = "http://image.tmdb.org/t/p";
    public static final String THEMOVIEDB_POSTER_PHONE_SIZE = "/w185/";
    public static final String THEMOVIEDB_BACKDROP_PHONE_SIZE = "/w300/";

    /** String for putExtra */
    public static final String EXTRA_MOVIE = "intent_extra_movie";

    /*
     * URL EXAMPLES
     * http://api.themoviedb.org/3/movie/popular?api_key=[API_KEY]
     * http://api.themoviedb.org/3/movie/top_rated?api_key=[API_KEY]
     */

    public static final String THEMOVIEDB_BASE_URL = "https://api.themoviedb.org/3/movie/";

    public static final String THEMOVIEDB_ENDPOINT_API_KEY = "api_key";
    public static final String THEMOVIEDB_API_KEY = BuildConfig.API_KEY;

    public static final String THEMOVIEDB_METHOD_POPULAR = "popular";
    public static final String THEMOVIEDB_METHOD_RATED = "top_rated";
    public static final String THEMOVIEDB_VIDEOS = "/#/videos";
    public static final String THEMOVIEDB_REVIEWS = "/#/reviews";

    public static final String THEMOVIEDB_LANGUAGE_QUERY_PARAM = "language";
    public static final String THEMOVIEDB_PAGE_QUERY_PARAM = "page";


    /**
     * Constants for preferences
     */
    public static final int SORTING_POPULAR = 1;
    public static final int SORTING_RATED = 2;
    public static final int SORTING_FAVORITES = 3;

    public static final String BUNDLE_MOVIES_KEY = "movie_list_key";
    public static final String BUNDLE_PAGE_KEY = "current_page_key";
    public static final String BUNDLE_SORTING_KEY = "current_sorting_key";
    public static final String BUNDLE_ERROR_KEY = "error_key";

    public static final String PARCELABLE_MOVIE_KEY = "parcelable_movie_key";
    public static final String BUNDLE_VIDEOS_KEY = "videos_key";
    public static final String BUNDlE_REVIEWS_KEY = "reviews_key";

    public static final String DETAIL_ELEMENT_VIDEOS = "videos";
    public static final String DETAIL_ELEMENT_REVIEWS = "reviews";


    /**
     * Constant for Favorite Movies Projection
     */
    public static final String[] FAVORITE_MOVIES_PROJECTION = {
            MovieEntry.COLUMN_MOVIE_ID,
            MovieEntry.COLUMN_MOVIE_RATING,
            MovieEntry.COLUMN_MOVIE_RELEASE_DATE,
            MovieEntry.COLUMN_MOVIE_OVERVIEW,
            MovieEntry.COLUMN_MOVIE_POSTER_PATH,
            MovieEntry.COLUMN_MOVIE_BACKDROP_PATH
    };

    /**
     * Getter method for getting the Language query param value
     *
     * @return the Languaje query param value
     */
    public static String getTheMovieDbLanguageQueryParam() {
        return Constants.THEMOVIEDB_LANGUAGE_QUERY_PARAM;
    }

    /**
     * Getter method for getting the Page query param value
     *
     * @return the Pege param value
     */
    public static String getTheMovieDbPageQueryParam() {
        return Constants.THEMOVIEDB_PAGE_QUERY_PARAM;
    }

    /**
     * Getter method for getting the Most Popular query param value
     *
     * @return the Most Popular param value
     */
    public static String getTheMovieDbMethodPopular() {
        return Constants.THEMOVIEDB_METHOD_POPULAR;
    }

    /**
     * Getter method for getting the Top Rated query param value
     *
     * @return the Top Rated param value
     */
    public static String getTheMovieDbMethodTopRated() {
        return Constants.THEMOVIEDB_METHOD_RATED;
    }

    /**
     * Getter method for getting the Videos query param
     *
     * @param movieId the Movie Id
     * @return tje Videos param value
     */
    public static String getTheMovieDbMethodVideos(int movieId) {
        return Constants.THEMOVIEDB_VIDEOS.replace("#", String.valueOf(movieId));
    }

    /**
     * Getter method for getting the Reviews query param
     *
     * @param movieId the Movie Id
     * @return tje Reviews param value
     */
    public static String getTheMovieDbMethodReviews(int movieId) {
        return Constants.THEMOVIEDB_REVIEWS.replace("#", String.valueOf(movieId));
    }

}
