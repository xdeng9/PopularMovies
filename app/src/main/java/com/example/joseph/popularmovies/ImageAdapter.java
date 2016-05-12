package com.example.joseph.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by administrator on 5/9/16.
 */
public class ImageAdapter extends BaseAdapter{

    private Context context;
    private Movie[] movies;

    public ImageAdapter(Context context, Movie[] movies){
        this.context = context;
        this.movies = movies;
    }

    @Override
    public int getCount() {
        return movies.length;
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

        if(convertView == null){
            imageView = new ImageView(context);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(context)
                .load(movies[position].getImageUrl())
                .into(imageView);

        return imageView;
    }
}