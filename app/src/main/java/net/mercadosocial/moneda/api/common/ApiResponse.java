package net.mercadosocial.moneda.api.common;


/**
 * Created by julio on 7/07/16.
 */
public class ApiResponse {

    private Integer code;
    private String message;
    private String token;

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }
}
