package net.mercadosocial.moneda.model;

import net.mercadosocial.moneda.api.common.ApiClient;

import java.util.List;

/**
 * Created by julio on 1/02/18.
 */

public class Person {

    private String id;
    private String nif;
    private String name;
    private String email;
    private String surname;
    private String profile_image;
    private String profile_thumbnail;
    private String address;
    private List<String> fav_entities;
    private String city;
    private Boolean is_guest_account;
    private String expiration_date;


    private transient String pin_code;
    private transient String pin_codeRepeat;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getProfile_image() {
        return ApiClient.BASE_URL + profile_image;
    }

    public String getProfile_thumbnail() {
        return ApiClient.BASE_URL + profile_thumbnail;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getPin_code() {
        return pin_code;
    }

    public void setPin_code(String pin_code) {
        this.pin_code = pin_code;
    }

    public String getPin_codeRepeat() {
        return pin_codeRepeat;
    }

    public void setPin_codeRepeat(String pin_codeRepeat) {
        this.pin_codeRepeat = pin_codeRepeat;
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
