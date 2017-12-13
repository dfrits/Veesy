package de.veesy.settings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.Set;

import de.veesy.R;
import de.veesy.core.MainMenu;
import de.veesy.util.Constants;


/**
 * Created by dfritsch on 18.11.2017.
 * veesy.de
 * hs-augsburg
 */

public class IntroductionActivity extends Activity {
    private boolean firstStart;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introduction);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        firstStart = pref.getBoolean(Constants.INTRODUCTION_FIRST_START_EXTRA, false);
        if (firstStart) {
            pref.edit().putBoolean(Constants.APP_FIRST_START_EXTRA, false).apply();
        }

        ImageView introImage = findViewById(R.id.introAnimation);
        introImage.setBackgroundResource(R.drawable.intro);
        AnimationDrawable introAnimation = (AnimationDrawable) introImage.getBackground();
        introAnimation.start();
    }

    public void onIntroAnimationClick(View view) {
        if (firstStart) {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra(Constants.INTRODUCTION_FIRST_START_EXTRA, true);
        startActivity(intent);
        }
        finish();
    }
}
