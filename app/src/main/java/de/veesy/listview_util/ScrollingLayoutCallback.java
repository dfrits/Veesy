package de.veesy.listview_util;

import android.support.v7.widget.RecyclerView;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.view.View;

/**
 * Created by dfritsch on 24.10.2017.
 * veesy.de
 * hs-augsburg
 */

public class ScrollingLayoutCallback extends WearableLinearLayoutManager.LayoutCallback  {

    @Override
    public void onLayoutFinished(View child, RecyclerView parent) {

        float centerOffset = ((float) child.getHeight() / 2.0f) / (float) parent.getHeight();
        float yRelativeToCenterOffset = (child.getY() / parent.getHeight()) + centerOffset;

        float progressToCenter = (float) (Math.sin(yRelativeToCenterOffset * Math.PI)+0.16);

        child.setScaleX(progressToCenter);
        child.setScaleY(progressToCenter);
    }
}
