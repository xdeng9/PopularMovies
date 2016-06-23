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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    Context mContext;
    int mId;
    LinearLayout trailerLayout;
    LinearLayout reviewLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Movie movie = intent.getExtras().getParcelable("movie");

        trailerLayout = (LinearLayout) findViewById(R.id.trailers_layout);
        reviewLayout = (LinearLayout) findViewById(R.id.reviews_layout);

        mId = movie.getMovieId();
        ImageView backdrop = (ImageView) findViewById(R.id.backdrop_imageview);
        Picasso.with(mContext).load(movie.getBackdrop()).into(backdrop);
        ((TextView) findViewById(R.id.movie_title_textview)).setText(movie.getMovieTitle());
        ((TextView) findViewById(R.id.movie_rating_textview)).setText(movie.getUserRating());
        ((TextView) findViewById(R.id.movie_release_date_textview)).setText(movie.getReleaseDate());
        ((TextView) findViewById(R.id.movie_plot_summary_textview)).setText(movie.getPlotSummary());
        setTitle(movie.getMovieTitle());
        ImageView movieImage = ((ImageView) findViewById(R.id.movie_poster_imageview));
        Picasso.with(mContext).load(movie.getImageUrl()).into(movieImage);
        //listView = (ListView) findViewById(R.id.trailer_listView);
        //Log.i("Post execute:", "Trailers size = " + mTrailerAdapter.getCount());
        //listView.setAdapter(mTrailerAdapter);
        getTrailers();
        getReviews();
    }

    private void getTrailers() {
        FetchMovieTrailerTask trailerTask = new FetchMovieTrailerTask(getApplicationContext(), trailerLayout);
        trailerTask.execute(mId);
    }

    private void getReviews() {
        FetchMovieReviewTask reviewTask = new FetchMovieReviewTask(getApplicationContext(), reviewLayout);
        reviewTask.execute(mId);

    }

    public void addMovieToFavorite() {
        Toast.makeText(getApplicationContext(), "Movie added to favorite!", Toast.LENGTH_SHORT).show();
    }

}
