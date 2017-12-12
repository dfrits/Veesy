package de.veesy.settings;

import android.graphics.Canvas;

/**
 * Created by Daniel on 12.12.2017.
 * veesy.de
 * hs-augsburg
 */
public class AnimationThread extends Thread {
    private AnimationView animationView;
    private boolean isRunning = false;
    private static final long FPS = 20;

    public AnimationThread(AnimationView view) {
        animationView = view;
    }

    public void setRunning(boolean run) {
        isRunning = run;
    }

    @Override
    public void run() {
        long TPS = 1000 / FPS;
        long startTime, sleepTime;
        while (isRunning) {
            Canvas canvas = null;
            startTime = System.currentTimeMillis();
            try {
                canvas = animationView.getHolder().lockCanvas();
                synchronized (animationView.getHolder()) {
                    animationView.draw(canvas);
                }
            } finally {
                if (canvas != null) {
                    animationView.getHolder().unlockCanvasAndPost(canvas);
                }
            }
            sleepTime = TPS - (System.currentTimeMillis() - startTime);
            try {
                if (sleepTime > 0)
                    sleep(sleepTime);
                else
                    sleep(10);
            } catch (Exception ignored) {}
        }
    }
}
