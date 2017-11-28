package de.veesy.contacts;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.StructuredName;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by dfritsch on 18.11.2017.
 * veesy.de
 * hs-augsburg
 * <p>
 * Diese Klasse verwaltet die Visitenkarten.
 */

public class ContactsManager {
    private static ContactsManager unique = null;
    private List<Contact> contacts;

    //TODO In eine Klasse für Konstanten auslagern
    private static final String FOLDER_PATH_APP = "VCards";
    private static final String FOLDER_PATH_CARDS_OWN = "Own_Card";
    private static final String FOLDER_PATH_CARDS_OTHER = "Other_Cards";
    private static final String FILE_ENDING = ".vcf";
    private static final String FILE_NAME_OWN = "Own_Card";
    private static final String FILE_NAME_OTHER = "Other_Card";

    private List<Contact> dummylist;

    //TODO dummydaten entfernen
    private ContactsManager() {
        updateContactList();
        dummylist = new ArrayList<>();
        dummylist.add(new Contact("Fritz", "Markus", "Sales Manager", "orderbird AG München",
                "Softwareentwickler", "015118293740", "markus.fritz@gmail.com",
                "Markusfritz Weg 24, 81765 München", "www.markusfritz.de", "12.08.1967",
                "Angeln", null, null));
        dummylist.add(new Contact("Fritz", "Markus", null, null, null,
                "015278492837", null, null, null, null, null, null, null));
        dummylist.add(new Contact("Meier", "Voltin", "Student", "Hochschule Augsburg", "Interaktive Mediensysteme", "0158726308",
                "voltin.meier@hs-augsburg.de", null, null, null, null, null, null));
        dummylist.add(new Contact("Beutlin", "Angelika", null, null, null, null,
                null, null, null, null, null, null, null));
        dummylist.add(new Contact("Katole", "Johanna", null, null, null, null,
                null, null, "www.johanna-katole.de", null, null, null, null));
    }

    public static ContactsManager instance() {
        if (unique == null) unique = new ContactsManager();
        return unique;
    }

    List<Contact> getdummydata() {
        if (dummylist == null) return new ArrayList<>();
        return dummylist;
    }

    /**
     * Liest alle Kontakte aus und gibt sie in einer Liste zurück.
     * @return Liste mit allen fremden Kontakten
     */
    public List<Contact> getContacts() {
        return contacts;
    }

    /**
     * Liest die Kontakte neu ein und aktualisiert die Liste der Kontakte.
     */
    //TODO Kontakte aus dem OtherFolder neu einlesen usw...
    public void updateContactList() {
        contacts = new ArrayList<>();
    }

    /**
     * Startet die ViewActivity mit dem angetippten Kontakt, um ihn anzuzeigen.
     * @param context  Context der ContactsActivity
     * @param position Position in der Liste
     */
    public void showContact(Context context, int position) {
        if (contacts != null && !contacts.isEmpty()) {
            showContact(context, contacts.get(position));
        } else {
            showContact(context, getdummydata().get(position));
        }
    }

    /**
     * Startet die ViewActivity mit dem angetippten Kontakt, um ihn anzuzeigen.
     * @param context Context der ContactsActivity
     * @param contact Kontakt, der angezeigt werden soll
     */
    public void showContact(Context context, @NonNull Contact contact) {
        context.startActivity(ViewContactNonEditableActivity.getIntent(context, contact));
    }

    /**
     * Speichert den Konakt als vcf-Datei.
     * @param contact Kontakt, der gespeichert werden soll
     */
    //TODO Bild irgendwie abspeichern
    public void safeContact(Contact contact) throws IOException {
        if (contact == null || contact.getContactPath() == null) {
            return;
        }

        VCard vCard = new VCard(VCardVersion.V4_0);
        StructuredName structuredName = new StructuredName();
        structuredName.setFamily(contact.getLastName());
        structuredName.setGiven(contact.getFirstName());
        vCard.setStructuredName(structuredName);
        vCard.setFormattedName(contact.getFirstName() + " " + contact.getLastName());
        vCard.addTelephoneNumber(contact.getPhoneNumber(), TelephoneType.CELL);
        Ezvcard.write(vCard).go(contact.getContactPath());
    }

    /**
     * Liest die Datei ein und konvertiert sie in ein Kontaktobjekt.
     * @param path Pfad der Datei
     * @return Kontakobjekt mit hinterlegten Daten
     * @throws IOException .
     */
    public Contact readContact(@NonNull File path) throws IOException {
        VCard vCard = Ezvcard.parse(path).first();
        if (vCard == null) {
            throw new IOException("No such Card");
        }

        StructuredName structuredName = vCard.getStructuredName();
        String first_name = structuredName.getGiven();
        String last_name = structuredName.getFamily();
        String phone_number = vCard.getTelephoneNumbers().get(0).getText();
        //TODO Richtiges Bild auslesen
        return new Contact(first_name, last_name, null, null, null, phone_number,
                null, null, null, null, null, null, path);
    }

    /**
     * Löscht den Kontakt.
     * @param position Position in der Liste
     */
    public boolean deleteContact(int position) {
        /*if (contacts == null || contacts.isEmpty()) {
            dummylist.remove(position);
            return true;
        }
        Contact contact = contacts.get(position);
        return contact.getContactPath() != null && contact.getContactPath().delete();*/
        return false;
    }

    public boolean deleteContact(@NonNull Contact contact) {
        return contact.getContactPath().delete();
    }

    public Contact getOwnContact(Context context) throws IOException {
        File appDir = context.getDir(FOLDER_PATH_APP, MODE_PRIVATE);

        File cardDir = new File(appDir, FOLDER_PATH_CARDS_OWN);
        if (!cardDir.exists()) {
            if (!cardDir.mkdir()) throw new IOException("Can't create new folder");
        }

        String filename = FILE_NAME_OWN + FILE_ENDING;
        File file = new File(cardDir, filename);

        Contact contact = new Contact("", "", "", "", "", "",
                "", "", "", "", "", null, null);
        if (!file.exists()) {
            if (!file.createNewFile()) throw new IOException("Can't create new file");
            return contact;
        }
        try {
            return readContact(file);
        } catch (IOException e) {
            return contact;
        }
    }
}
