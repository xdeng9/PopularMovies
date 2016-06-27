package com.example.joseph.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

/**
 * Created by administrator on 6/27/16.
 */
public class TestUtilities extends AndroidTestCase {

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static ContentValues createTestValues(){
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, 269149);
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_NAME, "Zootopia");
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, 2016-02-11);
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_RATING, 7.46);
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSETER_URL, "/sM33SANp9z6rXW8Itn7NnG1GOEs.jpg");
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, "Determined to prove herself...");
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_HEADER_URL, "/mhdeE1yShHTaDbJVdWyTlzFvNkr.jpg");

        return values;
    }

    static long insertTestValues(Context context) {

        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createTestValues();

        long id;
        id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert North Pole Location Values", id != -1);

        return id;
    }

}
