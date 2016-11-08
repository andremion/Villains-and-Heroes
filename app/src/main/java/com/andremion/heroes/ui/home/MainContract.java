package com.andremion.heroes.ui.home;

import android.support.annotation.NonNull;

import com.andremion.heroes.api.data.CharacterVO;

import java.util.List;

public interface MainContract {

    interface View {

        boolean showInfoDialog();

        void showProgress();

        void stopProgress(boolean hasMore);

        void showAttribution(String attribution);

        void showResult(@NonNull List<CharacterVO> entries);

        void showError(@NonNull Throwable e);

        void openCharacter(@NonNull android.view.View heroView, @NonNull CharacterVO character);

        void openSearch();
    }

    interface Actions {

        void initScreen();

        void loadCharacters(int offset);

        void characterClick(@NonNull android.view.View heroView, @NonNull CharacterVO character);

        void searchClick();
    }
}
