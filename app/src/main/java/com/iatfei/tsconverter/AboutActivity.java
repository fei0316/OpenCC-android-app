/*
 * Copyright (c) 2020-2021 Fei Kuan.
 *
 * This file is part of Chinese Converter
 * (see <https://github.com/fei0316/OpenCC-android-app>).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.iatfei.tsconverter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();

        Toolbar toolbar = findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.root_preferences);

            String version = BuildConfig.VERSION_NAME +
                    //"-" + BuildConfig.FLAVOR +
                    " v" + BuildConfig.VERSION_CODE;
            Preference ver = findPreference("edit_text_preference_2");
            if (ver != null) {
                ver.setSummary(version);
            }
            Preference license = findPreference("edit_text_preference_6");
            if (license != null) {
                license.setIntent(new Intent(getActivity(),OpenSourceActivity.class));
            }

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:"));
            PackageManager packageManager = requireActivity().getPackageManager();
            if(emailIntent.resolveActivity(packageManager) == null){
                Preference mail = findPreference("edit_text_preference_8");
                if (mail != null) {
                    Toast.makeText(getContext(), "R.string.shortcut_sent_toast", Toast.LENGTH_SHORT)
                            .show();
                    mail.setIntent(null);
                }
            }

            Intent webpageIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.example.com"));
            if(webpageIntent.resolveActivity(packageManager) == null) {
                Preference web = findPreference("edit_text_preference_4");
                if (web != null) {
                    web.setIntent(null);
                }
            }
        }
    }
}