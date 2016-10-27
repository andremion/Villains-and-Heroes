package com.andremion.heroes;

import com.andremion.heroes.api.MarvelApi;
import com.andremion.heroes.api.MarvelException;
import com.andremion.heroes.api.MarvelResult;
import com.andremion.heroes.api.data.CharacterVO;
import com.andremion.heroes.api.data.SectionVO;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class MarvelApiUnitTest {

    private static final int OFFSET = 0;
    private static final long CHARACTER_ID = 1011334;

    private MarvelApi mMarvelApi;

    @Before
    public void setUp() {
        mMarvelApi = MarvelApi.getInstance();
    }

    @Test
    public void listCharacters() throws IOException, MarvelException {

        // Fetch the character result and check for not null
        MarvelResult<CharacterVO> result = mMarvelApi.listCharacters(OFFSET);
        assertNotNull(result);

        // Get the character list and check for not null
        List<CharacterVO> entries = result.getEntries();
        assertNotNull(entries);

        // Get the character list and check for not empty
        assertFalse(entries.isEmpty());
    }

    @Test
    public void listComicsByCharacter() throws IOException, MarvelException {

        // Fetch the comic result and check for not null
        MarvelResult<SectionVO> result = mMarvelApi.listComics(CHARACTER_ID, OFFSET);
        assertNotNull(result);

        // Get the comic list and check for not null
        List<SectionVO> entries = result.getEntries();
        assertNotNull(entries);

        // Get the comic list and check for not empty
        assertFalse(entries.isEmpty());
    }

    @Test
    public void listSeriesByCharacter() throws IOException, MarvelException {

        // Fetch the series result and check for not null
        MarvelResult<SectionVO> result = mMarvelApi.listSeries(CHARACTER_ID, OFFSET);
        assertNotNull(result);

        // Get the series list and check for not null
        List<SectionVO> entries = result.getEntries();
        assertNotNull(entries);

        // Get the series list and check for not empty
        assertFalse(entries.isEmpty());
    }

    @Test
    public void listStoriesByCharacter() throws IOException, MarvelException {

        // Fetch the stories result and check for not null
        MarvelResult<SectionVO> result = mMarvelApi.listStories(CHARACTER_ID, OFFSET);
        assertNotNull(result);

        // Get the stories list and check for not null
        List<SectionVO> entries = result.getEntries();
        assertNotNull(entries);

        // Get the stories list and check for not empty
        assertFalse(entries.isEmpty());
    }

}