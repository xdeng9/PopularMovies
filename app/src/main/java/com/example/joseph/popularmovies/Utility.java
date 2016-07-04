package com.example.joseph.popularmovies;

/**
 * Created by administrator on 6/16/16.
 */
public class Utility {

    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE_w500 = "w500";
    private static final String POSTER_SIZE_w342 = "w342";
    private static final String YOUTUBE_TRAILER_BASE_URL = "http://www.youtube.com/watch?v=";
    private static final String YOUTUBE_THUMNAIL_BASE_URL = "http://img.youtube.com/vi/";

    public static String formatBackdropPath(String partialPath){

        return POSTER_BASE_URL+POSTER_SIZE_w500+partialPath;
    }

    public static String formateImageUrl(String partialUrl){

        return POSTER_BASE_URL+POSTER_SIZE_w342+partialUrl;
    }

    public static String formateTrailerUrl(String key){

        return YOUTUBE_TRAILER_BASE_URL+key;
    }

    public static String formateTrailThumnail(String key){

        return YOUTUBE_THUMNAIL_BASE_URL+key+"/1.jpg";
    }

    public static boolean isDouble(String input){
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }
}
