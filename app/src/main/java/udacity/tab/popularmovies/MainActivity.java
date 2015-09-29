package udacity.tab.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private static final String MOVIE_DISCOVERY_FRAGMENT = "movie_discovery_fragment";

    private MovieDBApi.MoviesOrder mMoviesOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesOrder = Utility.getPreferredSortOrder(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MovieDiscoveryFragment(), MOVIE_DISCOVERY_FRAGMENT)
                    .commit();
        }
    }

    @Override
    protected void onResume() {

        MovieDBApi.MoviesOrder order = Utility.getPreferredSortOrder(this);

        if (!order.equals(mMoviesOrder))
        {
            MovieDiscoveryFragment fragment = (MovieDiscoveryFragment) getSupportFragmentManager()
                    .findFragmentByTag(MOVIE_DISCOVERY_FRAGMENT);
            fragment.onSortOrderChanged();
            mMoviesOrder = order;
        }

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
