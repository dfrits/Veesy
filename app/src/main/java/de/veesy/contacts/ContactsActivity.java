package de.veesy.contacts;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.SnapHelper;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.support.wear.widget.WearableRecyclerView;
import android.support.wear.widget.drawer.WearableActionDrawerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.veesy.R;
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
    private final Activity context = this;
    private RoundListAdapter adapter;
    private WearableActionDrawerView wearableActionDrawer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_list_view);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        wearableActionDrawer = findViewById(R.id.action_drawer_non_editable);
        wearableActionDrawer.setIsAutoPeekEnabled(false);

        initListView();
        setData();
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
            public void onItemLongClicked(final int position, String value) {
                onListItemLongClicked(position, value);
            }
        });
        SnapHelper snapHelper = new GravitySnapHelper(Gravity.TOP);
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(
                new WearableLinearLayoutManager(this, scrollingLayoutCallback));
        recyclerView.setAdapter(adapter);
    }

    /**
     * Holt die aktuellen Kontakte vom Manager und zeigt diese an.
     */
    private void setData() {
        List<Contact> contacts = contactsManager.getContacts(this, true);
        List<AdapterObject> list = new ArrayList<>();
        //TODO Bild vom Kontakt oder neutraleres DummyBild anzeigen
        Drawable drawable = getResources().getDrawable(R.drawable.dummypicture, null);

        contacts = contacts.isEmpty() && ContactsManager.DEBUGGING ? contactsManager.getdummydata()
                : contacts;
        for (Contact contact : contacts) {
            String name = contact.getFirstName() + " " + contact.getLastName();
            list.add(new AdapterObject(name, drawable));
        }

        adapter.setData(list);
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
     * @param contactName Name des Kontakts
     */
    private void onListItemLongClicked(final int position, String contactName) {
        String language = Locale.getDefault().getLanguage();
        String title;
        if (Locale.GERMAN.toString().equals(language)) {
            title = contactName + " " + getString(R.string.delete_question);
        } else {
            title = getString(R.string.delete_question) + " " + contactName + "?";
        }
        wearableActionDrawer.setTitle(title);
        wearableActionDrawer.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.mDeleteYes:
                        if (ContactsManager.instance().deleteContact(position)) {
                            finish();
                        } else {
                            Util.showToast(context, R.string.delete_contact_error, Toast.LENGTH_LONG);
                        }
                        break;
                }
                wearableActionDrawer.getController().closeDrawer();
                return true;
            }
        });
        wearableActionDrawer.getController().openDrawer();
    }

    public void bSettingsClicked(View view) {
        finish();
    }
}


