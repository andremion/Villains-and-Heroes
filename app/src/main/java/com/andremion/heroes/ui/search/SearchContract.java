package com.andremion.heroes.ui.search;

import android.support.annotation.NonNull;

import com.andremion.heroes.api.data.CharacterVO;

import java.util.List;

public interface SearchContract {

    interface View {

        void showProgress();

        void stopProgress();

        void showResult(List<CharacterVO> entries);

        void showError(Throwable e);

        void openCharacter(@NonNull android.view.View heroView, @NonNull CharacterVO character);
    }

    interface Actions {

        void loadCharacters();

        void searchCharacters(String query);

        void characterClick(@NonNull android.view.View heroView, @NonNull CharacterVO character);
    }
}
