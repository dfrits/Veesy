package de.veesy.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.IOException;

import de.veesy.R;
import de.veesy.connection.ConnectionManager;
import de.veesy.contacts.Contact;
import de.veesy.contacts.ContactsManager;
import de.veesy.util.Util;

/**
 * Created by dfritsch on 24.10.2017.
 * veesy.de
 * hs-augsburg
 * <p><p>
 * Es muss dem Intent ein BooleanExtra mitgegeben werden.
 * <p>
 * Key ist {@link FeedbackActivity#SUCCESS_FLAG}<p>
 * Wert ist: {@link true} für <b>Erfolg</b> und {@link false} für <b>Misserfolg</b>.
 * <p>
 * Wird <b>kein</b> Wert mitgegeben, wird standardmäßig der FailureScreen angezeigt.
 */
public class FeedbackActivity extends Activity {
    public static final String SUCCESS_FLAG = "SUCCESS_FLAG";
    ConnectionManager connectionManager = ConnectionManager.instance();
    ContactsManager contactsManager = ContactsManager.instance();
    Contact contact = null;
    private boolean success = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(300);

        View feedback_failure = LayoutInflater.from(this).inflate(
                R.layout.feedback_failure, null);

        success = getIntent().getBooleanExtra(SUCCESS_FLAG, false);
        if (success) {
            contact = connectionManager.getReceivedContact();
            setContentView(R.layout.feedback_success);
            try {
                contactsManager.safeReceivedContact(this, contact);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            setContentView(feedback_failure);
        }
    }

    /**
     * Schickt den Nutzer zurück zum HomeScreen.
     * @param view .
     */
    public void bHomeClicked(View view) {
        finish();
    }

    /**
     * Zeigt die empfangenen Daten an.
     * @param view .
     */
    public void bDetailsClicked(View view) {
        contactsManager = ContactsManager.instance();
        if (success && contact != null) {
            finish();
            contactsManager.showContact(this, contact);
        } else {
            Util.showToast(this, "No Contact received", Toast.LENGTH_SHORT);
        }
    }

    /**
     * Schickt den Nutzer zurück zur ExchangeActivity.
     * Diese wird im Falle von nicht erfolgter Datenübertragung nicht gefinished,
     * d.h. die verbindung sollte noch da sein und der Datenaustausch müsste noch erfolgen.
     * @param view .
     */
    public void bShareClicked(View view) {
        if (!success) {
            startActivity(new Intent(this, ExchangeActivity.class));
            connectionManager.btReConnect();
        }
        finish();
    }
}



