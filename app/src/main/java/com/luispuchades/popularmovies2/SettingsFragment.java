package com.luispuchades.popularmovies2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

/**
 * The SettingsFragment serves as the display for all of the user's settings. In Popular Movies, the
 * user will be able to change their preferences for the most popular movies or the top rated
 * movies.
 */

public class SettingsFragment extends PreferenceFragment implements Preference
        .OnPreferenceChangeListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_main);

        Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
        bindPreferenceSummaryToValue(orderBy);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        String stringNewValue = newValue.toString();

        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringNewValue);
            if(prefIndex >= 0) {
                CharSequence[] labels = listPreference.getEntries();
                preference.setSummary(labels[prefIndex]);
            }
        } else {
            preference.setSummary(stringNewValue);
        }
        return true;
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(this);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference
                .getContext());
        String preferenceString = preferences.getString(preference.getKey(), "");
        onPreferenceChange(preference, preferenceString);
    }
}