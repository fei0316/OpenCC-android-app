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

public class Constant {
    public static final int APPINTRO_LAST_UPDATE_VERSIONCODE = 12;
    public static final String PREF_MAIN_EASY_MODE = "main_simplemode";
    public static final String PREF_PREVIOUS_STARTED_VERSION = "previous_started_ver";
    public static final String PREF_SETTINGS_EASY_MODE = "switch_preference_1";
    public static final String PREF_SETTINGS_AUTODETECT_MODE = "switch_preference_2";
    public static final String PREF_SETTINGS_TRAD_MODE = "list_preference_1";
    public static final String PREF_SETTINGS_SIMP_MODE = "list_preference_2";
    public static final String PREF_SETTINGS_TTS_LANGUAGE = "list_preference_tts_lang";

    public static final int CONV_ACTIVITY_TYPE_NONE = 0;
    public static final int CONV_ACTIVITY_TYPE_TILE = 1;
    public static final int CONV_ACTIVITY_TYPE_FILE = 2;
    public static final String TILE_CONVERT_INTENT_EXTRA = "fromTile";

    public static final String CLIPBOARD_LABEL = "ConvertedChinese";

    public static final String TTS_UTTERANCE_ID = "com.iatfei.tsconvert:MainActivity.editText_convert";

    public static final int S2T = 1;
    public static final int S2TW = 2;
    public static final int S2HK = 3;
    public static final int S2TWP = 4;
    public static final int T2S = 5;
    public static final int TW2S = 6;
    public static final int HK2S = 7;
    public static final int TW2SP = 8;
    public static final int T2TW = 9;
    public static final int T2HK = 10;
}
