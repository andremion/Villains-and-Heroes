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

package com.andremion.heroes.api.util;

import com.andremion.heroes.api.MarvelResult;
import com.andremion.heroes.api.data.CharacterVO;
import com.andremion.heroes.api.data.SectionVO;
import com.andremion.heroes.api.json.CharacterData;
import com.andremion.heroes.api.json.CharacterDataContainer;
import com.andremion.heroes.api.json.CharacterDataWrapper;
import com.andremion.heroes.api.json.SectionData;
import com.andremion.heroes.api.json.SectionDataContainer;
import com.andremion.heroes.api.json.SectionDataWrapper;
import com.andremion.heroes.api.json.SectionSummary;
import com.andremion.heroes.api.json.Url;

import java.util.ArrayList;
import java.util.List;

// TODO: 06/10/2016 Parse json to model in serialization phase.
public class DataParser {

    public static MarvelResult<CharacterVO> parse(CharacterDataWrapper dataWrapper) {
        MarvelResult<CharacterVO> result = new MarvelResult<>();
        CharacterDataContainer dataContainer = dataWrapper.data;
        if (dataContainer != null) {
            result.setOffset(dataContainer.offset);
            result.setTotal(dataContainer.total);
            CharacterData[] results = dataContainer.results;
            if (results != null) {
                List<CharacterVO> characterList = new ArrayList<>(results.length);
                for (CharacterData characterData : results) {
                    CharacterVO character = new CharacterVO();
                    character.setId(characterData.id);
                    character.setName(characterData.name);
                    character.setDescription(characterData.description);
                    character.setThumbnail(characterData.getThumbnail());
                    character.setImage(characterData.getImage());
                    for (Url url : characterData.urls) {
                        if (Url.TYPE_DETAIL.equals(url.type)) {
                            character.setDetail(url.url);
                        } else if (Url.TYPE_WIKI.equals(url.type)) {
                            character.setWiki(url.url);
                        } else if (Url.TYPE_COMICLINK.equals(url.type)) {
                            character.setComicLink(url.url);
                        }
                    }
                    character.setComics(parseSection(characterData.comics.items));
                    character.setSeries(parseSection(characterData.series.items));
                    character.setStories(parseSection(characterData.stories.items));
                    character.setEvents(parseSection(characterData.events.items));
                    characterList.add(character);
                }
                result.setEntries(characterList);
            }
        }
        result.setAttribution(dataWrapper.attributionText);
        return result;
    }

    private static List<SectionVO> parseSection(SectionSummary[] items) {
        List<SectionVO> list = new ArrayList<>();
        for (SectionSummary summary : items) {
            SectionVO section = new SectionVO();
            section.setId(summary.getId());
            section.setTitle(summary.name);
            list.add(section);
        }
        return list;
    }

    public static MarvelResult<SectionVO> parse(SectionDataWrapper dataWrapper) {
        MarvelResult<SectionVO> result = new MarvelResult<>();
        SectionDataContainer dataContainer = dataWrapper.data;
        if (dataContainer != null) {
            result.setOffset(dataContainer.offset);
            result.setTotal(dataContainer.total);
            SectionData[] results = dataContainer.results;
            if (results != null) {
                List<SectionVO> list = new ArrayList<>(results.length);
                for (SectionData sectionData : results) {
                    SectionVO sectionVO = new SectionVO();
                    sectionVO.setId(sectionData.id);
                    sectionVO.setTitle(sectionData.title);
                    sectionVO.setThumbnail(sectionData.getThumbnail());
                    sectionVO.setImage(sectionData.getImage());
                    list.add(sectionVO);
                }
                result.setEntries(list);
            }
        }
        result.setAttribution(dataWrapper.attributionText);
        return result;
    }

}
