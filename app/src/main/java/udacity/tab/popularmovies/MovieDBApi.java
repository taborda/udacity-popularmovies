package udacity.tab.popularmovies;

import android.content.Context;
import android.net.Uri;

/**
 * Created by taborda on 9/27/15.
 */
public class MovieDBApi {

    public static final String API_KEY = "<Please Insert API KEY Here>";

    public static final String MOVIESDB_BASE_URL = "http://api.themoviedb.org/3";

    public static final String MOVIESDB_BASE_IMAGE_URL = "http://image.tmdb.org/t/p";

    public static final String PATH_MOST_POPULAR = "movie/popular";
    public static final String PATH_HIGHEST_RATED = "movie/top_rated";

    public enum MoviesOrder
    {
        MostPopular,
        HighestRated
    }

    public static Uri buildMoviePosterUri(String posterPath)
    {
        return Uri.parse(MOVIESDB_BASE_IMAGE_URL).buildUpon()
                .appendPath("w185")
                .appendEncodedPath(posterPath).build();
    }

    public static Uri getRequestUri(MoviesOrder order)
    {
        switch (order)
        {
            case MostPopular:
                return getMostPopularUri();
            case HighestRated:
                return getHighestRatedUri();
            default:
                throw new UnsupportedOperationException("Order requested not supported " + order.toString());
        }
    }

    private static Uri getMostPopularUri()
    {
        final String API_KEY_PARAM = "api_key";

        return Uri.parse(MOVIESDB_BASE_URL).buildUpon()
                .appendEncodedPath(PATH_MOST_POPULAR)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
    }

    private static Uri getHighestRatedUri()
    {
        final String API_KEY_PARAM = "api_key";

        return Uri.parse(MOVIESDB_BASE_URL).buildUpon()
                .appendEncodedPath(PATH_HIGHEST_RATED)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
    }

}
