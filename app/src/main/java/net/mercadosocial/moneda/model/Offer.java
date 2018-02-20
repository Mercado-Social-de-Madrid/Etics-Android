
package net.mercadosocial.moneda.model;

import android.support.annotation.NonNull;

import net.mercadosocial.moneda.api.common.ApiClient;

import java.io.Serializable;
import java.text.ParseException;

public class Offer implements Serializable, Novelty {

    private Boolean active;
    private String bannerImage;
    private String bannerThumbnail;
    private String beginDate;
    private String description;
    private Integer discountPercent;
    private Integer discountedPrice;
    private String endDate;
    private String entity;
    private String id;
    private String publishedDate;
    private String title;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getBannerImage() {
        return ApiClient.BASE_URL + bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getBannerThumbnail() {
        return ApiClient.BASE_URL + bannerThumbnail;
    }

    public void setBannerThumbnail(String bannerThumbnail) {
        this.bannerThumbnail = bannerThumbnail;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Integer discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Integer getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(Integer discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }




    @Override
    public String getTitleNovelty() {
        return getTitle();
    }

    @Override
    public String getDescriptionShortNovelty() {
        return getDescription();
    }

    @Override
    public String getImageNoveltyUrl() {
        return getBannerImage();
    }

    @Override
    public String getDate() {
        return getBeginDate();
    }

    @Override
    public int getNoveltyType() {
        return TYPE_OFFER;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Novelty novelty2 = (Novelty) o;
        try {
            boolean isDateBeforeDate2 = Novelty.formatDatetime.parse(this.getDate()).before(Novelty.formatDatetime.parse(novelty2.getDate()));
            return isDateBeforeDate2 ? 1 : -1;
        } catch (ParseException e) {
        }
        return 0;
    }
}
