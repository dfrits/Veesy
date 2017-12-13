package de.veesy.settings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import de.veesy.R;
import de.veesy.connection.ConnectionManager;
import de.veesy.contacts.Contact;
import de.veesy.contacts.ContactsManager;
import de.veesy.contacts.ViewContactEditableActivity;
import de.veesy.util.Constants;
import de.veesy.util.Util;

import static de.veesy.util.Constants.DEBUGGING;

/**
 * Created by dfritsch on 17.11.2017.
 * veesy.de
 * hs-augsburg
 */

public class SettingsActivity extends Activity implements EditText.OnEditorActionListener {
    private ConnectionManager connectionManager;
    private ContactsManager contactsManager;

    private EditText tCounter;
    private EditText tThreshold;
    private EditText tTime;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.settings);
        connectionManager = ConnectionManager.instance();
        contactsManager = ContactsManager.instance();

        if (getIntent().getBooleanExtra(Constants.INTRODUCTION_FIRST_START_EXTRA, false)) {
            Util.showToast(this, R.string.fill_out_your_card_please, Toast.LENGTH_LONG);
            showOwnContact();
        }

//        if (!DEBUGGING) {
//            LinearLayout settingsLayout = findViewById(R.id.settings_view);
//            settingsLayout.removeView(findViewById(R.id.tCounter));
//            settingsLayout.removeView(findViewById(R.id.tThreshold));
//            settingsLayout.removeView(findViewById(R.id.tTime));
//        } else {
//            tCounter = findViewById(R.id.tCounter);
//            tThreshold = findViewById(R.id.tThreshold);
//            tTime = findViewById(R.id.tTime);
//        }
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
            connectionManager.unpairAllDevices();
            Util.showToast(this, getString(R.string.devies_removed), Toast.LENGTH_SHORT);
        }
    }

    public void tAboutClicked(View view) {
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

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
        edit.putInt(Constants.SHAKE_COUNTER, Integer.parseInt(tCounter.getText().toString()));
        edit.putFloat(Constants.SHAKE_THRESHOLD, Float.parseFloat(tCounter.getText().toString()));
        edit.putLong(Constants.SHAKE_TIME, Long.parseLong(tCounter.getText().toString()));
        edit.apply();
        return true;
    }
}
