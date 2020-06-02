package com.iatfei.tsconverter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Objects;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            String version = BuildConfig.VERSION_NAME +
                    //"-" + BuildConfig.FLAVOR +
                    " v" + BuildConfig.VERSION_CODE;
            Preference ver = findPreference("edit_text_preference_2");
            Objects.requireNonNull(ver).setSummary(version);
            Preference license = findPreference("edit_text_preference_6");
            Objects.requireNonNull(license).setIntent(new Intent(getActivity(),OpenSourceActivity.class));

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setType("*/*"); //todo:make sure it works on older versions
            PackageManager packageManager = requireActivity().getPackageManager();
            if(emailIntent.resolveActivity(packageManager) == null){
                Preference mail = findPreference("edit_text_preference_8");
                Objects.requireNonNull(mail).setIntent(null);
            }

            Intent webpageIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.example.com"));
            if(webpageIntent.resolveActivity(packageManager) == null) {
                Preference web = findPreference("edit_text_preference_4");
                Objects.requireNonNull(web).setIntent(null);
            }
        }
    }
}