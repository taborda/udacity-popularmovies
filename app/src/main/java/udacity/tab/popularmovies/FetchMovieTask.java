package udacity.tab.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import udacity.tab.popularmovies.data.MovieContract.MovieEntry;

/**
 * Created by taborda on 9/27/15.
 */
public class FetchMovieTask extends AsyncTask<MovieDBApi.MoviesOrder, Void, Void> {

    private final static String LOG_TAG = FetchMovieTask.class.getSimpleName();

    private final Context mContext;

    public FetchMovieTask(Context context) {
        this.mContext = context;
    }

    private void getMovieDataFromJson(String movieJsonStr)
            throws JSONException {

        final String MOVIEDB_LIST = "results";

        final String MOVIEDB_MOVIE_ID = "id";
        final String MOVIEDB_ORIGINAL_TITLE = "original_title";
        final String MOVIEDB_OVERVIEW = "overview";
        final String MOVIEDB_RELEASE_DATE = "release_date";
        final String MOVIEDB_POSTER_PATH = "poster_path";
        final String MOVIEDB_POPULARITY = "popularity";
        final String MOVIEDB_TITLE = "title";
        final String MOVIEDB_VOTE_AVERAGE = "vote_average";
        final String MOVIEDB_VOTE_COUNT = "vote_count";


        try {
            JSONObject moviesJson = new JSONObject(movieJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(MOVIEDB_LIST);

            Vector<ContentValues> cVVector = new Vector<ContentValues>(moviesArray.length());

            for (int i = 0; i < moviesArray.length(); i++) {
                // These are the values that will be collected.
                int movie_id;
                String original_title;
                String overview;
                long release_date;
                String poster_path;
                double popularity;
                String title;
                double vote_average;
                int vote_count;

                JSONObject movie = moviesArray.getJSONObject(i);

                movie_id = movie.getInt(MOVIEDB_MOVIE_ID);
                original_title = movie.getString(MOVIEDB_ORIGINAL_TITLE);
                overview = movie.getString(MOVIEDB_OVERVIEW);

                release_date = 0;
                String strDate = movie.getString(MOVIEDB_RELEASE_DATE);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                try {
                    Date date = format.parse(strDate);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    release_date = cal.get(Calendar.YEAR);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                poster_path = movie.getString(MOVIEDB_POSTER_PATH);
                popularity = movie.getDouble(MOVIEDB_POPULARITY);
                title = movie.getString(MOVIEDB_TITLE);
                vote_average = movie.getDouble(MOVIEDB_VOTE_AVERAGE);
                vote_count = movie.getInt(MOVIEDB_VOTE_COUNT);

                ContentValues weatherValues = new ContentValues();

                weatherValues.put(MovieEntry.COLUMN_MOVIE_ID, movie_id);
                weatherValues.put(MovieEntry.COLUMN_ORIGINAL_TITLE, original_title);
                weatherValues.put(MovieEntry.COLUMN_OVERVIEW, overview);
                weatherValues.put(MovieEntry.COLUMN_RELEASE_DATE, release_date);
                weatherValues.put(MovieEntry.COLUMN_POSTER_PATH, poster_path);
                weatherValues.put(MovieEntry.COLUMN_POPULARITY, popularity);
                weatherValues.put(MovieEntry.COLUMN_TITLE, title);
                weatherValues.put(MovieEntry.COLUMN_VOTE_AVERAGE, vote_average);
                weatherValues.put(MovieEntry.COLUMN_VOTE_COUNT, vote_count);

                cVVector.add(weatherValues);
            }

            // add to database
            if (cVVector.size() > 0) {
                int rowsDeleted = mContext.getContentResolver().delete(MovieEntry.CONTENT_URI, null, null);
                Log.d(LOG_TAG, "Deleted " + rowsDeleted + " rows");

                mContext.getContentResolver().bulkInsert(
                        MovieEntry.CONTENT_URI,
                        cVVector.toArray(new ContentValues[1])
                );
            }

            Log.d(LOG_TAG, "FetchMovieTask Complete. " + cVVector.size() + " Inserted");

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(MovieDBApi.MoviesOrder... params) {

        if (params.length == 0) {
            return null;
        }

        MovieDBApi.MoviesOrder order = params[0];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJsonStr = null;

        try {
            Uri uri = MovieDBApi.getRequestUri(order);

            URL url = new URL(uri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            moviesJsonStr = buffer.toString();

            Log.d(LOG_TAG, "MovieJsonStr: " + moviesJsonStr);

            getMovieDataFromJson(moviesJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return null;
    }
}
