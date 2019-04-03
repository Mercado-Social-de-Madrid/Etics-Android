package net.mercadosocial.moneda.api.model;

public class ResetPincodeRequest {

    private String pincode;
    private String password;

    public ResetPincodeRequest(String pincode, String password) {
        this.pincode = pincode;
        this.password = password;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
