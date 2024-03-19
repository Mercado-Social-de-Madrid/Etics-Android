
package net.mercadosocial.moneda.model;

import androidx.annotation.NonNull;

import net.mercadosocial.moneda.api.common.ApiClient;

import java.io.Serializable;
import java.text.ParseException;

public class Offer implements Serializable, Novelty {

    private String id;
    private Boolean active;
    private String title;
    private String description;
    private String banner_image;
    private String banner_thumbnail;
    private Float discount_percent;
    private Float discounted_price;
    private String begin_date;
    private String end_date;
    private Entity entity;
    private String published_date;

    public String getBeginDateFormatted() {
        try {
            return formatDateUser.format(formatDatetimeApi2.parse(getBegin_date()));
        } catch (ParseException e) {
            return getBegin_date();
        }
    }

    public String getEndDateFormatted() {
        try {
            return formatDateUser.format(formatDatetimeApi2.parse(getEnd_date()));
        } catch (ParseException e) {
            return getEnd_date();
        }
    }

    public String getPublishedDateFormatted() {
        try {
            return formatDateUser.format(formatDatetimeApi.parse(getPublished_date()));
        } catch (ParseException e) {
            return getEnd_date();
        }
    }

    public String getBanner_image() {
        return ApiClient.MEDIA_URL + banner_image;
    }

    public void setBanner_image(String banner_image) {
        this.banner_image = banner_image;
    }

    public String getBanner_thumbnail() {
        return ApiClient.MEDIA_URL + banner_thumbnail;
    }

    public void setBanner_thumbnail(String banner_thumbnail) {
        this.banner_thumbnail = banner_thumbnail;
    }

    public String getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(String begin_date) {
        this.begin_date = begin_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getDiscount_percent() {
        return discount_percent;
    }

    public void setDiscount_percent(Float discount_percent) {
        this.discount_percent = discount_percent;
    }

    public Float getDiscounted_price() {
        return discounted_price;
    }

    public void setDiscounted_price(Float discounted_price) {
        this.discounted_price = discounted_price;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublished_date() {
        return published_date;
    }

    public void setPublished_date(String published_date) {
        this.published_date = published_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }


    @Override
    public String getTitleNovelty() {
        return getTitle();
    }

    @Override
    public String getSubtitleNovelty() {
        return (getEntity() != null ? getEntity().getName() + " - " : "") + getEndDateFormatted();
    }

    @Override
    public String getDescriptionShortNovelty() {
        return getDescription();
    }

    @Override
    public String getDescriptionFullNovelty() {
        return getDescription();
    }

    @Override
    public String getImageNoveltyUrl() {
        return getBanner_image();
    }

    @Override
    public String getDate() {
        return getEndDateFormatted();
    }

    @Override
    public String getDatePublished() {
        return getPublished_date();
    }

    @Override
    public int getNoveltyType() {
        return TYPE_OFFER;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Novelty novelty2 = (Novelty) o;
        try {
            boolean isDateAfterDate2 = Novelty.formatDatetimeApi.parse(this.getDatePublished())
                    .before(Novelty.formatDatetimeApi.parse(novelty2.getDatePublished()));
            return isDateAfterDate2 ? 1 : -1;
        } catch (ParseException e) {
        }
        return 0;
    }
}
