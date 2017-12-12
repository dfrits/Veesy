package de.veesy.core;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import de.veesy.R;
import de.veesy.connection.ConnectionManager;
import de.veesy.connection.MESSAGE;
import de.veesy.contacts.Contact;
import de.veesy.contacts.ContactsActivity;
import de.veesy.contacts.ContactsManager;
import de.veesy.settings.IntroductionActivity;
import de.veesy.settings.SettingsActivity;
import de.veesy.util.Constants;
import de.veesy.util.Util;

import static de.veesy.util.Constants.DEBUGGING;

/**
 * Created by dfritsch on 24.10.2017.
 * veesy.de
 * hs-augsburg
 */
public class MainMenu extends WearableActivity implements Observer {
    private int debugCounter = 0;
    private ConnectionManager connectionManager = null;
    private ContactsManager contactsManager = null;

    private Contact my_contact = null;

    private int shakesDetected = 0;
    private ShakeDetector.ShakeListener shakeListener;
    private CountDownTimer countDownTimer = null;

    private SharedPreferences pref = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Introduction beim ersten Start der App
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (!DEBUGGING && pref.getBoolean(Constants.APP_FIRST_START_EXTRA, true)) {
            Intent intent = new Intent(this, IntroductionActivity.class);
            intent.putExtra(Constants.INTRODUCTION_FIRST_START_EXTRA, true);
            startActivity(intent);
        }

        initContactsManager();
        initConnectionManager();
        setContentView(R.layout.main_menu);
        initSensey();
    }

    private void initContactsManager(){
        contactsManager = ContactsManager.instance();
        try{
            my_contact = contactsManager.getOwnContact(this, true);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void initConnectionManager(){
        connectionManager = ConnectionManager.instance();
        connectionManager.setSendContact(my_contact);
        connectionManager.device_setVeesyName();
    }

    private void startConnectionManager() {
        connectionManager.addObserver(this);
        connectionManager.btCheckPermissions(this);
    }

    public void startShare() {
        System.out.println("StartShare called");
        shakesDetected = 0;
        if (connectionManager.checkName()) startActivity(new Intent(this, ShareActivity.class));
        else Util.showToast(this, "Renaming device... try again", Toast.LENGTH_SHORT);
    }

    /**
     * Aktion des Share-Buttons.
     * @param view .
     */
    public void bShareClicked(View view) {
        startShare();
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
    protected void onStart() {
        startConnectionManager();
        super.onStart();
    }

    @Override
    protected void onResume() {
        //Sensey.getInstance().startShakeDetection(threshold,timeBeforeDeclaringShakeStopped,shakeListener);
        // default: threshold: 3.0F, timeBeforeCeclaringShakeStopped: 1000L

        long timeBeforeDeclaringShakeStopped = pref.getLong(Constants.SHAKE_TIME, 650L);
        float threshold = pref.getFloat(Constants.SHAKE_TIME, 5.0F);

        if (shakeListener != null) Sensey.getInstance().startShakeDetection(threshold, timeBeforeDeclaringShakeStopped, shakeListener);
        super.onResume();
    }

    @Override
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

    @Override
    public void update(Observable observable, Object o) {
        if ((Integer) o == MESSAGE.READY_TO_SHUTDOWN) {
            finish();
        }
    }

    private void initSensey() {
        /*
         * This is an example on how to use Sensey
         */

        Sensey.getInstance().init(this);

        if(shakeListener != null){
            Sensey.getInstance().stopShakeDetection(shakeListener);
            shakeListener = null;
        }

        shakeListener = new ShakeDetector.ShakeListener() {
            @Override
            public void onShakeDetected() {
                // Shake detected, do something
                shakesDetected++;
                //System.out.println("ShakeCounter: " + shakesDetected);
                int count = pref.getInt(Constants.SHAKE_COUNTER, 30);
                System.out.println("Shake count: " + count);
                if (shakesDetected == count) {
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

    // Debug
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 265 && event.getAction() == KeyEvent.ACTION_DOWN) {

            initSensey();
            onResume();
            /*debugCounter++;
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
            }*/

        }
        return true;
    }
}
