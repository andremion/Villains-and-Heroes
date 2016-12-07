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

package com.andremion.heroes.ui.character;

import android.support.annotation.NonNull;

import com.andremion.heroes.api.data.CharacterVO;
import com.andremion.heroes.api.data.SectionVO;

import java.util.List;

public interface CharacterContract {

    interface View {

        void showAttribution(String attribution);

        void showCharacter(@NonNull CharacterVO character);

        void showComics(@NonNull List<SectionVO> entries);

        void showSeries(@NonNull List<SectionVO> entries);

        void showStories(@NonNull List<SectionVO> entries);

        void showEvents(@NonNull List<SectionVO> entries);

        void showError(@NonNull Throwable e);

        void openSection(@SectionVO.Type int type, @NonNull android.view.View heroView, String attribution, @NonNull List<SectionVO> entries, int position);

        void openLink(@NonNull String url);

        void openSearch();
    }

    interface Actions {

        void loadComics(long characterId, int offset);

        void loadSeries(long characterId, int offset);

        void loadStories(long characterId, int offset);

        void loadEvents(long characterId, int offset);

        void sectionClick(@SectionVO.Type int type, @NonNull android.view.View heroView, @NonNull List<SectionVO> entries, int position);

        void linkClick(@NonNull String url);

        void searchClick();
    }
}
