package com.example.joseph.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        String[] imageUrls = {
                "http://media.comicbook.com/2016/05/captain-america-civil-war-181827.jpg",
                "http://media.comicbook.com/2016/05/captain-america-civil-war-181827.jpg",
                "http://media.comicbook.com/2016/05/captain-america-civil-war-181827.jpg",
                "http://media.comicbook.com/2016/05/captain-america-civil-war-181827.jpg",
                "http://media.comicbook.com/2016/05/captain-america-civil-war-181827.jpg",
                "http://media.comicbook.com/2016/05/captain-america-civil-war-181827.jpg",
                "http://media.comicbook.com/2016/05/captain-america-civil-war-181827.jpg"
        };

        GridView gridView = (GridView) rootView.findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter(getContext(), imageUrls));

        FetchMovieTask movieTask = new FetchMovieTask();
        movieTask.execute();
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
            String api_key = "651b1c41da70293dcec9902d43fc47dc";

            try {
                final String POPULAR_MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/popular?";
                final String TOP_MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/top_rated?";
                final String API_KEY_PARAM = "api_key";

                Uri builturi = Uri.parse(POPULAR_MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, api_key).build();

                URL url = new URL(builturi.toString());

                Log.v(LOG_TAG, "Built URI "+ builturi.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null)
                    return null;

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line = reader.readLine()) != null){
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0)
                    return null;
                movieJsonStr = buffer.toString();
                Log.v(LOG_TAG, "Parsed movie string: "+movieJsonStr);

            }catch (IOException e){
                Log.e(LOG_TAG, "Error ", e);
                        return null;
            }finally {
                if (urlConnection !=null)
                    urlConnection.disconnect();
                if(reader != null){
                    try {
                        reader.close();
                    }catch (final IOException e){
                        Log.e(LOG_TAG, "Error closing stream: ", e);
                    }
                }
            }

            return null;
        }

        private String[] getPopularMovieDataFromJson(int numMovies) throws JSONException{

            return null;
        }

    }

}
