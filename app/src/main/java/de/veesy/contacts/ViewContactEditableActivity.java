package de.veesy.contacts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import java.io.Serializable;

import de.veesy.R;
import de.veesy.util.Constants;

/**
 * Created by Daniel on 28.11.2017.
 * veesy.de
 * hs-augsburg
 */

public class ViewContactEditableActivity extends Activity {
    private static final String CONTACT_EXTRA = "CONTACT_EXTRA";

    private Contact contact;
    private EditText tFirstName;
    private EditText tLastName;
    private EditText tPhone;
    private EditText tOccupation;
    private EditText tCompany;
    private EditText tBusinessArea;
    private EditText tMail;
    private EditText tAddress;
    private EditText tWebsite;
    private EditText tBirthday;
    private EditText tHobbies;

    public static Intent getIntent(Context context, Contact contact) {
        Intent intent = new Intent(context, ViewContactEditableActivity.class);
        intent.putExtra(CONTACT_EXTRA, contact);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_view_editable);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        getContactExtra(getIntent());

        initFields();

        // Werte setzen
        setValues();
    }

    private void initFields() {
        tFirstName = findViewById(R.id.tVorname);
        tLastName = findViewById(R.id.tNachname);
        tOccupation = findViewById(R.id.tOccupation);
        tCompany = findViewById(R.id.tCompany);
        tBusinessArea = findViewById(R.id.tBusiness_area);
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
        if (s != null && !s.isEmpty()) {
            tFirstName.setText(s);
        } else {
            tFirstName.setText("");
        }
        s = contact.getLastName();
        if (s != null && !s.isEmpty()) {
            tLastName.setText(s);
        } else {
            tLastName.setText("");
        }
        s = contact.getPhoneNumber();
        if (s != null && !s.isEmpty()) {
            tPhone.setText(s);
        } else {
            tPhone.setText("");
        }
        s = contact.getOccupation();
        if (s != null && !s.isEmpty()) {
            tOccupation.setText(s);
        } else {
            tOccupation.setText("");
        }
        s = contact.getCompany();
        if (s != null && !s.isEmpty()) {
            tCompany.setText(s);
        } else {
            tCompany.setText("");
        }
        s = contact.getBusinessArea();
        if (s != null && !s.isEmpty()) {
            tBusinessArea.setText(s);
        } else {
            tBusinessArea.setText("");
        }
        s = contact.getMail();
        if (s != null && !s.isEmpty()) {
            tMail.setText(s);
        } else {
            tMail.setText("");
        }
        s = contact.getAddress();
        if (s != null && !s.isEmpty()) {
            tAddress.setText(s);
        } else {
            tAddress.setText("");
        }
        s = contact.getWebsite();
        if (s != null && !s.isEmpty()) {
            tWebsite.setText(s);
        } else {
            tWebsite.setText("");
        }
        s = contact.getBirthday();
        if (s != null && !s.isEmpty()) {
            tBirthday.setText(s);
        } else {
            tBirthday.setText("");
        }
        s = contact.getHobbies();
        if (s != null && !s.isEmpty()) {
            tHobbies.setText(s);
        } else {
            tHobbies.setText("");
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

    public void bSaved(View view) {
        Intent intent = getIntent();

        getEditedContact();

        intent.putExtra(Constants.INTENT_RESULT, contact);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void getEditedContact() {
        contact.setFirstName(tFirstName.getText().toString());
        contact.setLastName(tLastName.getText().toString());
        contact.setOccupation(tOccupation.getText().toString());
        contact.setCompany(tCompany.getText().toString());
        contact.setAddress(tAddress.getText().toString());
        contact.setBirthday(tBirthday.getText().toString());
        contact.setBusinessArea(tBusinessArea.getText().toString());
        contact.setHobbies(tHobbies.getText().toString());
        contact.setMail(tMail.getText().toString());
        contact.setWebsite(tWebsite.getText().toString());
        contact.setPhoneNumber(tPhone.getText().toString());
    }
}
