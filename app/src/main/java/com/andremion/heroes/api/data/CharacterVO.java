package com.andremion.heroes.api.data;

import java.io.Serializable;
import java.util.List;

public class CharacterVO implements Serializable {

    private long mId;
    private String mName;
    private String mDescription;
    private String mThumbnail;
    private String mImage;
    private List<ComicVO> mComics;
    private List<SeriesVO> mSeries;
    private String mDetail;
    private String mWiki;
    private String mComicLink;

    public CharacterVO() {
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(String thumbnail) {
        mThumbnail = thumbnail;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public List<ComicVO> getComics() {
        return mComics;
    }

    public void setComics(List<ComicVO> comics) {
        mComics = comics;
    }

    public List<SeriesVO> getSeries() {
        return mSeries;
    }

    public void setSeries(List<SeriesVO> series) {
        mSeries = series;
    }

    public String getDetail() {
        return mDetail;
    }

    public void setDetail(String detail) {
        mDetail = detail;
    }

    public String getWiki() {
        return mWiki;
    }

    public void setWiki(String wiki) {
        mWiki = wiki;
    }

    public String getComicLink() {
        return mComicLink;
    }

    public void setComicLink(String comicLink) {
        mComicLink = comicLink;
    }

    @Override
    public String toString() {
        return "Character{" + mId + ", '" + mName + "'}";
    }
}
