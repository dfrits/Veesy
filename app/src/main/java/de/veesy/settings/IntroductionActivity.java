package de.veesy.settings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import de.veesy.R;
import de.veesy.util.Constants;

/**
 * Created by dfritsch on 18.11.2017.
 * veesy.de
 * hs-augsburg
 */

public class IntroductionActivity extends Activity implements View.OnTouchListener {
    private boolean firstStart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnimationView view = new AnimationView(this);
        setContentView(view);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        firstStart = pref.getBoolean(Constants.INTRODUCTION_FIRST_START_EXTRA, false);
        if (firstStart) {
            pref.edit().putBoolean(Constants.APP_FIRST_START_EXTRA, false).apply();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (firstStart) {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra(Constants.INTRODUCTION_FIRST_START_EXTRA, true);
            startActivity(intent);
        }
        finish();
        view.performClick();
        return false;
    }
}
