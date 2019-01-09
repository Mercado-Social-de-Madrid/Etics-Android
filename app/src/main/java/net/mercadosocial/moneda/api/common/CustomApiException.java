package net.mercadosocial.moneda.api.common;

public class CustomApiException extends Exception {

    public CustomApiException() {
    }

    public CustomApiException(String message) {
        super(message);
    }

    public CustomApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
