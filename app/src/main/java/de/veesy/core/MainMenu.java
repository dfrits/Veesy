package de.veesy.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
    private static int backpressed = 0;

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
    }

    /**
     * Aktion des Settings-Buttons.
     * @param view .
     */
    public void bSettingsClicked(View view) {
    }

    @Override
    protected void onPause() {
        backpressed = 0;
        super.onPause();
    }

    /**
     * Beendet die App, wenn der Nutzer den Zurück-Button 2mal gedrückt hat. Wurde nach einer
     * Sekunde der Button nicht zweimal gedrückt, dann wird der Counter zurück gesetzt.
     */
    @Override
    public void onBackPressed() {
        backpressed++;
        if (backpressed == 2) {
            finish();
        } else {
            Toast.makeText(this, R.string.backpress_hint, Toast.LENGTH_SHORT).show();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    backpressed = 0;
                }
            }, 1000);
        }
    }
}
