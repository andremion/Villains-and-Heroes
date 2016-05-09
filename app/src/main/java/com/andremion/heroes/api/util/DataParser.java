package com.andremion.heroes.api.util;

import com.andremion.heroes.api.MarvelResult;
import com.andremion.heroes.api.data.CharacterVO;
import com.andremion.heroes.api.data.ComicVO;
import com.andremion.heroes.api.data.SeriesVO;
import com.andremion.heroes.api.json.CharacterData;
import com.andremion.heroes.api.json.CharacterDataContainer;
import com.andremion.heroes.api.json.CharacterDataWrapper;
import com.andremion.heroes.api.json.ComicData;
import com.andremion.heroes.api.json.ComicDataContainer;
import com.andremion.heroes.api.json.ComicDataWrapper;
import com.andremion.heroes.api.json.ComicSummary;
import com.andremion.heroes.api.json.SeriesData;
import com.andremion.heroes.api.json.SeriesDataContainer;
import com.andremion.heroes.api.json.SeriesDataWrapper;
import com.andremion.heroes.api.json.SeriesSummary;
import com.andremion.heroes.api.json.Url;

import java.util.ArrayList;
import java.util.List;

public class DataParser {

    public static MarvelResult<CharacterVO> parse(CharacterDataWrapper dataWrapper) {
        MarvelResult<CharacterVO> result = new MarvelResult<>();
        CharacterDataContainer dataContainer = dataWrapper.data;
        if (dataContainer != null) {
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
                    List<ComicVO> comicList = new ArrayList<>();
                    for (ComicSummary comicSummary : characterData.comics.items) {
                        ComicVO comic = new ComicVO();
                        comic.setId(comicSummary.getId());
                        comic.setTitle(comicSummary.name);
                        comicList.add(comic);
                    }
                    character.setComics(comicList);
                    List<SeriesVO> seriesList = new ArrayList<>();
                    for (SeriesSummary seriesSummary : characterData.series.items) {
                        SeriesVO series = new SeriesVO();
                        series.setId(seriesSummary.getId());
                        series.setTitle(seriesSummary.name);
                        seriesList.add(series);
                    }
                    character.setSeries(seriesList);
                    characterList.add(character);
                }
                result.setEntries(characterList);
            }
        }
        result.setAttribution(dataWrapper.attributionText);
        return result;
    }

    public static MarvelResult<ComicVO> parse(ComicDataWrapper dataWrapper) {
        MarvelResult<ComicVO> result = new MarvelResult<>();
        ComicDataContainer dataContainer = dataWrapper.data;
        if (dataContainer != null) {
            result.setTotal(dataContainer.total);
            ComicData[] results = dataContainer.results;
            if (results != null) {
                List<ComicVO> comicList = new ArrayList<>(results.length);
                for (ComicData comicData : results) {
                    ComicVO comic = new ComicVO();
                    comic.setId(comicData.id);
                    comic.setTitle(comicData.title);
                    comic.setThumbnail(comicData.getThumbnail());
                    comic.setImage(comicData.getImage());
                    comicList.add(comic);
                }
                result.setEntries(comicList);
            }
        }
        result.setAttribution(dataWrapper.attributionText);
        return result;
    }

    public static MarvelResult<SeriesVO> parse(SeriesDataWrapper dataWrapper) {
        MarvelResult<SeriesVO> result = new MarvelResult<>();
        SeriesDataContainer dataContainer = dataWrapper.data;
        if (dataContainer != null) {
            result.setTotal(dataContainer.total);
            SeriesData[] results = dataContainer.results;
            if (results != null) {
                List<SeriesVO> seriesList = new ArrayList<>(results.length);
                for (SeriesData seriesData : results) {
                    SeriesVO series = new SeriesVO();
                    series.setId(seriesData.id);
                    series.setTitle(seriesData.title);
                    series.setThumbnail(seriesData.getThumbnail());
                    series.setImage(seriesData.getImage());
                    seriesList.add(series);
                }
                result.setEntries(seriesList);
            }
        }
        result.setAttribution(dataWrapper.attributionText);
        return result;
    }

}
