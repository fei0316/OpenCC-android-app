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

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.appbar.MaterialToolbar;

import androidx.preference.PreferenceManager;

import android.provider.DocumentsContract;
import android.provider.OpenableColumns;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.io.FilenameUtils;

import kotlin.text.Charsets;

public class MainActivity extends AppCompatActivity {

    private final AtomicBoolean fileOpened = new AtomicBoolean(false);
    private Uri lastUri;
    private boolean userRadioChange = true;
    private final AtomicBoolean simple = new AtomicBoolean(true);  // simple = Easy Mode
    private TextToSpeech tts = null;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        TextView filename_text = findViewById(R.id.filename_text);
        outState.putBoolean("fileOpened", fileOpened.get());
        outState.putCharSequence("filenameText", filename_text.getText());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        int previousStartedVer = prefs.getInt(Constant.PREF_PREVIOUS_STARTED_VERSION, 0);
        if (previousStartedVer < Constant.APPINTRO_LAST_UPDATE_VERSIONCODE) {
            tutorialScreen();
        }

        if (savedInstanceState != null) {
            // restore state after screen rotation. EditText retains data, no restoration necessary.
            TextView filename_text = findViewById(R.id.filename_text);
            EditText et = findViewById(R.id.editText_convert);
            filename_text.setText(savedInstanceState.getString("filenameText"));
            boolean fileOpenedLocal = savedInstanceState.getBoolean("fileOpened");
            fileOpened.set(fileOpenedLocal);
            et.setEnabled(!fileOpenedLocal);
        }

        makeMainActivity();
    }

    @Override
    protected void onPause() {
        if (tts != null) {
            tts.shutdown();
            tts = null;
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.shutdown();
            tts = null;
        }
        super.onDestroy();
    }

    private void makeMainActivity() {
        // find all objects on screen
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        EditText et = findViewById(R.id.editText_convert);
        TextView filename_text = findViewById(R.id.filename_text);
        Button file_button = findViewById(R.id.file_button);
        Button conv_button = findViewById(R.id.convert_button);
        Button savefile_button = findViewById(R.id.savefile_button);
        Button clear_button = findViewById(R.id.clear_button);
        MaterialSwitch easySW = findViewById(R.id.easyModeSwitch);
        RadioGroup rgVar = findViewById(R.id.radioGroupVar);
        RadioButton var1 = findViewById((R.id.radioButtonVar1));
        RadioButton var2 = findViewById((R.id.radioButtonVar2));
        RadioButton var3 = findViewById((R.id.radioButtonVar3));
        RadioButton var4 = findViewById((R.id.radioButtonVar4));
        RadioButton var5 = findViewById((R.id.radioButtonVar5));
        RadioGroup rgIdiom = findViewById(R.id.radioGroupIdiom);
        RadioButton noWord = findViewById(R.id.radioButtonIdiom1);
        RadioButton twWord = findViewById(R.id.radioButtonIdiom2);
        RadioButton cnWord = findViewById(R.id.radioButtonIdiom3);
        RadioGroup rgType = findViewById(R.id.radioGroupType);
        RadioButton type1 = findViewById((R.id.radioButtonType1));
        RadioButton type2 = findViewById((R.id.radioButtonType2));
        RadioButton type3 = findViewById((R.id.radioButtonType3));

        //initialize UI
        simple.set(prefs.getBoolean(Constant.PREF_MAIN_EASY_MODE, true));
        simpleResetRadio(simple.get(), rgVar, var1, var2, var3, var4, var5, rgIdiom, noWord,
                twWord, cnWord, rgType, type1, type2, type3);
        easySW.setChecked(simple.get());
        boolean editTextEmpty = et.getText().toString().trim().isEmpty();
        conv_button.setEnabled(!editTextEmpty);
        savefile_button.setEnabled(!editTextEmpty);

        // set listeners to elements
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean editTextEmpty = s.toString().trim().isEmpty();
                conv_button.setEnabled(!editTextEmpty);
                savefile_button.setEnabled(!editTextEmpty);
            }
        });
        easySW.setOnCheckedChangeListener((v, isChecked) -> {
            simple.set(isChecked);
            simpleResetRadio(isChecked, rgVar, var1, var2, var3, var4, var5, rgIdiom, noWord, twWord, cnWord,
                    rgType, type1, type2, type3);
        });
        rgType.setOnCheckedChangeListener((group, checkedId) -> {
            if (userRadioChange)
                radioButtonSetup(false, rgVar, var1, var2, var3, var4, var5, rgIdiom, noWord, twWord, cnWord,
                        rgType, type1, type2, type3);
        });
        rgVar.setOnCheckedChangeListener((group, checkedId) -> {
            if (userRadioChange)
                radioButtonSetup(false, rgVar, var1, var2, var3, var4, var5, rgIdiom, noWord, twWord, cnWord,
                        rgType, type1, type2, type3);
        });
        twWord.setOnClickListener(v -> {
            if (userRadioChange)
                radioButtonSetup(true, rgVar, var1, var2, var3, var4, var5, rgIdiom, noWord, twWord, cnWord,
                        rgType, type1, type2, type3);
        });
        cnWord.setOnClickListener(v -> {
            if (userRadioChange)
                radioButtonSetup(true, rgVar, var1, var2, var3, var4, var5, rgIdiom, noWord, twWord, cnWord,
                        rgType, type1, type2, type3);
        });
        conv_button.setOnClickListener(v -> {
            if (simple.get()) {
                easyConvToClipboard(et);
            } else {
                int type = ConvertUtils.radioToType(type1.isChecked(), type2.isChecked(), type3.isChecked(),
                        var1.isChecked(), var2.isChecked(), var3.isChecked(), var4.isChecked(),
                        var5.isChecked(),noWord.isChecked(), twWord.isChecked(), cnWord.isChecked());
                advConvToClipboard(et, type);
            }
            clearOpenedFile(et, filename_text);
        });
        clear_button.setOnClickListener(view -> {
            et.setText("");
            clearOpenedFile(et, filename_text);
        });

        ActivityResultLauncher<Intent> openFileActivityResultsLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            Uri data = result.getData().getData();
                            getContentResolver().takePersistableUriPermission(
                                    data,
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                            );
                            lastUri = data;
                            openFile(data);
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.menu_toast_file_readwrite_error),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        file_button.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("text/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            try {
                openFileActivityResultsLauncher.launch(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(), getString(R.string.menu_toast_no_filemanager), Toast.LENGTH_SHORT).show();
            }
        });

        ActivityResultLauncher<Intent> saveFileActivityResultsLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            Uri fileUri = result.getData().getData();
                            getContentResolver().takePersistableUriPermission(
                                    fileUri,
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                            );
                            try {
                                String text = et.getText().toString();
                                String converted;
                                int type;
                                if (simple.get()) {
                                    type = easyConvGetConvertType(text);
                                } else {
                                    type = ConvertUtils.radioToType(type1.isChecked(), type2.isChecked(), type3.isChecked(), var1.isChecked(), var2.isChecked(), var3.isChecked(), var4.isChecked(), var5.isChecked(), noWord.isChecked(), twWord.isChecked(), cnWord.isChecked());
                                }
                                converted = ConvertUtils.openCCConv(text, type, getApplicationContext());
                                et.setText(converted);
                                OutputStream outputStream = getContentResolver().openOutputStream(fileUri);
                                outputStream.write(converted.getBytes(Charsets.UTF_8));
                                outputStream.close();
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), getString(R.string.menu_toast_file_readwrite_error), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );
        savefile_button.setOnClickListener(v -> {
            if (simple.get()) {
                if (easyConvGetConvertType(et.getText().toString()) == 0) {
                    Toast.makeText(getApplicationContext(), getString(R.string.menu_autonotdetected), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            String filename = filename_text.getText().toString();
            if (filename.isEmpty()) {
                filename = "converted.txt";
            } else {
                filename = FilenameUtils.removeExtension(filename) + "-converted.txt";
            }
            clearOpenedFile(et, filename_text);
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.setType("text/plain");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_TITLE, filename);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && lastUri != null) {
                intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, lastUri);
            }
            try {
                saveFileActivityResultsLauncher.launch(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(), getString(R.string.menu_toast_no_filemanager), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openFile(Uri fileUri) {
        TextView filename_text = findViewById(R.id.filename_text);
        StringBuilder sb = new StringBuilder();
        try {
            InputStream inputStream = getContentResolver().openInputStream(fileUri);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(Objects.requireNonNull(inputStream))
            );
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            inputStream.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getString(R.string.menu_toast_file_readwrite_error), Toast.LENGTH_SHORT).show();
            return;
        }
        filename_text.setText(getFileNameFromUri(getApplicationContext(), fileUri));
        String fileData = sb.toString();
        EditText et = findViewById(R.id.editText_convert);
        et.setEnabled(false);
        et.setText(fileData);
        fileOpened.set(true);
    }

    private void easyConvToClipboard(EditText et) {
        String text = et.getText().toString();
        int convType = easyConvGetConvertType(text);
        if (convType < 1 || convType > 10) {
            Toast.makeText(getApplicationContext(), getString(R.string.menu_autonotdetected), Toast.LENGTH_SHORT).show();
            return;
        }
        String converted = ConvertUtils.openCCConv(text, convType, getApplicationContext());
        convertedTextToEditTextAndClipboard(et, converted);
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(Constant.PREF_MAIN_EASY_MODE, true).apply();
    }

    private void advConvToClipboard(EditText et, int type) {
        if (type >= 1 && type <= 10) {
            String text = et.getText().toString();
            String converted = ConvertUtils.openCCConv(text, type, getApplicationContext());
            convertedTextToEditTextAndClipboard(et, converted);
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(Constant.PREF_MAIN_EASY_MODE, false).apply();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.menu_toast_unknown_error), Toast.LENGTH_SHORT).show();
        }
    }

    private int easyConvGetConvertType(String text) {
        //special value: 0 = can't determine, 1 and 5 are S2T and T2S respectively.
        ChineseTypes zhType = SimpleConvert.checkString(text, getBaseContext());
        if (zhType == ChineseTypes.TRADITIONAL_CHINESE) {
            return Constant.T2S;
        } else if (zhType == ChineseTypes.SIMPLIFIED_CHINESE) {
            return Constant.S2T;
        }
        return 0;
    }

    private void convertedTextToEditTextAndClipboard(EditText et, String string) {
        et.setText(string);
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(Constant.CLIPBOARD_LABEL, string);
        clipboard.setPrimaryClip(clip);
        Toast toast = Toast.makeText(getApplicationContext(), R.string.menu_readonly, Toast.LENGTH_LONG);
        toast.show();
    }

    private void simpleResetRadio(boolean simple, RadioGroup rgVar, RadioButton var1, RadioButton var2,
                                  RadioButton var3, RadioButton var4, RadioButton var5,
                                  RadioGroup rgIdiom, RadioButton noWord, RadioButton twWord,
                                  RadioButton cnWord, RadioGroup rgType, RadioButton type1,
                                  RadioButton type2, RadioButton type3) {
        noWord.setEnabled(!simple);
        cnWord.setEnabled(!simple);
        twWord.setEnabled(!simple);
        type1.setEnabled(!simple);
        type2.setEnabled(!simple);
        type3.setEnabled(!simple);
        if (simple) {
            // just disable everything
            var1.setEnabled(false);
            var2.setEnabled(false);
            var3.setEnabled(false);
            var4.setEnabled(false);
            var5.setEnabled(false);
        } else {
            // enable conversion type and vocabulary radios, let code figure out which variant radio to enable
            radioButtonSetup(false, rgVar, var1, var2, var3, var4, var5,
                    rgIdiom, noWord, twWord, cnWord, rgType, type1, type2, type3);
        }
    }

    private void radioButtonSetup(boolean idiomChange, RadioGroup rgVar, RadioButton var1, RadioButton var2,
                                  RadioButton var3, RadioButton var4, RadioButton var5,
                                  RadioGroup rgIdiom, RadioButton noWord, RadioButton twWord,
                                  RadioButton cnWord, RadioGroup rgType, RadioButton type1,
                                  RadioButton type2, RadioButton type3) {
        userRadioChange = false;
        if (idiomChange) {
            if (twWord.isChecked()) {
                rgType.check(R.id.radioButtonType1);
                rgVar.check(R.id.radioButtonVar2);
            } else if (cnWord.isChecked()) {
                rgType.check(R.id.radioButtonType2);
                rgVar.check(R.id.radioButtonVar4);
            }
        }
        if (type1.isChecked()) {
            // Simplified to Traditional
            var1.setEnabled(true);
            var2.setEnabled(true);
            var3.setEnabled(true);
            var4.setEnabled(false);
            var5.setEnabled(false);
        } else if (type2.isChecked()) {
            // Traditional to Simplified
            var1.setEnabled(true);
            var2.setEnabled(false);
            var3.setEnabled(false);
            var4.setEnabled(true);
            var5.setEnabled(true);
        } else if (type3.isChecked()) {
            // Traditional variants conversion
            var1.setEnabled(false);
            var2.setEnabled(true);
            var3.setEnabled(true);
            var4.setEnabled(false);
            var5.setEnabled(false);
        }
        if (!noWord.isChecked()) {
            if (ConvertUtils.radioToType(
                    type1.isChecked(), type2.isChecked(), type3.isChecked(),
                    var1.isChecked(), var2.isChecked(), var3.isChecked(), var4.isChecked(), var5.isChecked(),
                    noWord.isChecked(), twWord.isChecked(), cnWord.isChecked()) == -1) {
                // Vocabulary conversion is selected and currently has invalid option. Change back to no vocabulary conversion.
                rgIdiom.check(R.id.radioButtonIdiom1);
            }
        }
        if (ConvertUtils.radioToType(
                type1.isChecked(), type2.isChecked(), type3.isChecked(),
                var1.isChecked(), var2.isChecked(), var3.isChecked(), var4.isChecked(), var5.isChecked(),
                noWord.isChecked(), twWord.isChecked(), cnWord.isChecked()) == -1) {
            // Selection invalid with no vocabulary conversion.
            if (type1.isChecked() || type2.isChecked()) {
                // Change to OpenCC if S2T or T2S is selected
                rgVar.check(R.id.radioButtonVar1);
            } else if (type3.isChecked()) {
                // change to Taiwanese if Conversion between Trad. Variants selected
                rgVar.check(R.id.radioButtonVar2);
            }
        }
        userRadioChange = true;
    }

    private void clearOpenedFile(EditText et, TextView filename_text) {
        et.setEnabled(true);
        filename_text.setText("");
        fileOpened.set(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_about) {
            aboutScreen();
            return true;
        } else if (id == R.id.action_tutorial) {
            tutorialScreen();
            return true;
        } else if (id == R.id.action_settings) {
            settingsScreen();
            return true;
        } else if (id == R.id.action_tts) {
            if (tts == null) {
                initTts(item);
            } else if (tts.isSpeaking()) {
                stopTts();
            } else {
                doTts();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void aboutScreen() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void tutorialScreen() {
        Intent intent = new Intent(this, FirstStartupActivity.class);
        startActivity(intent);
    }

    public void settingsScreen() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void doTts() {
        EditText et = findViewById(R.id.editText_convert);
        String text = et.getText().toString();
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, Constant.TTS_UTTERANCE_ID);
    }

    private void stopTts() {
        tts.stop();
    }

    private void initTts(MenuItem ttsMenuItem) {
        tts = new TextToSpeech(getApplicationContext(), status -> {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            String ttsCountry = prefs.getString(Constant.PREF_SETTINGS_TTS_LANGUAGE, "TW");
            if(status == TextToSpeech.SUCCESS) {
                tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String utteranceId) {
                        if (utteranceId.equals(Constant.TTS_UTTERANCE_ID)) {
                            ttsMenuItem.setTitle(R.string.tts_stop);
                        }
                    }

                    @Override
                    public void onDone(String utteranceId) {
                        if (utteranceId.equals(Constant.TTS_UTTERANCE_ID)) {
                            ttsMenuItem.setTitle(R.string.tts_play);
                        }
                    }

                    @Override
                    public void onStop(String utteranceId, boolean interrupted) {
                        if (utteranceId.equals(Constant.TTS_UTTERANCE_ID)) {
                            ttsMenuItem.setTitle(R.string.tts_play);
                        }
                    }

                    @Override
                    public void onError(String utteranceId) {
                    }
                });
                int setLangResult;
                /* Notes: In an ideal world we can just use "zh" "TW"/"HK"/"CN" to select respective language/dialect.
                   In real-world testing it looks like "zh" "HK" is not supported on some devices but "yue" does. */
                // Logic: Attempt to use standard "zh" "TW"/"HK"/"CN". If not supported, use "yue" for HK and "zh" for others.
                setLangResult = tts.setLanguage(new Locale("zh", ttsCountry));
                if (setLangResult != TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                    if (ttsCountry.equals("HK")) {
                        setLangResult = tts.setLanguage(new Locale("yue"));
                    } else {
                        setLangResult = tts.setLanguage(new Locale("zh"));
                    }
                    if (setLangResult != TextToSpeech.LANG_AVAILABLE) {
                        Toast.makeText(getApplicationContext(), getString(R.string.tts_error_nolanguage),
                            Toast.LENGTH_SHORT).show();
                    }
                }
                doTts();
            }
        });
    }

    // from Praveen from https://stackoverflow.com/questions/70795185/android-how-to-get-file-name
    private String getFileNameFromUri(Context context, Uri uri) {
        String fileName;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        fileName = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
        cursor.close();
        return fileName;
    }

}