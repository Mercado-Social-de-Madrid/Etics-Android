package net.mercadosocial.moneda.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FediverseResponse {

    @SerializedName("id")
    private String id;
    @SerializedName("account")
    private Account account;
    @SerializedName("content")
    private String content;
    @SerializedName("media_attachments")
    private List<MediaAttachment> mediaAttachments;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("emojis")
    private List<Emoji> emojis;


    public String getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public String getContent() {
        for (Emoji emoji : emojis) {
            String shortcodePattern = ":" + emoji.getShortcode() + ":";
            String replacement = String.format("<img src=\"%s\" alt=\"Emoji %s\">", emoji.getUrl(), emoji.getShortcode());
            content = content.replaceAll(shortcodePattern, replacement);
        }

        return content;
    }

    public List<MediaAttachment> getMediaAttachments() {
        return mediaAttachments;
    }
    public String getCreatedAt() {
        return createdAt;
    }


    public static class Account {
        @SerializedName("display_name")
        private String name;
        @SerializedName("acct")
        private String username;
        @SerializedName("avatar")
        private String avatar;

        public String getName() {
            return name;
        }

        public String getUsername() {
            return username;
        }
        public String getAvatar() {
            return avatar;
        }

    }

    public static class MediaAttachment {
        @SerializedName("url")
        private String url;

        public String getUrl() {
            return url;
        }
    }

    public static class Emoji {
        @SerializedName("shortcode")
        private String shortcode;
        @SerializedName("url")
        private String url;


        public String getShortcode() {
            return shortcode;
        }

        public String getUrl() {
            return url;
        }
    }
}
