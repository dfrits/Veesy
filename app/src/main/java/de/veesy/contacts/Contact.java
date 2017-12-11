package de.veesy.contacts;

import android.net.Uri;

import java.io.File;
import java.io.Serializable;

/**
 * Created by dfritsch on 24.10.2017.
 * veesy.de
 * hs-augsburg
 */

public class Contact implements Serializable {

    private String lastName;
    private String firstName;
    private String occupation;
    private String company;
    private String businessArea;
    private String phoneNumber;
    private String mail;
    private String address;
    private String website;
    private String birthday;
    private String hobbies;
    private Uri picture;
    private File contactPath;


    public Contact(String firstName, String lastName, String occupation, String company,
                   String businessArea, String phoneNumber, String mail, String address,
                   String website, String birthday, String hobbies, Uri picture, File contactPath) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.occupation = occupation;
        this.company = company;
        this.businessArea = businessArea;
        this.phoneNumber = phoneNumber;
        this.mail = mail;
        this.address = address;
        this.website = website;
        this.birthday = birthday;
        this.hobbies = hobbies;
        this.picture = picture;
        this.contactPath = contactPath;
    }

    /* Getter */

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getCompany() {
        return company;
    }

    public String getBusinessArea() {
        return businessArea;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getMail() {
        return mail;
    }

    public String getAddress() {
        return address;
    }

    public String getWebsite() {
        return website;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getHobbies() {
        return hobbies;
    }

    public Uri getPicture() {
        return picture;
    }

    public File getContactPath() {
        return contactPath;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String toString() {
        return firstName + " " + lastName + ": " + phoneNumber;
    }

    /* Setter */

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setLastName(String last_name) {
        this.lastName = last_name;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setBusinessArea(String businessArea) {
        this.businessArea = businessArea;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public void setPicture(Uri picture) {
        this.picture = picture;
    }

    public void setContactPath(File contactPath) {
        this.contactPath = contactPath;
    }
}
