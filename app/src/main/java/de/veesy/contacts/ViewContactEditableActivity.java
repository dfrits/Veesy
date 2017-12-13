package de.veesy.contacts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.Serializable;

import de.veesy.R;
import de.veesy.util.Constants;

/**
 * Created by Daniel on 28.11.2017.
 * veesy.de
 * hs-augsburg
 */

public class ViewContactEditableActivity extends Activity implements EditText.OnEditorActionListener {
    // Felder für die Kontaktdetails
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
        intent.putExtra(Constants.CONTACT_EXTRA, contact);
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
        tFirstName.setOnEditorActionListener(this);
        tLastName = findViewById(R.id.tNachname);
        tLastName.setOnEditorActionListener(this);
        tOccupation = findViewById(R.id.tOccupation);
        tOccupation.setOnEditorActionListener(this);
        tCompany = findViewById(R.id.tCompany);
        tCompany.setOnEditorActionListener(this);
        tBusinessArea = findViewById(R.id.tBusiness_area);
        tBusinessArea.setOnEditorActionListener(this);
        tPhone = findViewById(R.id.tPhone);
        tPhone.setOnEditorActionListener(this);
        tMail = findViewById(R.id.tMail);
        tMail.setOnEditorActionListener(this);
        tAddress = findViewById(R.id.tAddress);
        tAddress.setOnEditorActionListener(this);
        tWebsite = findViewById(R.id.tWebsite);
        tWebsite.setOnEditorActionListener(this);
        tBirthday = findViewById(R.id.tBirthday);
        tBirthday.setOnEditorActionListener(this);
        tHobbies = findViewById(R.id.tHobbies);
        tHobbies.setOnEditorActionListener(this);
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
        Serializable extra = intent.getSerializableExtra(Constants.CONTACT_EXTRA);
        if (extra != null && extra instanceof Contact) {
            contact = (Contact) extra;
        } else {
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        ContactsManager cm = ContactsManager.instance();

        String s = textView.getText().toString();
        switch (textView.getId()) {
            case R.id.tVorname:
                contact.setFirstName(s);
                break;
            case R.id.tNachname:
                contact.setLastName(s);
                break;
            case R.id.tOccupation:
                contact.setOccupation(s);
                break;
            case R.id.tCompany:
                contact.setCompany(s);
                break;
            case R.id.tBusiness_area:
                contact.setBusinessArea(s);
                break;
            case R.id.tPhone:
                contact.setPhoneNumber(s);
                break;
            case R.id.tMail:
                contact.setMail(s);
                break;
            case R.id.tWebsite:
                contact.setWebsite(s);
                break;
            case R.id.tBirthday:
                contact.setBirthday(s);
                break;
            case R.id.tHobbies:
                contact.setHobbies(s);
                break;
            case R.id.tAddress:
                contact.setHobbies(s);
        }

        InputMethodManager imm = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
        }

        try {
            cm.safeOwnContact(this, contact);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
