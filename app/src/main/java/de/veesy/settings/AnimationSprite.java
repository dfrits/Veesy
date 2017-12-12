package de.veesy.settings;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by Daniel on 12.12.2017.
 * veesy.de
 * hs-augsburg
 */

public class AnimationSprite {
    static final private int BMP_COLUMNS = 6;
    static final private int BMP_ROWS = 6;

    private Bitmap bmp;
    private View view;
    private int width;
    private int height;
    private int currentColumn = 0;
    private int currentRow = 0;
    private Rect source;
    private Rect destine;

    public AnimationSprite(View view, Bitmap bmp) {
        this.view = view;
        this.bmp = bmp;
        this.width = bmp.getWidth() / BMP_COLUMNS;
        this.height = bmp.getHeight() / BMP_ROWS;
    }

    public void setRects() {
        currentColumn++;
        if (currentColumn > BMP_COLUMNS) {
            currentColumn = 0;
            currentRow++;
        }
        int sourceX = currentColumn * width;
        int sourceY = currentRow * height;
        source = new Rect(sourceX, sourceY, sourceX + width, sourceY + height);
        destine = new Rect(0, 0, view.getWidth(), view.getHeight());
    }

    public void onDraw(Canvas canvas) {
        if (bmp == null || source == null || destine == null) {
            return;
        }
        canvas.drawBitmap(bmp, source, destine, null);
    }
}
