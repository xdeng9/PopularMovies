package com.example.joseph.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joseph.popularmovies.data.MovieContract;
import com.example.joseph.popularmovies.data.MovieDbHelper;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    Context mContext;
    int mId;
    LinearLayout trailerLayout;
    LinearLayout reviewLayout;
    Movie mMovie;
    Button favbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        mMovie = intent.getExtras().getParcelable("movie");

        trailerLayout = (LinearLayout) findViewById(R.id.trailers_layout);
        reviewLayout = (LinearLayout) findViewById(R.id.reviews_layout);

        mId = mMovie.getMovieId();
        ImageView backdrop = (ImageView) findViewById(R.id.backdrop_imageview);
        Picasso.with(mContext).load(mMovie.getBackdrop()).into(backdrop);
        ((TextView) findViewById(R.id.movie_title_textview)).setText(mMovie.getMovieTitle());
        ((TextView) findViewById(R.id.movie_rating_textview)).setText(mMovie.getUserRating());
        ((TextView) findViewById(R.id.movie_release_date_textview)).setText(mMovie.getReleaseDate());
        ((TextView) findViewById(R.id.movie_plot_summary_textview)).setText(mMovie.getPlotSummary());
        favbtn = (Button) findViewById(R.id.fav_btn);
        setTitle(mMovie.getMovieTitle());
        ImageView movieImage = ((ImageView) findViewById(R.id.movie_poster_imageview));
        Picasso.with(mContext).load(mMovie.getImageUrl()).into(movieImage);
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

    public void addMovieToFavorite(View v) {
//        MovieDbHelper dbHelper = new MovieDbHelper(getApplicationContext());
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mMovie.getMovieId());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_NAME, mMovie.getMovieTitle());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, mMovie.getReleaseDate());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_RATING, mMovie.getUserRating());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSETER_URL, mMovie.getImageUrl());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, mMovie.getPlotSummary());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_HEADER_URL, mMovie.getBackdrop());
       // db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
       // db.close();

        getApplicationContext().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);
        favbtn.setText("Remove from favorite");
        Toast.makeText(getApplicationContext(), "Movie added to favorite!", Toast.LENGTH_SHORT).show();
    }

}
