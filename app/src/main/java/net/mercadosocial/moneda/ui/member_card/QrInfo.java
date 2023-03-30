package net.mercadosocial.moneda.ui.member_card;

import com.google.gson.annotations.SerializedName;

public class QrInfo {
    private String city;

    @SerializedName("member_id")
    private String memberId;

    public QrInfo(String city, String member_id) {
        this.city = city;
        this.memberId = member_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}
