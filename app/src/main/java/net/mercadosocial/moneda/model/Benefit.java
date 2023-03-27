package net.mercadosocial.moneda.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Benefit implements Serializable {

    private String id;
    private boolean active;

    @SerializedName("benefit_for_entities")
    private String benefitForEntities;

    @SerializedName("benefit_for_members")
    private String benefitForMembers;

    @SerializedName("discount_code")
    private String discountCode;

    @SerializedName("discount_link")
    private String discountLink;

    @SerializedName("discount_link_text")
    private String discountLinkText;

    @SerializedName("in_person")
    private boolean inPerson;

    private boolean online;

    @SerializedName("includes_intercoop_members")
    private boolean includesIntercoopMembers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getBenefitForEntities() {
        return benefitForEntities;
    }

    public void setBenefitForEntities(String benefitForEntities) {
        this.benefitForEntities = benefitForEntities;
    }

    public String getBenefitForMembers() {
        return benefitForMembers;
    }

    public void setBenefitForMembers(String benefitForMembers) {
        this.benefitForMembers = benefitForMembers;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public String getDiscountLink() {
        return discountLink;
    }

    public void setDiscountLink(String discountLink) {
        this.discountLink = discountLink;
    }

    public String getDiscountLinkText() {
        return discountLinkText;
    }

    public void setDiscountLinkText(String discountLinkText) {
        this.discountLinkText = discountLinkText;
    }

    public boolean isInPerson() {
        return inPerson;
    }

    public void setInPerson(boolean inPerson) {
        this.inPerson = inPerson;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isIncludesIntercoopMembers() {
        return includesIntercoopMembers;
    }

    public void setIncludesIntercoopMembers(boolean includesIntercoopMembers) {
        this.includesIntercoopMembers = includesIntercoopMembers;
    }
}
