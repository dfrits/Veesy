package de.veesy.contacts;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.SnapHelper;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.support.wear.widget.WearableRecyclerView;
import android.view.View;
import android.view.WindowManager;

import com.amulyakhare.textdrawable.TextDrawable;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.veesy.R;
import de.veesy.listview_util.AdapterObject;
import de.veesy.listview_util.ListItemCallback;
import de.veesy.listview_util.RoundListAdapter;
import de.veesy.listview_util.ScrollingLayoutCallback;
import de.veesy.util.Constants;

/**
 * Created by dfritsch on 24.10.2017.
 * veesy.de
 * hs-augsburg
 */

public class ContactsActivity extends Activity {
    private final ContactsManager contactsManager = ContactsManager.instance();
    private RoundListAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_list_view);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            setData();
        }
    }

    private void initListView() {
        final WearableRecyclerView recyclerView = findViewById(R.id.lVContacts);
        recyclerView.setEdgeItemsCenteringEnabled(true);
        final ScrollingLayoutCallback scrollingLayoutCallback =
                new ScrollingLayoutCallback();
        adapter = new RoundListAdapter(new ListItemCallback() {
            @Override
            public void onItemClicked(int position, String value) {
                onListItemClicked(position);
            }

            @Override
            public void onItemLongClicked(int position, String value) {
                onListItemLongClicked(position);
            }
        });
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(
                new WearableLinearLayoutManager(this, scrollingLayoutCallback));
        recyclerView.setAdapter(adapter);
    }

    /**
     * Holt die aktuellen Kontakte vom Manager und zeigt diese an.
     */
    private void setData() {
        List<Contact> contacts = contactsManager.getContacts(this);
        List<AdapterObject> list = new ArrayList<>();

        contacts = contacts.isEmpty() && Constants.DEBUGGING ? contactsManager.getdummydata()
                : contacts;
        for (Contact contact : contacts) {
            String name = contact.getFirstName() + " " + contact.getLastName();
            list.add(new AdapterObject(name, getDrawable(contact)));
        }

        adapter.setData(list);
    }

    /**
     * Transferiert die Uri vom Kontakt zu einem Drawable. Ist die Uri null wird jeweils der erste
     * Buchstabe vom Vor- und Nachname zurückgegeben als Drawable. Ist eines dieser Werte null, wird
     * dieser einfach leergelassen.
     * @param contact Kontakt
     * @return Drawable mit Bild vom Kontakt oder Initialen
     */
    private Drawable getDrawable(Contact contact) {
        if (contact.getPicture() != null) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(contact.getPicture());
                return Drawable.createFromStream(inputStream, contact.getPicture().toString());
            } catch (FileNotFoundException ignored) {
            }
        }
        String firstname = contact.getFirstName();
        firstname = firstname != null ? firstname.substring(0, 1) : "";
        String lastname = contact.getLastName();
        lastname = lastname != null ? lastname.substring(0, 1) : "";
        String text = firstname + lastname;

        return TextDrawable.builder()
                .buildRound(text, Color.BLACK);
    }

    /**
     * Startet die ViewAct, um denn Kontakt anzuzeigen.
     * @param position Position in der Liste
     */
    private void onListItemClicked(final int position) {
        contactsManager.showContact(this, position);
    }

    /**
     * Öffnet der ActionDrawer und fragt den Nutzer, bevor der Kontakt gelöscht wird.
     * @param position Position in der Liste
     */
    private void onListItemLongClicked(final int position) {
        contactsManager.showContact(this, position);
    }

    public void bShareClicked(View view) {
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_out_left);
    }
}


