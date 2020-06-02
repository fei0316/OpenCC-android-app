/*
 * Copyright (c) 2020-2020 Fei Kuan.
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