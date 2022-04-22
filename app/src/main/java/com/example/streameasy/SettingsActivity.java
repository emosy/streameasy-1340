package com.example.streameasy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
        ListPreference preset, resolution;
        EditTextPreference destination, bitrate;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            preset = findPreference("preset");
            resolution = findPreference("resolution");
            bitrate = findPreference("bitrate");
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        }

        @Override
        public void onPause() {
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
            super.onPause();
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            switch (s) {
                case "preset":
                    String v = sharedPreferences.getString(s, "");
                    switch (v) {
                        case "low":
                            resolution.setValue("480");
                            bitrate.setText("2000");
                            break;
                        case "standard":
                            resolution.setValue("720");
                            bitrate.setText("6000");
                            break;
                        case "high":
                            resolution.setValue("1080");
                            bitrate.setText("9000");
                            break;
                        case "ludicrous":
                            resolution.setValue("1440");
                            bitrate.setText("18000");
                            break;
                        case "custom":
                        default:
                            break;
                    }
                    break; // end case "preset"
                case "resolution":
                case "bitrate":
                    String r = sharedPreferences.getString("resolution", "");
                    String b = sharedPreferences.getString("bitrate", "");
                    if (r.equals("480") && b.equals("2000")) {
                        preset.setValue("low");
                    } else if (r.equals("720") && b.equals("6000")) {
                        preset.setValue("standard");
                    } else if (r.equals("1080") && b.equals("9000")) {
                        preset.setValue("high");
                    } else if (r.equals("1440") && b.equals("18000")) {
                        preset.setValue("ludicrous");
                    } else {
                        preset.setValue("custom");
                    }
                    break; // end cases "resolution" and "bitrate"
                default:
                    ;
            }
        }
    }
}