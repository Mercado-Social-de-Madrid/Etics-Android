package net.mercadosocial.moneda.model;

/**
 * Created by julio on 21/08/17.
 */

public class Entity {

    private int id;
    private String name;
    private String description;
    private String logo_url;
    private String address;
    private String category;

    public Entity() {

    }
    public Entity(int id, String name, String description, String logo_url, String address, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.logo_url = logo_url;
        this.address = address;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
