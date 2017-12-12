package de.veesy.settings;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import de.veesy.R;

/**
 * Created by Daniel on 12.12.2017.
 * veesy.de
 * hs-augsburg
 */

public class AnimationView extends SurfaceView {
    private AnimationSprite sprite;
    private AnimationThread loop;

    public AnimationView(Context context) {
        super(context);
        loop = new AnimationThread(this);
        final SurfaceHolder holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                loop.setRunning(true);
                loop.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {}

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                boolean retry = true;
                loop.setRunning(false);
                while(retry){
                    try {
                        loop.join();
                        retry=false;
                    }catch(InterruptedException ignored){}
                }
            }
        });
        /*BitmapFactory.Options op = new BitmapFactory.Options();
        op.inDensity = 360;*/
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.intro_spritesheet_small);
        sprite = new AnimationSprite(this, bmp);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        sprite.onDraw(canvas);
    }
}
