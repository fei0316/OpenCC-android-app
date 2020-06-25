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
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private boolean userRadioChange = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean("previous_started", false);
        if (!previouslyStarted) {
            tutorialScreen();
        }

        Button clear_button = findViewById(R.id.clear_button);
        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText et = findViewById(R.id.editText_convert);
                et.setText("");
            }
        });

        Button conv_button = findViewById(R.id.convert_button);
        conv_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton var1 = findViewById((R.id.radioButtonVar1));
                RadioButton var2 = findViewById((R.id.radioButtonVar2));
                RadioButton var3 = findViewById((R.id.radioButtonVar3));
                RadioButton var4 = findViewById((R.id.radioButtonVar4));
                RadioButton var5 = findViewById((R.id.radioButtonVar5));
                RadioButton noWord = findViewById(R.id.radioButtonIdiom1);
                RadioButton twWord = findViewById(R.id.radioButtonIdiom2);
                RadioButton cnWord = findViewById(R.id.radioButtonIdiom3);
                RadioButton type1 = findViewById((R.id.radioButtonType1));
                RadioButton type2 = findViewById((R.id.radioButtonType2));
                RadioButton type3 = findViewById((R.id.radioButtonType3));

                int type = Convert.radioToType(type1.isChecked(), type2.isChecked(), type3.isChecked(), var1.isChecked(), var2.isChecked(), var3.isChecked(), var4.isChecked(), var5.isChecked(), noWord.isChecked(), twWord.isChecked(), cnWord.isChecked());
                if (type >= 1 && type <= 10) {
                    EditText conv_text = findViewById(R.id.editText_convert);
                    String text = conv_text.getText().toString();
                    String converted = Convert.openCCConv(text, type, getApplicationContext());
                    conv_text.setText(converted);
                } else
                    Toast.makeText(getApplicationContext(), "Error!!!", Toast.LENGTH_SHORT).show();
            }
        });

        RadioGroup rgType = findViewById(R.id.radioGroupType);
        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioGroup rgVar = findViewById(R.id.radioGroupVar);
                RadioButton var1 = findViewById(R.id.radioButtonVar1);
                RadioButton var2 = findViewById(R.id.radioButtonVar2);
                RadioButton var3 = findViewById(R.id.radioButtonVar3);
                RadioButton var4 = findViewById(R.id.radioButtonVar4);
                RadioButton var5 = findViewById(R.id.radioButtonVar5);

                if (checkedId == R.id.radioButtonType1) {
                    var1.setEnabled(true);
                    var2.setEnabled(true);
                    var3.setEnabled(true);
                    var4.setEnabled(false);
                    var5.setEnabled(false);
                } else if (checkedId == R.id.radioButtonType2) {
                    var1.setEnabled(true);
                    var2.setEnabled(false);
                    var3.setEnabled(false);
                    var4.setEnabled(true);
                    var5.setEnabled(true);
                } else if (checkedId == R.id.radioButtonType3) {
                    var1.setEnabled(false);
                    var2.setEnabled(true);
                    var3.setEnabled(true);
                    var4.setEnabled(false);
                    var5.setEnabled(false);
                }
                if (userRadioChange && checkedId == R.id.radioButtonType3) {
                    rgVar.clearCheck();
                    rgVar.check(R.id.radioButtonVar2);
                } else if (userRadioChange) {
                    rgVar.clearCheck();
                    rgVar.check(R.id.radioButtonVar1);
                }
            }
        });

        RadioGroup rgVar = findViewById(R.id.radioGroupVar);
        rgVar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioGroup rgIdiom = findViewById(R.id.radioGroupIdiom);
                if (userRadioChange) {
                    rgIdiom.check(R.id.radioButtonIdiom1);
                }
            }
        });

        RadioButton rbTW = findViewById(R.id.radioButtonIdiom2);
        RadioButton rbCN = findViewById(R.id.radioButtonIdiom3);


        rbTW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup rgVar = findViewById(R.id.radioGroupVar);
                RadioGroup rgType = findViewById(R.id.radioGroupType);
                userRadioChange = false;
                rgType.check(R.id.radioButtonType1);
                rgVar.check(R.id.radioButtonVar2);
                userRadioChange = true;
            }
        });

        rbCN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup rgVar = findViewById(R.id.radioGroupVar);
                RadioGroup rgType = findViewById(R.id.radioGroupType);
                userRadioChange = false;
                rgType.check(R.id.radioButtonType2);
                rgVar.check(R.id.radioButtonVar4);
                userRadioChange = true;
            }
        });
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
}