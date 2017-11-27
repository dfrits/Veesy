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
    private TextView tFirst_name;
    private TextView tLast_name;
    private TextView tPhone;
    private TextView tOccupation;
    private TextView tCompany;
    private TextView tBusiness_area;
    private TextView tMail;
    private TextView tAddress;
    private TextView tWebsite;
    private TextView tBirthday;
    private TextView tHobbies;



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
        tFirst_name = findViewById(R.id.tVorname);
        tLast_name = findViewById(R.id.tNachname);
        tOccupation = findViewById(R.id.tOccupation);
        tCompany = findViewById(R.id.tCompany);
        tBusiness_area = findViewById(R.id.tBusiness_area);
        tPhone = findViewById(R.id.tPhone);
        tMail = findViewById(R.id.tMail);
        tAddress = findViewById(R.id.tAddress);
        tWebsite = findViewById(R.id.tWebsite);
        tBirthday = findViewById(R.id.tBirthday);
        tHobbies = findViewById(R.id.tHobbies);

        tFirst_name.setText(contact.getFirst_name());
        tLast_name.setText(contact.getLast_name());
        tLast_name.setText(contact.getLast_name());
        tOccupation.setText(contact.getOccupation());
        tCompany.setText(contact.getCompany());
        tBusiness_area.setText(contact.getBusiness_area());
        tPhone.setText(contact.getPhone_number());
        tMail.setText(contact.getMail());
        tAddress.setText(contact.getAddress());
        tWebsite.setText(contact.getWebsite());
        tBirthday.setText(contact.getBirthday());
        tHobbies.setText(contact.getHobbies());

        LinearLayout layout = findViewById(R.id.showContactBackground);
        Uri image = contact.getPicture();
        if (image != null) {
            // TODO Bild laden und als Hintergrund setzen
        } else {
            layout.setBackgroundResource(R.drawable.contacts_show_background);
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
            if (id == tFirst_name.getId()) {
                contact.setFirst_name(data.getStringExtra(INTENT_RESULT));
                tFirst_name.setText(data.getStringExtra(INTENT_RESULT));
            } else if (id == tLast_name.getId()) {
                contact.setLast_name(data.getStringExtra(INTENT_RESULT));
                tLast_name.setText(data.getStringExtra(INTENT_RESULT));
            } else if (id == tPhone.getId()) {
                contact.setPhone_number(data.getStringExtra(INTENT_RESULT));
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
            startActivityForResult(InputActivity.getIntent(this, R.string.first_name,
                    R.string.save, InputActivity.INPUT_TYPE_TEXT), INPUT_ACTIVITY_REQUEST_CODE);
        }
    }

    public void tNachnameClicked(View view) {
        if (isOwnCard) {
            id = view.getId();
            startActivityForResult(InputActivity.getIntent(this, R.string.last_name,
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
