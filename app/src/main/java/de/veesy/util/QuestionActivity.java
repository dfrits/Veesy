package de.veesy.util;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import de.veesy.R;

/**
 * Created by Daniel on 03.01.2018.
 * veesy.de
 * hs-augsburg
 */

public class QuestionActivity extends Activity {
    public static final String TITEL = "TITEL";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.simple_question);

        TextView title = findViewById(R.id.tTitle);
        title.setText(getIntent().getStringExtra(TITEL));
    }

    public void bYesClicked(View view) {
        setResult(RESULT_OK);
        finish();
    }

    public void bNoClicked(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}
