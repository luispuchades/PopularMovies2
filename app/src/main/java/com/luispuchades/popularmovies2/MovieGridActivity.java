package com.luispuchades.popularmovies2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import butterknife.BindString;

public class MovieGridActivity extends AppCompatActivity {

    @BindString(R.string.settings_sorting_key)
    String mPrefSortingKey;

    @BindString(R.string.settings_order_by_default)
    String mPrefSortingDefault;

    @BindString(R.string.settings_order_by_most_popular_value)
    String mPrefSortingPopularValue;

    @BindString(R.string.settings_order_by_top_rated_value)
    String mPrefSortingTopRatedValue;

    @BindString(R.string.settings_favorites_value)
    String mPrefSortingFavoritesValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_grid);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
