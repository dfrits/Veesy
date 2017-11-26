package de.veesy.contacts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;

import de.veesy.R;
import de.veesy.settings.InputActivity;

import static de.veesy.util.Constants.INTENT_RESULT;
import static de.veesy.util.Constants.INPUT_ACTIVITY_REQUEST_CODE;

/**
 * Created by dfritsch on 18.11.2017.
 * veesy.de
 * hs-augsburg
 *
 * Zeigt den übergebenen Kontakt an. Ist es die eigene, kann man die Felder bearbeiten.
 */

public class ContactViewActivity extends Activity {
    private static final String IS_OWN_CARD = "IS_OWN_CARD";
    private static final String CONTACT_EXTRA = "CONTACT_EXTRA";
    private boolean isOwnCard;
    private Contact contact;
    private TextView tVorname;
    private TextView tNachname;
    private TextView tPhone;

    /**
     * Erstellt automatisch einen Intent mit den nötigen Extras.
     * @param context Context der aufrufenden Activity
     * @param contact Kontakt, der angezeigt werden soll
     * @param isOwnCard Ob es sich um die eigene VK handelt oder nicht
     * @return Intent zum starten dieser Activity
     */
    public static Intent getIntent(Context context, Contact contact, boolean isOwnCard) {
        Intent showContactIntent = new Intent(context, ContactViewActivity.class);
        showContactIntent.putExtra(IS_OWN_CARD, isOwnCard);
        showContactIntent.putExtra(CONTACT_EXTRA, contact);
        return showContactIntent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_show_contact);

        Intent intent = getIntent();
        isOwnCard = intent.getBooleanExtra(IS_OWN_CARD, false);
        getContactExtra(intent);
        tVorname = findViewById(R.id.tVorname);
        tNachname = findViewById(R.id.tNachname);
        tPhone = findViewById(R.id.tPhone);

        tVorname.setText(contact.getVorname());
        tNachname.setText(contact.getNachname());
        tPhone.setText(contact.getTelefonnr());

        LinearLayout layout = findViewById(R.id.showContactBackground);
        Uri image = contact.getBild();
        if (image != null) {
            // TODO Bild laden und als Hintergrund setzen
        } else {
            layout.setBackgroundResource(R.drawable.dummypicture);
        }
    }

    private void getContactExtra(Intent intent) {
        Serializable extra = intent.getSerializableExtra(CONTACT_EXTRA);
        if (extra != null && extra instanceof Contact) {
            contact = (Contact) extra;
        } else {
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    }

    /**
     * Wird aufgerufen, wenn die InputActivity beendet wird. Setzt dann beim entsprechenden Textfeld
     * den neuen Wert und speichert die VK automatisch ab.
     */
    private int id;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INPUT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            if (id == tVorname.getId()) {
                contact.setVorname(data.getStringExtra(INTENT_RESULT));
                tVorname.setText(data.getStringExtra(INTENT_RESULT));
            } else if (id == tNachname.getId()) {
                contact.setNachname(data.getStringExtra(INTENT_RESULT));
                tNachname.setText(data.getStringExtra(INTENT_RESULT));
            } else if (id == tPhone.getId()) {
                contact.setTelefonnr(data.getStringExtra(INTENT_RESULT));
                tPhone.setText(data.getStringExtra(INTENT_RESULT));
            }
            final Context context = this;
            try {
                ContactsManager contactsManager = ContactsManager.instance();
                contactsManager.safeContact(contact);
            } catch (IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Fehler", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    public void tVornameClicked(View view) {
        if (isOwnCard) {
            id = view.getId();
            startActivityForResult(InputActivity.getIntent(this, R.string.vorname,
                    R.string.save, InputActivity.INPUT_TYPE_TEXT), INPUT_ACTIVITY_REQUEST_CODE);
        }
    }

    public void tNachnameClicked(View view) {
        if (isOwnCard) {
            id = view.getId();
            startActivityForResult(InputActivity.getIntent(this, R.string.nachname,
                    R.string.save, InputActivity.INPUT_TYPE_TEXT), INPUT_ACTIVITY_REQUEST_CODE);
        }
    }

    public void tPhoneClicked(View view) {
        if (isOwnCard) {
            id = view.getId();
            startActivityForResult(InputActivity.getIntent(this, R.string.phone,
                    R.string.save, InputActivity.INPUT_TYPE_NUMBER), INPUT_ACTIVITY_REQUEST_CODE);
        }
    }
}
