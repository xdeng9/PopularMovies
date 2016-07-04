package com.example.joseph.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by administrator on 6/18/16.
 */
public class FetchMovieTrailerTask extends AsyncTask<Integer, Void, Trailer[]> {

    private final String LOG_TAG = FetchMovieTrailerTask.class.getSimpleName();
    private int mId;
    private Context mContext;
    TrailerAdapter mTrailerAdapter;
    LinearLayout mTrailerLayout;

    public FetchMovieTrailerTask(Context context, LinearLayout linearLayout) {

        this.mContext = context;
        this.mTrailerLayout = linearLayout;
    }

    @Override
    protected Trailer[] doInBackground(Integer... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String trailerJsonStr = null;
        mId = params[0];

        try {
            final String TRAILER_URL = "http://api.themoviedb.org/3/movie/" + mId + "/videos";
            final String API_KEY_PARAM = "api_key";

            Uri builturi = Uri.parse(TRAILER_URL).buildUpon()
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.API_KEY)
                    .build();

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
            trailerJsonStr = buffer.toString();

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
            return getTrailerDataFromJson(trailerJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    private Trailer[] getTrailerDataFromJson(String JsonStr) throws JSONException {

        final String MDB_KEY = "key";
        final String MDB_NAME = "name";
        final String MDB_RESULTS = "results";

        JSONObject trailerJson = new JSONObject(JsonStr);
        JSONArray trailerArray = trailerJson.getJSONArray((MDB_RESULTS));

        Trailer[] trailers = new Trailer[trailerArray.length()];

        for (int i = 0; i < trailerArray.length(); i++) {
            Trailer trailerInfo;
            String trailerUrl;
            String trailerName;

            trailerUrl = trailerArray.getJSONObject(i).getString(MDB_KEY);
            trailerName = trailerArray.getJSONObject(i).getString(MDB_NAME);

            trailerInfo = new Trailer(trailerName, trailerUrl);
            trailers[i] = trailerInfo;

        }

        return trailers;
    }

    @Override
    protected void onPostExecute(Trailer[] result) {

        if (result != null) {
            mTrailerAdapter = new TrailerAdapter(mContext, result);
            for (int i = 0; i < mTrailerAdapter.getCount(); i++) {
                mTrailerLayout.addView(mTrailerAdapter.getView(i,null,null));
            }
        }
    }


}
