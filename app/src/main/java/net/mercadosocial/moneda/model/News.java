package net.mercadosocial.moneda.model;

import androidx.annotation.NonNull;

import net.mercadosocial.moneda.api.common.ApiClient;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by julio on 18/09/17.
 */

public class News implements Serializable, Novelty {


    private String id;
    private String title;
    private String short_description;
    private String description;
    private String banner_image;
    private String more_info_text;
    private String more_info_url;
    private String published_date;


//    offersMock.add(new Offer("Titulo oferta " + i, "Texto corto de la oferta " + i, "Descripción completa de la oferta " + i, null));

    private String getDateFormatted() {
        try {
            return formatDateUser.format(formatDatetimeApi.parse(published_date));
        } catch (ParseException e) {
            return published_date;
        }
    }

    public static List<News> newsMock = new ArrayList<>();
    static {
        for (int i = 1; i < 6; i++) {
            newsMock.add(new News("Titular noticia " + i, "Texto corto de la noticia " + i, "Este sería el texto completo de la noticia " + i, null, getRandomDate(i)));

        }
    }

    private static String getRandomDate(int i) {
        Random random = new Random();
        int dayRandom = random.nextInt(30) + 1;
        return "2017-09-" + new DecimalFormat("00").format(i);
    }

    public News() {
    }

    public News(String title, String short_description, String description, String banner_image, String published_date) {
        this.title = title;
        this.short_description = short_description;
        this.description = description;
        this.banner_image = banner_image;
        this.published_date = published_date;
    }

    @Override
    public String getTitleNovelty() {
        return title;
    }

    @Override
    public String getSubtitleNovelty() {
        return getDate();
    }

    @Override
    public String getDescriptionShortNovelty() {
        return short_description;
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
        return getDateFormatted();
    }

    @Override
    public String getDatePublished() {
        return getPublished_date();
    }

    @Override
    public int getNoveltyType() {
        return TYPE_NEWS;
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


    // -----------------------

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBanner_image() {
        if (banner_image != null && banner_image.startsWith("http")) {
            return banner_image;
        } else {
            return ApiClient.MEDIA_URL + banner_image;
        }
    }

    public void setBanner_image(String banner_image) {
        this.banner_image = banner_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMore_info_text() {
        return more_info_text;
    }

    public void setMore_info_text(String more_info_text) {
        this.more_info_text = more_info_text;
    }

    public String getMore_info_url() {
        return more_info_url;
    }

    public void setMore_info_url(String more_info_url) {
        this.more_info_url = more_info_url;
    }

    public String getPublished_date() {
        return published_date;
    }

    public void setPublished_date(String published_date) {
        this.published_date = published_date;
    }
}
