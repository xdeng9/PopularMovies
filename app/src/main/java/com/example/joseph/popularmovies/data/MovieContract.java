package com.example.joseph.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by administrator on 6/16/16.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.joseph.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final String PATH ="movie";

    public static final class MovieEntry implements BaseColumns{
        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_NAME ="movie_name";
        public static final String COLUMN_MOVIE_RELEASE_DATE ="movie_release_date";
        public static final String COLUMN_MOVIE_RATING ="movie_rating";
        public static final String COLUMN_MOVIE_POSETER_URL = "movie_poster_url";
        public static final String COLUMN_MOVIE_OVERVIEW = "movie_overview";
        public static final String COLUMN_MOVIE_HEADER_URL = "movie_header_url";
    }
}
