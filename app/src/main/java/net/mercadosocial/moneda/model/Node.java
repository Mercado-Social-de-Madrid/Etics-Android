package net.mercadosocial.moneda.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Node {

    private long id;
    private String name;
    private String shortname;
    private boolean visible;
    private double latitude;
    private double longitude;

    @SerializedName("contact_email")
    private String contactEmail;
    @SerializedName("self_register_allowed")
    private boolean selfRegisterAllowed;
    @SerializedName("register_provider_url")
    private String registerProviderURL;
    @SerializedName("register_consumer_url")
    private String registerConsumerURL;
    @SerializedName("preferred_locale")
    private String preferredLocale;
    @SerializedName("banner_image")
    private String bannerImage;
    @SerializedName("info_page_url")
    private String infoPageUrl;

    @SerializedName("webpage_link")
    private String webpageLink;

    @SerializedName("member_card_enabled")
    private boolean memberCardEnabled = true;

    @SerializedName("social_profiles")
    private List<SocialProfile> socialProfiles;

    @SerializedName("takahe_server")
    private String fediverseServer;

    public long getID() { return id; }
    public void setID(long value) { this.id = value; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public String getShortname() { return shortname; }
    public void setShortname(String value) { this.shortname = value; }

    public boolean getVisible() { return visible; }
    public void setVisible(boolean value) { this.visible = value; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double value) { this.latitude = value; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double value) { this.longitude = value; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String value) { this.contactEmail = value; }

    public boolean isSelfRegisterAllowed() { return selfRegisterAllowed; }
    public void setSelfRegisterAllowed(boolean value) { this.selfRegisterAllowed = value; }

    public String getRegisterProviderURL() { return registerProviderURL; }
    public void setRegisterProviderURL(String value) { this.registerProviderURL = value; }

    public String getRegisterConsumerURL() { return registerConsumerURL; }
    public void setRegisterConsumerURL(String value) { this.registerConsumerURL = value; }

    public String getPreferredLocale() { return preferredLocale; }
    public void setPreferredLocale(String value) { this.preferredLocale = value; }

    public String getBannerImage() { return bannerImage; }
    public void setBannerImage(String value) { this.bannerImage = value; }

    public boolean isMemberCardEnabled() {
        return memberCardEnabled;
    }

    public void setMemberCardEnabled(boolean memberCardEnabled) {
        this.memberCardEnabled = memberCardEnabled;
    }

    public String getFediverseServer() {
        return fediverseServer;
    }

    public void setFediverseServer(String fediverseServer) {
        this.fediverseServer = fediverseServer;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Node) {
            return ((Node) obj).getShortname().equals(this.getShortname());
        }
        return false;
    }

    public String getWebpageLink() {
        return webpageLink;
    }

    public void setWebpageLink(String webpageLink) {
        this.webpageLink = webpageLink;
    }

    public List<SocialProfile> getSocialProfiles() {
        return socialProfiles;
    }

    public void setSocialProfiles(List<SocialProfile> socialProfiles) {
        this.socialProfiles = socialProfiles;
    }

    public String getInfoPageUrl() {
        return infoPageUrl;
    }

    public void setInfoPageUrl(String infoPageUrl) {
        this.infoPageUrl = infoPageUrl;
    }
}
