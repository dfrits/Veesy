package de.veesy.contacts;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.support.wear.widget.WearableRecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.veesy.R;
import de.veesy.dialog.QuestionDialog;
import de.veesy.listview_util.AdapterObject;
import de.veesy.listview_util.ListItemCallback;
import de.veesy.listview_util.RoundListAdapter;
import de.veesy.listview_util.ScrollingLayoutCallback;
import de.veesy.util.Util;

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

        initListView();
        setData();
    }

    private void initListView() {
        final WearableRecyclerView recyclerView = findViewById(R.id.lVContacts);
        recyclerView.setEdgeItemsCenteringEnabled(true);
        final ScrollingLayoutCallback scrollingLayoutCallback =
                new ScrollingLayoutCallback();
        final Activity activity = this;
        adapter = new RoundListAdapter(new ListItemCallback() {
            @Override
            public void onItemClicked(int position, String value) {
                onListItemClick(position);
            }

            @Override
            public void onItemLongClicked(final int position, String value) {
                String message = value + " wirklich löschen?";
                final QuestionDialog dialog = QuestionDialog.newInstance(message,
                        new QuestionDialog.QuestionDialogCallback() {
                            @Override
                            public void positiveButtonClicked() {
                                if (contactsManager.deleteContact(position)) {
                                    Util.showToast(activity, "Gelöscht", Toast.LENGTH_SHORT);
                                    setData();
                                } else {
                                    Util.showToast(activity, "Nicht gelöscht", Toast.LENGTH_SHORT);
                                }
                            }

                            @Override
                            public void negativeButtonClicked() {

                            }
                        });
                dialog.show();
            }
        });
        recyclerView.setLayoutManager(
                new WearableLinearLayoutManager(this, scrollingLayoutCallback));
        recyclerView.setAdapter(adapter);
    }

    /**
     * Holt die aktuellen Kontakte vom Manager und zeigt diese an.
     */
    private void setData() {
        List<Contact> contacts = contactsManager.getContacts();
        List<AdapterObject> list = new ArrayList<>();
        //TODO Bild vom Kontakt oder neutraleres DummyBild anzeigen
        Drawable drawable = getResources().getDrawable(R.drawable.dummypicture, null);

        contacts = contacts.isEmpty() ? contactsManager.getdummydata() : contacts; //TODO einfach nichts anzeigen dann?
        for (Contact contact : contacts) {
            String name = contact.getVorname() + " " + contact.getNachname();
            list.add(new AdapterObject(name, drawable));
        }

        adapter.setData(list);
    }

    /**
     * Startet die ViewAct, um denn Kontakt anzuzeigen.
     * @param position Position in der Liste
     */
    private void onListItemClick(final int position) {
        final Context context = this;
        Util.showToast(this,
                "Clicked on " + contactsManager.getdummydata().get(position),
                Toast.LENGTH_LONG);
        contactsManager.showContact(this, position);
    }

    public void bSettingsClicked(View view) {
        finish();
    }
}


