package net.mercadosocial.moneda.api.response;

import android.text.TextUtils;

import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.model.MES;
import net.mercadosocial.moneda.model.Person;

/**
 * Created by julio on 1/02/18.
 */

public class Data {

    private String api_key;
    private Entity entity;
    private Person person;
    private String type; // person or entity. not necessary with previous fields (one of them always is null)


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
            return entity.getLogoThumbnail();
        } else {
            return person.getProfile_thumbnail();
        }
    }

    public String getCity() {
        String cityCode = "";
        if (entity != null) {
            cityCode = entity.getCity();
        } else {
            cityCode = person.getCity();
        }

        if (!TextUtils.isEmpty(cityCode)) {
            return MES.mesList.get(MES.getMESPositionByCode(cityCode)).getName();
        } else {
            return null;
        }
    }


    public String getApiKeyFull() {
        return "ApiKey " + getUsername() + ":" + getApi_key();
    }

    public boolean isEntity() {
        return type != null && type.equals("entity");
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
