package com.iatfei.tsconverter;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    boolean userRadioChange = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final Button conv_button = findViewById(R.id.convert_button);
        conv_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton var1 = findViewById((R.id.radioButtonVar1));
                RadioButton var2 = findViewById((R.id.radioButtonVar2));
                RadioButton var3 = findViewById((R.id.radioButtonVar3));
                RadioButton var4 = findViewById((R.id.radioButtonVar4));
                RadioButton var5 = findViewById((R.id.radioButtonVar5));
                RadioButton noword = findViewById(R.id.radioButtonIdiom1);
                RadioButton twword = findViewById(R.id.radioButtonIdiom2);
                RadioButton cnword = findViewById(R.id.radioButtonIdiom3);
                RadioButton type1 = findViewById((R.id.radioButtonType1));
                RadioButton type2 = findViewById((R.id.radioButtonType2));
                RadioButton type3 = findViewById((R.id.radioButtonType3));

                int type = Convert.radioToType(type1.isChecked(), type2.isChecked(), type3.isChecked(), var1.isChecked(), var2.isChecked(), var3.isChecked(), var4.isChecked(), var5.isChecked(), noword.isChecked(), twword.isChecked(), cnword.isChecked());
                if (type >= 1 && type <= 10) {
                    EditText conv_text = findViewById(R.id.editText_convert);
                    String text = conv_text.getText().toString();
                    String converted = Convert.openCCConv(text, type, getApplicationContext());
                    conv_text.setText(converted);
                } else
                    Toast.makeText(getApplicationContext(), "error!!!", Toast.LENGTH_SHORT).show();


            }
        });

        RadioGroup rgType = findViewById(R.id.radioGroupType);
        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioGroup rgVar = findViewById(R.id.radioGroupVar);
                RadioGroup rgIdiom = findViewById(R.id.radioGroupIdiom);
                RadioButton var1 = findViewById((R.id.radioButtonVar1));
                RadioButton var2 = findViewById((R.id.radioButtonVar2));
                RadioButton var3 = findViewById((R.id.radioButtonVar3));
                RadioButton var4 = findViewById((R.id.radioButtonVar4));
                RadioButton var5 = findViewById((R.id.radioButtonVar5));

                if (userRadioChange) {
                    rgVar.clearCheck();
                }

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

                if ((checkedId != R.id.radioButtonType1 && !var2.isChecked() && userRadioChange) || (checkedId != R.id.radioButtonType2 && !var4.isChecked() && userRadioChange)) {
                    rgIdiom.check(R.id.radioButtonIdiom1);
                }

            }
        });

        RadioGroup rgVar = findViewById(R.id.radioGroupVar);
        rgVar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioGroup rgIdiom = findViewById(R.id.radioGroupIdiom);
                RadioButton type1 = findViewById((R.id.radioButtonType1));
                RadioButton type2 = findViewById((R.id.radioButtonType2));

                if ((checkedId != R.id.radioButtonVar2 && !type1.isChecked() && userRadioChange) || (checkedId != R.id.radioButtonVar4 && !type2.isChecked() && userRadioChange)) {
                    rgIdiom.check(R.id.radioButtonIdiom1);
                }
            }
        });

        RadioGroup rgWordUse = findViewById(R.id.radioGroupIdiom);
        rgWordUse.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioGroup rgVar = findViewById(R.id.radioGroupVar);
                RadioGroup rgType = findViewById(R.id.radioGroupType);

                if (checkedId == R.id.radioButtonIdiom2) {
                    userRadioChange = false;
                    rgType.clearCheck();
                    rgVar.clearCheck();
                    rgType.check(R.id.radioButtonType1);
                    rgVar.check(R.id.radioButtonVar2);
                    userRadioChange = true;

                } else if (checkedId == R.id.radioButtonIdiom3) {
                    userRadioChange = false;
                    rgType.clearCheck();
                    rgVar.clearCheck();
                    rgType.check(R.id.radioButtonType2);
                    rgVar.check(R.id.radioButtonVar4);
                    userRadioChange = true;
                }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}