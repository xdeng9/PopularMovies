package com.example.joseph.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by administrator on 5/10/16.
 */
public class Movie implements Parcelable{

    private int id;
    private String originalTitle;
    private String imageUrl;
    private String plotSummary;
    private String userRating;
    private String releaseDate;
    private String backdrop;

    public Movie(int id, String originalTitle, String imageUrl, String plotSummary,
                 String userRating, String releaseDate, String backdrop){
        this.id = id;
        this.originalTitle = originalTitle;
        this.imageUrl = imageUrl;
        this.plotSummary = plotSummary;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.backdrop = backdrop;
    }

    public int getMovieId(){
        return id;
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

        if (Utility.isDouble(userRating)){
            return roundUserRating(userRating)+"/10";
        } else {
            return userRating;
        }

    }

    public String getReleaseDate(){
        return releaseDate;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setImageUrl(String url){
        imageUrl = url;
    }


    private String roundUserRating(String rating){
        double result = Double.parseDouble(rating);
        result = Math.round(result*10)/10.0;

        return ((Double)result).toString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(originalTitle);
        dest.writeString(imageUrl);
        dest.writeString(plotSummary);
        dest.writeString(userRating);
        dest.writeString(releaseDate);
        dest.writeString(backdrop);
    }

    private Movie(Parcel in){

        id = in.readInt();
        originalTitle = in.readString();
        imageUrl = in.readString();
        plotSummary = in.readString();
        userRating = in.readString();
        releaseDate = in.readString();
        backdrop = in.readString();
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
