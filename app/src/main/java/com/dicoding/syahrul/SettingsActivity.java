package com.dicoding.syahrul;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.dicoding.syahrul.alarmManager.AlarmReceiver;
import com.dicoding.syahrul.databinding.SettingsActivityBinding;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    SettingsActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SettingsActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.title_activity_settings);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            Preference myPref = findPreference("language");
            assert myPref != null;
            myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                    startActivity(mIntent);
                    return true;
                }
            });
            Preference myPref2 = findPreference("alarm");
            assert myPref2 != null;
            myPref2.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    AlarmReceiver alarmReceiver = new AlarmReceiver();
                    if (newValue.equals(true)) {
                        alarmReceiver.setRepeatingAlarm(getActivity(), getString(R.string.time_alarm), getString(R.string.app_name), getString(R.string.message), getString(R.string.messageAction));
                    } else {
                        alarmReceiver.cancelAlarm(getActivity(), getString(R.string.messageAction2));
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Animatoo.animateSlideRight(SettingsActivity.this);
    }
}