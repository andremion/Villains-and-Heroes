/*
 * Copyright (c) 2016. André Mion
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
