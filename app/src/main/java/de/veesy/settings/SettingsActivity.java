package de.veesy.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.veesy.R;
import de.veesy.connection.ConnectionManager;
import de.veesy.contacts.Contact;
import de.veesy.contacts.ViewContactEditableActivity;
import de.veesy.contacts.ContactsManager;
import de.veesy.util.Util;

import static de.veesy.settings.ESettingItems.MY_CARD;
import static de.veesy.util.Constants.SHOW_CONTACT_EDITABLE_REQUEST_CODE;

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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        connectionManager = ConnectionManager.instance();
        contactsManager = ContactsManager.instance();

        //initListView();
    }

    public void bMyCardClicked(View view){
        showOwnContact();
    }

    public void bBluetoothSettingsClicked(View view){
        //TODO bluetooth einstellungen
    }

    public void bRemoveDevicesClicked(View view){
        if (connectionManager != null) {
            connectionManager.unpairAllDevices();
        }
    }

    public void bAboutClicked(View view){
        //TODO about us info
    }

/*    private void initListView() {
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
    }*/

    private void showOwnContact() {
        try {
            Contact ownContact = contactsManager.getOwnContact(this);
            Intent intent = ViewContactEditableActivity.getIntent(this, ownContact);
            startActivity(intent);
        } catch (IOException e) {
            Util.showToast(this, "Error reading your card", Toast.LENGTH_SHORT);
        }
    }

    public void bShareClicked(View view) {
        finish();
    }

    //TODO Kann weg?
    public void bUnpairClicked(View view) {

    }
}
