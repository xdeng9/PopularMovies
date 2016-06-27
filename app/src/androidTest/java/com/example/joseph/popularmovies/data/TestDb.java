package com.example.joseph.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by administrator on 6/24/16.
 */
public class TestDb extends AndroidTestCase {

    void deleteDataBase(){
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
    }

    public void setUp() {
        deleteDataBase();
    }

    public void testCreateDb(){
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(MovieContract.MovieEntry.TABLE_NAME);
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new MovieDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        c = db.rawQuery("PRAGMA table_info(" + MovieContract.MovieEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        final HashSet<String> columnNameHashSet = new HashSet<String>();
        columnNameHashSet.add(MovieContract.MovieEntry._ID);
        columnNameHashSet.add(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
        columnNameHashSet.add(MovieContract.MovieEntry.COLUMN_MOVIE_NAME);
        columnNameHashSet.add(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE);
        columnNameHashSet.add(MovieContract.MovieEntry.COLUMN_MOVIE_RATING);
        columnNameHashSet.add(MovieContract.MovieEntry.COLUMN_MOVIE_POSETER_URL);
        columnNameHashSet.add(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW);
        columnNameHashSet.add(MovieContract.MovieEntry.COLUMN_MOVIE_HEADER_URL);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            columnNameHashSet.remove(columnName);
        } while(c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                columnNameHashSet.isEmpty());
        db.close();
    }

    public void testMovieTable(){

        MovieDbHelper dbHelper = new MovieDbHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, 269149);
        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_NAME, "Zootopia");
        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, "2016-02-11");
        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RATING, "7.46");
        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSETER_URL, "/sM33SANp9z6rXW8Itn7NnG1GOEs.jpg");
        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,"something");
        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_HEADER_URL, "/mhdeE1yShHTaDbJVdWyTlzFvNkr.jpg");

        long rowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, testValues);
        assertTrue(rowId != -1);

        Cursor cursor = db.query(
                MovieContract.MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        assertTrue( "Error: No Records returned from location query", cursor.moveToFirst() );

        Set<Map.Entry<String, Object>> valueSet = testValues.valueSet();
        for(Map.Entry<String, Object> entry : valueSet){
            String columnName = entry.getKey();
            int idx = cursor.getColumnIndex(columnName);
            assertTrue("Column "+columnName+" not found.", idx !=-1);
            String value = entry.getValue().toString();
            assertTrue("Values did not match!",cursor.getString(idx).equals(value));
        }

        cursor.close();
        db.close();
    }
}
