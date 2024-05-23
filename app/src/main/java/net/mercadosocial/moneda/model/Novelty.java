package net.mercadosocial.moneda.model;

import java.text.SimpleDateFormat;

/**
 * Created by julio on 18/09/17.
 */

public interface Novelty extends Comparable {

    int TYPE_NEWS = 0;
    int TYPE_OFFER = 1;

    SimpleDateFormat   formatDatetimeApi = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"); //2018-02-27T10:53:47.340816
    SimpleDateFormat formatDateApi = new SimpleDateFormat("yyyy-MM-dd"); //2018-02-27
    SimpleDateFormat formatDateUser = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat formatDatetimeUser = new SimpleDateFormat("dd/MM/yyyy '-' HH:mm");

    String getTitleNovelty();

    String getSubtitleNovelty();

    String getDescriptionShortNovelty();

    String getDescriptionFullNovelty();

    String getImageNoveltyUrl();

    String getDate();

    String getDatePublished();

    int getNoveltyType();

}
