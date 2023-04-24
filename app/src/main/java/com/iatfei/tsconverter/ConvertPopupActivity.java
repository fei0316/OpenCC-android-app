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

public class ConvertPopupActivity extends AppCompatActivity {

    boolean tileClipboardAccessRequested = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String action = intent.getAction();
        String intentType = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && intentType != null) {
            // reached here through share action
            CharSequence textTemp = "";
            if ("text/plain".equals(intentType)) {
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (sharedText != null) {
                    textTemp = sharedText;
                }
            }
            convHelper(true, false, textTemp);
        } else if (intent.getBooleanExtra(Constant.TILE_CONVERT_INTENT_EXTRA, false)) {
            // reached here through tile
            // open empty activity to trigger onWindowFocusChanged
            tileClipboardAccessRequested = true;
            setContentView(R.layout.activity_convert_empty);
        } else {
            // reached here through text selection menu
            convHelper(getIntent().getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false),
                    false, getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT));
        }
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // get clipboard data once in activity, then call convHelper like others
        if (hasFocus) {
            if (tileClipboardAccessRequested) {
                tileClipboardAccessRequested = false;
                ClipboardManager clipBoard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = clipBoard.getPrimaryClip();
                if (clipData == null) {
                    return;
                }
                CharSequence cs = clipData.getItemAt(0).getText();
                if (cs == null) {
                    return;
                }
                String clipboardText = cs.toString();
                convHelper(true, true, clipboardText);
            }
        }
    }

    private void convHelper (boolean readonly, boolean quitAfterConv, CharSequence text) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean easyMode = pref.getBoolean(Constant.PREF_SETTINGS_EASY_MODE, true);
        boolean autodetect = pref.getBoolean(Constant.PREF_SETTINGS_AUTODETECT_MODE, true);
        int tradMode = Integer.parseInt(pref.getString(Constant.PREF_SETTINGS_TRAD_MODE, "0"));
        int simpMode = Integer.parseInt(pref.getString(Constant.PREF_SETTINGS_SIMP_MODE, "0"));

        if (easyMode) {
            // easy mode: if detected trad/simp, convert with OpenCC; if not, ask for trad/simp then convert with OpenCC
            ChineseTypes type = SimpleConvert.checkString(text.toString(), getApplicationContext());
            if (type == ChineseTypes.TRADITIONAL_CHINESE) {
                convAndSet(Constant.T2S, text, readonly);
                finish();
                if (quitAfterConv) {
                    moveTaskToBack(true);
                }
            } else if (type == ChineseTypes.SIMPLIFIED_CHINESE) {
                convAndSet(Constant.S2T, text, readonly);
                finish();
                if (quitAfterConv) {
                    moveTaskToBack(true);
                }
            } else {
                tradSimpPopup(Constant.T2S, Constant.S2T, text, readonly, quitAfterConv);
            }
        } else if (autodetect) {
            // auto detect mode: if detected trad/simp, convert directly if conversion preference chosen,
            //                                          otherwise present all trad or all simp options
            //                   if not detected, ask if text is simp or trad if conversion preference chosen,
            //                                    otherwise present every possible options
            ChineseTypes type = SimpleConvert.checkString(text.toString(), getApplicationContext());
            if (type == ChineseTypes.TRADITIONAL_CHINESE) {
                if (tradMode == 0) {
                    tradPopup(text, readonly, quitAfterConv);
                } else {
                    convAndSet(tradMode, text, readonly);
                    finish();
                    if (quitAfterConv) {
                        moveTaskToBack(true);
                    }
                }
            } else if (type == ChineseTypes.SIMPLIFIED_CHINESE) {
                if (simpMode == 0) {
                    simpPopup(text, readonly, quitAfterConv);
                } else {
                    convAndSet(simpMode, text, readonly);
                    finish();
                    if (quitAfterConv) {
                        moveTaskToBack(true);
                    }
                }
            } else {
                if (tradMode == 0 || simpMode == 0) {
                    allOptionsPopup(text, readonly, quitAfterConv);
                } else {
                    tradSimpPopup(tradMode, simpMode, text, readonly, quitAfterConv);
                }
            }
        }
        else {
            // fully manual mode: show all options
            allOptionsPopup(text, readonly, quitAfterConv);
        }
    }

    private void tradSimpPopup (int tradType, int simpType, CharSequence text, boolean readonly, boolean quitAfterConv) {
        // popup when user selection of traditional or simplified text is required
        setContentView(R.layout.activity_convert_simple);
        popupSetCancelListener(quitAfterConv);

        Button conv_button = findViewById(R.id.button4);
        conv_button.setOnClickListener(v -> {
            RadioGroup rgPopup = findViewById(R.id.radioGroup);
            int id = rgPopup.getCheckedRadioButtonId();
            int sel;
            if (id == R.id.popupRadioType1) {
                sel = tradType;    // traditional
            } else if (id == R.id.popupRadioType2) {
                sel = simpType;    // simplified
            } else {
                sel = 0;
            }
            convAndSet(sel, text, readonly);
            finish();
            if (quitAfterConv) {
                moveTaskToBack(true);
            }
        });
    }

    private void tradPopup (CharSequence text, boolean readonly, boolean quitAfterConv) {
        // popup with only traditional options
        setContentView(R.layout.activity_convert_trad);
        popupSetCancelListener(quitAfterConv);

        Button conv_button = findViewById(R.id.button4);
        conv_button.setOnClickListener(v -> {
            RadioGroup rgPopup = findViewById(R.id.radioGroup);
            int id = rgPopup.getCheckedRadioButtonId();
            int sel;
            if (id == R.id.popupRadioType5) {
                sel = Constant.T2S;
            } else if (id == R.id.popupRadioType6) {
                sel = Constant.TW2S;
            } else if (id == R.id.popupRadioType7) {
                sel = Constant.HK2S;
            } else if (id == R.id.popupRadioType8) {
                sel = Constant.TW2SP;
            } else if (id == R.id.popupRadioType9) {
                sel = Constant.T2TW;
            } else if (id == R.id.popupRadioType10) {
                sel = Constant.T2HK;
            } else {
                sel = 0;
            }
            convAndSet(sel, text, readonly);
            finish();
            if (quitAfterConv) {
                moveTaskToBack(true);
            }
        });
    }

    private void simpPopup (CharSequence text, boolean readonly, boolean quitAfterConv) {
        // popup with only simplified options
        setContentView(R.layout.activity_convert_simp);
        popupSetCancelListener(quitAfterConv);

        Button conv_button = findViewById(R.id.button4);
        conv_button.setOnClickListener(v -> {
            RadioGroup rgPopup = findViewById(R.id.radioGroup);
            int id = rgPopup.getCheckedRadioButtonId();
            int sel;
            if (id == R.id.popupRadioType1) {
                sel = Constant.S2T;
            } else if (id == R.id.popupRadioType2) {
                sel = Constant.S2TW;
            } else if (id == R.id.popupRadioType3) {
                sel = Constant.S2HK;
            } else if (id == R.id.popupRadioType4) {
                sel = Constant.S2TWP;
            } else {
                sel = 0;
            }
            convAndSet(sel, text, readonly);
            finish();
            if (quitAfterConv) {
                moveTaskToBack(true);
            }
        });
    }

    private void allOptionsPopup (CharSequence text, boolean readonly, boolean quitAfterConv) {
        // popup with every single possible options
        setContentView(R.layout.activity_convert);
        popupSetCancelListener(quitAfterConv);

        Button conv_button = findViewById(R.id.button4);
        conv_button.setOnClickListener(v -> {
            int sel;
            RadioGroup rgPopup = findViewById(R.id.radioGroup);
            int id = rgPopup.getCheckedRadioButtonId();
            if (id == R.id.popupRadioType1) {
                sel = Constant.S2T;
            } else if (id == R.id.popupRadioType2) {
                sel = Constant.S2TW;
            } else if (id == R.id.popupRadioType3) {
                sel = Constant.S2HK;
            } else if (id == R.id.popupRadioType4) {
                sel = Constant.S2TWP;
            } else if (id == R.id.popupRadioType5) {
                sel = Constant.T2S;
            } else if (id == R.id.popupRadioType6) {
                sel = Constant.TW2S;
            } else if (id == R.id.popupRadioType7) {
                sel = Constant.HK2S;
            } else if (id == R.id.popupRadioType8) {
                sel = Constant.TW2SP;
            } else if (id == R.id.popupRadioType9) {
                sel = Constant.T2TW;
            } else if (id == R.id.popupRadioType10) {
                sel = Constant.T2HK;
            } else {
                sel = 0;
            }
            convAndSet(sel, text, readonly);
            finish();
            if (quitAfterConv) {
                moveTaskToBack(true);
            }
        });
    }

    private void showConversionError () {
        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.menu_autonotdetected), Toast.LENGTH_LONG);
        toast.show();
    }

    private void popupSetCancelListener (boolean quitAfterConv) {
        Button cancel_button = findViewById(R.id.button5);
        cancel_button.setOnClickListener(v -> {
            finish();
            if (quitAfterConv) {
                moveTaskToBack(true);
            }
        });
    }

    private void convAndSet(int sel, CharSequence text, boolean readonly) {
        if (sel != 0) {
            String fromText = Objects.requireNonNull(text).toString();
            String resultText = ConvertUtils.openCCConv(fromText, sel, getApplicationContext());

            if (readonly) {
                // copy converted to clipboard
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(Constant.CLIPBOARD_LABEL, resultText);
                clipboard.setPrimaryClip(clip);
                Toast toast = Toast.makeText(getApplicationContext(), R.string.menu_readonly, Toast.LENGTH_LONG);
                toast.show();
            } else {
                // replace text directly
                //todo:bugreport from email not working
                Intent intent = new Intent();
                intent.putExtra(Intent.EXTRA_PROCESS_TEXT, resultText);
                setResult(RESULT_OK, intent);
            }
        } else {
            showConversionError();
        }
    }
}
