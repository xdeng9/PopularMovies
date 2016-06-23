package com.example.joseph.popularmovies;

/**
 * Created by administrator on 6/18/16.
 */
public class Trailer {

    private String trailerTitle;
    private String trailerUrl;

    public Trailer(String trailerTitle, String trailerUrl){
        this.trailerTitle = trailerTitle;
        this.trailerUrl = trailerUrl;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }


    public String getTrailerTitle() {
        return trailerTitle;
    }

    public String getTrailerThumbnail(){
        return Utility.formateTrailThumnail(trailerUrl);
    }

}
