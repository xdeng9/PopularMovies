package com.example.joseph.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import com.example.joseph.popularmovies.data.MovieContract;
import com.example.joseph.popularmovies.data.MovieDbHelper;
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
    Movie[] movies;

    public MainActivityFragment() {
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
        mImageAdapter = new ImageAdapter(getActivity());
        gridView.setAdapter(mImageAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Movie movieItem = (Movie) mImageAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(MOVIE_PARCEL_KEY, movieItem);
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

    private void updateMovie() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort = pref.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_default));

        if(sort.equals("favorite")){
            getFavoriteMovies();
        } else{
            FetchMovieTask movieTask = new FetchMovieTask();
            movieTask.execute();
        }

    }

    private void getFavoriteMovies() {
        MovieDbHelper dbHelper = new MovieDbHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query(
                MovieContract.MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        Movie[] favmovies = new Movie[c.getCount()];
        Movie movieInfo;
        int movieId;
        String originalTitle;
        String imageUrl;
        String plotSummary;
        String userRating;
        String releaseDate;
        String backdrop;

        int i = 0;
        while (c.moveToNext()) {
            movieId = c.getInt(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
            originalTitle = c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_NAME));
            imageUrl = c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSETER_URL));
            plotSummary = c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW));
            userRating = c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RATING));
            releaseDate = c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE));
            backdrop = c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_HEADER_URL));

            movieInfo = new Movie(movieId, originalTitle, imageUrl,
                    plotSummary, userRating, releaseDate, backdrop);
            favmovies[i] = movieInfo;
            i++;
        }

        c.close();
        db.close();

//        for(Movie m: favmovies){
//            Log.v("imageUrl=", m.getImageUrl());
//        }

        mImageAdapter.addMovies(favmovies);
        //mImageAdapter.notifyDataSetChanged();
        gridView.setAdapter(mImageAdapter);

    }

    public class FetchMovieTask extends AsyncTask<String, Void, Movie[]> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        @Override
        protected Movie[] doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sort = pref.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_default));

            try {
                final String POPULAR_MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/popular?";
                final String TOP_MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/top_rated?";
                final String API_KEY_PARAM = "api_key";
                String movieUrl = POPULAR_MOVIE_BASE_URL;

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

        private Movie[] getMovieDataFromJson(String JsonStr) throws JSONException {

            final String MDB_ID = "id";
            final String MDB_RESULTS = "results";
            final String MDB_POSTER = "poster_path";
            final String MDB_OVERVIEW = "overview";
            final String MDB_RELEASE_DATE = "release_date";
            final String MDB_TITLE = "original_title";
            final String MDB_RATING = "vote_average";
            final String MDB_BACKDROP = "backdrop_path";

            JSONObject movieJson = new JSONObject(JsonStr);
            JSONArray movieArray = movieJson.getJSONArray((MDB_RESULTS));

            movies = new Movie[movieArray.length()];

            for (int i = 0; i < movieArray.length(); i++) {
                Movie movieInfo;
                int movieId;
                String originalTitle;
                String imageUrl;
                String plotSummary;
                String userRating;
                String releaseDate;
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
                mImageAdapter.addMovies(result);
                gridView.setAdapter(mImageAdapter);
            }
        }

    }

}
