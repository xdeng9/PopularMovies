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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    Movie []movieResult;
    GridView gridView;

    public MainActivityFragment() {
    }

   private void updateMovie(){
       FetchMovieTask movieTask = new FetchMovieTask();
       movieTask.execute();
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

        FetchMovieTask movieTask = new FetchMovieTask();
        movieTask.execute("");
        gridView = (GridView) rootView.findViewById(R.id.gridview);

        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
        updateMovie();
    }

    public class FetchMovieTask extends AsyncTask<String,Void,Movie[]> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        @Override
        protected Movie[] doInBackground(String...params){

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            //This string will contain the raw Json data
            String movieJsonStr = null;
            String sortMovieBy = params[0];
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

            try{
                return getMovieDataFromJson(movieJsonStr);
            }catch(JSONException e){
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        //Parse Json to useful data
        private Movie[] getMovieDataFromJson(String JsonStr) throws JSONException{

            final String MDB_RESULTS = "results";
            final String MDB_POSTER = "poster_path";
            final String MDB_OVERVIEW = "overview";
            final String MDB_RELEASE_DATE = "release_date";
            final String MDB_TITLE = "original_title";
            final String MDB_RATING= "vote_average";

            JSONObject movieJson = new JSONObject(JsonStr);
            JSONArray movieArray = movieJson.getJSONArray((MDB_RESULTS));

            Movie []movies = new Movie[movieArray.length()];

            for(int i=0; i<movieArray.length(); i++){
                Movie movieInfo;
                String  originalTitle;
                String imageUrl;
                String plotSummary;
                String userRating;
                String releaseDate;

                originalTitle = movieArray.getJSONObject(i).getString(MDB_TITLE);
                imageUrl = movieArray.getJSONObject(i).getString(MDB_POSTER);
                plotSummary = movieArray.getJSONObject(i).getString(MDB_OVERVIEW);
                userRating = movieArray.getJSONObject(i).getString(MDB_RATING);
                releaseDate = movieArray.getJSONObject(i).getString(MDB_RELEASE_DATE);

                movieInfo = new Movie(originalTitle,imageUrl,plotSummary,userRating,releaseDate);
                movies[i] = movieInfo;
                Log.v(LOG_TAG,"You are watching "+movieInfo.getMovieTitle()+" Poster url is"
                        +movieInfo.getImageUrl()+"\n");
            }

            return movies;
        }

        @Override
        protected void onPostExecute(Movie[] result){
            if(result != null) {
                movieResult = result;
                movieResult = buildPosterUrl(movieResult);
                gridView.setAdapter(new ImageAdapter(getActivity(), movieResult));
            }
        }

        private Movie[] buildPosterUrl(Movie[] movieResult){
            final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
            final String POSTER_SIZE = "w185";

            for(int i=0; i< movieResult.length; i++){
                String partialUrl;
                partialUrl = movieResult[i].getImageUrl();
                movieResult[i].setImageUrl(POSTER_BASE_URL+POSTER_SIZE+partialUrl);
            }

            return movieResult;
        }

    }

}