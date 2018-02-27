package net.mercadosocial.moneda.model;

import android.os.Bundle;

/**
 * Created by julio on 27/02/18.
 */

public class Notification {

    public static final String FIELD_TYPE = "type";

    public static final String TYPE_PAYMENT = "payment";
    public static final String TYPE_TRANSACTION = "transaction";
    public static final String TYPE_NEWS = "news";

    private String type;
    private String id; // for payment and news
    private Float amount; // for payment and transaction
    private Boolean is_bonification;
    private Boolean is_euro_purchase;
    private String concept;
    private String sender;

        /*
         'type': 'payment',
            'amount': self.currency_amount,
            'id': str(self.pk),
            'sender': self.sender.username

         'type': 'transaction',
            'amount': transaction.amount,
            'is_bonification': transaction.is_bonification,
            'is_euro_purchase': transaction.is_euro_purchase,
            'concept': transaction.concept

         'type': 'news',
                id
          */

    public static Notification parseNotification(Bundle extras) {
        if (extras.containsKey(FIELD_TYPE)) {
            Notification notification = new Notification();
            notification.setAmount(extras.getFloat("amount"));
        }
        return null;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Boolean getIs_bonification() {
        return is_bonification;
    }

    public void setIs_bonification(Boolean is_bonification) {
        this.is_bonification = is_bonification;
    }

    public Boolean getIs_euro_purchase() {
        return is_euro_purchase;
    }

    public void setIs_euro_purchase(Boolean is_euro_purchase) {
        this.is_euro_purchase = is_euro_purchase;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

}
