package com.example.joseph.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Arrays;

/**
 * Created by administrator on 5/9/16.
 */
public class ImageAdapter extends BaseAdapter {

    private Context context;
    private Movie[] movies;

    public ImageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        if (movies == null) {
            return 0;
        } else {
            return movies.length;
        }

    }

    @Override
    public Object getItem(int position) {
        return movies[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(context);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(context)
                .load(movies[position].getImageUrl())
                .into(imageView);

        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setAdjustViewBounds(true);

        return imageView;
    }

    public void addMovies(Movie[] movies) {
        this.movies = movies;
    }

}
