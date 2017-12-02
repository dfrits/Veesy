package de.veesy.contacts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;

import de.veesy.R;
import de.veesy.util.Constants;
import de.veesy.util.Util;

/**
 * Created by dfritsch on 18.11.2017.
 * veesy.de
 * hs-augsburg
 * <p>
 * Zeigt den übergebenen Kontakt an. Die Felder sind nicht bearbeitbar.
 */

public class ViewContactNonEditableActivity extends Activity {
    private static final String CONTACT_EXTRA = "CONTACT_EXTRA";
    private static final ContactsManager cm = ContactsManager.instance();

    private Contact contact;
    private TextView tFirstName;
    private TextView tLastName;
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
     * @param context   Context der aufrufenden Activity
     * @param contact   Kontakt, der angezeigt werden soll
     * @return Intent zum starten dieser Activity
     */
    public static Intent getIntent(Context context, Contact contact) {
        Intent showContactIntent = new Intent(context, ViewContactNonEditableActivity.class);
        showContactIntent.putExtra(CONTACT_EXTRA, contact);
        return showContactIntent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = getIntent();

        setContentView(R.layout.contacts_view_non_editable);

        getContactExtra(intent);

        initFields();

        // Werte setzen
        setValues();

        ScrollView layout = findViewById(R.id.showContactBackground);
        Uri image = contact.getPicture();
        if (image != null) {
            // TODO Bild laden und als Hintergrund setzen
        } else {
            layout.setBackgroundResource(R.drawable.contacts_show_background);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.SHOW_CONTACT_EDITABLE_REQUEST_CODE && resultCode == RESULT_OK) {
            setValues();
            try {
                cm.safeContact(contact);
            } catch (IOException e) {
                Util.showToast(this, R.string.error_safe_contact, Toast.LENGTH_SHORT);
            }
        }
    }

    private void initFields() {
        tFirstName = findViewById(R.id.tVorname);
        tLastName = findViewById(R.id.tNachname);
        tOccupation = findViewById(R.id.tOccupation);
        tCompany = findViewById(R.id.tCompany);
        tBusiness_area = findViewById(R.id.tBusiness_area);
        tPhone = findViewById(R.id.tPhone);
        tMail = findViewById(R.id.tMail);
        tAddress = findViewById(R.id.tAddress);
        tWebsite = findViewById(R.id.tWebsite);
        tBirthday = findViewById(R.id.tBirthday);
        tHobbies = findViewById(R.id.tHobbies);
    }

    /**
     * Setzt Werte in die Felder. Sind noch keine Werte vorhanden, wird nichts gesetzt, sodass
     * weiterhin der Hint angezeigt wird.
     */
    private void setValues() {
        String s = contact.getFirstName();
        ScrollView parentlayout = findViewById(R.id.showContactBackground);
        if (s != null && !s.isEmpty()) {
            tFirstName.setText(s);
        } else {
            tFirstName.setText(R.string.no_first_name);
        }
        s = contact.getLastName();
        if (s != null && !s.isEmpty()) {
            tLastName.setText(s);
        } else {
            tLastName.setText(R.string.no_last_name);
        }
        s = contact.getPhoneNumber();
        if (s != null && !s.isEmpty()) {
            tPhone.setText(s);
        } else {
            LinearLayout layout = findViewById(R.id.phone);
            layout.setVisibility(View.INVISIBLE);
            parentlayout.removeView(layout);
        }
        s = contact.getOccupation();
        if (s != null && !s.isEmpty()) {
            tOccupation.setText(s);
        } else {
            LinearLayout layout = findViewById(R.id.occupation);
            layout.setVisibility(View.INVISIBLE);
            parentlayout.removeView(layout);
        }
        s = contact.getCompany();
        if (s != null && !s.isEmpty()) {
            tCompany.setText(s);
        } else {
            LinearLayout layout = findViewById(R.id.company);
            layout.setVisibility(View.INVISIBLE);
            parentlayout.removeView(layout);
        }
        s = contact.getBusinessArea();
        if (s != null && !s.isEmpty()) {
            tBusiness_area.setText(s);
        } else {
            LinearLayout layout = findViewById(R.id.business_area);
            layout.setVisibility(View.INVISIBLE);
            parentlayout.removeView(layout);
        }
        s = contact.getMail();
        if (s != null && !s.isEmpty()) {
            tMail.setText(s);
        } else {
            LinearLayout layout = findViewById(R.id.mail);
            layout.setVisibility(View.INVISIBLE);
            parentlayout.removeView(layout);
        }
        s = contact.getAddress();
        if (s != null && !s.isEmpty()) {
            tAddress.setText(s);
        } else {
            LinearLayout layout = findViewById(R.id.address);
            layout.setVisibility(View.INVISIBLE);
            parentlayout.removeView(layout);
        }
        s = contact.getWebsite();
        if (s != null && !s.isEmpty()) {
            tWebsite.setText(s);
        } else {
            LinearLayout layout = findViewById(R.id.website);
            layout.setVisibility(View.INVISIBLE);
            parentlayout.removeView(layout);
        }
        s = contact.getBirthday();
        if (s != null && !s.isEmpty()) {
            tBirthday.setText(s);
        } else {
            LinearLayout layout = findViewById(R.id.birthday);
            layout.setVisibility(View.INVISIBLE);
            parentlayout.removeView(layout);
        }
        s = contact.getHobbies();
        if (s != null && !s.isEmpty()) {
            tHobbies.setText(s);
        } else {
            LinearLayout layout = findViewById(R.id.hobbies);
            layout.setVisibility(View.INVISIBLE);
            parentlayout.removeView(layout);
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

    public void bEditClicked(View view) {
        Intent intent = ViewContactEditableActivity.getIntent(this, contact);
        startActivityForResult(intent, Constants.SHOW_CONTACT_EDITABLE_REQUEST_CODE);
    }

    public void bDeleteClicked(View view) {
        if (cm.deleteContact(contact)) {
            finish();
        } else {
            Util.showToast(this, R.string.delete_contact_error, Toast.LENGTH_SHORT);
        }
    }
}
