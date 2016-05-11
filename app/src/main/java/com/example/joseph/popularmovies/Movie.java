package com.example.joseph.popularmovies;

/**
 * Created by administrator on 5/10/16.
 */
public class Movie {

    private String originalTitle;
    private String imageUrl;
    private String plotSummary;
    private String userRating;
    private String releaseDate;

    public Movie(String originalTitle, String imageUrl, String plotSummary, String userRating, String releaseDate){
        this.originalTitle = originalTitle;
        this.imageUrl = imageUrl;
        this.plotSummary = plotSummary;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    public String getMovieTitle(){
        return originalTitle;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public String getPlotSummary(){
        return plotSummary;
    }

    public String getUserRating(){
        return userRating;
    }

    public String getReleaseDate(){
        return releaseDate;
    }

    public void setImageUrl(String url){
        imageUrl = url;
    }


}
