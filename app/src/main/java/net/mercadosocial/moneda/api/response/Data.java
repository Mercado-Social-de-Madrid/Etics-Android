package net.mercadosocial.moneda.api.response;

import net.mercadosocial.moneda.model.Account;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.model.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by julio on 1/02/18.
 */

public class Data {

    private String api_key;
    private Entity entity;
    private Person person;

    // ---
    private String username;

    public String getName(boolean withSurnamesPerson) {
        if (entity != null) {
            return entity.getName();
        } else {
            return person.getName() + (withSurnamesPerson ? " " + person.getSurname() : "");
        }
    }

    public String getLogoThumbnail() {
        if (entity != null) {
            return entity.getProfileImage();
        } else {
            return person.getProfile_thumbnail();
        }
    }

    public void setFav_entities(List<String> favEntitiesUpdated) {

        if (entity != null) {
            entity.setFav_entities(favEntitiesUpdated);
        } else {
            person.setFav_entities(favEntitiesUpdated);
        }
    }

    public List<String> getFav_entities() {
        if (entity != null) {
            List<String> favsEntities = entity.getFav_entities(); // First time will be null (workaround)
            return favsEntities !=  null ? favsEntities : new ArrayList<>();
        } else {
            return person.getFav_entities();
        }
    }

    public String getCityCode() {
        if (entity != null) {
            return entity.getCity();
        } else {
            return person.getCity();
        }
    }


    public String getApiKeyFull() {
        return "ApiKey " + getUsername() + ":" + getApi_key();
    }

    public boolean isEntity() {
        return entity != null;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Account getAccount() {
        if (person != null) {
            return person;
        } else if (entity != null) {
            return entity;
        } else {
            return null;
        }
    }

}
