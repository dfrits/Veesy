package de.veesy.core;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.wearable.activity.WearableActivity;
import android.view.KeyEvent;
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
import de.veesy.settings.IntroductionActivity;
import de.veesy.settings.SettingsActivity;
import de.veesy.util.Util;

/**
 * Created by dfritsch on 24.10.2017.
 * veesy.de
 * hs-augsburg
 */
public class MainMenu extends WearableActivity implements Observer {
    int debugCounter = 0;
    private ConnectionManager connectionManager = null;
    private int shakesDetected = 0;
    private ShakeDetector.ShakeListener shakeListener;
    private CountDownTimer countDownTimer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        isFirstUsed();
        setContentView(R.layout.main_menu);
        initSensey();
    }

    private void initSensey() {
        /*
         * This is an example on how to use Sensey
         */
        Sensey.getInstance().init(this);
        shakeListener = new ShakeDetector.ShakeListener() {
            @Override
            public void onShakeDetected() {
                // Shake detected, do something
                shakesDetected++;
                System.out.println("ShakeCounter: " + shakesDetected);
                if (shakesDetected == 30) {
                    startShare();
                }
            }

            @Override
            public void onShakeStopped() {
                // Shake stopped, do something
                System.out.println("Shake on Stop");
                shakesDetected = 0;
            }
        };
    }

    private void isFirstUsed() {
        //Introduction beim ersten Start der App
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (pref.getBoolean("FirstStart", true)) {
            pref.edit().putBoolean("FirstStart", false).apply();
            startActivity(new Intent(this, IntroductionActivity.class));
        }
    }

    protected void onStart() {
        System.out.println("Main onStart called");
        initConnectionManager();
        super.onStart();
    }

    protected void onResume() {
        //Sensey.getInstance().startShakeDetection(threshold,timeBeforeDeclaringShakeStopped,shakeListener);
        // default: threshold: 3.0F, timeBeforeCeclaringShakeStopped: 1000L
        Sensey.getInstance().startShakeDetection(5.0F, 650L, shakeListener);
        super.onResume();
    }

    // launching
    private void initConnectionManager() {
        connectionManager = ConnectionManager.instance();
        connectionManager.addObserver(this);
        connectionManager.btCheckPermissions(this);
    }

    /**
     * Aktion des Share-Buttons.
     * @param view .
     */
    public void bShareClicked(View view) {
        startShare();
    }


    public void startShare() {
        System.out.println("StartShare called");
        shakesDetected = 0;
        //TODO
        // rework user flow / automatisch shareAct starten wenn der name korrekt ist
        if (connectionManager.checkName()) startActivity(new Intent(this, ShareActivity.class));
        else Util.showToast(this, "Renaming device... try again", Toast.LENGTH_SHORT);
    }


    /**
     * Aktion des Contacts-Buttons.
     * @param view .
     */
    public void bContactsClicked(View view) {
        startActivity(new Intent(this, ContactsActivity.class));
    }

    /**
     * Aktion des Settings-Buttons.
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
     * @param view .
     */
    public void bShutdownClicked(View view) {
        Util.showToast(this, "Shutdown", Toast.LENGTH_SHORT);
        connectionManager.addObserver(this);
        connectionManager.unpairAllDevices();
        connectionManager.setBackOriginalDeviceName();
    }

    @Override
    public void update(Observable observable, Object o) {
        if ((Integer) o == MESSAGE.READY_TO_SHUTDOWN) {
            finish();
        }
    }

    protected void onStop() {
        System.out.println("Main onStop called");
        if (connectionManager != null) connectionManager.deleteObserver(this);
        Sensey.getInstance().stopShakeDetection(shakeListener);
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        // We need to do this because somehow it happens that the connection manager is still alive
        connectionManager.finish();
        super.onDestroy();
    }

    // Debug
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 265 && event.getAction() == KeyEvent.ACTION_DOWN) {
            debugCounter++;
            if (debugCounter == 1) {
                final Context context = this;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Press twice to shutdown..", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            if (debugCounter == 2) {
                if (countDownTimer != null) countDownTimer.cancel();
                System.out.println("Forcing Shutdown");
                //connectionManager.setBackOriginalDeviceName();
                finish();

            } else {
                countDownTimer = new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        debugCounter = 0;
                    }
                }.start();

            }
            if (keyCode == 265 && event.getAction() == KeyEvent.ACTION_UP) {
            }

        }
        return true;
    }
}
