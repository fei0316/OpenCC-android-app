package com.iatfei.tsconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

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
            String fromtext = text.toString();
            String resulttext = Convert.openCCConv(fromtext, 1, getApplicationContext());

            Toast toast = Toast.makeText(getApplicationContext(),resulttext, Toast.LENGTH_LONG);
            toast.show();

            Intent intent = new Intent();
            intent.putExtra(Intent.EXTRA_PROCESS_TEXT, resulttext);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
    protected void failed () {
        Toast toast = Toast.makeText(getApplicationContext(),"failed", Toast.LENGTH_LONG);
        toast.show();
    }
}
