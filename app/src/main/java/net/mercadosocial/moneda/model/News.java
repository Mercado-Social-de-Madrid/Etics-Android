package net.mercadosocial.moneda.model;

import android.support.annotation.NonNull;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by julio on 18/09/17.
 */

public class News implements Novelty {


    private String title;
    private String textShort;
    private String textFull;
    private String imageUrl;
    private String date;


//    offersMock.add(new Offer("Titulo oferta " + i, "Texto corto de la oferta " + i, "Descripción completa de la oferta " + i, null));


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

    public News(String title, String textShort, String textFull, String imageUrl, String date) {
        this.title = title;
        this.textShort = textShort;
        this.textFull = textFull;
        this.imageUrl = imageUrl;
        this.date = date;
    }

    @Override
    public String getTitleNovelty() {
        return title;
    }

    @Override
    public String getDescriptionShortNovelty() {
        return textShort;
    }

    @Override
    public String getImageNoveltyUrl() {
        return imageUrl;
    }

    @Override
    public String getDate() {
        return date;
    }

    @Override
    public int getNoveltyType() {
        return TYPE_NEWS;
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


    // -----------------------

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTextShort() {
        return textShort;
    }

    public void setTextShort(String textShort) {
        this.textShort = textShort;
    }

    public String getTextFull() {
        return textFull;
    }

    public void setTextFull(String textFull) {
        this.textFull = textFull;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
