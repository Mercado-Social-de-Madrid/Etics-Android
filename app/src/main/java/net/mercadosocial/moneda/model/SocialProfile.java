package net.mercadosocial.moneda.model;

import net.mercadosocial.moneda.api.common.ApiClient;

import java.io.Serializable;

public class SocialProfile implements Serializable {

    private String name;
    private String url;
    private String logo;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogo() {
        return ApiClient.MEDIA_URL + logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
