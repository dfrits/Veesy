package de.veesy.contacts;

import java.io.Serializable;

import android.net.Uri;

import java.io.File;

/**
 * Created by dfritsch on 24.10.2017.
 * veesy.de
 * hs-augsburg
 */

public class Contact implements Serializable {

    private String last_name;
    private String first_name;
    private String occupation;
    private String company;
    private String business_area;
    private String phone_number;
    private String mail;
    private String address;
    private String website;
    private String birthday;
    private String hobbies;
    private Uri picture;
    private File contactPath;


    public Contact(String last_name, String first_name, String occupation, String company,
                   String business_area, String phone_number, String mail, String address,
                   String website, String birthday, String hobbies, Uri picture, File contactPath) {
        this.last_name = last_name;
        this.first_name = first_name;
        this.occupation = occupation;
        this.company = company;
        this.business_area = business_area;
        this.phone_number = phone_number;
        this.mail = mail;
        this.address = address;
        this.website = website;

        this.birthday = birthday;
        this.hobbies = hobbies;
        this.picture = picture;
        this.contactPath = contactPath;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getBusiness_area() {
        return business_area;
    }

    public void setBusiness_area(String business_area) {
        this.business_area = business_area;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public Uri getPicture() {
        return picture;
    }

    public void setPicture(Uri picture) {
        this.picture = picture;
    }

    public File getContactPath() {
        return contactPath;
    }

    public void setContactPath(File contactPath) {
        this.contactPath = contactPath;
    }

    public String toString() {
        return first_name + " " + last_name + ": " + phone_number;
    }
}
