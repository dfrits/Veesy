package de.veesy.introduction;

import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * Created by dfritsch on 08.01.2018.
 * veesy.de
 * hs-augsburg
 */

class IntroAnimation extends AsyncTask<ImageView, Void, AnimationDrawable> {
    private AnimationDrawable introAnimation;

    @Override
    protected AnimationDrawable doInBackground(ImageView... imageViews) {
        ImageView introImage = imageViews[0];
        return (AnimationDrawable) introImage.getBackground();
    }

    @Override
    protected void onPostExecute(AnimationDrawable animationDrawable) {
        introAnimation = animationDrawable;
    }

    AnimationDrawable getIntroAnimation() {
        return introAnimation;
    }
}
