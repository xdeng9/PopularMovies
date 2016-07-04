package com.example.joseph.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.LinearLayout;
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

/**
 * Created by administrator on 6/21/16.
 */
public class FetchMovieReviewTask extends AsyncTask<Integer, Void, Review[]> {

    private final String LOG_TAG = FetchMovieReviewTask.class.getSimpleName();
    private int mId;
    private Context mContext;
    LinearLayout mReviewLayout;

    public FetchMovieReviewTask(Context context, LinearLayout linearLayout) {

        this.mContext = context;
        this.mReviewLayout = linearLayout;
    }

    @Override
    protected Review[] doInBackground(Integer... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String reviewJsonStr = null;
        mId = params[0];

        try {
            final String TRAILER_URL = "http://api.themoviedb.org/3/movie/" + mId + "/reviews";
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
            reviewJsonStr = buffer.toString();

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
            return getReviewDataFromJson(reviewJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    private Review[] getReviewDataFromJson(String JsonStr) throws JSONException {

        final String MDB_AUTHOR = "author";
        final String MDB_CONTENT = "content";
        final String MDB_RESULTS = "results";

        JSONObject reviewJson = new JSONObject(JsonStr);
        JSONArray reviewArray = reviewJson.getJSONArray((MDB_RESULTS));

        Review[] reviews = new Review[reviewArray.length()];

        for (int i = 0; i < reviewArray.length(); i++) {
            Review reviewInfo;
            String author;
            String content;

            author = reviewArray.getJSONObject(i).getString(MDB_AUTHOR);
            content = reviewArray.getJSONObject(i).getString(MDB_CONTENT);

            reviewInfo = new Review(author, content);
            reviews[i] = reviewInfo;

        }

        return reviews;
    }

    @Override
    protected void onPostExecute(Review[] result) {

        if (result != null) {
            for (int i = 0; i < result.length; i++) {
                TextView tv = new TextView(mContext);
                tv.setText(result[i].getAuthor()+"\n"+result[i].getReview()+"\n\n");
                tv.setPadding(12,0,12,0);
                mReviewLayout.addView(tv);
            }
        }
    }
}
