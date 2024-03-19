package net.mercadosocial.moneda.model;

import android.os.Bundle;

import java.io.Serializable;

/**
 * Created by julio on 27/02/18.
 */

public class Notification implements Serializable {
    public static final String EVENT = "notification";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_FROM_OUTSIDE = "from_outside";

    public static final String TYPE_NEWS = "news";
    public static final String TYPE_OFFER = "offers";

    private String type;
    private String id;
    private String title;
    private String message;
    private Bundle extras;

    private boolean fromOutside;

    public static Notification parseNotification(Bundle extras) {

        if (extras != null && extras.containsKey(FIELD_TYPE)) {

            Notification notification = new Notification();

            notification.setType(extras.getString(FIELD_TYPE));
            notification.setFromOutside(extras.getBoolean(FIELD_FROM_OUTSIDE, false));

            notification.setId(extras.getString("id"));
            notification.setTitle(extras.getString("title"));
            notification.setMessage(extras.getString("message"));
            notification.setExtras(extras);

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

    private void setExtras(Bundle extras) {
        this.extras = extras;
    }

    public Bundle getExtras() { return extras; }
}
