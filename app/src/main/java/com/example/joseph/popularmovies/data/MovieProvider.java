package com.example.joseph.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by administrator on 6/26/16.
 */
public class MovieProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper dbHelper;

    static final int MOVIE = 1;
    static final int MOVIE_WITH_ID = 2;

    private static final String sMovieWithIdSelection = MovieContract.MovieEntry.TABLE_NAME +
            "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID + "= ?";

    static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = MovieContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, MovieContract.PATH, MOVIE);
        uriMatcher.addURI(authority, MovieContract.PATH + "/#", MOVIE_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {

        dbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_WITH_ID:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        switch (sUriMatcher.match(uri)) {
            case MOVIE:
            {
                return dbHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
            }
            case MOVIE_WITH_ID:
            {
                String id = uri.getPathSegments().get(1);
                return dbHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        sMovieWithIdSelection,
                        new String[]{id},
                        null,
                        null,
                        sortOrder
                );
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
        if(_id > 0)
            return MovieContract.MovieEntry.buildMovieUri(_id);
        else
            throw new android.database.SQLException("Failed to insert row into " + uri);

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted;

        if(sUriMatcher.match(uri) == MOVIE_WITH_ID){
            String id = uri.getPathSegments().get(1);
            rowsDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME, sMovieWithIdSelection, new String[]{id});
        }else{
            rowsDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsUpdated;

        if(sUriMatcher.match(uri) == MOVIE_WITH_ID){
            String id = uri.getPathSegments().get(1);
            rowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME, values, sMovieWithIdSelection, new String[]{id});
        }else{
            rowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME, values,selection, selectionArgs);
        }
        return rowsUpdated;
    }
}
