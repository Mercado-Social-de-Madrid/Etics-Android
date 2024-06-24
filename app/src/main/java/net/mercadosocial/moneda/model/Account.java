package net.mercadosocial.moneda.model;

import com.google.gson.annotations.SerializedName;

import net.mercadosocial.moneda.api.common.ApiClient;

import java.io.Serializable;

public abstract class Account implements Serializable {

    private String id;

    @SerializedName("is_active")
    private boolean active;

    private boolean inactive;

    @SerializedName("member_id")
    private String memberId;

    private String cif;

    @SerializedName("is_intercoop")
    private Boolean intercoop;
    private String registered;

    @SerializedName("profile_image")
    private String profileImage;

    private transient String pin_code;
    private transient String pin_codeRepeat;

    public abstract String getMemberName();

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

    public boolean isIntercoop() {
        return intercoop != null ? intercoop.booleanValue() : false;
    }

    public void setIntercoop(Boolean intercoop) {
        this.intercoop = intercoop;
    }

    public boolean isActive() {
        return active | !inactive;
    }

    public void setInactive(boolean inactive) {
        this.inactive = inactive;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getProfileImage() {
        if (profileImage == null) {
            return null;
        }

        if (profileImage.startsWith("http")) {
            return profileImage;
        } else {
            return ApiClient.MEDIA_URL + profileImage.replace("media/", "");
        }
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }
}
