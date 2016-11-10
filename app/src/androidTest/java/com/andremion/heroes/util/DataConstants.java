package com.andremion.heroes.util;

import com.andremion.heroes.api.data.CharacterVO;
import com.andremion.heroes.api.data.SectionVO;

import java.util.ArrayList;

public class DataConstants {

    public static final CharacterVO CHARACTER = new CharacterVO();
    public static final long CHARACTER_ID = 1011334;
    public static final String CHARACTER_NAME = "3-D Man";
    public static final String CHARACTER_TO_FIND = "Hulk";
    static {
        CHARACTER.setId(CHARACTER_ID);
        CHARACTER.setName(CHARACTER_NAME);
        CHARACTER.setComics(new ArrayList<SectionVO>());
        CHARACTER.setSeries(new ArrayList<SectionVO>());
        CHARACTER.setStories(new ArrayList<SectionVO>());
        CHARACTER.setEvents(new ArrayList<SectionVO>());
    }

}
