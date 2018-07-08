package de.tum.in.tca.ticketcheck.component.settings;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.view.View;

import de.tum.in.tca.ticketcheck.R;
import de.tum.in.tca.ticketcheck.component.onboarding.StartupActivity;
import de.tum.in.tca.ticketcheck.utils.Const;
import de.tum.in.tca.ticketcheck.utils.Utils;

public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener {

    public static final String FRAGMENT_TAG = "my_preference_fragment";
    private static final String BUTTON_CLEAR_CACHE = "button_clear_cache";
    private static final String SETUP_EDUROAM = "card_eduroam_setup";

    private FragmentActivity mContext;

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        //Load the correct preference category
        setPreferencesFromResource(R.xml.settings, rootKey);
        mContext = getActivity();

        //Only do these things if we are in the root of the preferences
        if (rootKey == null) {
            // Click listener for preference list entries. Used to simulate a button
            // (since it is not possible to add a button to the preferences screen)
            findPreference(BUTTON_CLEAR_CACHE).setOnPreferenceClickListener(this);

            setSummary("card_default_campus");
            setSummary("silent_mode_set_to");
            setSummary("background_mode_set_to");
        } else if (rootKey.equals("card_cafeteria")) {
            setSummary("card_cafeteria_default_G");
            setSummary("card_cafeteria_default_K");
            setSummary("card_cafeteria_default_W");
            setSummary("card_role");
        } else if (rootKey.equals("card_mvv")) {
            setSummary("card_stations_default_G");
            setSummary("card_stations_default_C");
            setSummary("card_stations_default_K");
        } else if (rootKey.equals("card_eduroam")) {
            findPreference(SETUP_EDUROAM).setOnPreferenceClickListener(this);
        }

        // Register the change listener to react immediately on changes
        PreferenceManager.getDefaultSharedPreferences(mContext)
                         .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the default white background in the view so as to avoid transparency
        view.setBackgroundColor(Color.WHITE);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);
        if (pref instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) pref;
            String entry = listPreference.getEntry().toString();
            listPreference.setSummary(entry);
        }

        // When newspread selection changes
        // deselect all newspread sources and select only the
        // selected source if one of all was selected before
        if ("news_newspread".equals(key)) {
            SharedPreferences.Editor e = sharedPreferences.edit();
            boolean value = false;
            for (int i = 7; i < 14; i++) {
                if (sharedPreferences.getBoolean("card_news_source_" + i, false)) {
                    value = true;
                }
                e.putBoolean("card_news_source_" + i, false);
            }
            String newSource = sharedPreferences.getString(key, "7");
            e.putBoolean("card_news_source_" + newSource, value);
            e.apply();
        }
    }

    private void setSummary(CharSequence key) {
        Preference pref = findPreference(key);
        if (pref instanceof ListPreference) {
            ListPreference listPref = (ListPreference) pref;
            String entry = listPref.getEntry().toString();
            listPref.setSummary(entry);
        }
    }

    /**
     * Handle all clicks on 'button'-preferences
     *
     * @param preference Preference that has been clicked
     * @return True, if handled
     */
    @Override
    public boolean onPreferenceClick(Preference preference) {
        final String key = preference.getKey();

        switch (key) {
            case BUTTON_CLEAR_CACHE:
                // This button invokes the clear cache method
                new AlertDialog.Builder(mContext)
                        .setMessage(R.string.delete_cache_sure)
                        .setPositiveButton(R.string.delete, (dialogInterface, i) -> clearCache())
                        .setNegativeButton(R.string.cancel, null)
                        .show();
                break;
            default:
                return false;
        }

        return true;
    }

    /**
     * Clears all downloaded data from SD card and database
     */
    private void clearCache() {
        Utils.showToast(mContext, R.string.success_clear_cache);
        Utils.setSetting(mContext, Const.EVERYTHING_SETUP, false);

        mContext.finish();
        startActivity(new Intent(mContext, StartupActivity.class));
    }

}