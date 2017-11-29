package de.veesy.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import de.veesy.R;
import de.veesy.connection.ConnectionManager;
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
 * Wird <b>kein</b> Wert mitgegeben, wird standartmäßig der FailureScreen angezeigt.
 */
public class FeedbackActivity extends Activity {
    public static final String SUCCESS_FLAG = "SUCCESS_FLAG";
    ConnectionManager cm = ConnectionManager.instance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(300);

        View feedback_failure = LayoutInflater.from(this).inflate(
                R.layout.feedback_failure, null);

        if (getIntent().getBooleanExtra(SUCCESS_FLAG, false)) {
            setContentView(R.layout.feedback_success);
        } else {
            setContentView(feedback_failure);
        }
    }

    /**
     * Schickt den Nutzer zurück zum HomeScreen.
     * @param view .
     */
    public void bHomeClicked(View view){
        finish();
    }

    /**
     * Zeigt die empfangenen Daten an.
     * @param view .
     */
    public void bDetailsClicked(View view){
        //TODO Daniel, Details der empfangenen visitenkarte anzeigen
        ConnectionManager connM = ConnectionManager.instance();
        ContactsManager contM = ContactsManager.instance();
        if (connM.getReceivedContact() != null) {
            contM.showContact(this, connM.getReceivedContact());
            finish();
        } else {
            Util.showToast(this, "No Contact received", Toast.LENGTH_SHORT);
        }
    }

    /**
     * Schickt den Nutzer zurück zur ShareActivity.
     * @param view .
     */
    public void bShareClicked(View view) {
        //startActivity(new Intent(this, ShareActivity.class));
        cm.btSendData();

        finish();
    }
}



