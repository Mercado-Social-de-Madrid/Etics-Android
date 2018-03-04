package net.mercadosocial.moneda.model.gallery_entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by julio on 2/03/18.
 */

public class Gallery implements Serializable {

    private List<Photo> photos;

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
}
