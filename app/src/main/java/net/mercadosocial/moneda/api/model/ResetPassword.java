package net.mercadosocial.moneda.api.model;

/**
 * Created by julio on 22/03/18.
 */

public class ResetPassword {

    private String email;
    private String username;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
