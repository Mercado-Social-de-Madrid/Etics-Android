
package net.mercadosocial.moneda.model;

import net.mercadosocial.moneda.api.common.ApiClient;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class Entity implements Serializable {


    private String logo_thumbnail;
    private String logo;
    private String instagram_link;
    private String phone_number;
    private Integer bonification_percent;
    private String webpage_link;
    private List<Offer> offers;
    private String twitter_link;
    private String facebook_link;
    private String telegram_link;
    private Integer num_workers;
    private String id;
    private String short_description;
    private String cif;
    private String legal_form;
    private String address;
    private String email;
    private String registered;
    private String description;
    private String name;
    private Float max_percent_payment;
    private Double longitude;
    private List<String> categories;
    private Double latitude;


    public String getCategoriesString() {

        String categoriesStr = "";

        // todo get names by map
//        for (int i = 0; i < categories.size(); i++) {
//            categoriesStr += categories.get(i);
//            if (i < categories.size() - 1) {
//                categoriesStr += ",";
//            }
//        }

        return categoriesStr;
    }

    public String getLogoThumbnailFullUrl() {
        return ApiClient.BASE_URL + getLogo_thumbnail();
    }

    public String getLogoFullUrl() {
        return ApiClient.BASE_URL + getLogo();
    }


    public float getMaxAcceptedBoniatosAmount(Float totalAmountFloat) {
        return getMax_percent_payment() * totalAmountFloat / 100f;
    }

    public String getMaxAcceptedBoniatosAmountFormatted(Float totalAmountFloat) {
        NumberFormat numberFormat = new DecimalFormat("0.##");
        String amountFormatted = numberFormat.format(getMaxAcceptedBoniatosAmount(totalAmountFloat));
        return amountFormatted;
    }

    public Float getBonus(Float totalAmount) {
        return (float) getBonus_percent() * totalAmount / 100f;
    }

    public String getBonusFormatted(Float totalAmount) {
        NumberFormat numberFormat = new DecimalFormat("0.##");
        String amountFormatted = numberFormat.format(getBonus(totalAmount));
        return amountFormatted;
    }


    // ------------------------------

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getBonus_percent() {
        if (bonification_percent == null) {
            return 0;
        }
        return bonification_percent;
    }

    public void setBonification_percent(Integer bonification_percent) {
        this.bonification_percent = bonification_percent;
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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
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

    public Float getMax_percent_payment() {
        if (max_percent_payment == null) {
            return 100f;
        }

        return max_percent_payment;
    }

    public void setMax_percent_payment(Float max_percent_payment) {
        this.max_percent_payment = max_percent_payment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }


    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public String getLogo_thumbnail() {
        return logo_thumbnail;
    }

    public void setLogo_thumbnail(String logo_thumbnail) {
        this.logo_thumbnail = logo_thumbnail;
    }

    public String getInstagram_link() {
        return instagram_link;
    }

    public void setInstagram_link(String instagram_link) {
        this.instagram_link = instagram_link;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getWebpage_link() {
        return webpage_link;
    }

    public void setWebpage_link(String webpage_link) {
        this.webpage_link = webpage_link;
    }

    public String getTwitter_link() {
        return twitter_link;
    }

    public void setTwitter_link(String twitter_link) {
        this.twitter_link = twitter_link;
    }

    public String getFacebook_link() {
        return facebook_link;
    }

    public void setFacebook_link(String facebook_link) {
        this.facebook_link = facebook_link;
    }

    public String getTelegram_link() {
        return telegram_link;
    }

    public void setTelegram_link(String telegram_link) {
        this.telegram_link = telegram_link;
    }

    public Integer getNum_workers() {
        return num_workers;
    }

    public void setNum_workers(Integer num_workers) {
        this.num_workers = num_workers;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getLegal_form() {
        return legal_form;
    }

    public void setLegal_form(String legal_form) {
        this.legal_form = legal_form;
    }
}