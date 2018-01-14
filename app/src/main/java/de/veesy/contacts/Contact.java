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


    Contact(String firstName, String lastName, String occupation, String company,
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

    String getLastName() {
        return lastName;
    }

    String getFirstName() {
        return firstName;
    }

    String getOccupation() {
        return occupation;
    }

    String getCompany() {
        return company;
    }

    String getBusinessArea() {
        return businessArea;
    }

    String getPhoneNumber() {
        return phoneNumber;
    }

    String getMail() {
        return mail;
    }

    String getAddress() {
        return address;
    }

    String getWebsite() {
        return website;
    }

    String getBirthday() {
        return birthday;
    }

    String getHobbies() {
        return hobbies;
    }

    Uri getPicture() {
        return picture;
    }

    File getContactPath() {
        return contactPath;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String toString() {
        return firstName + " " + lastName + ": " + phoneNumber;
    }

    public boolean isEmpty() {
        return (lastName != null && firstName != null && lastName.isEmpty() && firstName.isEmpty());
    }

    /* Setter */

    void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    void setLastName(String last_name) {
        this.lastName = last_name;
    }

    void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    void setCompany(String company) {
        this.company = company;
    }

    void setBusinessArea(String businessArea) {
        this.businessArea = businessArea;
    }

    void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    void setMail(String mail) {
        this.mail = mail;
    }

    void setAddress(String address) {
        this.address = address;
    }

    void setWebsite(String website) {
        this.website = website;
    }

    void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    void setPicture(Uri picture) {
        this.picture = picture;
    }

    void setContactPath(File contactPath) {
        this.contactPath = contactPath;
    }
}
