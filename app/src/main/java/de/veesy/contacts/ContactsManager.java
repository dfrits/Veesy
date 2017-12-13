package de.veesy.contacts;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.veesy.util.Constants;
import de.veesy.util.Util;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.parameter.ImageType;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.Address;
import ezvcard.property.Birthday;
import ezvcard.property.Email;
import ezvcard.property.Hobby;
import ezvcard.property.Organization;
import ezvcard.property.Photo;
import ezvcard.property.StructuredName;
import ezvcard.property.Telephone;
import ezvcard.property.Url;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by dfritsch on 18.11.2017.
 * veesy.de
 * hs-augsburg
 * <p>
 * Diese Klasse verwaltet die Visitenkarten.
 */

public class ContactsManager {
    private static final String FOLDER_PATH_APP = "VCards";
    private static final String FOLDER_PATH_CARDS_OWN = "Own_Card";
    private static final String FOLDER_PATH_CARDS_OTHER = "Other_Cards";
    private static final String FILE_ENDING = ".vcf";
    private static final String FILE_NAME_OWN = "Own_Card";
    private static final String FILE_NAME_OTHER = "Other_Card";

    private static ContactsManager unique = null;

    private List<Contact> contacts;
    private List<Contact> dummylist;

    private ContactsManager() {
        if (Constants.DEBUGGING) {
            dummylist = new ArrayList<>();
            dummylist.add(new Contact("Fritz", "Markus", "Sales Manager", "orderbird AG München",
                    "Softwareentwickler", "015118293740", "markus.fritz@gmail.com",
                    "Markusfritz Weg 24, 81765 München", "www.markusfritz.de", "12.08.1967",
                    "Angeln", null, null));
            dummylist.add(new Contact("Fritz", "Martin", null, null, null,
                    "015278492837", null, null, null, null, null, null, null));
            dummylist.add(new Contact("Meier", "Voltin", "Student", "Hochschule Augsburg", "Interaktive Mediensysteme", "0158726308",
                    "voltin.meier@hs-augsburg.de", null, null, null, null, null, null));
            dummylist.add(new Contact("Beutlin", "Angelika", null, null, null, null,
                    null, null, null, null, null, null, null));
            dummylist.add(new Contact("Katole", "Johanna", null, null, null, null,
                    null, null, "www.johanna-katole.de", null, null, null, null));
        }
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
     * @param context Kontext von der Aktivity
     * @param refresh Gibt an, ob die Liste aktualisiert werden soll
     * @return Liste mit allen fremden Kontakten
     */
    List<Contact> getContacts(Context context, boolean refresh) {
        if (refresh) {
            updateContactList(context);
        }
        return contacts;
    }

    /**
     * Liest die Kontakte neu ein und aktualisiert die Liste der Kontakte.
     * @param context Kontext von der Aktivity
     */
    private void updateContactList(Context context) {
        contacts = new ArrayList<>();

        File appDir = context.getDir(FOLDER_PATH_APP, MODE_PRIVATE);

        File cardDir = new File(appDir, FOLDER_PATH_CARDS_OTHER);
        if (!cardDir.exists()) {
            if (!cardDir.mkdir()) return;
        }

        File[] cards = cardDir.listFiles();
        for (File path : cards) {
            try {
                contacts.add(readContact(path));
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * Startet die ViewActivity mit dem angetippten Kontakt, um ihn anzuzeigen.
     * @param context  Context der ContactsActivity
     * @param position Position in der Liste
     */
    void showContact(Context context, int position) {
        if (contacts != null && !contacts.isEmpty()) {
            showContact(context, contacts.get(position));
        } else {
            if (Constants.DEBUGGING) {
                showContact(context, getdummydata().get(position));
            }
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
     * Speichert den empfangenen Kontakt als vcf-Datei.
     * @param context Kontext der Activity
     * @param contact Kontakt, der gespeichert werden soll
     */
    public void safeReceivedContact(Context context, @NonNull Contact contact) throws IOException {
        if (contact.getContactPath() == null
                || contact.getContactPath().getAbsolutePath().contains(FILE_NAME_OWN)) {
            contact.setContactPath(generatePath(context));
        }

        safeContact(contact);
    }

    /**
     * Speichert <b>NUR</b> die <b>EIGENE</b> Karte ab. Ist es nicht die eigene, dann
     * @param contact Der eigene Kontakt
     * @throws IOException .
     */
    void safeOwnContact(@NonNull Contact contact) throws IOException {
        if (contact.getContactPath() == null
                || contact.getContactPath().getAbsolutePath().contains(FILE_NAME_OTHER)) {
            return;
        }

        safeContact(contact);
    }

    /**
     * Speichert dann endgültig den Kontakt, wenn die Bedingungen stimmen.
     * @param contact Kontakt, der gepeichert werden soll
     * @throws IOException .
     */
    private void safeContact(@NonNull Contact contact) throws IOException {
        VCard vCard = new VCard(VCardVersion.V4_0);
        StructuredName structuredName = new StructuredName();
        structuredName.setFamily(contact.getLastName());
        structuredName.setGiven(contact.getFirstName());
        vCard.setStructuredName(structuredName);
        vCard.setFormattedName(contact.getFirstName() + " " + contact.getLastName());
        vCard.addTelephoneNumber(contact.getPhoneNumber(), TelephoneType.CELL);
        vCard.addHobby(contact.getHobbies());
        vCard.addEmail(new Email(contact.getMail()));
        Address address = new Address();
        address.setStreetAddress(contact.getAddress());
        vCard.addAddress(address);
        if (contact.getPicture() != null) {
            Photo picture = new Photo(contact.getPicture().toString(), ImageType.PNG);
            vCard.addPhoto(picture);
        }
        Birthday birthday = new Birthday(contact.getBirthday());
        vCard.setBirthday(birthday);
        vCard.addUrl(contact.getWebsite());
        Organization organization = new Organization();
        organization.getValues().add(contact.getCompany());
        organization.getValues().add(contact.getOccupation());
        organization.getValues().add(contact.getBusinessArea());
        vCard.setOrganization(organization);
        Ezvcard.write(vCard).go(contact.getContactPath());
    }

    private File generatePath(Context context) throws IOException {
        File appDir = context.getDir(FOLDER_PATH_APP, MODE_PRIVATE);

        File cardDir = new File(appDir, FOLDER_PATH_CARDS_OTHER);
        if (!cardDir.exists()) {
            if (!cardDir.mkdir()) throw new IOException("Can't create new folder");
        }

        int i = 0;
        String filename = FILE_NAME_OTHER + i + FILE_ENDING;
        File file = new File(cardDir, filename);
        while (file.exists()) {
            i++;
            filename = FILE_NAME_OTHER + i + FILE_ENDING;
            file = new File(cardDir, filename);
        }

        if (!file.createNewFile()) throw new IOException("Can't create new file");

        return file;
    }

    /**
     * Liest die Datei ein und konvertiert sie in ein Kontaktobjekt.
     * @param path Pfad der Datei
     * @return Kontakobjekt mit hinterlegten Daten
     * @throws IOException .
     */
    private Contact readContact(@NonNull File path) throws IOException {
        VCard vCard = Ezvcard.parse(path).first();
        if (vCard == null) {
            throw new IOException("No such Card");
        }

        StructuredName structuredName = vCard.getStructuredName();
        String firstName;
        String lastName;
        if (structuredName != null) {
            firstName = structuredName.getGiven();
            lastName = structuredName.getFamily();
        } else {
            firstName = "";
            lastName = "";
        }
        List<Telephone> tel = vCard.getTelephoneNumbers();
        String phoneNumber = tel == null ? "" : tel.get(0).getText();
        Organization org = vCard.getOrganization();
        String company = org == null ? "" : org.getValues().get(0);
        String occupation = org == null ? "" : org.getValues().get(1);
        String businessArea = org == null ? "" : org.getValues().get(2);
        Birthday birth = vCard.getBirthday();
        String birthday = birth == null ? "" : birth.getText();
        List<Address> add = vCard.getAddresses();
        String address;
        try {
            address = add == null ? "" : add.get(0).getStreetAddress();
        } catch (Exception e) {
            address = "";
        }
        List<Email> emails = vCard.getEmails();
        String mail;
        try {
            mail = emails == null ? "" : emails.get(0).getValue();
        } catch (Exception e) {
            mail = "";
        }
        List<Url> urls = vCard.getUrls();
        String website;
        try {
            website = urls == null ? "" : urls.get(0).getValue();
        } catch (Exception e) {
            website = "";
        }
        List<Hobby> hobb = vCard.getHobbies();
        String hobbies;
        try {
            hobbies = hobb == null ? "" : hobb.get(0).getValue();
        } catch (Exception e) {
            hobbies = "";
        }
        List<Photo> photos = vCard.getPhotos();
        Uri picture = null;
        try {
            picture = photos == null ? null : Uri.parse(photos.get(0).getUrl());
        } catch (Exception ignored) {
        }

        return new Contact(firstName, lastName, occupation, company, businessArea, phoneNumber,
                mail, address, website, birthday, hobbies, picture, path);
    }

    /**
     * Löscht den Kontakt.
     * @param position Position in der Liste
     */
    boolean deleteContact(int position) {
        if ((contacts == null || contacts.isEmpty()) && Constants.DEBUGGING) {
            dummylist.remove(position);
            return true;
        }
        Contact contact = contacts.get(position);
        return contact.getContactPath() != null && deleteContact(contact);
    }

    /**
     * Löscht den Kontakt.
     * @param contact Kontakt, der gelöscht werden soll
     */
    boolean deleteContact(@NonNull Contact contact) {
        if ((contacts == null || contacts.isEmpty()) && Constants.DEBUGGING) {
            dummylist.remove(contact);
            for (int i = 0; i < dummylist.size(); i++) {
                if(dummylist.get(i).getFullName().equals(contact.getFullName()))
                    dummylist.remove(i);
            }
            return true;
        }
        return contact.getContactPath().delete();
    }

    /**
     * Liest die eigene VK aus. Gibt es noch keine, wird eine neue leere erstellt.
     * @param context Kontext der Activity
     * @return Eigene VK
     * @throws IOException .
     */
    public Contact getOwnContact(Context context) throws IOException {
        File appDir = context.getDir(FOLDER_PATH_APP, MODE_PRIVATE);

        File cardDir = new File(appDir, FOLDER_PATH_CARDS_OWN);
        if (!cardDir.exists()) {
            if (!cardDir.mkdir()) throw new IOException("Can't create new folder");
        }

        String filename = FILE_NAME_OWN + FILE_ENDING;
        File file = new File(cardDir, filename);

        Contact contact = new Contact("", "", "", "", "", "",
                "", "", "", "", "", null, file);
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
