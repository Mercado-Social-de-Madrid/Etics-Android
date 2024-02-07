package net.mercadosocial.moneda.model;

import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;

public class FediversePost {

    private final String id;
    private final String profilePicture;
    private final String accountName;
    private final String accountUsername;
    private final String publishedDate;
    private final String content;
    private final List<String> attachedImages;

    public FediversePost(String id, String profilePicture, String accountName, String accountUsername, String publishedDate, String content, List<String> attachedImages) {
        this.id = id;
        this.profilePicture = profilePicture;
        this.accountName = accountName;
        this.accountUsername = accountUsername;
        this.publishedDate = publishedDate;
        this.content = content;
        this.attachedImages = attachedImages;
    }


    public String getId() {
        return id;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountUsername() {
        return accountUsername;
    }

    public String getPublishedDate() {
        try {
            Instant providedInstant = Instant.parse(publishedDate);
            Instant currentInstant = Instant.now();
            Duration duration = Duration.between(providedInstant, currentInstant);

            long minutes = duration.toMinutes() % 60;
            long hours = duration.toHours();
            long days = duration.toDays();

            return  (days > 0) ? days + "d" :
                    (hours > 0) ? hours + "h" :
                    minutes + "m";

        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getContent() {
        return content;
    }

    public List<String> getAttachedImages() {
        return attachedImages;
    }

}

