package de.veesy.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.IOException;

import de.veesy.R;
import de.veesy.connection.ConnectionManager;
import de.veesy.contacts.Contact;
import de.veesy.contacts.ContactsManager;
import de.veesy.contacts.ViewContactEditableActivity;
import de.veesy.introduction.IntroductionActivity;
import de.veesy.util.Constants;
import de.veesy.util.Util;

/**
 * Created by dfritsch on 17.11.2017.
 * veesy.de
 * hs-augsburg
 */

public class SettingsActivity extends Activity {
    private ConnectionManager connectionManager;
    private ContactsManager contactsManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.settings);
        connectionManager = ConnectionManager.instance();
        contactsManager = ContactsManager.instance();
    }

    public void tMyCardClicked(View view) {
        showOwnContact();
    }

    public void tIntroductionClicked(View view) {
        Intent intent = new Intent(this, IntroductionActivity.class);
        intent.putExtra(Constants.INTRODUCTION_FIRST_START_EXTRA, false);
        startActivity(intent);
    }

    public void tRemoveDevicesClicked(View view) {
        if (connectionManager != null) {
            connectionManager.unpairAllDevices(true);
            Util.showToast(this, getString(R.string.devies_removed), Toast.LENGTH_SHORT);
        }
    }

    public void tAboutClicked(View view) {
        startActivity(new Intent(this, AboutUsActivity.class));
    }

    private void showOwnContact() {
        try {
            Contact ownContact = contactsManager.getOwnContact(this);
            Intent intent = ViewContactEditableActivity.getIntent(this, ownContact);
            startActivity(intent);
        } catch (IOException e) {
            Util.showToast(this, "Error reading your card", Toast.LENGTH_SHORT);
        }
    }

    public void bShareClicked(View view) {
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_out_right);
    }
}
