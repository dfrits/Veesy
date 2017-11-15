package de.veesy.core;

import android.support.v7.widget.RecyclerView;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.util.Log;
import android.view.View;

/**
 * Created by dfritsch on 24.10.2017.
 * veesy.de
 * hs-augsburg
 */

public class ScrollingLayoutCallback extends WearableLinearLayoutManager.LayoutCallback  {

    @Override
    public void onLayoutFinished(View child, RecyclerView parent) {

        // Figure out % progress from top to bottom
        float centerOffset = ((float) child.getHeight() / 2.0f) / (float) parent.getHeight();
        float yRelativeToCenterOffset = (child.getY() / parent.getHeight()) + centerOffset;

        float progresstoCenter = (float) (Math.sin(yRelativeToCenterOffset * Math.PI)+0.1);

        Log.d("OFFSETT", String.valueOf(progresstoCenter));

        child.setScaleX(progresstoCenter);
        child.setScaleY(progresstoCenter);


    }
}
