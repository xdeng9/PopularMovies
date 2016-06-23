package com.example.joseph.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

    GridView gridView;
    ImageAdapter mImageAdapter;
    final String MOVIE_PARCEL_KEY = "movie";

    public MainActivityFragment() {
    }

    private void updateMovie() {
        FetchMovieTask movieTask = new FetchMovieTask();
        movieTask.execute();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        gridView = (GridView) rootView.findViewById(R.id.gridview);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Movie movieItem = (Movie) mImageAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
               // intent.putExtra(Intent.EXTRA_TEXT,movieItem.getUserRating());
                intent.putExtra(MOVIE_PARCEL_KEY,movieItem);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovie();
    }

    public class FetchMovieTask extends AsyncTask<String, Void, Movie[]> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        @Override
        protected Movie[] doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            //This string will contain the raw Json data
            String movieJsonStr = null;

            try {
                final String POPULAR_MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/popular?";
                final String TOP_MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/top_rated?";
                final String API_KEY_PARAM = "api_key";
                String movieUrl = POPULAR_MOVIE_BASE_URL;

                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String sort = pref.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_default));

                if (sort.equals("top")) {
                    movieUrl = TOP_MOVIE_BASE_URL;
                }

                Uri builturi = Uri.parse(movieUrl).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, BuildConfig.API_KEY).build();

                URL url = new URL(builturi.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null)
                    return null;

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                movieJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream: ", e);
                    }
                }
            }

            try {
                return getMovieDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        //Parse Json to useful data
        private Movie[] getMovieDataFromJson(String JsonStr) throws JSONException {

            final String MDB_ID = "id";
            final String MDB_RESULTS = "results";
            final String MDB_POSTER = "poster_path";
            final String MDB_OVERVIEW = "overview";
            final String MDB_RELEASE_DATE = "release_date";
            final String MDB_TITLE = "original_title";
            final String MDB_RATING = "vote_average";
            final String MDB_BACKDROP="backdrop_path";

            JSONObject movieJson = new JSONObject(JsonStr);
            JSONArray movieArray = movieJson.getJSONArray((MDB_RESULTS));

            Movie[] movies = new Movie[movieArray.length()];

            for (int i = 0; i < movieArray.length(); i++) {
                Movie movieInfo;
                String originalTitle;
                String imageUrl;
                String plotSummary;
                String userRating;
                String releaseDate;
                int movieId;
                String backdrop;

                movieId = movieArray.getJSONObject(i).getInt(MDB_ID);
                originalTitle = movieArray.getJSONObject(i).getString(MDB_TITLE);
                imageUrl = movieArray.getJSONObject(i).getString(MDB_POSTER);
                plotSummary = movieArray.getJSONObject(i).getString(MDB_OVERVIEW);
                userRating = movieArray.getJSONObject(i).getString(MDB_RATING);
                releaseDate = movieArray.getJSONObject(i).getString(MDB_RELEASE_DATE);
                backdrop = movieArray.getJSONObject(i).getString(MDB_BACKDROP);

                movieInfo = new Movie(movieId, originalTitle, Utility.formateImageUrl(imageUrl),
                        plotSummary, userRating, releaseDate, Utility.formatBackdropPath(backdrop));
                movies[i] = movieInfo;

            }

            return movies;
        }

        @Override
        protected void onPostExecute(Movie[] result) {
            if (result != null) {
                mImageAdapter = new ImageAdapter(getActivity(), result);
                gridView.setAdapter(mImageAdapter);
            }
        }

    }

}
