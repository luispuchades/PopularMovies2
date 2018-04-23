package com.luispuchades.popularmovies2.utils;

import android.text.TextUtils;
import android.util.Log;

import com.luispuchades.popularmovies2.models.Movie;
import com.luispuchades.popularmovies2.models.Review;
import com.luispuchades.popularmovies2.models.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MovieJsonUtils {

    /* Get the JSON class simple name in order to have better information under exceptions thrown */
    private static final String LOG_TAG = MovieJsonUtils.class.getSimpleName();

    /*
     * Defining variables from fields to parse with JSON. These fields are extracted from
     * THE MOVIEDATABASE API
     **/
    private static final String THEMOVIEDB_RESULTS = "results";
    /* Movies variables */
    private static final String THEMOBIEDB_MOVIE_ID = "id";
    private static final String THEMOVIEDB_TITLE = "title";
    private static final String THEMOVIEDB_VOTE_AVERAGE = "vote_average";
    private static final String THEMOVIEDB_RELEASE_DATE = "release_date";
    private static final String THEMOVIEDB_OVERVIEW = "overview";
    private static final String THEMOVIEDB_POSTER_PATH = "poster_path";
    private static final String THEMOVIEDB_BACKDROP_PATH = "backdrop_path";
    /* Movie Review Variables*/
    private static final String THEMOVIEDB_VIDEO_ID = "id";
    private static final String THEMOVIEDB_VIDEO_LANGUAGE_CODE = "iso_639_1";
    private static final String THEMOVIEDB_VIDEO_ISO_COUNTRY_CODE = "iso_3166_1";
    private static final String THEMOVIEDB_VIDEO_KEY = "key";
    private static final String THEMOVIEDB_VIDEO_NAME = "name";
    private static final String THEMOVIEDB_VIDEO_SITE = "site";
    private static final String THEMOVIEDB_VIDEO_SIZE = "size";
    private static final String THEMOVIEDB_VIDEO_TYPE = "type";
    /* Movie Review Variables */
    private static final String THEMOVIEDB_REVIEW_ID = "id";
    private static final String THEMOVIEDB_REVIEW_AUTHOR = "author";
    private static final String THEMOVIEDB_REVIEW_CONTENT = "content";
    private static final String THEMOVIEDB_REVIEW_URL = "url";



    private MovieJsonUtils() {
    }

    public static List<Movie> fetchMovieData(String requestUrl) {

        Log.i(LOG_TAG, "fetchMovieData() called");
        //Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Movie}s
        List<Movie> movies = extractMovieFromJson(jsonResponse);

        // Return the list of {@link Movie}s
        return movies;
    }

    public static List<Video> fetchVideoData(String requestUrl) {
        Log.i(LOG_TAG, "fetchVideoData() called");
        //Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Video}s
        List<Video> videos = extractVideoFromJson(jsonResponse);

        // Return the list of {@link Video}s
        return videos;
    }

    public static List<Review> fetchReviewData(String requestUrl) {
        Log.i(LOG_TAG, "fetchReviewData() called");
        //Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Video}s
        List<Review> reviews = extractReviewFromJson(jsonResponse);

        // Return the list of {@link Video}s
        return reviews;
    }
    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the movie JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,
                    Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Movie} objects that has been built up from parsing the given JSON
     * response
     */
    private static List<Movie> extractMovieFromJson(String movieJsonString) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(movieJsonString)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding movies to
        List<Movie> movies = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject moviesJson = new JSONObject(movieJsonString);

            // Extract the JSONArray associated with the key called "results",
            // which represents a list of results (or movies).
            JSONArray moviesJsonArray = moviesJson.getJSONArray(THEMOVIEDB_RESULTS);

            //For each movie found in the moviesJsonArray, create a {@Link Movie} object
            for (int i = 0; i < moviesJsonArray.length(); i++) {

                // Get a single movie at position i within the list of movies
                JSONObject currentJsonMovie = moviesJsonArray.getJSONObject(i);

                /* Extract the ID value */
                int movieId = currentJsonMovie.getInt(THEMOBIEDB_MOVIE_ID);

                // Extract the value for the key "title"
                String titleJson = currentJsonMovie.getString(THEMOVIEDB_TITLE);

                // Extract the value for the key "vote_average"
                String voteAverageJson = currentJsonMovie.getString(THEMOVIEDB_VOTE_AVERAGE);

                // Extract the value for the key "release_date"
                String releaseDateJson = currentJsonMovie.getString(THEMOVIEDB_RELEASE_DATE);

                // Extract the value for the key "title"
                String overviewJson = currentJsonMovie.getString(THEMOVIEDB_OVERVIEW);

                String posterPathJson = currentJsonMovie.getString(THEMOVIEDB_POSTER_PATH);

                String backdropPathJson = currentJsonMovie.getString(THEMOVIEDB_BACKDROP_PATH);

                // Create a new {@link Movie} object with the title, vote_average, release_date,
                // overview and get the poster path from the JSON response.
                Movie movie = new Movie(movieId, titleJson, voteAverageJson, releaseDateJson,
                        overviewJson, posterPathJson, backdropPathJson);

                // Add the new {@link Movie} to the list of movies,
                movies.add(movie);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the movie JSON results", e);
        }

        //Return the list of movies
        return movies;
    }

    /**
     * Return a list of {@link Video} objects that has been built up from parsing the given JSON
     * response
     */
    private static List<Video> extractVideoFromJson (String videoJsonString) {
        // If the JSON string is empty or null, then return early.
        if(TextUtils.isEmpty(videoJsonString)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding videos to
        List<Video> videos = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON is
        // formatted, a JSONException exception object will be thrown.
        // Cash the exception so the app doesn't crash and prin the message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject videosJson = new JSONObject(videoJsonString);

            // Extract the JSONArray associated with the key called "results", which represents a
            // list of results (or videos)
            JSONArray videosJsonArray = videosJson.getJSONArray(THEMOVIEDB_RESULTS);

            // For each video found in the videosJsonArray, create a {@Link Video} object
            for ( int i = 0; i < videosJsonArray.length(); i++ ) {
                // Get a single video at position i within the list of videos
                JSONObject currentJsonVideo = videosJsonArray.getJSONObject(i);

                /* Extract the video Id value */
                String videoId = currentJsonVideo.getString(THEMOVIEDB_VIDEO_ID);

                /* Extract the value for the video language code */
                String videoLanguageCodeJson = currentJsonVideo.getString
                        (THEMOVIEDB_VIDEO_LANGUAGE_CODE);

                /* Extract the value for the video country code */
                String videoCountryCodeJson = currentJsonVideo.getString
                        (THEMOVIEDB_VIDEO_ISO_COUNTRY_CODE);

                /* Extract the value for the video key */
                String videoKeyJson = currentJsonVideo.getString(THEMOVIEDB_VIDEO_KEY);

                /* Extract the value for the video name */
                String videoNameJson = currentJsonVideo.getString(THEMOVIEDB_VIDEO_NAME);

                /* Extract the value for the video site */
                String videoSiteJson = currentJsonVideo.getString(THEMOVIEDB_VIDEO_SITE);

                /* Extract the value for the video siZe */
                String videoSizeJson = currentJsonVideo.getString(THEMOVIEDB_VIDEO_SIZE);

                /* Extract the value for the video type */
                String videoTypeJson = currentJsonVideo.getString(THEMOVIEDB_VIDEO_TYPE);

                /* Create a new {@Link Video} object with the video id, laguage code, country
                 * code, key, name, site, size and type.
                 */
                Video video = new Video (videoId, videoLanguageCodeJson, videoCountryCodeJson,
                        videoKeyJson, videoNameJson, videoSiteJson, videoSizeJson, videoTypeJson);

                /* Add the new {@Link Video} to the list of videos */
                videos.add(video);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception
            Log.e(LOG_TAG, "Problem parsing the video JSON resutls", e);
        }

        return videos;
    }


    private static List<Review> extractReviewFromJson (String reviewJsonString) {
        // If the JSON string is empty or null, then return early.
        if(TextUtils.isEmpty(reviewJsonString)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding reviews to
        List<Review> reviews = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON is
        // formatted, a JSONException exception object will be thrown.
        // Cash the exception so the app doesn't crash and prin the message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject reviewsJson = new JSONObject(reviewJsonString);

            // Extract the JSONArray associated with the key called "results", which represents a
            // list of results (or reviews)
            JSONArray reviewsJsonArray = reviewsJson.getJSONArray(THEMOVIEDB_RESULTS);

            // For each review found in the reviewsJsonArray, create a {@Link Review} object
            for ( int i = 0; i < reviewsJsonArray.length(); i++ ) {
                // Get a single review at position i within the list of videos
                JSONObject currentReviewJson = reviewsJsonArray.getJSONObject(i);

                /* Extract the review Id value */
                String reviewId = currentReviewJson.getString(THEMOVIEDB_REVIEW_ID);

                /* Extract the value for the author */
                String reviewAuthorJson = currentReviewJson.getString(THEMOVIEDB_REVIEW_AUTHOR);

                /* Extract the value for the review content */
                String reviewContentJson = currentReviewJson.getString
                        (THEMOVIEDB_REVIEW_CONTENT);

                /* Extract the value for the review url */
                String reviewUrlJson = currentReviewJson.getString(THEMOVIEDB_REVIEW_URL);

                /* Create a new {@Link Review} object with the review id, author, content and url */

                Review review = new Review(reviewId, reviewAuthorJson, reviewContentJson,
                        reviewUrlJson);

                /* Add the new {@Link Review} to the list of videos */
                reviews.add(review);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception
            Log.e(LOG_TAG, "Problem parsing the video JSON resutls", e);
        }

        return reviews;
    }
}

