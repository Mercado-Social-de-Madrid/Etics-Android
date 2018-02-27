package net.mercadosocial.moneda.model;

/**
 * Created by julio on 1/02/18.
 */

public class Device {


    private String uuid;
    private String name;
    private String registration_id;
    private String type = "android";
    private boolean active;

    public Device(String name, String registration_id) {
        this.name = name;
        this.registration_id = registration_id;
        this.active = true;
    }

    public Device() {

    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistration_id() {
        return registration_id;
    }

    public void setRegistration_id(String registration_id) {
        this.registration_id = registration_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
