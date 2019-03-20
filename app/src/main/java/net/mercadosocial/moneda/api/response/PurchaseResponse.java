package net.mercadosocial.moneda.api.response;

/**
 * Created by julio on 11/10/17.
 */

public class PurchaseResponse {

    private String reference;
    private String url;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
