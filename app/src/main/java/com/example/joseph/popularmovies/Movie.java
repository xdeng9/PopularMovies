package com.example.joseph.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by administrator on 5/10/16.
 */
public class Movie implements Parcelable{

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(originalTitle);
        dest.writeString(imageUrl);
        dest.writeString(plotSummary);
        dest.writeString(userRating);
        dest.writeString(releaseDate);
    }

    private Movie(Parcel in){

        String [] data = new String[5];

        in.readStringArray(data);
        this.originalTitle = data[0];
        this.imageUrl = data[1];
        this.plotSummary = data[2];
        this.userRating = data[3];
        this.releaseDate = data[4];
    }

    public static final Parcelable.Creator<Movie> CREATOR =
            new Parcelable.Creator<Movie>(){

                @Override
                public Movie createFromParcel(Parcel source){
                    return new Movie(source);
                }

                @Override
            public Movie[] newArray(int size){
                    return new Movie[size];
                }

            };
}
