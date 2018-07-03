package net.mercadosocial.moneda.model;

import net.mercadosocial.moneda.api.common.ApiClient;

/**
 * Created by julio on 1/02/18.
 */

public class Person {

    private String id;
    private String uuid_user;
    private String nif;
    private String name;
    private String email;
    private String surname;
    private String profile_image;
    private String address;

    private transient String pin_code;
    private transient String pin_codeRepeat;

    public String getIid() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid_user() {
        return uuid_user;
    }

    public void setUuid_user(String uuid_user) {
        this.uuid_user = uuid_user;
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
}
