package de.veesy.listview_util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by dfritsch on 17.11.2017.
 * veesy.de
 * hs-augsburg
 */

public class StraightListAdapter extends ArrayAdapter<String> {

    public StraightListAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
    }
}
