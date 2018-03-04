package net.mercadosocial.moneda.model.gallery_entity;

import net.mercadosocial.moneda.api.common.ApiClient;

import java.io.Serializable;

/**
 * Created by julio on 2/03/18.
 */

public class Photo implements Serializable {

    private String image;
    private int order;

    public String getImage() {
        return ApiClient.BASE_URL + image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
