package de.veesy.contacts;

import android.net.Uri;

import java.io.File;

/**
 * Created by dfritsch on 24.10.2017.
 * veesy.de
 * hs-augsburg
 */
class Contact {

    private String nachname;
    private String vorname;
    private String telefonnr;
    private Uri bild;
    private File contactPath;

    public Contact(String nachname, String vorname, String telefonnr,
                   Uri bild, File contactPath) {
        this.nachname = nachname;
        this.vorname = vorname;
        this.telefonnr = telefonnr;
        this.bild = bild;
        this.contactPath = contactPath;
    }

    /* Getter */

    public String getNachname() {
        return nachname;
    }

    public String getVorname() {
        return vorname;
    }

    public String getTelefonnr() {
        return telefonnr;
    }

    public Uri getBild() {
        return bild;
    }

    public File getContactPath() {
        return contactPath;
    }

    /* Setter */

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public void setTelefonnr(String telefonnr) {
        this.telefonnr = telefonnr;
    }

    public void setBild(Uri bild) {
        this.bild = bild;
    }
}
