package com.example.joseph.popularmovies.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by administrator on 6/26/16.
 */
public class TestUriMatcher extends AndroidTestCase{

    private static final int  MOVIE_WITH_ID = 269149;
    private static final Uri TEST_MOVIE_DIR = MovieContract.MovieEntry.CONTENT_URI;
    private static final Uri TEST_MOVIE_WITH_ID = MovieContract.MovieEntry.buildMovieUri(MOVIE_WITH_ID);

    public void testUriMatcher(){
        UriMatcher testMatcher = MovieProvider.buildUriMatcher();
        assertEquals("Error: The MOVIE URI was matched incorrectly.", testMatcher.match(TEST_MOVIE_DIR),MovieProvider.MOVIE);
        assertEquals("Error: The MOVIE_WITH_ID URI was matched incorrectly.", testMatcher.match(TEST_MOVIE_WITH_ID)
                ,MovieProvider.MOVIE_WITH_ID);
    }
}
