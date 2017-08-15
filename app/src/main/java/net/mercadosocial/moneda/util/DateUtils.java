package net.mercadosocial.moneda.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by julio on 27/02/16.
 */
public class DateUtils {

    public static SimpleDateFormat formatDateTimeApi = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
    public static SimpleDateFormat formatDateTimeUser = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static SimpleDateFormat formatDateApi = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat formatDateUser = new SimpleDateFormat("dd/MM/yyyy");


    public static String getCurrentDateTimeHumanFormat() {
        return formatDateTimeUser.format(new Date());
    }

    public static String getCurrentDateApiFormat() {
        return formatDateApi.format(new Date());
    }

    public static String convertDateTimeApiToUserFormat(String dateTimeString) {

        try {
            Date date = formatDateTimeApi.parse(dateTimeString);
            return formatDateTimeUser.format(date);
        } catch (Exception e) {
        }

        return dateTimeString;
    }

    public static String convertDateApiToUserFormat(String dateString) {

        try {
            Date date = formatDateApi.parse(dateString);
            return formatDateUser.format(date);
        } catch (Exception e) {
        }

        return dateString;
    }

    public static String convertTimeMillisToUserFormat(Long millis) {

        return formatDateTimeUser.format(new Date(millis));

    }

    public static String convertDateApiToUserFormat(Date date) {
        return formatDateUser.format(date);
    }
}
