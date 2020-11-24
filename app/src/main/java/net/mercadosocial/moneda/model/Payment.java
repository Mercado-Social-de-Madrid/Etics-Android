package net.mercadosocial.moneda.model;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Created by julio on 1/02/18.
 */

public class Payment implements Comparable<Payment> {

    //when receiving pending
    private String id;
    private String sender;
    private String status;
    private String concept;
    private String processed;
    private String timestamp;
    private String user_type;

    // when sending new
    private String receiver;
    private String pin_code;

    // common
    private Float total_amount;
    private Float currency_amount;

    private transient boolean blockButtons;

    public transient DateFormat timestampFormat = Novelty.formatDatetimeApi;


    public String getCurrencyAmountFormatted() {

        NumberFormat numberFormat = new DecimalFormat("0.##");
        String amountFormatted = numberFormat.format(getCurrency_amount());
        return amountFormatted;
    }

    public String getEurosAmountFormatted() {
        NumberFormat numberFormat = new DecimalFormat("0.##");
        String amountFormatted = numberFormat.format(getTotal_amount() - getCurrency_amount());
        return amountFormatted;
    }

    public String getTotalAmountFormatted() {
        NumberFormat numberFormat = new DecimalFormat("0.##");
        String amountFormatted = numberFormat.format(getTotal_amount());
        return amountFormatted;
    }

    public String getTimestampFormatted() {
        try {
            return Novelty.formatDatetimeUser.format(timestampFormat.parse(getTimestamp()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getTimestamp();
    }


    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Float getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(Float total_amount) {
        this.total_amount = total_amount;
    }

    public Float getCurrency_amount() {
        return currency_amount;
    }

    public void setCurrency_amount(Float currency_amount) {
        this.currency_amount = currency_amount;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProcessed() {
        return processed;
    }

    public void setProcessed(String processed) {
        this.processed = processed;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(@NonNull Payment payment2) {
        try {
            boolean isDateAfterDate2 = timestampFormat.parse(getTimestamp())
                    .before(timestampFormat.parse(payment2.getTimestamp()));
            return isDateAfterDate2 ? 1 : -1;
        } catch (ParseException e) {
        }
        return 0;
    }

    public boolean isBlockButtons() {
        return blockButtons;
    }

    public void setBlockButtons(boolean blockButtons) {
        this.blockButtons = blockButtons;
    }

    public String getPin_code() {
        return pin_code;
    }

    public void setPin_code(String pin_code) {
        this.pin_code = pin_code;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
