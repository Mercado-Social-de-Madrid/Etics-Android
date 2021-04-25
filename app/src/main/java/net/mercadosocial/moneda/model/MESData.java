package net.mercadosocial.moneda.model;

public class MESData {

    private String emailContact;
    private String web;
    private String facebook;
    private String twitter;
    private String vimeo;
    private String instagram;

    public static MESData madrid() {
        MESData mesData = new MESData();
        mesData.setEmailContact("etics@mercadosocial.net");
        mesData.setWeb("https://mercadosocial.net/");
        mesData.setFacebook("https://www.facebook.com/MercadoSocialMadrid");
        mesData.setTwitter("https://twitter.com/MES_Madrid");
        mesData.setVimeo("https://vimeo.com/mercadosocial");
        return mesData;
    }

    public static MESData murcia() {
        MESData mesData = new MESData();
        mesData.setEmailContact("reasmurcia@reasnet.com");
        mesData.setWeb("https://www.reasred.org/reas-murcia");
        mesData.setFacebook("https://www.facebook.com/reasmurcia");
        mesData.setInstagram("https://www.instagram.com/reas_murcia/");
        return mesData;
    }


    public String getEmailContact() {
        return emailContact;
    }

    public void setEmailContact(String emailContact) {
        this.emailContact = emailContact;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getVimeo() {
        return vimeo;
    }

    public void setVimeo(String vimeo) {
        this.vimeo = vimeo;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }
}
