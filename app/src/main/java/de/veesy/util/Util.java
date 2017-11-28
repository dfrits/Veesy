package de.veesy.util;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Toast;

/**
 * Created by Daniel on 23.11.2017.
 * veesy.de
 * hs-augsburg
 */

public abstract class Util {

    /**
     * Nimmt den ganzen Schmarn mit runOnUiThread... ab und zeigt einen Toast für eine beliebige
     * Zeit an.
     * @param activity       Die aufrufende Acitivity
     * @param stringResource Text der angezeigt werden soll als String-Resource
     * @param length         Dauer der Anzeige. {@link android.widget.Toast#LENGTH_SHORT
     *                       Toast.LENGTH_SHORT} oder {@link android.widget.Toast#LENGTH_LONG
     *                       Toast.LENGTH_LONG}
     */
    public static void showToast(final Activity activity, final int stringResource, final int length) {
        showToast(activity, activity.getResources().getString(stringResource), length);
    }

    /**
     * Die selbe wie {@link Util#showToast(Activity, int, int)}, nur mit einem String statt @string.
     * @param activity Die aufrufende Acitivity
     * @param message  Text der angezeigt werden soll als String
     * @param length   Dauer der Anzeige. {@link android.widget.Toast#LENGTH_SHORT
     *                 Toast.LENGTH_SHORT} oder {@link android.widget.Toast#LENGTH_LONG
     *                 Toast.LENGTH_LONG}
     */
    public static void showToast(final Activity activity, final String message, final int length) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity.getApplicationContext(), message, length).show();
            }
        });
    }

    public static void runOnUiAnimation(final Activity activity, final View view, final Animation animation){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.startAnimation(animation);
            }
        });
    }
}
