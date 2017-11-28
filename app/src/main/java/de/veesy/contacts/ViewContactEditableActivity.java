package de.veesy.contacts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import de.veesy.R;
import de.veesy.settings.InputActivity;

import static de.veesy.util.Constants.INPUT_ACTIVITY_REQUEST_CODE;

/**
 * Created by Daniel on 28.11.2017.
 * veesy.de
 * hs-augsburg
 */

public class ViewContactEditableActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    private int id;

    /**
     * Wird aufgerufen, wenn die InputActivity beendet wird. Setzt dann beim entsprechenden Textfeld
     * den neuen Wert und speichert die VK automatisch ab.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if (requestCode == INPUT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            if (id == tFirstName.getId()) {
                contact.setFirstName(data.getStringExtra(INTENT_RESULT));
                tFirstName.setText(data.getStringExtra(INTENT_RESULT));
            } else if (id == tLastName.getId()) {
                contact.setLastName(data.getStringExtra(INTENT_RESULT));
                tLastName.setText(data.getStringExtra(INTENT_RESULT));
            } else if (id == tPhone.getId()) {
                contact.setPhoneNumber(data.getStringExtra(INTENT_RESULT));
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
        }*/
    }

    public void tVornameClicked(View view) {
        id = view.getId();
        startActivityForResult(InputActivity.getIntent(this, R.string.first_name,
                R.string.save, InputActivity.INPUT_TYPE_TEXT), INPUT_ACTIVITY_REQUEST_CODE);
    }

    public void tNachnameClicked(View view) {
        id = view.getId();
        startActivityForResult(InputActivity.getIntent(this, R.string.last_name,
                R.string.save, InputActivity.INPUT_TYPE_TEXT), INPUT_ACTIVITY_REQUEST_CODE);
    }

    public void tPhoneClicked(View view) {
        id = view.getId();
        startActivityForResult(InputActivity.getIntent(this, R.string.phone,
                R.string.save, InputActivity.INPUT_TYPE_NUMBER), INPUT_ACTIVITY_REQUEST_CODE);
    }

    public static Intent getIntent(Context context, Contact contact) {
        return null;
    }
}
