package de.veesy.contacts;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.support.wear.widget.WearableRecyclerView;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.veesy.R;
import de.veesy.listview_util.AdapterObject;
import de.veesy.listview_util.ListItemCallback;
import de.veesy.listview_util.RoundListAdapter;
import de.veesy.listview_util.ScrollingLayoutCallback;

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
        setContentView(R.layout.contacts);

        initListView();
        adapter.setData(getData());
    }

    private List<AdapterObject> getData() {
        return null;
    }

    private void initListView() {
        WearableRecyclerView recyclerView = findViewById(R.id.lVContacts);
        recyclerView.setEdgeItemsCenteringEnabled(true);
        final ScrollingLayoutCallback scrollingLayoutCallback =
                new ScrollingLayoutCallback();
        adapter = new RoundListAdapter(new ListItemCallback() {
            @Override
            public void onItemClicked(int position, String value) {
                onListItemClick(position);
            }
        });
        recyclerView.setLayoutManager(
                new WearableLinearLayoutManager(this, scrollingLayoutCallback));
        recyclerView.setAdapter(adapter);
    }

    private void onListItemClick(final int position) {
        final Context context = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context,
                        "Clicked on " + contactsManager.getdummydata().get(position),
                        Toast.LENGTH_LONG)
                        .show();
            }
        });
        contactsManager.showContact(this, position);
    }

    /**
     * Speichert einen Kontakt ab.
     * @param contact Kontakt
     */
    private void saveContact(Contact contact) {

    }

    /**
     * Liest alle Kontakte aus und gibt sie in einer Liste zurück.
     * @return Liste mit allen fremden Kontakten
     */
    private List<Contact> readContacts() {
        return new ArrayList<>();
    }

    /**
     * Löscht den Kontakt.
     */
    private void deleteContact(File file) {

    }

    public void bSettingsClicked(View view) {
        finish();
    }
}


