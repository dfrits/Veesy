package de.veesy.introduction;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import de.veesy.R;

/**
 * Created by dfritsch on 22.12.2017.
 * veesy.de
 * hs-augsburg
 * <p>
 * Seite 5 der Einführung. Die Animation startet, wenn die Seite aufgerufen wird.
 */

public class Tab5 extends Fragment {
    private AnimationDrawable introAnimation;
    private ImageView introImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View view = inflater.inflate(R.layout.gesture_animation_view, container, false);
        initAnimation(view);
        return view;
    }

    private void initAnimation(View view) {
        if (!introAnimation.isRunning()) {
            introImage = view.findViewById(R.id.introAnimation);
            introImage.setBackgroundResource(R.drawable.intro);
            introAnimation = (AnimationDrawable) introImage.getBackground();
            introAnimation.start();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        stopAnimation();
        introImage.setBackgroundResource(R.drawable.intro0001);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAnimation();
    }

    private void stopAnimation() {
        if (introAnimation != null && introAnimation.isRunning()) {
            introAnimation.stop();
            introAnimation = null;
        }
    }
}
