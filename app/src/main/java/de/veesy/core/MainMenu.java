package de.veesy.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import de.veesy.R;
import de.veesy.connection.ConnectionManager;

/**
 * Created by dfritsch on 24.10.2017.
 * veesy.de
 * hs-augsburg
 */
public class MainMenu extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        initConnectionManager();

    }

    // launching
    private void initConnectionManager(){
        ConnectionManager.instance();
    }

    /**
     * Aktion des Share-Buttons.
     * @param view .
     */
    public void bShareClicked(View view) {
        startActivity(new Intent(this, ShareActivity.class));
        finish();
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




}
