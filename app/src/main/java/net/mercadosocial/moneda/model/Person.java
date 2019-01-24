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
    private String address;
    private List<String> fav_entities;

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
}
