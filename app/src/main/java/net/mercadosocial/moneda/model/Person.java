package net.mercadosocial.moneda.model;

import com.google.gson.annotations.SerializedName;

import net.mercadosocial.moneda.api.common.ApiClient;

import java.util.List;

/**
 * Created by julio on 1/02/18.
 */

public class Person extends Account {

    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    private String email;
    private String address;
    private List<String> fav_entities;

    public static Person createPersonProfileFavourites(List<String> favEntitiesUpdated) {
        Person person = new Person();
        person.setFav_entities(favEntitiesUpdated);
        return person;
    }

    public static Person createPersonProfileData(String name, String surname, String nif) {
        Person person = new Person();
        person.setFirstName(name);
        person.setLastName(surname);
        person.setCif(nif);
        return person;
    }

    @Override
    public String getMemberName() {
        return String.format("%s %s", getFirstName(), getLastName());
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
