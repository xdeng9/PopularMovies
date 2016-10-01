package com.example.joseph.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.joseph.popularmovies.data.MovieContract.MovieEntry;

/**
 * Creats Movie table and database
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME ="movie.db";
    static final int DATABASE_VERSION = 2;

    public MovieDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE "+
                MovieEntry.TABLE_NAME + " ("+
                MovieEntry._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                MovieEntry.COLUMN_MOVIE_ID+" INTEGER NOT NULL, "+
                MovieEntry.COLUMN_MOVIE_NAME+" TEXT NOT NULL, "+
                MovieEntry.COLUMN_MOVIE_RELEASE_DATE+" TEXT NOT NULL, "+
                MovieEntry.COLUMN_MOVIE_RATING+" TEXT NOT NULL, "+
                MovieEntry.COLUMN_MOVIE_POSETER_URL+" TEXT NOT NULL, "+
                MovieEntry.COLUMN_MOVIE_OVERVIEW+" TEXT NOT NULL, "+
                MovieEntry.COLUMN_MOVIE_HEADER_URL+" TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+ MovieEntry.TABLE_NAME);

        onCreate(db);
    }
}
