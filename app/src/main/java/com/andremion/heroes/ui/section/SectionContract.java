package com.andremion.heroes.ui.section;

import com.andremion.heroes.api.data.SectionVO;

import java.util.List;

public interface SectionContract {

    interface View {

        void showItems(List<SectionVO> entries, int position);

        void close();
    }

    interface Actions {

        void closeClick();
    }
}
