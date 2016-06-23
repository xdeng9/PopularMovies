package com.example.joseph.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by administrator on 6/18/16.
 */
public class TrailerAdapter extends BaseAdapter {

    private Context context;
    private Trailer[] trailers;

    public TrailerAdapter(Context context, Trailer[] trailers){
        this.context = context;
        this.trailers = trailers;
    }
    @Override
    public int getCount() {
        return trailers.length;
    }

    @Override
    public Object getItem(int position) {
        return trailers[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_trailer, parent, false);
        }

        ((TextView)convertView.findViewById(R.id.trailer_name_textview)).setText(trailers[position].getTrailerTitle());
        ImageView imageView = (ImageView) convertView.findViewById(R.id.trail_imageview);
        Picasso.with(context)
                .load(trailers[position].getTrailerThumbnail())
                .into(imageView);

        convertView.setOnClickListener(new View.OnClickListener(){
            @Override
        public void onClick(View view){

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri
                        .parse(Utility.formateTrailerUrl(trailers[position].getTrailerUrl())));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
