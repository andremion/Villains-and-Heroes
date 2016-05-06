package com.andremion.heroes.api.json;

public class ComicData {

    public long id;
    public String title;
    public Image thumbnail;

    public String getThumbnail() {
        return Image.getUrl(thumbnail.path, Image.SIZE_PORTRAIT_XLARGE, thumbnail.extension);
    }

    public String getImage() {
        return Image.getUrl(thumbnail.path, Image.SIZE_DETAIL, thumbnail.extension);
    }

}
