package net.mercadosocial.moneda.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by julio on 2/02/18.
 */

public class Transaction implements Serializable {

    private Float amount;
    private String concept;
    private String related;
    private Float current_balance;
    private Boolean is_bonification;
    private Boolean is_euro_purchase;
    private Boolean made_byadmin;
    private String timestamp;

    private transient DateFormat dateFormatApi = Novelty.formatDatetimeApi;
    private transient DateFormat dateFormatDateTimeUser = new SimpleDateFormat("dd/MM/yy'\n'HH:mm");

    public String getDateTimeFormatted() {
        try {
            Date dateTime = dateFormatApi.parse(getTimestamp());
            String dateTimeFormmated = dateFormatDateTimeUser.format(dateTime);
            return dateTimeFormmated.replace(" ", "\n");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getTimestamp();
    }



    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getRelated() {
        return related;
    }

    public void setRelated(String related) {
        this.related = related;
    }

    public Float getCurrent_balance() {
        return current_balance;
    }

    public void setCurrent_balance(Float current_balance) {
        this.current_balance = current_balance;
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

    public Boolean getMade_byadmin() {
        return made_byadmin;
    }

    public void setMade_byadmin(Boolean made_byadmin) {
        this.made_byadmin = made_byadmin;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
