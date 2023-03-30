package net.mercadosocial.moneda.api.model;

import com.google.gson.annotations.SerializedName;

public class MemberStatus {

    private String city;

    @SerializedName("is_active")
    private boolean active;

    @SerializedName("is_guest_account")
    private boolean guest_account;

    @SerializedName("is_intercoop")
    private boolean intercoop;

    @SerializedName("member_type")
    private String memberType;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isGuest_account() {
        return guest_account;
    }

    public void setGuest_account(boolean guest_account) {
        this.guest_account = guest_account;
    }

    public boolean isIntercoop() {
        return intercoop;
    }

    public void setIntercoop(boolean intercoop) {
        this.intercoop = intercoop;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }
}
