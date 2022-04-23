package com.example.streameasy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
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

    // corrects behavior of action bar back button (does not work without this method)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
        ListPreference preset, resolution;
        EditTextPreference destination, fps, bitrate;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            preset = findPreference("preset");
            resolution = findPreference("resolution");
            fps = findPreference("fps");
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
                            fps.setText("30");
                            bitrate.setText("2000");
                            break;
                        case "standard":
                            resolution.setValue("720");
                            fps.setText("30");
                            bitrate.setText("6000");
                            break;
                        case "high":
                            resolution.setValue("1080");
                            fps.setText("30");
                            bitrate.setText("9000");
                            break;
                        case "ludicrous":
                            resolution.setValue("1440");
                            fps.setText("60");
                            bitrate.setText("18000");
                            break;
                        case "custom":
                        default:
                            break;
                    }
                    break; // end case "preset"
                case "resolution":
                case "fps":
                case "bitrate":
                    String r = sharedPreferences.getString("resolution", "");
                    String f = sharedPreferences.getString("fps", "");
                    String b = sharedPreferences.getString("bitrate", "");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    switch (r) {
                        case "480":
                            editor.putInt("width", 640);
                            editor.putInt("height", 480);
                            if (b.equals("2000") && f.equals("30")) {
                                preset.setValue("low");
                            }
                            break;
                        case "720":
                            editor.putInt("width", 1280);
                            editor.putInt("height", 720);
                            if (b.equals("6000") && f.equals("30")) {
                                preset.setValue("standard");
                            }
                            break;
                        case "1080":
                            editor.putInt("width", 1920);
                            editor.putInt("height", 1080);
                            if (b.equals("9000") && f.equals("30")) {
                                preset.setValue("high");
                            }
                            break;
                        case "1440":
                            editor.putInt("width", 2560);
                            editor.putInt("height", 1440);
                            if (b.equals("18000") && f.equals("60")) {
                                preset.setValue("ludicrous");
                            }
                            break;
                        default:
                            preset.setValue("custom");
                    }
                    editor.apply();
                    break; // end cases "resolution" and "bitrate"
                default:
                    ;
            }
        }
    }
}