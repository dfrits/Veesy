package de.veesy.contacts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Locale;

import de.veesy.R;
import de.veesy.util.Constants;
import de.veesy.util.QuestionActivity;
import de.veesy.util.Util;

/**
 * Created by dfritsch on 18.11.2017.
 * veesy.de
 * hs-augsburg
 * <p>
 * Zeigt den übergebenen Kontakt an. Die Felder sind nicht bearbeitbar.
 */

public class ViewContactNonEditableActivity extends Activity implements
        MenuItem.OnMenuItemClickListener {
    private Contact contact;

    // Felder für die Kontaktinfos
    private LinearLayout lDetailsView;
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
     * @param context Context der aufrufenden Activity
     * @param contact Kontakt, der angezeigt werden soll
     * @return Intent zum starten dieser Activity
     */
    public static Intent getIntent(Context context, Contact contact) {
        Intent showContactIntent = new Intent(context, ViewContactNonEditableActivity.class);
        showContactIntent.putExtra(Constants.CONTACT_EXTRA, contact);
        return showContactIntent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_view_non_editable);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = getIntent();
        getContactExtra(intent);
        initFields();

        // Werte setzen
        setValues();

        ScrollView background = findViewById(R.id.lContactBackground);
        Uri image = contact.getPicture();
        if (image != null) {
            // TODO Bild laden und als Hintergrund setzen oder eigenes Bild. Wie bei About
        } else {
            background.setBackgroundResource(R.drawable.contacts_show_background);
        }
    }

    private void initFields() {
        lDetailsView = findViewById(R.id.lContactDetails);
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
            LinearLayout lPhone = findViewById(R.id.phone);
            lDetailsView.removeView(lPhone);
        }
        s = contact.getOccupation();
        if (s != null && !s.isEmpty()) {
            tOccupation.setText(s);
        } else {
            LinearLayout lOccupation = findViewById(R.id.occupation);
            lDetailsView.removeView(lOccupation);
        }
        s = contact.getCompany();
        if (s != null && !s.isEmpty()) {
            tCompany.setText(s);
        } else {
            LinearLayout lCompany = findViewById(R.id.company);
            lDetailsView.removeView(lCompany);
        }
        s = contact.getBusinessArea();
        if (s != null && !s.isEmpty()) {
            tBusiness_area.setText(s);
        } else {
            LinearLayout lBusiness = findViewById(R.id.business_area);
            lDetailsView.removeView(lBusiness);
        }
        s = contact.getMail();
        if (s != null && !s.isEmpty()) {
            tMail.setText(s);
        } else {
            LinearLayout lMail = findViewById(R.id.mail);
            lDetailsView.removeView(lMail);
        }
        s = contact.getAddress();
        if (s != null && !s.isEmpty()) {
            tAddress.setText(s);
        } else {
            LinearLayout lAddress = findViewById(R.id.address);
            lDetailsView.removeView(lAddress);
        }
        s = contact.getWebsite();
        if (s != null && !s.isEmpty()) {
            tWebsite.setText(s);
        } else {
            LinearLayout lWebsite = findViewById(R.id.website);
            lDetailsView.removeView(lWebsite);
        }
        s = contact.getBirthday();
        if (s != null && !s.isEmpty()) {
            tBirthday.setText(s);
        } else {
            LinearLayout lBirth = findViewById(R.id.birthday);
            lDetailsView.removeView(lBirth);
        }
        s = contact.getHobbies();
        if (s != null && !s.isEmpty()) {
            tHobbies.setText(s);
        } else {
            LinearLayout lHobb = findViewById(R.id.hobbies);
            lDetailsView.removeView(lHobb);
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
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.mDeleteYes:
                if (ContactsManager.instance().deleteContact(contact)) {
                    finish();
                } else {
                    Util.showToast(this, R.string.delete_contact_error, Toast.LENGTH_LONG);
                }
                break;
        }
        return true;
    }

    public void bDeleteClicked(View view) {
        Intent deleteIntent = new Intent(this, QuestionActivity.class);

        String language = Locale.getDefault().getLanguage();
        String title;
        if (Locale.GERMAN.toString().equals(language)) {
            title = contact.getFullName() + "\n" + getString(R.string.delete_question);
        } else {
            title = getString(R.string.delete_question) + "\n" + contact.getFullName() + "?";
        }

        deleteIntent.putExtra(QuestionActivity.TITEL, title);
        startActivityForResult(deleteIntent, Constants.QUESTION_REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.QUESTION_REQUEST_CODE && resultCode == RESULT_OK) {
            if (ContactsManager.instance().deleteContact(contact)) {
                finish();
            } else {
                Util.showToast(this, R.string.delete_contact_error, Toast.LENGTH_LONG);
            }
        }
    }
}
