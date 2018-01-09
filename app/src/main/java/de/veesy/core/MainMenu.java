package de.veesy.core;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.wearable.activity.WearableActivity;
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
import de.veesy.introduction.IntroductionActivity;
import de.veesy.settings.SettingsActivity;
import de.veesy.util.Constants;
import de.veesy.util.Util;

/**
 * Created by dfritsch on 24.10.2017.
 * veesy.de
 * hs-augsburg
 */
public class MainMenu extends WearableActivity implements Observer {
    private ConnectionManager connectionManager = null;
    private ContactsManager contactsManager = null;

    private Contact my_contact = null;

    private int shakesDetected = 0;
    private ShakeDetector.ShakeListener shakeListener;

    private SharedPreferences pref = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Introduction beim ersten Start der App
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        if (pref.getBoolean(Constants.APP_FIRST_START_EXTRA, true)) {
            //        if (true) {
            pref.edit().putBoolean(Constants.APP_FIRST_START_EXTRA, false).apply();
            Intent intent = new Intent(this, IntroductionActivity.class);
            intent.putExtra(Constants.INTRODUCTION_FIRST_START_EXTRA, true);
            startActivityForResult(intent, Constants.INTRODUCTION_REQUEST_CODE);
        } else {
            setContentView(R.layout.main_menu);
        }
        initContactsManager();
        initConnectionManager();
        initSensey();
    }

    private void initContactsManager() {
        contactsManager = ContactsManager.instance();
        try {
            my_contact = contactsManager.getOwnContact(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initConnectionManager() {
        connectionManager = ConnectionManager.instance();
        connectionManager.setSendContact(my_contact);
        connectionManager.device_setVeesyName();
    }

    private void startConnectionManager() {
        connectionManager.addObserver(this);
        connectionManager.btCheckPermissions(this);
    }

    public void startShare() {
        System.out.println("StartShare called from main");


        // TODO für debug zwecke, kann wieder naus
        try {
            my_contact = contactsManager.getOwnContact(this);
            connectionManager.setSendContact(my_contact);
        } catch (IOException e) {
            e.printStackTrace();
        }

        shakesDetected = 0;
        Sensey.getInstance().stopShakeDetection(shakeListener);
        if (connectionManager.checkName()) startActivity(new Intent(this, ShareActivity.class));
        else Util.showToast(this, "Renaming device... try again", Toast.LENGTH_SHORT);

        //setContentView(R.layout.main_menu);
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
        overridePendingTransition(R.anim.slide_in_left, R.anim.stay);
    }

    /**
     * Aktion des Settings-Buttons.
     * @param view .
     */
    public void bSettingsClicked(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
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
        connectionManager.unpairAllDevices(false);
        connectionManager.setBackOriginalDeviceName();
    }

    @Override
    public void update(Observable observable, Object o) {
        if ((Integer) o == MESSAGE.READY_TO_SHUTDOWN) {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Wird die Einführung beendet, wird die View gesetzt.
        if (requestCode == Constants.INTRODUCTION_REQUEST_CODE) {
            setContentView(R.layout.main_menu);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startConnectionManager();
    }


    @Override
    protected void onResume() {
        super.onResume();
        //Sensey.getInstance().startShakeDetection(threshold,timeBeforeDeclaringShakeStopped,shakeListener);
        // default: threshold: 3.0F, timeBeforeCeclaringShakeStopped: 1000L

        long timeBeforeDeclaringShakeStopped = pref.getLong(Constants.SHAKE_TIME, 650L);
        float threshold = pref.getFloat(Constants.SHAKE_TIME, 5.0F);
        if (shakeListener != null)
            Sensey.getInstance().startShakeDetection(threshold, timeBeforeDeclaringShakeStopped, shakeListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (connectionManager != null) connectionManager.deleteObserver(this);
        Sensey.getInstance().stopShakeDetection(shakeListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // We need to do this because somehow it happens that the connection manager is still alive
        connectionManager.finish();
    }

    private void initSensey() {
        /*
         * This is an example on how to use Sensey
         */

        Sensey.getInstance().init(this);

        if (shakeListener != null) {
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

        //long timeBeforeDeclaringShakeStopped = pref.getLong(Constants.SHAKE_TIME, 650L);
        long timeBeforeDeclaringShakeStopped = 1000L;
        //float threshold = pref.getFloat(Constants.SHAKE_TIME, 5.0F);long timeBeforeDeclaringShakeStopped = pref.getLong(Constants.SHAKE_TIME, 650L);
        float threshold = 3.0F;

        if (shakeListener != null) {
            //Sensey.getInstance().startShakeDetection(threshold, timeBeforeDeclaringShakeStopped, shakeListener);
            Sensey.getInstance().startShakeDetection(shakeListener);
        }
    }
}
