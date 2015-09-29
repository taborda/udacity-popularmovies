package udacity.tab.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * Created by taborda on 9/27/15.
 */
public class MovieAdapter extends CursorAdapter {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    public MovieAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_movie, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String posterPath = cursor.getString(MovieDiscoveryFragment.COL_MOVIE_POSTER_PATH);

        Uri posterUri = MovieDBApi.buildMoviePosterUri(posterPath);

        Log.d(LOG_TAG, "Loading poster " + posterUri.toString());

        Picasso.with(context).load(posterUri).into(viewHolder.imageView);

    }

    public static class ViewHolder
    {
        public final ImageView imageView;

        public ViewHolder(View view)
        {
            imageView = (ImageView) view.findViewById(R.id.movie_imageview);
        }
    }
}
