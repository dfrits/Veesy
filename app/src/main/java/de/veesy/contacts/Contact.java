package de.veesy.contacts;

import java.io.Serializable;

/**
 * Created by dfritsch on 24.10.2017.
 * veesy.de
 * hs-augsburg
 */

public class Contact implements Serializable{

    private String name;
    private String vorname;
    private String telefonnr;

    public Contact(){

    }

    public Contact(String nachname, String vorname, String telefonnr){
        this.name = nachname;
        this.vorname = vorname;
        this.telefonnr = telefonnr;
    }

    public String getName() {
        return name;
    }

    public String getVorname() {
        return vorname;
    }

    public String getTelefonnr() {
        return telefonnr;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public void setTelefonnr(String telefonnr) {
        this.telefonnr = telefonnr;
    }

    public String toString(){
        return vorname + " " + name + ": " + telefonnr;
    }
}
