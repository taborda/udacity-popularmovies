package udacity.tab.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import udacity.tab.popularmovies.data.MovieContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    private static final int MOVIE_LOADER = 2;

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_POPULARITY,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,
            MovieContract.MovieEntry.COLUMN_VOTE_COUNT,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE
    };

    static final int COL_MOVIE_ID = 0;
    static final int COL_MOVIE_MOVIE_ID = 1;
    static final int COL_MOVIE_TITLE = 2;
    static final int COL_MOVIE_ORIGINAL_TITLE = 3;
    static final int COL_MOVIE_POSTER_PATH = 4;
    static final int COL_MOVIE_POPULARITY = 5;
    static final int COL_MOVIE_OVERVIEW = 6;
    static final int COL_MOVIE_VOTE_AVERAGE = 7;
    static final int COL_MOVIE_VOTE_COUNT = 8;
    static final int COL_MOVIE_RELEASE_DATE = 9;

    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private TextView mYearTextView;
    private TextView mVoteAverageTextView;
    private TextView mOverviewTextView;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mTitleTextView = (TextView) rootView.findViewById(R.id.movie_title);
        mPosterImageView = (ImageView) rootView.findViewById(R.id.movie_poster);
        mYearTextView = (TextView) rootView.findViewById(R.id.movie_year);
        mVoteAverageTextView = (TextView) rootView.findViewById(R.id.movie_vote_average);
        mOverviewTextView = (TextView) rootView.findViewById(R.id.movie_overview);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            Uri uri = intent.getData();

            Log.d(LOG_TAG, "Movie Uri: " + uri);

            return new CursorLoader(getActivity(),
                    uri,
                    MOVIE_COLUMNS,
                    null,
                    null,
                    null);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) { return; }

        String movieTitle = data.getString(COL_MOVIE_ORIGINAL_TITLE);
        mTitleTextView.setText(movieTitle);

        String posterPath = data.getString(COL_MOVIE_POSTER_PATH);
        Uri posterUri = MovieDBApi.buildMoviePosterUri(posterPath);
        Picasso.with(getActivity()).load(posterUri).into(mPosterImageView);

        long releaseDate = data.getLong(COL_MOVIE_RELEASE_DATE);
        mYearTextView.setText(Long.toString(releaseDate));

        double voteAverage = data.getDouble(COL_MOVIE_VOTE_AVERAGE);
        mVoteAverageTextView.setText(Double.toString(voteAverage));

        String overView = data.getString(COL_MOVIE_OVERVIEW);
        mOverviewTextView.setText(overView);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
