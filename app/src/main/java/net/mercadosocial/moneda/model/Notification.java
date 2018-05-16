package net.mercadosocial.moneda.model;

import android.os.Bundle;

import java.io.Serializable;

/**
 * Created by julio on 27/02/18.
 */

public class Notification implements Serializable {

    public static final String FIELD_TYPE = "type";
    public static final String FIELD_FROM_OUTSIDE = "from_outside";

    public static final String TYPE_PAYMENT = "payment";
    public static final String TYPE_TRANSACTION = "transaction";
    public static final String TYPE_NEWS = "news";

    private String type;
    private String user_type; // for payment
    private String id; // for payment and news
    private Float amount; // for payment and transaction
    private Float total_amount;
    private Boolean is_bonification;
    private Boolean is_euro_purchase;
    private String concept;
    private String sender;

    private String title;
    private String message;

    private boolean fromOutside;

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

            notification.setType(extras.getString("type"));
            notification.setFromOutside(extras.getBoolean(FIELD_FROM_OUTSIDE, false));

            switch (extras.getString(Notification.FIELD_TYPE)) {
                case Notification.TYPE_PAYMENT:
                    notification.setAmount(Float.parseFloat(extras.getString("amount")));
                    notification.setId(extras.getString("id"));
                    notification.setSender(extras.getString("sender"));
                    notification.setTotal_amount(Float.parseFloat(extras.getString("total_amount")));
                    break;

                case Notification.TYPE_TRANSACTION:
                    notification.setAmount(Float.parseFloat(extras.getString("amount")));
                    notification.setConcept(extras.getString("concept"));
                    notification.setIs_bonification(Boolean.parseBoolean(extras.getString("is_bonification")));
                    notification.setIs_euro_purchase(Boolean.parseBoolean(extras.getString("is_euro_purchase")));
                    break;

                case Notification.TYPE_NEWS:
                    notification.setId(extras.getString("id"));
                    notification.setTitle(extras.getString("title"));
                    notification.setMessage(extras.getString("short_description"));
                    break;
            }

            return notification;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isFromOutside() {
        return fromOutside;
    }

    public void setFromOutside(boolean fromOutside) {
        this.fromOutside = fromOutside;
    }

    public Float getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(Float total_amount) {
        this.total_amount = total_amount;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
