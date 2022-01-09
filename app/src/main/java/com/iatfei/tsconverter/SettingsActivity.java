/*
 * Copyright (c) 2020-2022 Fei Kuan.
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
import androidx.appcompat.widget.Toolbar;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import java.util.Objects;

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
            addPreferencesFromResource(R.xml.settings);

            final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(requireContext());
            final SwitchPreference simpleSwitch = findPreference("switch_preference_1");
            final SwitchPreference autodetectSwitch = findPreference("switch_preference_2");
            final ListPreference lpTraditional = findPreference("list_preference_1");
            final ListPreference lpSimplified = findPreference("list_preference_2");

            if (autodetectSwitch != null && lpTraditional != null && lpSimplified != null) {
                if (pref.getBoolean("switch_preference_1", true)) {
                    lpTraditional.setEnabled(false);
                    lpSimplified.setEnabled(false);
                    autodetectSwitch.setEnabled(false);
                } else {
                    autodetectSwitch.setEnabled(true);
                    if (pref.getBoolean("switch_preference_2", true)) {
                        lpTraditional.setEnabled(true);
                        lpSimplified.setEnabled(true);
                    } else {
                        lpTraditional.setEnabled(false);
                        lpSimplified.setEnabled(false);
                    }
                }
                String trad = pref.getString("list_preference_1", "0");
                String simp = pref.getString("list_preference_2", "0");
                String trad_sel = getConvTypeText(trad);
                String simp_sel = getConvTypeText(simp);
                lpTraditional.setSummary(trad_sel);
                lpSimplified.setSummary(simp_sel);
                Objects.requireNonNull(simpleSwitch).setOnPreferenceChangeListener((preference, newValue) -> {
                    boolean newVal = (Boolean) newValue;
                    if (newVal) {
                        lpTraditional.setEnabled(false);
                        lpSimplified.setEnabled(false);
                        autodetectSwitch.setEnabled(false);
                    } else {
                        autodetectSwitch.setEnabled(true);
                        if (pref.getBoolean("switch_preference_2", true)) {
                            lpTraditional.setEnabled(true);
                            lpSimplified.setEnabled(true);
                        } else {
                            lpTraditional.setEnabled(false);
                            lpSimplified.setEnabled(false);
                        }
                    }
                    pref.edit().putBoolean("switch_preference_1", newVal).apply();
                    return true;
                });
                autodetectSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
                    boolean newVal = (Boolean) newValue;
                    if (newVal) {
                        lpTraditional.setEnabled(true);
                        lpSimplified.setEnabled(true);
                    } else {
                        lpTraditional.setEnabled(false);
                        lpSimplified.setEnabled(false);
                    }
                    pref.edit().putBoolean("switch_preference_2", newVal).apply();
                    return true;
                });
                lpTraditional.setOnPreferenceChangeListener((preference, newValue) -> {
                    String newVal = newValue.toString();
                    lpTraditional.setSummary(getConvTypeText(newVal));
                    return true;
                });
                lpSimplified.setOnPreferenceChangeListener((preference, newValue) -> {
                    String newVal = newValue.toString();
                    lpSimplified.setSummary(getConvTypeText(newVal));
                    return true;
                });
            }



        }

        private String getConvTypeText(String selection) {
            switch (selection) {
                case "1":
                    return getString(R.string.menu_popup_type_1);
                case "2":
                    return getString(R.string.menu_popup_type_2);
                case "3":
                    return getString(R.string.menu_popup_type_3);
                case "4":
                    return getString(R.string.menu_popup_type_4);
                case "5":
                    return getString(R.string.menu_popup_type_5);
                case "6":
                    return getString(R.string.menu_popup_type_6);
                case "7":
                    return getString(R.string.menu_popup_type_7);
                case "8":
                    return getString(R.string.menu_popup_type_8);
                case "9":
                    return getString(R.string.menu_popup_type_9);
                case "10":
                    return getString(R.string.menu_popup_type_10);
                default:
                    return getString(R.string.menu_popup_type_0);
            }
        }
    }
}