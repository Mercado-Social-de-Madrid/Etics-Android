package net.mercadosocial.moneda.api.model;

/**
 * Created by julio on 20/03/18.
 */

public class Purchase {

    private Float amount;

    public Purchase(Float amount) {
        this.amount = amount;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }
}
