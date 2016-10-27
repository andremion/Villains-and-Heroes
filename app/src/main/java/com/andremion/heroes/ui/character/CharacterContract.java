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

        void openSection(@NonNull android.view.View heroView, String attribution, @NonNull List<SectionVO> entries, int position);

        void openLink(@NonNull String url);

        void openSearch();
    }

    interface Actions {

        void loadComics(long characterId, int offset);

        void loadSeries(long characterId, int offset);

        void loadStories(long characterId, int offset);

        void loadEvents(long characterId, int offset);

        void sectionClick(@NonNull android.view.View heroView, @NonNull List<SectionVO> entries, int position);

        void linkClick(@NonNull String url);

        void searchClick();
    }
}
