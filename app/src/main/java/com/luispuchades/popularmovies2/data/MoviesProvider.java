package com.luispuchades.popularmovies2.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.luispuchades.popularmovies2.data.MoviesContract.*;

/**
 * This class serves as the ContentProvider for all of Movie's data. This class allows us to
 * bulkInsert data, query data, and delete data.
 * <p>
 * Although ContentProvider implementation requires the implementation of additional methods to
 * perform single inserts, updates, and the ability to get the type of the data from a URI.
 * However, here, they are not implemented for the sake of brevity and simplicity.
 */
public class MoviesProvider extends ContentProvider {

    /*
     * These constant will be used to match URIs with the data they are looking for. We will take
     * advantage of the UriMatcher class to make that matching MUCH easier than doing something
     * ourselves, such as using regular expressions.
     */

    /* Tag for Log Messages */
    private static final String LOG_TAG = MoviesProvider.class.getSimpleName();

    public static final int CODE_FAVORITES = 100;
    public static final int CODE_FAVORITES_ID = 101;

    /*
     * The URI Matcher used by this content provider. The leading "s" in this variable name
     * signifies that this UriMatcher is a static member variable of MovieProvider and is a
     * common convention in Android programming.
     */
    public static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDBHelper mMoviesDbHelper;

    /**
     * Creates the UriMatcher that will match each URI to the CODE_FAVORITES and
     * CODE_CODE_FAVORITES_ID constants defined above.
     * <p>
     * It's possible you might be thinking, "Why create a UriMatcher when you can use regular
     * expressions instead? After all, we really just need to match some patterns, and we can
     * use regular expressions to do that right?" Because you're not crazy, that's why.
     * <p>
     * UriMatcher does all the hard work for you. You just have to tell it which code to match
     * with which URI, and it does the rest automatically. Remember, the best programmers try
     * to never reinvent the wheel. If there is a solution for a problem that exists and has
     * been tested and proven, you should almost always use it unless there is a compelling
     * reason not to.
     *
     * @return A UriMatcher that correctly matches the constants for CODE_FAVORITES and
     * CODE_FAVORITES_ID.
     */
    public static UriMatcher buildUriMatcher() {

        /*
         * All paths added to the UriMatcher have a corresponding code to return when a match is
         * found. The code passed into the constructor of UriMatcher here represents the code to
         * return for the root URI. It's common to use NO_MATCH as the code for this case.
         */
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String movieAuthority = CONTENT_AUTHORITY;
        Log.d(LOG_TAG, "CONTENT_AUTHORITY: " + movieAuthority);

        /*
         * For each type of URI you want to add, create a corresponding code. Preferably, these are
         * constant fields in your class so that you can use them throughout the class and you no
         * they aren't going to change. In Sunshine, we use CODE_FAVORITES and CODE_FAVORITES_ID.
         */

        /* This URI is content://com.luispuchades.popularmovies2/movie/ */
        uriMatcher.addURI(movieAuthority, PATH_FAVORITES, CODE_FAVORITES);

        /*
         * This URI would look something like content://com.luispuchades.popularmovies2/movie/147222
         * The "/#" signifies to the UriMatcher that if PATH_FAVORITE is followed by ANY number,
         * that it should return the CODE_FAVORITES_ID code.
         */
        uriMatcher.addURI(movieAuthority, PATH_FAVORITES
                + "/#", CODE_FAVORITES_ID);

        return uriMatcher;
    }

    /**
     * In onCreate, we initialize our content provider on startup. This method is called for all
     * registered content providers on the application main thread at application launch time.
     * It must not perform lengthy operations, or application startup will be delayed.
     *
     * Nontrivial initialization (such as opening, upgrading, and scanning
     * databases) should be deferred until the content provider is used (via {@link #query},
     * {@link #bulkInsert(Uri, ContentValues[])}, etc). (Check Sunshine App for more information)
     *
     * Deferred initialization keeps application startup fast, avoids unnecessary work if the
     * provider turns out not to be needed, and stops database errors (such as a full disk) from
     * halting application launch.
     *
     * @return true if the provider was successfully loaded, false otherwise
     */
    @Override
    public boolean onCreate() {
        /*
         * As noted in the comment above, onCreate is run on the main thread, so performing any
         * lengthy operations will cause lag in your app. Since MovieDbHelper's constructor is
         * very lightweight, we are safe to perform that initialization here.
         */
        mMoviesDbHelper = new MoviesDBHelper(getContext());
        return true;
    }

    /**
     * Handles query requests from clients. We will use this method in Popular Movies to query for
     * all of our movie data as well as to query for a particular movie.
     *
     * @param uri           The URI to query
     * @param projection    The list of columns to put into the cursor. If null, all columns are
     *                      included.
     * @param selection     A selection criteria to apply when filtering rows. If null, then all
     *                      rows are included.
     * @param selectionArgs You may include ?s in selection, which will be replaced by
     *                      the values from selectionArgs, in order that they appear in the
     *                      selection.
     * @param sortOrder     How the rows in the cursor should be sorted.
     * @return A Cursor containing the results of the query. In our implementation,
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[]
            selectionArgs, String sortOrder) {

        final SQLiteDatabase db = mMoviesDbHelper.getReadableDatabase();

        Cursor cursor;

        /*
         * Here's the switch statement that, given a URI, will determine what kind of request is
         * being made and query the database accordingly.
         */
        switch (sUriMatcher.match(uri)) {

            /*
             * When sUriMatcher's match method is called with a URI that looks EXACTLY like this
             *
             *      content://com.luispuchades.popularmovies2/movie/
             *
             * sUriMatcher's match method will return the code that indicates to us that we need
             * to return all of the weather in our weather table.
             *
             * In this case, we want to return a cursor that contains every row of weather data
             * in our weather table.
             */
            case CODE_FAVORITES:

                cursor = db.query(MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            /*
             * When sUriMatcher's match method is called with a URI that looks something like this
             *
             *      content://com.luispuchades.popularmovies2/movie/147222
             *
             * sUriMatcher's match method will return the code that indicates to us that we need
             * to return a particular movie.
             *
             * In this case, we want to return a cursor that contains one row of movie data for
             * a particular movie.
             */
            case CODE_FAVORITES_ID:
                selection = MovieEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                /*
                 * In order to determine the date associated with this URI, we look at the last
                 * path segment.
                 */
                cursor = db.query(MovieEntry.TABLE_NAME,
                        /*
                         * A projection designates the columns we want returned in our Cursor.
                         * Passing null will return all columns of data within the Cursor.
                         * However, if you don't need all the data from the table, it's best
                         * practice to limit the columns returned in the Cursor with a projection.
                         */
                        projection,
                        /*
                         * The URI that matches CODE_FAVORITES_ID contains a date at the end
                         * of it. We extract that date and use it with these next two lines to
                         * specify the row of movie we want returned in the cursor. We use a
                         * question mark here and then designate selectionArguments as the next
                         * argument for performance reasons. Whatever Strings are contained
                         * within the selectionArguments array will be inserted into the
                         * selection statement by SQLite under the hood.
                         */
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Query doesn't support: " + uri.toString());
        }
        return cursor;
    }

    /**
     * In Popular Movies, we aren't going to do anything with this method. However, we are
     * required to override it as MoiveProvider extends ContentProvider and getType is an
     * abstract method in ContentProvider. Normally, this method handles requests for the MIME
     * type of the data at the given URI. For example, if your app provided images at a
     * particular URI, then you would return an image URI from this method.
     *
     * @param uri the URI to query.
     * @return nothing in Popular Movies, but normally a MIME type string, or null if there is no
     * type.
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("We are not implementing getType in Sunshine.");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();
        Uri insertRecord = null;

        switch (sUriMatcher.match(uri)) {

            case CODE_FAVORITES:

                long insertRecordId = db.insert(MovieEntry.TABLE_NAME,
                        null,
                        values);

                if (insertRecordId > 0) {
                    insertRecord = ContentUris.withAppendedId(MovieEntry.CONTENT_URI,
                            insertRecordId);
                }
                break;

            default:
                throw new IllegalArgumentException("insert doesn't support: " + uri.toString());
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return insertRecord;
    }

    /**
     * Deletes data at a given URI with optional arguments for more fine tuned deletions.
     *
     * @param uri           The full URI to query
     * @param selection     An optional restriction to apply to rows when deleting.
     * @param selectionArgs Used in conjunction with the selection statement
     * @return The number of rows deleted
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();

        /* Users of the delete method will expect the number of rows deleted to be returned. */

        int numRowsDeleted = 0;

        /*
         * If we pass null as the selection to SQLiteDatabase#delete, our entire table will be
         * deleted. However, if we do pass null and delete all of the rows in the table, we won't
         * know how many rows were deleted. According to the documentation for SQLiteDatabase,
         * passing "1" for the selection will delete all rows and return the number of rows
         * deleted, which is what the caller of this method expects.
         */
        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITES:

                db.delete(MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("delete doen't support: " + uri.toString());
        }
        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new RuntimeException("update() is not implemented in Popular Movies 2");
    }

    /**
     * You do not need to call this method. This is a method specifically to assist the testing
     * framework in running smoothly. You can read more at:
     * http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
     */
    @Override
    @TargetApi(11)
    public void shutdown() {
        mMoviesDbHelper.close();
        super.shutdown();
    }
}
