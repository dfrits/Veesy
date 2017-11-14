package de.veesy.core;

import android.support.v7.widget.RecyclerView;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.view.View;

/**
 * Created by dfritsch on 24.10.2017.
 * veesy.de
 * hs-augsburg
 */

public class ScrollingLayoutCallback extends WearableLinearLayoutManager.LayoutCallback  {
    private static final float MAX_ICON_PROGRESS = 0.65f;

    @Override
    public void onLayoutFinished(View child, RecyclerView parent) {

        // Figure out % progress from top to bottom
        float centerOffset = ((float) child.getHeight() / 2.0f) / (float) parent.getHeight();
        float yRelativeToCenterOffset = (child.getY() / parent.getHeight()) + centerOffset;

        /*// Normalize for center
        float mProgressToCenter = Math.abs(0.5f - yRelativeToCenterOffset);
        // Adjust to the maximum scale
        mProgressToCenter = Math.min(mProgressToCenter, MAX_ICON_PROGRESS);*/

        float yProgresstoCenter = (float) Math.sin(yRelativeToCenterOffset*Math.PI);
        float xProgresstoCenter = (float) Math.cos(yRelativeToCenterOffset*Math.PI);

        child.setScaleX(yProgresstoCenter);
        child.setScaleY(yProgresstoCenter);
    }
}
