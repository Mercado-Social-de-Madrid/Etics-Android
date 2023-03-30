package net.mercadosocial.moneda.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public abstract class Account implements Serializable {

    private String id;

    private boolean inactive;

    @SerializedName("member_id")
    private String memberId;
    @SerializedName("is_intercoop")
    private Boolean intercoop;
    private String registered;

    private transient String pin_code;
    private transient String pin_codeRepeat;

    public abstract String getMemberName();

    public abstract String getMemberImage();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public Boolean isIntercoop() {
        return intercoop;
    }

    public void setIntercoop(Boolean intercoop) {
        this.intercoop = intercoop;
    }

    public boolean isActive() {
        return !inactive;
    }

}
