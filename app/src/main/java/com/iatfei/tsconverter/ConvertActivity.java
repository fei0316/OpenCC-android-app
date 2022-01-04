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

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Objects;

public class ConvertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Intent intent = getIntent();
        String action = intent.getAction();
        String intentType = intent.getType();
        boolean readonlyTemp = true;
        String callingPackageTemp = null;
        CharSequence textTemp = "";

        if (Intent.ACTION_SEND.equals(action) && intentType != null) {
            if ("text/plain".equals(intentType)) {
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (sharedText != null) {
                    textTemp = sharedText;
                }
            }
        } else {
            readonlyTemp = getIntent()
                    .getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false);
            callingPackageTemp = getCallingPackage();
            textTemp = getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
        }

        boolean readonly = readonlyTemp;
        String callingPackage = callingPackageTemp;
        CharSequence text = textTemp;

        boolean easyMode = pref.getBoolean("switch_preference_1", true);
        boolean autodetect = pref.getBoolean("switch_preference_2", true);

        if (easyMode) {
            ChineseTypes type = SimpleConvert.checkString(text.toString(), getApplicationContext());
            switch (type) {
                case TRADITIONAL_CHINESE:
                    convAndSet(5, text, readonly, callingPackage);
                    break;
                case SIMPLIFIED_CHINESE:
                    convAndSet(1, text, readonly, callingPackage);
                    break;
                default:
                    convAndSet(0, text, readonly, callingPackage);
            }
            finish();
        } else if (autodetect) {
            ChineseTypes type = SimpleConvert.checkString(text.toString(), getApplicationContext());
            if (type == ChineseTypes.TRADITIONAL_CHINESE) {
                int tradMode = Integer.parseInt(pref.getString("list_preference_1", "0"));
                if (tradMode == 0) {
                    setContentView(R.layout.activity_convert_trad);

                    Button cancel_button = findViewById(R.id.button5);
                    cancel_button.setOnClickListener(v -> finish());

                    Button conv_button = findViewById(R.id.button4);
                    conv_button.setOnClickListener(v -> {
                        RadioGroup rgPopup = findViewById(R.id.radioGroup);
                        int id = rgPopup.getCheckedRadioButtonId();
                        int sel;
                        if (id == R.id.popupRadioType5) {
                            sel = 5;
                        } else if (id == R.id.popupRadioType6) {
                            sel = 6;
                        } else if (id == R.id.popupRadioType7) {
                            sel = 7;
                        } else if (id == R.id.popupRadioType8) {
                            sel = 8;
                        } else if (id == R.id.popupRadioType9) {
                            sel = 9;
                        } else if (id == R.id.popupRadioType10) {
                            sel = 10;
                        } else {
                            sel = 0;
                        }
                        convAndSet(sel, text, readonly, callingPackage);
                        finish();
                    });
                } else {
                    convAndSet(tradMode, text, readonly, callingPackage);
                    finish();
                }
            } else if (type == ChineseTypes.SIMPLIFIED_CHINESE) {
                int simpMode = Integer.parseInt(pref.getString("list_preference_2", "0"));
                if (simpMode == 0) {
                    setContentView(R.layout.activity_convert_simp);

                    Button cancel_button = findViewById(R.id.button5);
                    cancel_button.setOnClickListener(v -> finish());

                    Button conv_button = findViewById(R.id.button4);
                    conv_button.setOnClickListener(v -> {
                        RadioGroup rgPopup = findViewById(R.id.radioGroup);
                        int id = rgPopup.getCheckedRadioButtonId();
                        int sel;
                        if (id == R.id.popupRadioType1) {
                            sel = 1;
                        } else if (id == R.id.popupRadioType2) {
                            sel = 2;
                        } else if (id == R.id.popupRadioType3) {
                            sel = 3;
                        } else if (id == R.id.popupRadioType4) {
                            sel = 4;
                        } else {
                            sel = 0;
                        }
                        convAndSet(sel, text, readonly, callingPackage);
                        finish();
                    });
                } else {
                    convAndSet(simpMode, text, readonly, callingPackage);
                    finish();
                }
            } else {
                convAndSet(0, text, readonly, callingPackage);
                finish();
            }
        } else {
            setContentView(R.layout.activity_convert);

            Button cancel_button = findViewById(R.id.button5);
            cancel_button.setOnClickListener(v -> finish());

            Button conv_button = findViewById(R.id.button4);
            conv_button.setOnClickListener(v -> {
                int sel;
                RadioGroup rgPopup = findViewById(R.id.radioGroup);
                int id = rgPopup.getCheckedRadioButtonId();
                if (id == R.id.popupRadioType1) {
                    sel = 1;
                } else if (id == R.id.popupRadioType2) {
                    sel = 2;
                } else if (id == R.id.popupRadioType3) {
                    sel = 3;
                } else if (id == R.id.popupRadioType4) {
                    sel = 4;
                } else if (id == R.id.popupRadioType5) {
                    sel = 5;
                } else if (id == R.id.popupRadioType6) {
                    sel = 6;
                } else if (id == R.id.popupRadioType7) {
                    sel = 7;
                } else if (id == R.id.popupRadioType8) {
                    sel = 8;
                } else if (id == R.id.popupRadioType9) {
                    sel = 9;
                } else if (id == R.id.popupRadioType10) {
                    sel = 10;
                } else {
                    sel = 0;
                }
                convAndSet(sel, text, readonly, callingPackage);
                finish();
            });
        }
    }

    private void convAndSet(int sel, CharSequence text, boolean readonly, String callingPackage) {
        if (callingPackage == null)
            callingPackage = "bruh";
        boolean cantReplace = callingPackage.equalsIgnoreCase("com.tencent.mm"); //simplified so it might look weird at first glance

        if (sel != 0) {
            String fromText = Objects.requireNonNull(text).toString();
            String resultText = Convert.openCCConv(fromText, sel, getApplicationContext());

            if (readonly || cantReplace) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("ConvertedChinese", resultText);
                clipboard.setPrimaryClip(clip);
                Toast toast = Toast.makeText(getApplicationContext(), R.string.menu_readonly, Toast.LENGTH_LONG);
                toast.show();
            } else {
                Intent intent = new Intent();
                intent.putExtra(Intent.EXTRA_PROCESS_TEXT, resultText);
                setResult(RESULT_OK, intent);
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.menu_autonotdetected), Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
