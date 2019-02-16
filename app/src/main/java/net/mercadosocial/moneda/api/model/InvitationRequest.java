package net.mercadosocial.moneda.api.model;

public class InvitationRequest {
    private String email;

    public InvitationRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
