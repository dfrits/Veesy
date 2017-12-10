package de.veesy.settings;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import de.veesy.R;
import de.veesy.core.MainMenu;

/**
 * Created by dfritsch on 18.11.2017.
 * veesy.de
 * hs-augsburg
 */

public class IntroductionActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introduction);

        ImageView introImage = findViewById(R.id.introAnimation);
        introImage.setBackgroundResource(R.drawable.intro);
        AnimationDrawable introAnimation = (AnimationDrawable) introImage.getBackground();
        introAnimation.start();
    }

    public void onIntroAnimationClick(View view) {
        startActivity(new Intent(this, MainMenu.class));
    }
}
