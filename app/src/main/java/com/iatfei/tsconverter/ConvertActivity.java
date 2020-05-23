package com.iatfei.tsconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Objects;

public class ConvertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);

        boolean readonly = getIntent()
                .getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false);



        if (readonly)
            failed();
        else {
            CharSequence text = getIntent()
                    .getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
            String fromText = Objects.requireNonNull(text).toString();
            String resultText = Convert.openCCConv(fromText, 1, getApplicationContext());

            Toast toast = Toast.makeText(getApplicationContext(), getIntent().getPackage(), Toast.LENGTH_LONG);
            toast.show();

            Intent intent = new Intent();
            intent.putExtra(Intent.EXTRA_PROCESS_TEXT, resultText);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void failed() {
        Toast toast = Toast.makeText(getApplicationContext(), R.string.menu_readonly, Toast.LENGTH_LONG);
        toast.show();
    }
}
