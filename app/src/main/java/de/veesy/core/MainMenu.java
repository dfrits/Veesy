package de.veesy.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;
import java.util.Timer;

import de.veesy.R;
import de.veesy.connection.ConnectionManager;
import de.veesy.connection.MESSAGE;

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
     *
     * @param view .
     */
    public void bShareClicked(View view) {
        startActivity(new Intent(this, ShareActivity.class));
    }

    /**
     * Aktion des Contacts-Buttons.
     *
     * @param view .
     */
    public void bContactsClicked(View view) {
        //TODO ContactsActivity starten und entsprechenden Screen anzeigen
        //        startActivity(new Intent(this, ContactsActivity.class));

    }

    /**
     * Aktion des Settings-Buttons.
     *
     * @param view .
     */
    public void bSettingsClicked(View view) {
        //TODO SettinsgActivity starten und entsprechenden Screen anzeigen
        //        startActivity(new Intent(this, SettingsActivity.class));

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
     * TODO swipe detector, rechts swipe mit finish aufrufen
     *
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
                if(countDownTimer!= null) countDownTimer.cancel();
                System.out.println("Shutting down....");
                cm.setBackOriginalDeviceName();

            } else {
                countDownTimer = new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        counter-=2;
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
