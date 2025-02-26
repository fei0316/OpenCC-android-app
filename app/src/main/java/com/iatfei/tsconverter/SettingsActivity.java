/*
 * Copyright (c) 2020-2023 Fei Kuan.
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

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        MaterialToolbar toolbar = findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            // todo make popups material 3
            addPreferencesFromResource(R.xml.settings);

            // finding preferences and setting initial values
            final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(requireContext());
            final SwitchPreferenceCompat simpleSwitch = findPreference(Constant.PREF_SETTINGS_EASY_MODE);
            final SwitchPreferenceCompat autodetectSwitch = findPreference(Constant.PREF_SETTINGS_AUTODETECT_MODE);
            final ListPreference lpTraditional = findPreference(Constant.PREF_SETTINGS_TRAD_MODE);
            final ListPreference lpSimplified = findPreference(Constant.PREF_SETTINGS_SIMP_MODE);

            if (autodetectSwitch != null && lpTraditional != null && lpSimplified != null && simpleSwitch != null) {
                boolean easyMode = pref.getBoolean(Constant.PREF_SETTINGS_EASY_MODE, true);
                boolean autoDetect = pref.getBoolean(Constant.PREF_SETTINGS_AUTODETECT_MODE, true);
                autodetectSwitch.setEnabled(!easyMode);
                lpTraditional.setEnabled(!easyMode && autoDetect);
                lpSimplified.setEnabled(!easyMode && autoDetect);
                simpleSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
                    boolean autoDetect2 = pref.getBoolean(Constant.PREF_SETTINGS_AUTODETECT_MODE, true);
                    boolean newValEasy = (Boolean) newValue;
                    autodetectSwitch.setEnabled(!newValEasy);
                    lpTraditional.setEnabled(!newValEasy && autoDetect2);
                    lpSimplified.setEnabled(!newValEasy && autoDetect2);
                    return true;
                });
                autodetectSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
                    boolean newVal = (Boolean) newValue;
                    lpTraditional.setEnabled(newVal);
                    lpSimplified.setEnabled(newVal);
                    return true;
                });
            }
            ListPreference langSelectorPref = findPreference(Constant.PREF_SETTINGS_UI_LANGUAGE);
            LocaleListCompat selectedLocaleList = AppCompatDelegate.getApplicationLocales();
            List<String> supportedLocaleIDs = Arrays.asList(getResources().getStringArray(R.array.ui_lang_settings_languages_vals));
            List<String> supportedLocale = Arrays.asList(getResources().getStringArray(R.array.ui_lang_settings_languages));
            if (langSelectorPref != null ) {
                if (selectedLocaleList.isEmpty()) {
                    // no language override selected
                    langSelectorPref.setSummary(supportedLocale.get(0));
                    langSelectorPref.setValueIndex(0);
                } else {
                    Locale selectedLocale = selectedLocaleList.get(0);
                    String selectedLocaleString = selectedLocale != null ? selectedLocale.toLanguageTag() : "";
                    if (selectedLocaleString.equals("und")) {
                        // no language override selected, happens on Android 6?
                        langSelectorPref.setSummary(supportedLocale.get(0));
                        langSelectorPref.setValueIndex(0);
                    } else if (supportedLocaleIDs.contains(selectedLocaleString)) {
                        // language override selected. matches an item in the preference language list
                        langSelectorPref.setValue(selectedLocaleString);
                        langSelectorPref.setSummary(getResources()
                                .getStringArray(R.array.ui_lang_settings_languages)
                                    [supportedLocaleIDs.indexOf(selectedLocaleString)]);
                    } else {
                        // language override selected. does not match an item in our list (probably selected from system settings)
                        langSelectorPref.setValue(null);
                        langSelectorPref.setSummary(selectedLocaleString);
                    }
                }
                langSelectorPref.setOnPreferenceChangeListener((preference, newValue) -> {
                    LocaleListCompat newLocale = LocaleListCompat.forLanguageTags(newValue.toString());
                    AppCompatDelegate.setApplicationLocales(newLocale);
                    return true;
                });
            }
        }
    }
}