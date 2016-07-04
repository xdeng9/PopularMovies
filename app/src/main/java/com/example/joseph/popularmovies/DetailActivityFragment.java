package com.example.joseph.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joseph.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * Created by administrator on 6/27/16.
 */
public class DetailActivityFragment extends Fragment {

    int mId;
    LinearLayout trailerLayout;
    LinearLayout reviewLayout;
    Movie mMovie;
    Button favbtn;
    boolean isFavorite;
    String btnText = "Remove from favorite";

    public DetailActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Bundle args = getArguments();
        if(args != null){
            mMovie = args.getParcelable("movie");
        }else{
            Intent intent = getActivity().getIntent();
            mMovie = intent.getExtras().getParcelable("movie");
        }

        favbtn = (Button) rootView.findViewById(R.id.fav_btn);
        trailerLayout = (LinearLayout) rootView.findViewById(R.id.trailers_layout);
        reviewLayout = (LinearLayout) rootView.findViewById(R.id.reviews_layout);
        mId = mMovie.getMovieId();

        Cursor c = getContext().getContentResolver().query(
                MovieContract.MovieEntry.buildMovieUri(mId),
                null,
                null,
                null,
                null
        );

        if (c.getCount() == 0) {
            isFavorite = false;
        } else {
            isFavorite = true;
            favbtn.setText(btnText);
        }

        ImageView backdrop = (ImageView) rootView.findViewById(R.id.backdrop_imageview);
        Picasso.with(getContext()).load(mMovie.getBackdrop()).into(backdrop);
        ((TextView) rootView.findViewById(R.id.movie_title_textview)).setText(mMovie.getMovieTitle());
        ((TextView) rootView.findViewById(R.id.movie_rating_textview)).setText(mMovie.getUserRating());
        ((TextView) rootView.findViewById(R.id.movie_release_date_textview)).setText(mMovie.getReleaseDate());
        ((TextView) rootView.findViewById(R.id.movie_plot_summary_textview)).setText(mMovie.getPlotSummary());

        ImageView movieImage = ((ImageView) rootView.findViewById(R.id.movie_poster_imageview));
        Picasso.with(getContext()).load(mMovie.getImageUrl()).into(movieImage);
        getTrailers();
        getReviews();

        favbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorite) {
                    isFavorite = false;
                    removeMovieFromFavorite();
                    return;
                }
                ContentValues values = new ContentValues();
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mMovie.getMovieId());
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_NAME, mMovie.getMovieTitle());
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, mMovie.getReleaseDate());
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_RATING, mMovie.getUserRating());
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSETER_URL, mMovie.getImageUrl());
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, mMovie.getPlotSummary());
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_HEADER_URL, mMovie.getBackdrop());

                isFavorite = true;
                getContext().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);
                favbtn.setText(btnText);
                Toast.makeText(getContext(), "Movie added to favorite!", Toast.LENGTH_SHORT).show();
            }
        });

        c.close();

        return rootView;
    }

    private void getTrailers() {
        FetchMovieTrailerTask trailerTask = new FetchMovieTrailerTask(getContext(), trailerLayout);
        trailerTask.execute(mId);
    }

    private void getReviews() {
        FetchMovieReviewTask reviewTask = new FetchMovieReviewTask(getContext(), reviewLayout);
        reviewTask.execute(mId);

    }

    public void removeMovieFromFavorite() {

        getContext().getContentResolver().delete(
                MovieContract.MovieEntry.buildMovieUri(mId),
                null,
                null
        );
        favbtn.setText("Add to favorite");
        Toast.makeText(getContext(), "Movie removed from favorite.", Toast.LENGTH_SHORT).show();
    }

}
