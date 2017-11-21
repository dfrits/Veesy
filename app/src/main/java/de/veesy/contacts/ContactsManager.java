package de.veesy.contacts;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.parameter.EmailType;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.StructuredName;
import ezvcard.property.Telephone;

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
    void showContact(Context context, int position) {
    }

    /**
     * Speichert den Konakt als vcf-Datei.
     * @param contact Kontakt, der gespeichert werden soll
     * @param path Pfad, an dem die Datei abgelegt werden soll
     */
    public void safeContact(Contact contact, File path) {
        if (contact == null || path == null) {
            return;
        }

        VCard vCard = new VCard();
        StructuredName structuredName = new StructuredName();
        structuredName.setFamily(contact.getNachname());
        structuredName.setGiven(contact.getVorname());
        vCard.setStructuredName(structuredName);
        vCard.setFormattedName(contact.getVorname() + " " + contact.getNachname());
        vCard.addTelephoneNumber(contact.getTelefonnr(), TelephoneType.CELL);
        Ezvcard.write(vCard).version(VCardVersion.V4_0);
    }

    /**
     * Liest die Datei ein und konvertiert sie in ein Kontaktobjekt.
     * @param path Pfad der Datei
     * @return Kontakobjekt mit hinterlegten Daten
     * @throws IOException .
     */
    public Contact readContact(File path) throws IOException {
        VCard vCard = Ezvcard.parse(path).first();
        StructuredName structuredName = vCard.getStructuredName();
        String vorname = structuredName.getGiven();
        String nachname = structuredName.getFamily();
        String telnr = vCard.getTelephoneNumbers().get(0).getText();
        return new Contact(nachname, vorname, telnr, null, path);
    }
}
