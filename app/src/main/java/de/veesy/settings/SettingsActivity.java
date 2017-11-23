package de.veesy.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.veesy.R;
import de.veesy.connection.ConnectionManager;
import de.veesy.contacts.Contact;
import de.veesy.contacts.ContactViewActivity;
import de.veesy.contacts.ContactsManager;
import de.veesy.listview_util.StraightListAdapter;
import de.veesy.util.Util;

import static de.veesy.contacts.ContactViewActivity.SHOW_CONTACT_REQUEST_CODE;
import static de.veesy.settings.ESettingItems.MY_CARD;

/**
 * Created by dfritsch on 17.11.2017.
 * veesy.de
 * hs-augsburg
 */

public class SettingsActivity extends Activity {
    private static List<String> listItems;
    private final Context context = this;

    static {
        listItems = new ArrayList<>();
        listItems.add(MY_CARD.getName());
        listItems.add(ESettingItems.BLUETOOTH_SETTING.getName());
        listItems.add(ESettingItems.REMOVE_DEVICES.getName());
        listItems.add(ESettingItems.ABOUT.getName());
    }

    private ConnectionManager connectionManager;
    private ContactsManager contactsManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        connectionManager = ConnectionManager.instance();
        contactsManager = ContactsManager.instance();

        initListView();
    }

    private void initListView() {
        ListView listView = findViewById(R.id.lVSettings);
        StraightListAdapter adapter = new StraightListAdapter(this, R.layout.straight_list_view_row,
                listItems);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setPadding(0, 0, 0, 5);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showOwnContact();
                        break;
                    case 1:
                        //break;
                    case 2:
                        if (connectionManager != null) {
                            connectionManager.unpairAllDevices();
                        }
                        break;
                    case 3:
                        break;
                }

            }
        });
    }

    private void showOwnContact() {
        try {
            Contact ownContact = contactsManager.getOwnContact(this);
            Intent intent = ContactViewActivity.getIntent(this, ownContact,true);
            startActivityForResult(intent, SHOW_CONTACT_REQUEST_CODE);
        } catch (IOException e) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SHOW_CONTACT_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            Util.showToast(this, R.string.error_reading_card, Toast.LENGTH_SHORT);
        }
    }

    public void bShareClicked(View view) {
        finish();
    }

    //TODO Kann weg?
    public void bUnpairClicked(View view) {

    }
}
