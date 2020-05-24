package com.iatfei.tsconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Objects;

public class ConvertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);

        Button cancel_button = findViewById(R.id.button5);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button conv_button = findViewById(R.id.button4);
        conv_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean readonly = getIntent()
                        .getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false);
                boolean cantReplace;
                String callingPackage = getCallingPackage();
                if (callingPackage == null)
                    callingPackage = "bruh";
                cantReplace = callingPackage.equalsIgnoreCase("com.tencent.mm"); //simplified so it might look weird at first glance

                RadioGroup rgPopup = findViewById(R.id.radioGroup);
                int id = rgPopup.getCheckedRadioButtonId();

                int sel;
                switch (id) {
                    case R.id.popupRadioType2:
                        sel = 2;
                        break;
                    case R.id.popupRadioType3:
                        sel = 3;
                        break;
                    case R.id.popupRadioType4:
                        sel = 4;
                        break;
                    case R.id.popupRadioType5:
                        sel = 5;
                        break;
                    case R.id.popupRadioType6:
                        sel = 6;
                        break;
                    case R.id.popupRadioType7:
                        sel = 7;
                        break;
                    case R.id.popupRadioType8:
                        sel = 8;
                        break;
                    case R.id.popupRadioType9:
                        sel = 9;
                        break;
                    case R.id.popupRadioType10:
                        sel = 10;
                        break;
                    default:
                        sel = 1;
                        break;
                }
                CharSequence text = getIntent()
                        .getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
                String fromText = Objects.requireNonNull(text).toString();
                String resultText = Convert.openCCConv(fromText, sel, getApplicationContext());

                if (readonly || cantReplace) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("ConvertedChinese", resultText);
                    Objects.requireNonNull(clipboard).setPrimaryClip(clip);
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.menu_readonly, Toast.LENGTH_LONG);
                    toast.show();
                    finish();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(Intent.EXTRA_PROCESS_TEXT, resultText);
                    setResult(RESULT_OK, intent);
                    finish();
                }


            }
        });
    }
}
