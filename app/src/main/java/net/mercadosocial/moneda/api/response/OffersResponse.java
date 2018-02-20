package net.mercadosocial.moneda.api.response;

import net.mercadosocial.moneda.model.Offer;

import java.util.List;

/**
 * Created by julio on 11/10/17.
 */

public class OffersResponse {

    private List<Offer> offers;
    private Meta meta;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }
}
