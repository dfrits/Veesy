package de.veesy.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.ShakeDetector;

import java.util.Observable;
import java.util.Observer;

import de.veesy.R;
import de.veesy.connection.ConnectionManager;
import de.veesy.connection.MESSAGE;
import de.veesy.contacts.ContactsActivity;
import de.veesy.settings.SettingsActivity;
import de.veesy.util.IntroductionActivity;
import de.veesy.util.Util;

/**
 * Created by dfritsch on 24.10.2017.
 * veesy.de
 * hs-augsburg
 */
public class MainMenu extends Activity implements Observer {
    private ConnectionManager cm = null;
    private int counter = 0;
    ShakeDetector.ShakeListener shakeListener;
    CountDownTimer countDownTimer = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        //Introduction bei mersten Start der App
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (pref.getBoolean("FirstStart", true)) {
            pref.edit().putBoolean("FirstStart", false).apply();
            startActivity(new Intent(this, IntroductionActivity.class));
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



        /**
         * This is an example on how to use Sensey
         */
        Sensey.getInstance().init(this);
        shakeListener = new ShakeDetector.ShakeListener() {
            @Override
            public void onShakeDetected() {
                // Shake detected, do something
                counter++;
                System.out.println("Counter: " + counter);
                System.out.println("Shake detected");

                if(counter > 30){
                    startShare();
                }
            }

            @Override
            public void onShakeStopped() {
                // Shake stopped, do something
                System.out.println("Shake on Stop");
                counter = 0;
            }
        };


    }


    public void startShare(){
        counter = 0;
        Sensey.getInstance().stopShakeDetection(shakeListener);
        onStop();
        startActivity(new Intent(this, ShareActivity.class));
    }

    protected void onStart() {
        System.out.println("onStart called");
        initConnectionManager();
        super.onStart();
    }

    // launching
    private void initConnectionManager() {
        cm = ConnectionManager.instance();
        Sensey.getInstance().startShakeDetection(shakeListener);
        cm.addObserver(this);
        cm.btCheckPermissions(this);
    }

    /**
     * Aktion des Share-Buttons.
     *
     * @param view .
     */
    public void bShareClicked(View view) {
        if (cm.checkName()) startActivity(new Intent(this, ShareActivity.class));
        else Util.showToast(this, "Renaming device... try again", Toast.LENGTH_SHORT);
    }

    /**
     * Aktion des Contacts-Buttons.
     *
     * @param view .
     */
    public void bContactsClicked(View view) {
        startActivity(new Intent(this, ContactsActivity.class));
    }

    /**
     * Aktion des Settings-Buttons.
     *
     * @param view .
     */
    public void bSettingsClicked(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    /**
     * Clicking this button will shutdown the app
     * Therefore, setBackOriginalName() is called which
     * will notify this observer with MESSAGE.READY_TO_SHUTDOWN
     * in update() the app calls finish()
     *
     * @param view
     */
    public void bShutdownClicked(View view) {
        Util.showToast(this, "Shutdown", Toast.LENGTH_SHORT);
        cm.unpairAllDevices();
        cm.setBackOriginalDeviceName();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onStop() {
        if (cm != null) cm.deleteObserver(this);
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        // We need to do this because somehow it happens that the connection manager is still alive
        cm.finish();
        super.onDestroy();
    }


    @Override
    public void update(Observable observable, Object o) {
        if ((Integer) o == MESSAGE.READY_TO_SHUTDOWN) {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 265 && event.getAction() == KeyEvent.ACTION_DOWN) {
            counter++;
            if (counter == 1) {
                final Context context = this;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Press twice to shutdown..", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            if (counter == 2) {
                if (countDownTimer != null) countDownTimer.cancel();
                System.out.println("Forcing Shutdown");
                //cm.setBackOriginalDeviceName();
                finish();

            } else {
                countDownTimer = new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        counter = 0;
                    }
                }.start();

            }
            if (keyCode == 265 && event.getAction() == KeyEvent.ACTION_UP) {
            }

        }
        return true;
    }
}
