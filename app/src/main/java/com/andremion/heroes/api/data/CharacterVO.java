/*
 * Copyright (c) 2017. André Mion
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.andremion.heroes.api.data;

import java.io.Serializable;
import java.util.List;

public class CharacterVO implements Serializable {

    private long mId;
    private String mName;
    private String mDescription;
    private String mThumbnail;
    private String mImage;
    private List<SectionVO> mComics;
    private List<SectionVO> mSeries;
    private List<SectionVO> mStories;
    private List<SectionVO> mEvents;
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

    public List<SectionVO> getComics() {
        return mComics;
    }

    public void setComics(List<SectionVO> comics) {
        mComics = comics;
    }

    public List<SectionVO> getSeries() {
        return mSeries;
    }

    public void setSeries(List<SectionVO> series) {
        mSeries = series;
    }

    public List<SectionVO> getStories() {
        return mStories;
    }

    public void setStories(List<SectionVO> stories) {
        mStories = stories;
    }

    public List<SectionVO> getEvents() {
        return mEvents;
    }

    public void setEvents(List<SectionVO> events) {
        mEvents = events;
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
