package de.veesy.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

import de.veesy.R;
import de.veesy.connection.ConnectionManager;

/**
 * Created by dfritsch on 24.10.2017.
 * veesy.de
 * hs-augsburg
 */
public class MainMenu extends Activity {
    // Counter für das Beenden der App
    private static int counter = 0;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        initConnectionManager();
    }

    // launching
    private void initConnectionManager() {
        ConnectionManager.instance();
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
        //TODO ContactsActivity starten und entsprechenden Screen anzeigen
        //        startActivity(new Intent(this, ContactsActivity.class));

    }

    /**
     * Aktion des Settings-Buttons.
     * @param view .
     */
    public void bSettingsClicked(View view) {
        //TODO SettinsgActivity starten und entsprechenden Screen anzeigen
        //        startActivity(new Intent(this, SettingsActivity.class));

    }

    @Override
    protected void onPause() {
        counter = 0;
        if (timer != null) timer.cancel();
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /** TODO Untere Button?
     * Diese Methhode erwartet ein KeyEvent, wenn der untere Button der Uhr gedrückt
     * wird (ID = 265) dann wird des counter bis 5 hochgezählt
     * dann sollte onDestroy() aufgerufen werden, funktioniert allerdings nicht (???)
     * @param keyCode .
     * @param event .
     * @return Immer true
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println("keyCode: " + keyCode + " Event: " + event);

        if (keyCode == 265 && event.getAction() == KeyEvent.ACTION_DOWN) {
            counter++;
            if (counter == 2) {
                finish();
            } else {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        counter = 0;
                    }
                }, 1000);
            }
        }
        return true;
    }
}
