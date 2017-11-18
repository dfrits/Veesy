package de.veesy.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import de.veesy.R;
import de.veesy.connection.ConnectionManager;
import de.veesy.connection.MESSAGE;
import de.veesy.contacts.ContactsActivity;
import de.veesy.settings.SettingsActivity;

/**
 * Created by dfritsch on 24.10.2017.
 * veesy.de
 * hs-augsburg
 */
public class MainMenu extends Activity implements Observer {
    // Counter für das Beenden der App
    private static int counter = 0;
    ConnectionManager cm;
    CountDownTimer countDownTimer;

    private float x1, x2;
    static final int MIN_DISTANCE = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        initConnectionManager();
    }

    // launching
    private void initConnectionManager() {
        cm = ConnectionManager.instance();
        cm.addObserver(this);
    }

    /**
     * Aktion des Share-Buttons.
     * @param view .
     */
    public void bShareClicked(View view) {
        startActivity(new Intent(this, ShareActivity.class));
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

    @Override
    protected void onPause() {
        counter = 0;
        super.onPause();
    }

    protected void onStop() {
        cm.deleteObserver(this);
        System.out.println("onStop called");
        super.onStop();
    }

    protected void onStart() {
        cm.addObserver(this);
        super.onStart();
    }


    @Override
    protected void onDestroy() {
        System.out.println("onDestroy called");
        super.onDestroy();
    }

    /**
     * diese Methode soll Swipe rechts detektieren
     * funktioniert so lala
     * @param event
     * @return
     */

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;
                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    System.out.println("Swipe right detected...");
                }
                break;
        }
        return super.onTouchEvent(event);
    }


    /**
     * TODO swipe detector, rechts swipe mit finish aufrufen
     * @param keyCode .
     * @param event   .
     * @return Immer true
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println("keyCode: " + keyCode + " Event: " + event);
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
                System.out.println("Shutting down....");
                cm.setBackOriginalDeviceName();

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

    @Override
    public void update(Observable observable, Object o) {
        if ((Integer) o == MESSAGE.READY_TO_SHUTDOWN) {
            finish();
        }
    }
}
