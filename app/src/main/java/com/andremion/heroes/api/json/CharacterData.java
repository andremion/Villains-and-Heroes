package com.andremion.heroes.api.json;

public class CharacterData {

    public long id;
    public String name;
    public String description;
    public Image thumbnail;
    public ComicList comics;
    public SeriesList series;
    public StoryList stories;
    public Url[] urls;

    public String getThumbnail() {
        return Image.getUrl(thumbnail.path, Image.SIZE_STANDARD_LARGE, thumbnail.extension);
    }

    public String getImage() {
        return Image.getUrl(thumbnail.path, Image.SIZE_STANDARD_INCREDIBLE, thumbnail.extension);
    }

}
