package de.veesy.contacts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dfritsch on 18.11.2017.
 * veesy.de
 * hs-augsburg
 */

public class ContactsManager {
    private static ContactsManager unique = null;
    private List<Contact> contacts;

    private ContactsManager() {
        contacts = new ArrayList<>();
    }

    public static ContactsManager instance() {
        if(unique == null) unique = new ContactsManager();
        return unique;
    }

    List<String> getdummydata() {
        List<String> list = new ArrayList<>();
        list.add("Fritz Markus");
        list.add("Meier Martin");
        list.add("Bauer Andrea");
        list.add("Huber Bernhard");
        return list;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    /**
     * Startet die ViewActivity mit dem angetippten Kontakt, um ihn anzuzeigen.
     * @param context Context der ContactsActivity
     * @param position Position in der Liste
     */
    void showContact(ContactsActivity context, int position) {
    }
}
