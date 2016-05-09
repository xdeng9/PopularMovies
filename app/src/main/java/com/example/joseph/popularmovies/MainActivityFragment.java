package com.example.joseph.popularmovies;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.BufferedReader;
import java.net.HttpURLConnection;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        String imageUrl = "http://media.comicbook.com/2016/05/captain-america-civil-war-181827.jpg";

        ImageView imageView = (ImageView)rootView.findViewById(R.id.movie_image);

        Picasso.with(imageView.getContext())
                .load(imageUrl)
                .into(imageView);
        return rootView;
    }

    public class FetchMovieTask extends AsyncTask<String,Void,String[]> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(String...params){

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            //This string will contain the raw Json data
            String movieJsonStr = null;



            return null;
        }

        private String[] getPopularMovieDataFromJson(int numMovies) throws JSONException{

            return null;
        }

    }

}
