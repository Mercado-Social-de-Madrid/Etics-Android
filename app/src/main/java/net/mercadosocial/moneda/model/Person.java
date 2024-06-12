package net.mercadosocial.moneda.model;

import com.google.gson.annotations.SerializedName;

import net.mercadosocial.moneda.api.common.ApiClient;

import java.util.List;

/**
 * Created by julio on 1/02/18.
 */

public class Person extends Account {

    private String nif;
    private String name;
    private String email;
    private String surname;
    private String profile_thumbnail;
    private String address;
    private List<String> fav_entities;
    private String city;
    private Boolean is_guest_account;
    private String expiration_date;

    public static Person createPersonProfileFavourites(List<String> favEntitiesUpdated) {
        Person person = new Person();
        person.setFav_entities(favEntitiesUpdated);
        return person;
    }

    public static Person createPersonProfileData(String name, String surname, String nif) {
        Person person = new Person();
        person.setName(name);
        person.setSurname(surname);
        person.setNif(nif);
        return person;
    }

    @Override
    public String getMemberName() {
        return String.format("%s %s", getName(), getSurname());
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getProfile_thumbnail() {
        return ApiClient.MEDIA_URL + profile_thumbnail;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getFav_entities() {
        return fav_entities;
    }

    public void setFav_entities(List<String> fav_entities) {
        this.fav_entities = fav_entities;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getExpiration_date() {
//        if (true) {
//            return "20/05/2019";
//        }
        return expiration_date;
    }


    public void setExpiration_date(String expiration_date) {
        this.expiration_date = expiration_date;
    }

    public boolean is_guest_account() {
        return is_guest_account == null ? false : is_guest_account;
    }

    public void setIs_guest_account(boolean is_guest_account) {
        this.is_guest_account = is_guest_account;
    }


    public void setProfile_thumbnail(String profile_thumbnail) {
        this.profile_thumbnail = profile_thumbnail;
    }

}
