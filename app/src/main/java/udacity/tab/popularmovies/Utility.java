package udacity.tab.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by taborda on 9/27/15.
 */
public class Utility {
    public static MovieDBApi.MoviesOrder getPreferredSortOrder(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String sort_order = prefs.getString(context.getString(R.string.pref_order_key),
                context.getString(R.string.pref_order_most_popular));

        if (sort_order.equals(context.getString(R.string.pref_order_most_popular)))
            return MovieDBApi.MoviesOrder.MostPopular;
        else if (sort_order.equals(context.getString(R.string.pref_order_highest_rated)))
            return MovieDBApi.MoviesOrder.HighestRated;

        return MovieDBApi.MoviesOrder.MostPopular;
    }
}
