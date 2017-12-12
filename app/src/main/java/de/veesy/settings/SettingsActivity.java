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
        setContentView(R.layout.settings);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        connectionManager = ConnectionManager.instance();
        contactsManager = ContactsManager.instance();

        if (getIntent().getBooleanExtra(Constants.FIRST_START_EXTRA, false)) {
            Util.showToast(this, R.string.fill_out_your_card_please, Toast.LENGTH_LONG);
            showOwnContact();
        }
    }

    public void bMyCardClicked(View view) {
        showOwnContact();
    }

    public void bIntroductionClicked(View view) {
        startActivity(new Intent(this, IntroductionActivity.class));
    }

    public void bRemoveDevicesClicked(View view) {
        if (connectionManager != null) {
            connectionManager.unpairAllDevices();
        }
    }

    public void bAboutClicked(View view) {
        //TODO about us info --> Webview mit unserer Website oder so......
    }

    private void showOwnContact() {
        try {
            Contact ownContact = contactsManager.getOwnContact(this, false);
            Intent intent = ViewContactEditableActivity.getIntent(this, ownContact);
            startActivity(intent);
        } catch (IOException e) {
            Util.showToast(this, "Error reading your card", Toast.LENGTH_SHORT);
        }
    }

    public void bShareClicked(View view) {
        finish();
    }
}
