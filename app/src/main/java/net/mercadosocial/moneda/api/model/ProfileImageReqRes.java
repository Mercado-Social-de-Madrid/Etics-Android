package net.mercadosocial.moneda.api.model;

import com.google.gson.annotations.SerializedName;

public class ProfileImageReqRes {

    @SerializedName("profile_image")
    private String profileImage;

    public ProfileImageReqRes(String imageBase64) {
        this.profileImage = imageBase64;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
