
package net.mercadosocial.moneda.model;

import android.content.Context;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.api.common.ApiClient;
import net.mercadosocial.moneda.model.gallery_entity.Gallery;
import net.mercadosocial.moneda.model.gallery_entity.Photo;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class Entity implements Serializable {

    private String id;
    private String name;
    private String email;
    private String cif;
    private String description;
    private String short_description;
    private String logo;
    private String logo_thumbnail;
    private String city;

    private String phone_number;

    private Float bonus_percent_entity;
    private Float bonus_percent_general;
    private Float max_percent_payment;

    private List<Offer> offers;
    private Integer num_workers;
    private String legal_form;
    private String address;
    private String registered;
    private List<String> categories;
    private Double latitude;
    private Double longitude;

    private Gallery gallery;

    private String webpage_link;
    private String twitter_link;
    private String facebook_link;
    private String telegram_link;
    private String instagram_link;


    private List<String> fav_entities;

    private transient String pin_code;
    private transient String pin_codeRepeat;

    private transient boolean favourite;

    public String getCategoriesString() {

        String categoriesStr = "";

        // todo get names by map
//        for (int i = 0; i < categories.size(); i++) {
//            categoriesStr += categories.get(i);
//            if (i < categories.size() - 1) {
//                categoriesStr += ", ";
//            }
//        }

        return categoriesStr;
    }

    public String getImageCover() {
        if (gallery != null && gallery.getPhotos() != null && !gallery.getPhotos().isEmpty()) {
            return gallery.getPhotos().get(0).getImage();
        } else {
            return getLogo();
        }
    }

    public String getLogoThumbnail() {
        return ApiClient.baseUrl + logo_thumbnail;
    }

    public String getLogo() {
        return ApiClient.baseUrl + logo;
    }

    public String getLogoThumbnailOrCover() {

        if (logo_thumbnail != null) {
            return getLogoThumbnail();
        } else if (gallery != null && gallery.getPhotos() != null && !gallery.getPhotos().isEmpty()) {
            return gallery.getPhotos().get(0).getImage();
        } else {
            return null;
        }

    }

    public float getMaxAcceptedBoniatosAmount(Float totalAmountFloat) {
        return getMax_percent_payment() * totalAmountFloat / 100f;
    }

    public String getMaxAcceptedBoniatosAmountFormatted(Float totalAmountFloat) {
        NumberFormat numberFormat = new DecimalFormat("0.##");
        String amountFormatted = numberFormat.format(getMaxAcceptedBoniatosAmount(totalAmountFloat));
        return amountFormatted;
    }

    public Float getBonusGeneral(Float totalAmount) {
        return (float) getBonus_percent_general() * totalAmount / 100f;
    }

    public Float getBonusEntity(Float totalAmount) {
        return (float) getBonus_percent_entity() * totalAmount / 100f;
    }

    public String getBonusFormatted(Context context, Float totalAmount) {
        NumberFormat numberFormat = new DecimalFormat("0.##");
        String amountFormatted = numberFormat.format(getBonus(context, totalAmount));
        return amountFormatted;
    }

    public Float getBonus(Context context, Float totalAmount) {
        if (App.isEntity(context)) {
            return getBonusEntity(totalAmount);
        } else {
            return getBonusGeneral(totalAmount);
        }
    }

    public Float getBonusPercent(Context context) {
        if (App.isEntity(context)) {
            return getBonus_percent_entity();
        } else {
            return getBonus_percent_general();
        }
    }

    public List<String> getGalleryImages() {
        List<String> images = new ArrayList<>();
        if (gallery != null && gallery.getPhotos() != null) {
            for (Photo photo : gallery.getPhotos()) {
                images.add(photo.getImage());
            }
        }

        if (images.isEmpty()) {
            images.add("https://wrongpathtoshowerrorimage.workaround");
        }

        return images;
    }

    public String getCityName() {
        return MES.getMESNameByCode(getCity());
    }

    // ------------------------------

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Float getBonus_percent_entity() {
        if (bonus_percent_entity == null) {
            return 0f;
        }
        return bonus_percent_entity;
    }

    public void setBonus_percent_entity(Float bonus_percent_entity) {
        this.bonus_percent_entity = bonus_percent_entity;
    }

    public Float getBonus_percent_general() {
        if (bonus_percent_general == null) {
            return 0f;
        }
        return bonus_percent_general;
    }

    public void setBonus_percent_general(Float bonus_percent_general) {
        this.bonus_percent_general = bonus_percent_general;
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

    public Gallery getGallery() {
        return gallery;
    }

    public void setGallery(Gallery gallery) {
        this.gallery = gallery;
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

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getFav_entities() {
        return fav_entities;
    }

    public void setFav_entities(List<String> fav_entities) {
        this.fav_entities = fav_entities;
    }
}