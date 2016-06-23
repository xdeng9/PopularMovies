package com.example.joseph.popularmovies;

/**
 * Created by administrator on 6/21/16.
 */
public class Review {

    private String author;
    private String review;

    public Review(String author, String review){
        this.author = author;
        this.review = review;
    }

    public String getAuthor(){
        return author;
    }

    public String getReview(){
        return review;
    }
}
