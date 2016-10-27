package com.andremion.heroes.api.json;

public class SectionData {

    public long id;
    public String title;
    public Image thumbnail;

    public String getThumbnail() {
        if (thumbnail == null) {
            return null;
        }
        return Image.getUrl(thumbnail.path, Image.SIZE_PORTRAIT_XLARGE, thumbnail.extension);
    }

    public String getImage() {
        if (thumbnail == null) {
            return null;
        }
        return Image.getUrl(thumbnail.path, Image.SIZE_DETAIL, thumbnail.extension);
    }

}
