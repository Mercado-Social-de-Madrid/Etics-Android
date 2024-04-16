package net.mercadosocial.moneda.model.gallery_entity;

import net.mercadosocial.moneda.api.common.ApiClient;

import java.io.Serializable;

/**
 * Created by julio on 2/03/18.
 */

public class Photo implements Serializable {

    private String photo;
    private int order;

    public String getImage() {
        return ApiClient.MEDIA_URL + photo;
    }

    public void setImage(String photo) {
        this.photo = photo;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
