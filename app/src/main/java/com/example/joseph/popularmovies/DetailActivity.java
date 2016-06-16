package com.example.joseph.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    Context context;
    int mId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Movie movie = intent.getExtras().getParcelable("movie");
        mId =movie.getMovieId();
        Log.i("Movie id: ", ""+mId);
        ((TextView) findViewById(R.id.movie_name_textview)).setText(movie.getMovieTitle());
        ((TextView) findViewById(R.id.movie_rating_textview)).setText(movie.getUserRating());
        ((TextView) findViewById(R.id.movie_release_date_textview)).setText(movie.getReleaseDate());
        ((TextView) findViewById(R.id.movie_plot_summary_textview)).setText(movie.getPlotSummary());
        setTitle(movie.getMovieTitle());
        ImageView movieImage = ((ImageView) findViewById(R.id.movie_poster_imageview));
        Picasso.with(context).load(movie.getImageUrl()).into(movieImage);
    }

    
}
