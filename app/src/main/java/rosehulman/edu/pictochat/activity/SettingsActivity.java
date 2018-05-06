package rosehulman.edu.pictochat.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.FirebaseDatabase;

import rosehulman.edu.pictochat.R;
import rosehulman.edu.pictochat.util.Constants;

public class SettingsActivity extends AppCompatActivity {

    public static final String KEY_PREF_DISPLAY_NAME = "pref_displayName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
        public SettingsFragment() {}

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onStop() {
            super.onStop();
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Preference logout = findPreference("pref_logout");
            logout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SettingsFragment.this.getContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.remove(Constants.KEY_PREF_USER_ID);
                    editor.commit();
                    return false;
                }
            });
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(KEY_PREF_DISPLAY_NAME)) {
                SharedPreferences prefs = getPreferenceManager().getSharedPreferences();
                String displayName = prefs.getString(KEY_PREF_DISPLAY_NAME, getString(R.string.no_name_given));
                FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(prefs.getString(Constants.KEY_PREF_USER_ID, null))
                        .child("display_name")
                        .setValue(displayName);
            }
        }
    }
}
