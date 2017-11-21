package de.veesy.listview_util;

import android.graphics.drawable.Drawable;

/**
 * Created by dfritsch on 18.11.2017.
 * veesy.de
 * hs-augsburg
 */

public class AdapterObject {
    private final String itemName;
    private final Drawable img;

    public AdapterObject(String itemName, Drawable imgResource) {
        this.itemName = itemName;
        this.img = imgResource;
    }

    String getItemName() {
        return itemName;
    }

    Drawable getImg() {
        return img;
    }
}
