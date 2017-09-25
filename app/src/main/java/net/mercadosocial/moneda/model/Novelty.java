package net.mercadosocial.moneda.model;

import java.text.SimpleDateFormat;

/**
 * Created by julio on 18/09/17.
 */

public interface Novelty extends Comparable {

    int TYPE_NEWS = 0;
    int TYPE_OFFER = 1;

    SimpleDateFormat formatDatetime = new SimpleDateFormat("yyyy-MM-dd");

    String getTitleNovelty();

    String getDescriptionShortNovelty();

    String getImageNoveltyUrl();

    String getDate();

    int getNoveltyType();
}
