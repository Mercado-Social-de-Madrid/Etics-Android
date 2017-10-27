
package net.mercadosocial.moneda.model;

import java.io.Serializable;
import java.util.List;

public class Entity implements Serializable {

    private String address;
    private Integer bonificationPercent;
    private List<String> categories = null;
    private String cif;
    private String description;
    private String email;
    private String facebookLink;
    private String id;
    private String instagramLink;
    private Double latitude;
    private Double longitude;
    private String legalForm;
    private String logo;
    private Float maxPercentPayment;
    private String name;
    private Integer numWorkers;
    private List<Offer> offers = null;
    private String phoneNumber;
    private String registered;
    private String shortDescription;
    private String telegramLink;
    private String twitterLink;
    private String webpageLink;


    public String getCategoriesString() {

        String categoriesStr = "";
        for (int i = 0; i < categories.size(); i++) {
            categoriesStr += categories.get(i) + (i < categories.size() - 1 ? "," : "");
        }

        return categoriesStr;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getBonificationPercent() {
        return bonificationPercent;
    }

    public void setBonificationPercent(Integer bonificationPercent) {
        this.bonificationPercent = bonificationPercent;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacebookLink() {
        return facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstagramLink() {
        return instagramLink;
    }

    public void setInstagramLink(String instagramLink) {
        this.instagramLink = instagramLink;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getLegalForm() {
        return legalForm;
    }

    public void setLegalForm(String legalForm) {
        this.legalForm = legalForm;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Float getMaxPercentPayment() {
        return maxPercentPayment;
    }

    public void setMaxPercentPayment(Float maxPercentPayment) {
        this.maxPercentPayment = maxPercentPayment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumWorkers() {
        return numWorkers;
    }

    public void setNumWorkers(Integer numWorkers) {
        this.numWorkers = numWorkers;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getTelegramLink() {
        return telegramLink;
    }

    public void setTelegramLink(String telegramLink) {
        this.telegramLink = telegramLink;
    }

    public String getTwitterLink() {
        return twitterLink;
    }

    public void setTwitterLink(String twitterLink) {
        this.twitterLink = twitterLink;
    }

    public String getWebpageLink() {
        return webpageLink;
    }

    public void setWebpageLink(String webpageLink) {
        this.webpageLink = webpageLink;
    }

}