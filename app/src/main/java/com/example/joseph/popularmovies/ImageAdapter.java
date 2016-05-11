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
    private String[] posterUrl;

    public ImageAdapter(Context context, String[] posterUrl){
        this.context = context;
        this.posterUrl = posterUrl;
    }

    @Override
    public int getCount() {
        return posterUrl.length;
    }

    @Override
    public Object getItem(int position) {
        return posterUrl[position];
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
            imageView.setPadding(5,5,5,5);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(context)
                .load(posterUrl[position])
                .into(imageView);

        return imageView;
    }
}
